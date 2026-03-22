package com.twisted.net.channel;

import com.twisted.net.codec.LoginDecoder;
import com.twisted.net.codec.LoginEncoder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author @author os-scape team
 */
public final class ChannelPipelineHandler extends ChannelInitializer<Channel> {

    public static final Set<String> IGNORED_ERRORS = new HashSet<>(Arrays.asList(
        "An existing connection was forcibly closed by the remote host",
        "An established connection was aborted by the software in your host machine",
        "Connection reset by peer"
    ));

    private static final InternalLogger logger = InternalLoggerFactory.getInstance(ChannelInitializer.class);


    @Override
    protected void initChannel(Channel channel) {
        channel.pipeline()
            .addLast("timeout", new ReadTimeoutHandler(30, TimeUnit.SECONDS))
            .addLast("decoder", new LoginDecoder())
            .addLast("encoder", new LoginEncoder())
            .addLast("handler", new SessionHandler());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (IGNORED_ERRORS.stream().anyMatch(msg -> cause.getMessage() != null && cause.getMessage().contains(msg))) {
            return;
        }
        logger.warn("Failed to initialize a channel. Closing: " + ctx.channel(), cause);
        ctx.channel().close();
    }
}
