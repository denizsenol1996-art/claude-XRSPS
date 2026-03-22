package com.twisted.net.channel;

import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.net.NetworkUtils;
import com.twisted.net.Session;
import com.twisted.net.SessionState;
import com.twisted.net.login.LoginDetailsMessage;
import com.twisted.net.packet.PacketBuilder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketException;

public final class SessionHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(SessionHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.read();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof LoginDetailsMessage message) {
            final Channel channel = ctx.channel();
            if (channel != null && channel.isActive() && !message.getHost().isEmpty()) {
                Session session = channel.attr(NetworkUtils.SESSION_KEY).get();
                if (session == null) {
                    session = new Session(channel);
                    channel.attr(NetworkUtils.SESSION_KEY).set(session);
                }
                session.finalizeLogin(message);
            }
        }
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) {
        final Channel channel = ctx.channel();
        if (!channel.isActive()) return;
        if (channel.isWritable()) {
            Session session = channel.attr(NetworkUtils.SESSION_KEY).get();
            if (session != null) session.flushQueuedPackets();
        }
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) {
        final Channel channel = ctx.channel();
        final Session session = channel.attr(NetworkUtils.SESSION_KEY).get();
        if (session == null) return;

        onUnregisteredIngame(session);
    }

    void onUnregisteredIngame(final Session session) {
        final MessagePassingQueue<PacketBuilder> queue = session.outboundPacketsQueue;
        while (!queue.isEmpty()) {
            try (PacketBuilder builder = queue.poll()) {
                builder.buffer().release();
            } catch (Exception e) {
                logger.error("Catching Exception On Unregister: ", e);
            }
        }

        final Player player = session.getPlayer();
        final SessionState state = session.getState();
        if (player != null && player.getUsername() != null && !player.getUsername().isEmpty() && SessionState.LOGGED_IN.equals(state)) {
            player.getForcedLogoutTimer().start(60);
            player.putAttrib(AttributeKey.LOGOUT_CLICKED, true);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable throwable) throws Exception {
        if (ChannelPipelineHandler.IGNORED_ERRORS.stream().anyMatch(msg -> throwable.getMessage() != null && throwable.getMessage().contains(msg))) {
            return;
        }
        try {
            final Channel channel = ctx.channel();

            if (!channel.isActive()) {
                return;
            }

            final Session session = channel.attr(NetworkUtils.SESSION_KEY).get();

            if (throwable.getStackTrace().length > 0 && throwable.getStackTrace()[0].getMethodName().equals("read0")) {
                return;
            }

            if (throwable instanceof SocketException && throwable.getStackTrace().length > 0 && throwable.getStackTrace()[0].getMethodName().equals("throwConnectionReset")) {
                return;
            }
            if (throwable instanceof IOException && throwable.getStackTrace().length > 0 && throwable.getStackTrace()[0].getMethodName().equals("writev0")) {
                return;
            }
            if (throwable instanceof ReadTimeoutException) {
                ctx.close();
            } else {
                logger.error("An exception has been caused in the pipeline: {} {}", session, throwable);
                ctx.close();
            }
        } catch (Exception e) {
            logger.error("Uncaught server exception!", e);
            ctx.close();
        }
    }
}
