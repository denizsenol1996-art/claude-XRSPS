package com.twisted.net.codec;

import com.twisted.net.NetworkUtils;
import com.twisted.net.Session;
import com.twisted.net.packet.ClientToServerPackets;
import com.twisted.net.packet.Packet;
import com.twisted.net.security.IsaacRandom;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


/**
 * Decodes packets that are received from the player's channel.
 * These packets are received from the client C2S packets.
 * These are the packets from the Clients "PacketSender".
 * Those packets need to have their sizes written in the array below.
 *
 * Size calculations: byte is 1, short is 2, int is 4, long is 8, String is -1 and -3 is skip.
 *
 * @author Swiffy
 */
public final class PacketDecoder extends ByteToMessageDecoder {

    private static final int SINGLE_PACKET_MAX_ACCEPTED_LENGTH = 1_600;

    private static final Logger logger = LoggerFactory.getLogger(PacketDecoder.class);

    private enum State {
        READ_OPCODE,
        READ_LENGTH,
        READ_PAYLOAD,
    }

    private final IsaacRandom streamCipher;

    private State state = State.READ_OPCODE;
    private int opcode = -1;
    private int length = 0;

    public PacketDecoder(IsaacRandom streamCipher) {
        this.streamCipher = streamCipher;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf input, List<Object> out) {
        if (state == State.READ_OPCODE) {
            if (!input.isReadable()) {
                return;
            }

            this.opcode = (input.readUnsignedByte() - streamCipher.nextInt()) & 0xFF;
            this.length = ClientToServerPackets.PACKET_SIZES[opcode];
            state = this.length >= 0 ? State.READ_PAYLOAD : State.READ_LENGTH;
        }

        if (state == State.READ_LENGTH) {
            switch (length) {
                case -1 -> {
                    if (!input.isReadable(Byte.BYTES)) return;
                    this.length = input.readUnsignedByte();
                }
                case -2 -> {
                    if (!input.isReadable(Short.BYTES)) return;
                    this.length = input.readUnsignedShort();
                }
                case -3 -> {
                    final Channel channel = ctx.channel();
                    if (channel == null) return;
                    final int readable = input.readableBytes();
                    input.skipBytes(readable);
                    channel.close();
                    return;
                }
                default -> throw new RuntimeException("Error handling packet processing, falling back to default throw.");
            }

            if (this.isInvalidLength(ctx, input)) {
                return;
            }

            state = State.READ_PAYLOAD;
        }

        if (state == State.READ_PAYLOAD) {
            if (!input.isReadable(length)) {
                return;
            }

            decodePayload(ctx, input);
            state = State.READ_OPCODE;
        }
    }

    boolean isInvalidLength(ChannelHandlerContext ctx, final ByteBuf buffer) {
        if (length > SINGLE_PACKET_MAX_ACCEPTED_LENGTH) {
            buffer.skipBytes(buffer.readableBytes());

            final Channel channel = ctx.channel();
            if (channel == null || !channel.isActive()) return true;
            channel.close();
            return true;
        }
        return false;
    }

    private void decodePayload(final ChannelHandlerContext ctx, final ByteBuf input) {
        final Channel channel = ctx.channel();
        if (channel == null || !channel.isActive()) return;

        final Session session = channel.attr(NetworkUtils.SESSION_KEY).get();
        if (session == null) return;

        try {
            // Define and initialize length based on your logic
            final int length = this.length; // Ensure length is properly set based on the protocol or context

            // Ensure that the input buffer has enough readable bytes
            if (input.readableBytes() < length) {
                throw new IllegalStateException("Insufficient bytes in input buffer. Required length: " + length);
            }

            // Slice the input buffer to get the payload
            ByteBuf payload = input.readRetainedSlice(length);

            // Check that the payload buffer has enough readable bytes
            if (!payload.isReadable(length)) {
                throw new IllegalStateException("Payload buffer is not readable for the expected length: " + length);
            }

            // Create the Packet with the sliced payload
            final Packet packet = new Packet(opcode, payload);

            // No need to check readability again if the length has been validated before
            // Queue the packet for processing
            session.queuePacket(packet);

        } catch (Throwable t) {
            // Include additional context for debugging
            throw new IllegalStateException("Exception occurred while handling packet. OpCode=" + opcode + " size=" + length, t);
        }
    }

}
