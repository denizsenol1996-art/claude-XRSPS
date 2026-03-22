package com.twisted.net;

import com.twisted.GameServer;
import com.twisted.game.task.impl.PlayerTask;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.rights.PlayerRights;
import com.twisted.net.login.LoginDetailsMessage;
import com.twisted.net.login.LoginResponsePacket;
import com.twisted.net.login.LoginResponses;
import com.twisted.net.packet.ClientToServerPackets;
import com.twisted.net.packet.Packet;
import com.twisted.net.packet.PacketBuilder;
import com.twisted.net.packet.PacketListener;
import com.twisted.util.Utils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue;
import io.netty.util.internal.shaded.org.jctools.queues.MpscArrayQueue;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import com.twisted.game.service.LoginRequest;

import static com.twisted.game.world.entity.AttributeKey.MAC_ADDRESS;

/**
 * The session handler dedicated to a player that will handle input and output
 * operations.
 *
 * @author Lare96
 * @author Swiffy
 * @editor Professor Oak
 */
public class Session {

    private static final Logger logger = LogManager.getLogger(Session.class);

    /**
     * The queue of packets that will be handled on the next sequence.
     */
    private final MessagePassingQueue<Packet> packetsQueue =
        new MpscArrayQueue<>(100);

    public final MessagePassingQueue<PacketBuilder> outboundPacketsQueue =
        new MpscArrayQueue<>(GameServer.properties().packetProcessLimit * 10);

    /**
     * The channel that will manage the connection for this player.
     */
    private final Channel channel;

    /**
     * The player I/O operations will be executed for.
     * -- GETTER --
     * Gets the player I/O operations will be executed for.
     *
     * @return the player I/O operations.
     */
    @Getter
    private final Player player;
    public ChannelHandlerContext ctx;

    /**
     * The current state of this I/O session.
     * -- GETTER --
     * Gets the current state of this I/O session.
     * <p>
     * <p>
     * -- SETTER --
     * Sets the value for
     * .
     *
     * @return the current state.
     * @param state the new value to set.
     */
    @Setter
    @Getter
    private SessionState state = SessionState.CONNETED;

    @Getter
    private LoginDetailsMessage msg;

    /**
     * Creates a new {@link Session}.
     */
    public Session(Channel channel) {
        this.channel = channel;
        this.player = new Player(this);
    }

    /**
     * Attempts to finalize a player's login.
     *
     * @param msg The player's login information.
     */
    public void finalizeLogin(LoginDetailsMessage msg) {
        this.msg = msg;
        state = SessionState.LOGGING_IN;
        String username = msg.getUsername();
        String password = msg.getPassword();

        // Passed initial check, submit login request.
        player.setUsername(username).setLongUsername(Utils.stringToLong(username)).setHostAddress(msg.getHost());
        player.putAttrib(MAC_ADDRESS, msg.getMac());
        ctx = msg.getContext();
        World.getWorld().ls.enqueue(new LoginRequest(player, msg));
    }

    public ChannelFuture sendOkLogin(final int response) {
        final Channel channel = this.channel;
        if (channel == null || !channel.isOpen()) return null;

        ChannelFuture future = channel.writeAndFlush(new LoginResponsePacket(response, player.getPlayerRights()));
        if (response != LoginResponses.LOGIN_SUCCESSFUL) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
        return future;
    }

    /**
     * Processes a packet immediately to be sent to the client.
     *
     * @param builder the packet to send.
     */
    public void writeAndFlush(PacketBuilder builder) {
        channel.writeAndFlush(builder.toPacket());
    }

    public void queuePacket(final Packet msg) {
        packetsQueue.offer(msg);
    }

    public void handleQueuedPackets() {
        if (packetsQueue.isEmpty()) return;

        int counter = 0;
        Packet packet;
        while ((packet = packetsQueue.poll()) != null && counter < 100) {
            final ByteBuf buffer = packet.getBuffer();

            try {
                int opcode = packet.getOpcode();
                int size = packet.getSize();

                PacketListener listener = ClientToServerPackets.PACKETS[opcode];

                if (listener == null) {
                    String errorMsg = "Error processing Opcode: [" + opcode + "] Size: [" + size + "] doesn't have a handler.";
                    if (PlayerRights.is(player, PlayerRights.ADMINISTRATOR)) {
                        player.getPacketSender().sendMessage("<col=ff0000>" + errorMsg);
                    }
                    System.err.println(errorMsg);
                    continue;
                }

                if (GameServer.broadcast != null) player.getPacketSender().sendBroadcast(GameServer.broadcast);

                try {
                    listener.handleMessage(player, packet);
                } catch (Throwable t) {
                    throw new RuntimeException("Failed to handle packet message during queue'd handling.", t);
                }

                if (player.getCurrentTask() instanceof PlayerTask task) {
                    if (task.stops(listener.getClass())) {
                        task.stop();
                    }
                }
            } catch (Throwable t) {
                String errorMsg = "Error processing Opcode: [" + packet.getOpcode() + "] Size: [" + packet.getSize() + "] doesn't have a handler.";
                if (PlayerRights.is(player, PlayerRights.ADMINISTRATOR)) {
                    player.getPacketSender().sendMessage("<col=ff0000>" + errorMsg);
                }
                logger.error("Unhandled size for OpCode={} size={} Info={}", packet.getOpcode(), packet.getSize(), player);
                this.disconnect();
                throw new RuntimeException("Packet Processing Error", t);
            } finally {
                assert buffer.refCnt() == 1;
                ReferenceCountUtil.release(buffer);
            }

            counter++;
        }
    }


    private void disconnect() {
        if (this.channel != null && this.channel.isOpen() && player != null) {
            player.requestLogout();
        }
    }

    public void clearQueues() {
        outboundPacketsQueue.clear();
    }

    public void flushQueuedPackets() {
        final Channel channel = this.channel;
        final MessagePassingQueue<PacketBuilder> outboundPacketsQueue = this.outboundPacketsQueue;
        while (!outboundPacketsQueue.isEmpty()) {
            PacketBuilder builder = outboundPacketsQueue.poll();
            if (builder == null) break;
            writeRaw(channel, builder);
        }
    }

    /**
     * Queues the {@code msg} for this session to be encoded and sent to the
     * client.
     *
     * @param builder the packet to queue.
     */
    public void write(final PacketBuilder builder) {
        final Channel channel = this.channel;
        if (channel == null || !channel.isOpen() || !channel.isActive())
            return;
        if (channel.isWritable()) {
            writeRaw(channel, builder);
        } else {
            outboundPacketsQueue.offer(builder);
        }
    }

    /**
     * Queues the {@code msg} for this session to be encoded and sent to the
     * client.
     *
     * @param builder the packet to queue.
     */
    private void writeRaw(final Channel channel, final PacketBuilder builder) {
        if (channel == null || !channel.isOpen() || !channel.isActive())
            return;

        try {
            final Packet packet = builder.toPacket();
            channel.write(packet, channel.voidPromise());
        } catch (Exception e) {
            logger.error("sadge", e);
        }
    }

    /**
     * Flushes this channel.
     */
    public void flush() {
        final Channel channel = this.channel;
        if (channel == null || !channel.isOpen() || !channel.isActive())
            return;

        channel.flush();
    }

    @Nullable
    public Channel getChannel() {
        return channel;
    }

    @Override
    public String toString() {
        return "Session{" +
            "player=" + player +
            ", state=" + state +
            '}';
    }

    public boolean read() {
        final Channel channel = this.channel;
        if (channel.isActive()) {
            channel.read();
            return true;
        }
        return false;
    }

}
