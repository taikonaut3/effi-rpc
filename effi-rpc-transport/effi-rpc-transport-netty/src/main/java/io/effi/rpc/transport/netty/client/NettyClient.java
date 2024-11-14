package io.effi.rpc.transport.netty.client;

import io.effi.rpc.common.constant.Constant;
import io.effi.rpc.common.exception.ConnectException;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.transport.channel.ChannelHandler;
import io.effi.rpc.transport.client.AbstractClient;
import io.effi.rpc.transport.netty.InitializedConfig;
import io.effi.rpc.transport.netty.NettyChannelObtain;
import io.effi.rpc.transport.netty.ProtocolSupport;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;

import java.util.concurrent.TimeUnit;

/**
 * {@link io.effi.rpc.transport.client.Client} implementation based on Netty.
 */
public class NettyClient extends AbstractClient implements NettyChannelObtain {

    protected static final NioEventLoopGroup NIO_EVENT_LOOP_GROUP = new NioEventLoopGroup(
            Constant.DEFAULT_IO_THREADS, new DefaultThreadFactory("netty-client-worker", true)
    );

    protected Bootstrap bootstrap;

    protected Channel channel;

    protected InitializedConfig initializedConfig;

    public NettyClient(URL url, ChannelHandler channelHandler) throws ConnectException {
        super(url, channelHandler);
    }

    /**
     * Closes the NioEventLoopGroup gracefully, releasing all resources.
     */
    public static void closeNioEventLoopGroup() {
        NIO_EVENT_LOOP_GROUP.shutdownGracefully();
    }

    @Override
    protected void doInit() throws ConnectException {
        var handler = new NettyClientChannelHandler(super.channelHandler, url);
        initializedConfig = new InitializedConfig(url, handler);
        bootstrap = new Bootstrap();
        // Configure the bootstrap options
        bootstrap.group(NIO_EVENT_LOOP_GROUP)
                .channel(NioSocketChannel.class)
                .remoteAddress(inetSocketAddress())
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeout)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        configHandler(bootstrap, initializedConfig);
    }

    protected void configHandler(Bootstrap bootstrap, InitializedConfig initializedConfig) {
        bootstrap.handler(ProtocolSupport.buildClientChannelInitializer(initializedConfig));
    }

    @Override
    protected void doConnect() throws ConnectException {
        ChannelFuture future = bootstrap.connect();
        // Wait for the connection to complete
        boolean success = future.awaitUninterruptibly(connectTimeout, TimeUnit.MILLISECONDS);
        // Check the outcome of the connection attempt
        if (success && future.isSuccess()) {
            channel = future.channel();
            // Wrap the channel
            super.channel = acquireChannel(channel);
        } else if (future.cause() != null) {
            throw new ConnectException(future.cause());
        } else {
            throw new ConnectException("Connect to " + url.address() + " timeout");
        }
    }

    @Override
    public boolean isActive() {
        return channel != null && channel.isActive();
    }

    @Override
    public URL endpointUrl() {
        return url;
    }
}

