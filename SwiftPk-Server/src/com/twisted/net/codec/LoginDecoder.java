package com.twisted.net.codec;

import com.twisted.net.ByteBufUtils;
import com.twisted.net.HostBlacklist;
import com.twisted.net.NetworkUtils;
import com.twisted.net.login.LoginDetailsMessage;
import com.twisted.net.login.LoginResponses;
import com.twisted.net.security.IsaacRandom;
import com.twisted.util.Utils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import save.Rsa;

import java.net.InetSocketAddress;
import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

import static org.apache.logging.log4j.util.Unbox.box;

/**
 * Attempts to decode a player's login request.
 *
 * @author Professor Oak
 */
public final class LoginDecoder extends ByteToMessageDecoder {

    private static final Logger logger = LogManager.getLogger(LoginDecoder.class);

    /**
     * Generates random numbers via secure cryptography. Generates the session key
     * for packet encryption.
     */
    private static final Random random = new SecureRandom();

    /**
     * The size of the encrypted data.
     */
    private int encryptedLoginBlockSize;

    /**
     * The current login decoder state
     */
    private LoginDecoderState state = LoginDecoderState.LOGIN_REQUEST;

    /**
     * Sends a response code to the client to notify the user logging in.
     *
     * @param ctx The context of the channel handler.
     * @param response The response code to send.
     */

    public static void sendCodeAndClose(final ChannelHandlerContext ctx, final int response) {
        if (ctx == null || ctx.isRemoved()) return;

        final Channel channel = ctx.channel();

        if (channel == null) {
            ctx.close();
            return;
        }

        ByteBuf buffer = ctx.alloc().buffer(Byte.BYTES);
        if (buffer.isWritable()) {
            buffer.writeByte(response);
            ctx.writeAndFlush(buffer).addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception {
        if (ctx == null || ctx.isRemoved())
            return;

        if (!buffer.isReadable()) {
            closeChannel(ctx, "Buffer not readable");
            return;
        }

        switch (state) {

            case LOGIN_REQUEST:
                decodeRequest(ctx, buffer);
                break;

            case LOGIN_TYPE_AND_SIZE:
                decodeTypeAndSize(ctx, buffer);
                break;

            case LOGIN:
                decodeLogin(ctx, buffer, out);
                break;
        }
    }

    private void decodeRequest(ChannelHandlerContext ctx, ByteBuf buffer) {
        if (ctx == null || !buffer.isReadable() || ctx.isRemoved()) {
            closeChannel(ctx, "Context removed or buffer not readable");
            return;
        }

        int loginOpcode = buffer.readUnsignedByte();
        if (loginOpcode != NetworkUtils.LOGIN_REQUEST_OPCODE) {
            closeChannel(ctx, "Invalid login opcode");
            return;
        }

        // Send information to the client
        ByteBuf buf = Unpooled.buffer(Byte.BYTES + Long.BYTES);
        buf.writeByte(0); // 0 = continue login
        buf.writeLong(random.nextLong()); // This long will be used for
        // encryption later on
        ctx.writeAndFlush(buf);

        state = LoginDecoderState.LOGIN_TYPE_AND_SIZE;
    }

    private void decodeTypeAndSize(ChannelHandlerContext ctx, ByteBuf buffer) {
        if (ctx == null || ctx.isRemoved() || !buffer.isReadable(2)) {
            closeChannel(ctx, "Context removed or buffer not readable for type and size");
            return;
        }

        int connectionType = buffer.readUnsignedByte();
        if (connectionType != NetworkUtils.NEW_CONNECTION_OPCODE && connectionType != NetworkUtils.RECONNECTION_OPCODE) {
            closeChannel(ctx, "Failed to decode type and size");
            return;
        }

        encryptedLoginBlockSize = buffer.readUnsignedByte();
        state = LoginDecoderState.LOGIN;
    }

    private void decodeLogin(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) {
        if (ctx == null || ctx.isRemoved())
            return;

        if (encryptedLoginBlockSize != buffer.readableBytes()) {
            closeChannel(ctx, "Encrypted login block size mismatch");
            return;
        }

        if (!buffer.isReadable(encryptedLoginBlockSize)) {
            closeChannel(ctx, "Buffer not readable for encrypted login block size");
            return;
        }

        if (buffer.readerIndex() > buffer.writerIndex()) {
            buffer.skipBytes(buffer.readableBytes());
            closeChannel(ctx, "Buffer reader index greater than writer index");
            return;
        }

        // obviously adjust the indentation below:
        int magicId = buffer.readUnsignedByte();
        if (magicId != 0xFF) {
            sendCodeAndClose(ctx, LoginResponses.LOGIN_REJECT_SESSION);
            return;
        }

        int memory = buffer.readByte();
        if (memory != 0 && memory != 1) {
            sendCodeAndClose(ctx, LoginResponses.LOGIN_REJECT_SESSION);
            return;
        }

        buffer.markReaderIndex();
        int length = buffer.readUnsignedByte();
        if (buffer.readableBytes() < length) {
            buffer.resetReaderIndex();
            return;
        }

        ByteBuf rsaBuffer = Rsa.rsa(buffer.readSlice(length));
        if (rsaBuffer == null || !rsaBuffer.isReadable()) {
            if (rsaBuffer != null) rsaBuffer.release();
            sendCodeAndClose(ctx, LoginResponses.LOGIN_REJECT_SESSION);
            return;
        }

        int securityId = rsaBuffer.readByte();
        if (securityId != 10) {
            sendCodeAndClose(ctx, LoginResponses.LOGIN_REJECT_SESSION);
            return;
        }

        long clientSeed = rsaBuffer.readLong();
        long seedReceived = rsaBuffer.readLong();

        int[] seed = {(int) (clientSeed >> 32), (int) clientSeed, (int) (seedReceived >> 32), (int) seedReceived};
        IsaacRandom decodingRandom = new IsaacRandom(seed);
        for (int i = 0; i < seed.length; i++) {
            seed[i] += 50;
        }

        String uid = ByteBufUtils.readString(rsaBuffer);
        String username = Utils.formatText(ByteBufUtils.readString(rsaBuffer));
        String password = ByteBufUtils.readString(rsaBuffer);
        String mac = ByteBufUtils.readString(rsaBuffer);
        rsaBuffer.release();

        if (username.isEmpty() || username.length() > 12 || password.length() < 3 || password.length() > 20) {
            sendCodeAndClose(ctx, LoginResponses.INVALID_CREDENTIALS_COMBINATION);
            return;
        }

        String hostName = ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostName();

        if(HostBlacklist.isBlocked(hostName)) {
            sendCodeAndClose(ctx, LoginResponses.LOGIN_REJECT_SESSION);
            return;
        }

        out.add(new LoginDetailsMessage(ctx, username, password, ByteBufUtils.getHost(ctx.channel()), mac, uid, new IsaacRandom(seed), decodingRandom));
    }

    private void closeChannel(ChannelHandlerContext ctx, String reason) {
        if (ctx != null && ctx.channel() != null && ctx.channel().isOpen()) {
            ctx.channel().close();
        }
    }

    private enum LoginDecoderState {
        LOGIN_REQUEST, LOGIN_TYPE_AND_SIZE, LOGIN;
    }
}
