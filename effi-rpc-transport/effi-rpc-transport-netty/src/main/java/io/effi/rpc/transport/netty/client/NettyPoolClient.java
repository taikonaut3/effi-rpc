package io.effi.rpc.transport.netty.client;

import io.effi.rpc.common.constant.Constant;
import io.effi.rpc.common.constant.KeyConstant;
import io.effi.rpc.common.exception.ConnectException;
import io.effi.rpc.common.exception.NetWorkException;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.transport.channel.ChannelHandler;
import io.effi.rpc.transport.netty.InitializedConfig;
import io.effi.rpc.transport.netty.NettyChannel;
import io.effi.rpc.transport.netty.ProtocolSupport;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.pool.ChannelPoolHandler;
import io.netty.channel.pool.FixedChannelPool;
import io.netty.util.concurrent.Future;

import java.util.concurrent.TimeUnit;


/**
 * Netty-based client that utilizes a fixed channel pool for efficient connection management.
 * <p>
 * This implementation allows for the reuse of channels, reducing the overhead of creating and
 * destroying connections for each request. It uses a {@link FixedChannelPool} to manage
 * the channels, ensuring that a maximum number of connections is maintained.
 * </p>
 */
public class NettyPoolClient extends NettyClient {

    protected FixedChannelPool channelPool;

    public NettyPoolClient(URL url, ChannelHandler channelHandler) throws ConnectException {
        super(url, channelHandler);
    }

    @Override
    protected void configHandler(Bootstrap bootstrap, InitializedConfig initializedConfig) {
        // Acquire a ChannelPoolHandler for managing channels in the pool
        ChannelPoolHandler channelPoolHandler = ProtocolSupport.buildClientChannelPoolHandler(initializedConfig, this);
        // Set up the fixed channel pool with a maximum number of connections
        int maxConnections = url.getIntParam(KeyConstant.MAX_UN_CONNECTIONS, Constant.DEFAULT_CLIENT_MAX_CONNECTIONS);
        channelPool = new FixedChannelPool(bootstrap, channelPoolHandler, maxConnections);
    }

    @Override
    protected void doConnect() throws ConnectException {
        // Connection is handled by the channel pool; no direct connection is made here.
    }

    @Override
    public io.effi.rpc.transport.channel.Channel channel() {
        Future<Channel> future = channelPool.acquire();
        boolean success = future.awaitUninterruptibly(connectTimeout, TimeUnit.MILLISECONDS);
        // Check the outcome of acquiring a channel
        if (success && future.isSuccess()) {
            return acquireChannel(future.getNow());
        } else if (future.cause() != null) {
            throw new ConnectException(future.cause());
        } else {
            throw new ConnectException("Connect to " + url.address() + " timeout");
        }
    }

    @Override
    public void send(Object message) {
        NettyChannel channel = (NettyChannel) channel();
        Channel nettyChannel = channel.nettyChannel();
        channel.send(message);
        release(nettyChannel);
    }

    @Override
    public boolean isActive() {
        // Return the initialization status of the client
        return isInit;
    }

    @Override
    public void close() throws NetWorkException {
        // Close the channel pool, releasing all resources
        channelPool.close();
    }

    /**
     * Releases the specified channel back to the channel pool.
     *
     * @param channel The channel to be released.
     */
    public void release(Channel channel) {
        channelPool.release(channel);
    }
}

