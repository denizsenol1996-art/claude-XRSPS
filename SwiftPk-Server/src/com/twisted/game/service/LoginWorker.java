package com.twisted.game.service;

import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.strands.Strand;
import com.twisted.game.GameEngine;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.mob.MobList;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.net.NetworkUtils;
import com.twisted.net.Session;
import com.twisted.net.codec.PacketDecoder;
import com.twisted.net.codec.PacketEncoder;
import com.twisted.net.login.LoginDetailsMessage;
import com.twisted.net.login.LoginResponses;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelPipeline;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Bart on 8/1/2015.
 */
public class LoginWorker implements Runnable {

    private static final Logger loginLogs = LogManager.getLogger("LoginLogs");
    private static final Level LOGIN;

    static {
        LOGIN = Level.getLevel("LOGIN");
    }

    private static final Logger logger = LogManager.getLogger(LoginWorker.class);

    private final LoginService service;

    public LoginWorker(LoginService service) {
        this.service = service;
    }

    @Override
    public void run() {
        while (true) {
            try {
                processLoginJob();
            } catch (Exception e) {
                logger.error("Error processing login worker job!", e);
            }
        }
    }

    private void processLoginJob() throws SuspendExecution, InterruptedException {
        final LinkedBlockingQueue<LoginRequest> requests = service.messages();
        if (requests.isEmpty()) {
            Strand.sleep(20);
            return;
        }

        LoginRequest request = requests.take();
        if (request.delayedUntil() > System.currentTimeMillis()) {
            service.enqueue(request);
            Strand.sleep(30);
            return;
        }
        GameEngine.getInstance().addSyncTask(() -> {
            final Player player = request.player;
            final Session session = player.getSession();
            if (session == null) {
                return;
            }

            final LoginDetailsMessage data = request.message;
            final Channel channel = session.getChannel();
            if ((channel == null || !channel.isActive())) {
                return;
            }

            final int response = LoginResponses.evaluateAsync(player, data);
            if (response != LoginResponses.LOGIN_SUCCESSFUL) {
                sendCodeAndClose(channel, response);
                logger.debug("Successful Login Response: {}", player.getUsername());
                return;
            }

            final String host = data.getHost();

            if (!host.isEmpty()) {
                System.out.println("completing login for " + player.getUsername() + " with host " + host);
                completeLogin(request, channel, data);
            }
        });
    }

    private void sendCodeAndClose(final Channel channel, final int response) {
        if (channel == null) return;

        final ByteBufAllocator allocator = channel.alloc();
        final ByteBuf buffer = allocator.buffer(Byte.BYTES);
        if (buffer.isWritable()) {
            buffer.writeByte(response);
            channel.writeAndFlush(buffer).addListener(ChannelFutureListener.CLOSE);
        }
    }

    private void initForGame(LoginDetailsMessage message, Channel channel) {
        if (channel != null) {
            final ChannelPipeline pipeline = channel.pipeline();
            pipeline.replace("decoder", "decoder", new PacketDecoder(message.getDecryptor()));
            pipeline.replace("encoder", "encoder", new PacketEncoder(message.getEncryptor()));
        }
    }

    private void completeLogin(final LoginRequest request, final Channel channel, final LoginDetailsMessage message) {
        GameEngine.getInstance().addSyncTask(
            () -> {
                LoginResult result = getLoginResult(channel);
                final Session finalSession = result.key;

                if (finalSession == null) return;

                final int response = result.response;
                ChannelFuture future = finalSession.sendOkLogin(response);

                final Player player = request.player;
                if (player == null) {
                    System.out.println("player is null");
                    return;
                }

                if (future == null) {
                    channel.close();
                    return;
                }

                if (response != LoginResponses.LOGIN_SUCCESSFUL) {
                    sendCodeAndClose(channel, response);
                    return;
                }

                initForGame(message, channel);

                final MobList<Player> players = World.getWorld().getPlayers();
                players.add(player);

                try {
                   // Utils.executeHook(GameServer.getLoginWebHook(), "Login Tracker", "[Player: " + player.getUsername() + "]", player.getUsername(), player.getIp());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            });
    }

    final @NotNull LoginResult getLoginResult(Channel channel) {
        final int sessionResponse = LoginResponses.evaluateOnGamethread(channel);
        final Session sessionKey = channel.attr(NetworkUtils.SESSION_KEY).get();
        return new LoginResult(sessionResponse, sessionKey);
    }

    private record LoginResult(int response, Session key) {
    }
}
