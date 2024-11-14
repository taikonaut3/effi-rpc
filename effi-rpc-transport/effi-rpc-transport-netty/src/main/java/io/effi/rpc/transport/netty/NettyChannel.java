package io.effi.rpc.transport.netty;

import io.effi.rpc.common.exception.NetWorkException;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.internal.logging.Logger;
import io.effi.rpc.internal.logging.LoggerFactory;
import io.effi.rpc.transport.channel.AbstractChannel;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * {@link io.effi.rpc.transport.channel.Channel} implementation based on netty's {@link Channel}.
 */
public final class NettyChannel extends AbstractChannel {

    private static final Logger logger = LoggerFactory.getLogger(NettyChannel.class);

    private static final ConcurrentMap<Channel, NettyChannel> CHANNEL_MAP = new ConcurrentHashMap<>();

    private final Channel channel;

    private NettyChannel(InetSocketAddress localAddress, InetSocketAddress remoteAddress,
                         URL endpointUrl, Channel channel) {
        super(localAddress, remoteAddress, endpointUrl);
        this.channel = channel;
    }

    /**
     * Acquires a Channel instance from the given netty channel and endpoint's url.
     *
     * @param channel
     * @param endpointUrl
     * @return
     */
    public static NettyChannel acquire(Channel channel, URL endpointUrl) {
        return CHANNEL_MAP.computeIfAbsent(channel, key -> {
            InetSocketAddress localAddress = (InetSocketAddress) channel.localAddress();
            InetSocketAddress remoteAddress = (InetSocketAddress) channel.remoteAddress();
            return new NettyChannel(localAddress, remoteAddress, endpointUrl, channel);
        });
    }

    public static void remove(Channel channel) {
        if (channel != null) {
            if (channel.isActive()) {
                channel.close();
            }
            CHANNEL_MAP.remove(channel);
        }
    }

    @Override
    public void doClose() throws NetWorkException {
        try {
            ChannelFuture channelFuture = channel.close();
            channelFuture.addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()) {
                    if (CHANNEL_MAP.containsKey(channel) && CHANNEL_MAP.remove(channel, this)) {
                        logger.debug("{} closed", this);
                    }
                } else {
                    logger.error(this + " closure failed", future.cause());
                }
            });
        } catch (Throwable e) {
            throw new NetWorkException(e);
        }
    }

    @Override
    public boolean isActive() {
        return channel.isActive();
    }

    @Override
    public void send(Object message) throws NetWorkException {
        if (isActive()) {
            channel.writeAndFlush(message);
        } else {
            logger.warn("Current channel: {} is closed", this);
        }
    }

    public Channel nettyChannel() {
        return channel;
    }
}
