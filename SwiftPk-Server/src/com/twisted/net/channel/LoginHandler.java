package com.twisted.net.channel;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.ReadTimeoutException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author os-scape team
 */
public final class LoginHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LogManager.getLogger(LoginHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (ctx == null) return;
        if (msg == null) return;
        super.channelRead(ctx, msg);
    }

    /**
     * channelUnregistered has no affect here because the last Netty.Handler in the pipeline is {@link SessionHandler#channelUnregistered(ChannelHandlerContext)}
     */
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        if (ctx == null)
            return;

        super.channelUnregistered(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable throwable) {
        try {
            final Channel channel = ctx.channel();
            if (throwable instanceof ReadTimeoutException) {
                logger.debug("Channel disconnected due to read timeout (30s): {}. {}", ctx.channel(), throwable);
                channel.close();
            } else {
                logger.error("An exception occurred in the pipeline: {}", ctx, throwable);
                channel.close();
            }
        } catch (Exception e) {
            logger.error("Uncaught server exception!", e);
        }
    }
}
