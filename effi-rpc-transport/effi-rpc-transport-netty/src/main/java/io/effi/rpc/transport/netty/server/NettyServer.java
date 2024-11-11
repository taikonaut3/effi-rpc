package io.effi.rpc.transport.netty.server;

import io.effi.rpc.common.constant.Constant;
import io.effi.rpc.common.constant.KeyConstant;
import io.effi.rpc.common.exception.BindException;
import io.effi.rpc.common.exception.NetWorkException;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.transport.channel.ChannelHandler;
import io.effi.rpc.transport.netty.InitializedConfig;
import io.effi.rpc.transport.netty.NettyChannel;
import io.effi.rpc.transport.netty.ProtocolSupport;
import io.effi.rpc.transport.server.AbstractServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;

import java.util.concurrent.ThreadFactory;

/**
 * Base on netty server.
 */
public class NettyServer extends AbstractServer {

    protected Channel channel;

    protected InitializedConfig initializedConfig;

    private ServerBootstrap bootstrap;

    private NioEventLoopGroup bossGroup;

    private NioEventLoopGroup workerGroup;

    public NettyServer(URL endpointUrl, ChannelHandler handler) throws BindException {
        super(endpointUrl, handler);
    }

    @Override
    protected void doInit() throws BindException {
        int workThreads = url.getIntParam(KeyConstant.MAX_THREADS, Constant.DEFAULT_MAX_CPU_THREADS);
        int maxUnConnections = url.getIntParam(KeyConstant.MAX_UN_CONNECTIONS, Constant.DEFAULT_MAX_UN_CONNECTIONS);
        bootstrap = new ServerBootstrap();
        bossGroup = new NioEventLoopGroup(1, buildThreadFactory("server-boss"));
        workerGroup = new NioEventLoopGroup(workThreads, buildThreadFactory("server-worker"));
        var handler = new NettyServerChannelHandler(channelHandler, url);
        initializedConfig = new InitializedConfig(url, handler);
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, maxUnConnections)
                //.option(ChannelOption.TCP_FASTOPEN_CONNECT, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childHandler(ProtocolSupport.buildServerChannelInitializer(initializedConfig));
    }

    @Override
    protected void doBind() throws BindException {
        ChannelFuture future = bootstrap.bind(port());
        future.syncUninterruptibly();
        channel = future.channel();
    }

    @Override
    protected void doClose() throws NetWorkException {
        try {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            NettyChannel.remove(channel);
        } catch (Throwable e) {
            throw new NetWorkException(e);
        }

    }

    @Override
    public boolean isActive() {
        return channel != null && channel.isActive();
    }

    private ThreadFactory buildThreadFactory(String name) {
        String protocol = url.protocol();
        name = name + "-" + protocol;
        return new DefaultThreadFactory(name, true);
    }

}
