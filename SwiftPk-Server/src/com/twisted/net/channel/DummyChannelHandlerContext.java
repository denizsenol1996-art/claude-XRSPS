package com.twisted.net.channel;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelProgressivePromise;
import io.netty.channel.ChannelPromise;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.jetbrains.annotations.NotNull;

import java.net.SocketAddress;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * for bots
 * @author shadowrs
 */
public class DummyChannelHandlerContext implements ChannelHandlerContext {

    public static final ChannelHandlerContext DUMMY = new DummyChannelHandlerContext();

    @Override
    public Channel channel() {
        return null;
    }

    @Override
    public EventExecutor executor() {
        return null;
    }

    @Override
    public String name() {
        return "null";
    }

    @Override
    public ChannelHandler handler() {
        return null;
    }

    @Override
    public boolean isRemoved() {
        return false;
    }

    @Override
    public ChannelHandlerContext fireChannelRegistered() {
        return DUMMY;
    }

    @Override
    public ChannelHandlerContext fireChannelUnregistered() {
        return DUMMY;
    }

    @Override
    public ChannelHandlerContext fireChannelActive() {
        return DUMMY;
    }

    @Override
    public ChannelHandlerContext fireChannelInactive() {
        return DUMMY;
    }

    @Override
    public ChannelHandlerContext fireExceptionCaught(Throwable throwable) {
        return DUMMY;
    }

    @Override
    public ChannelHandlerContext fireUserEventTriggered(Object o) {
        return DUMMY;
    }

    @Override
    public ChannelHandlerContext fireChannelRead(Object o) {
        return DUMMY;
    }

    @Override
    public ChannelHandlerContext fireChannelReadComplete() {
        return DUMMY;
    }

    @Override
    public ChannelHandlerContext fireChannelWritabilityChanged() {
        return DUMMY;
    }

    @Override
    public ChannelFuture bind(SocketAddress socketAddress) {
        return DUMMY_FUTURE;
    }

    @Override
    public ChannelFuture connect(SocketAddress socketAddress) {
        return DUMMY_FUTURE;
    }

    @Override
    public ChannelFuture connect(SocketAddress socketAddress, SocketAddress socketAddress1) {
        return DUMMY_FUTURE;
    }

    @Override
    public ChannelFuture disconnect() {
        return DUMMY_FUTURE;
    }

    @Override
    public ChannelFuture close() {
        return DUMMY_FUTURE;
    }

    @Override
    public ChannelFuture deregister() {
        return DUMMY_FUTURE;
    }

    @Override
    public ChannelFuture bind(SocketAddress socketAddress, ChannelPromise channelPromise) {
        return DUMMY_FUTURE;
    }

    @Override
    public ChannelFuture connect(SocketAddress socketAddress, ChannelPromise channelPromise) {
        return DUMMY_FUTURE;
    }

    @Override
    public ChannelFuture connect(SocketAddress socketAddress, SocketAddress socketAddress1, ChannelPromise channelPromise) {
        return DUMMY_FUTURE;
    }

    @Override
    public ChannelFuture disconnect(ChannelPromise channelPromise) {
        return DUMMY_FUTURE;
    }

    @Override
    public ChannelFuture close(ChannelPromise channelPromise) {
        return DUMMY_FUTURE;
    }

    @Override
    public ChannelFuture deregister(ChannelPromise channelPromise) {
        return DUMMY_FUTURE;
    }

    @Override
    public ChannelHandlerContext read() {
        return DUMMY;
    }

    @Override
    public ChannelFuture write(Object o) {
        return DUMMY_FUTURE;
    }

    @Override
    public ChannelFuture write(Object o, ChannelPromise channelPromise) {
        return DUMMY_FUTURE;
    }

    @Override
    public ChannelHandlerContext flush() {
        return DUMMY;
    }

    @Override
    public ChannelFuture writeAndFlush(Object o, ChannelPromise channelPromise) {
        return DUMMY_FUTURE;
    }

    @Override
    public ChannelFuture writeAndFlush(Object o) {
        return DUMMY_FUTURE;
    }

    @Override
    public ChannelPromise newPromise() {
        return null;
    }

    @Override
    public ChannelProgressivePromise newProgressivePromise() {
        return null;
    }

    @Override
    public ChannelFuture newSucceededFuture() {
        return DUMMY_FUTURE;
    }

    @Override
    public ChannelFuture newFailedFuture(Throwable throwable) {
        return DUMMY_FUTURE;
    }

    @Override
    public ChannelPromise voidPromise() {
        return null;
    }

    @Override
    public ChannelPipeline pipeline() {
        return null;
    }

    @Override
    public ByteBufAllocator alloc() {
        return null;
    }

    @Override
    public <T> Attribute<T> attr(AttributeKey<T> attributeKey) {
        return null;
    }

    @Override
    public <T> boolean hasAttr(AttributeKey<T> attributeKey) {
        return false;
    }

    public static final ChannelFuture DUMMY_FUTURE = new ChannelFuture() {
        @Override
        public Channel channel() {
            return null;
        }

        @Override
        public ChannelFuture addListener(GenericFutureListener<? extends Future<? super Void>> genericFutureListener) {
            return DUMMY_FUTURE;
        }

        @Override
        public ChannelFuture addListeners(GenericFutureListener<? extends Future<? super Void>>... genericFutureListeners) {
            return DUMMY_FUTURE;
        }

        @Override
        public ChannelFuture removeListener(GenericFutureListener<? extends Future<? super Void>> genericFutureListener) {
            return DUMMY_FUTURE;
        }

        @Override
        public ChannelFuture removeListeners(GenericFutureListener<? extends Future<? super Void>>... genericFutureListeners) {
            return DUMMY_FUTURE;
        }

        @Override
        public ChannelFuture sync() throws InterruptedException {
            return DUMMY_FUTURE;
        }

        @Override
        public ChannelFuture syncUninterruptibly() {
            return DUMMY_FUTURE;
        }

        @Override
        public ChannelFuture await() throws InterruptedException {
            return DUMMY_FUTURE;
        }

        @Override
        public ChannelFuture awaitUninterruptibly() {
            return DUMMY_FUTURE;
        }

        @Override
        public boolean isVoid() {
            return false;
        }

        @Override
        public boolean isSuccess() {
            return false;
        }

        @Override
        public boolean isCancellable() {
            return false;
        }

        @Override
        public Throwable cause() {
            return null;
        }

        @Override
        public boolean await(long l, TimeUnit timeUnit) throws InterruptedException {
            return false;
        }

        @Override
        public boolean await(long l) throws InterruptedException {
            return false;
        }

        @Override
        public boolean awaitUninterruptibly(long l, TimeUnit timeUnit) {
            return false;
        }

        @Override
        public boolean awaitUninterruptibly(long l) {
            return false;
        }

        @Override
        public Void getNow() {
            return null;
        }

        @Override
        public boolean cancel(boolean b) {
            return false;
        }

        @Override
        public boolean isCancelled() {
            return false;
        }

        @Override
        public boolean isDone() {
            return false;
        }

        @Override
        public Void get() throws InterruptedException, ExecutionException {
            return null;
        }

        @Override
        public Void get(long timeout, @NotNull TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            return null;
        }
    };
}
