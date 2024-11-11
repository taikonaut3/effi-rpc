package io.effi.rpc.transport.netty.protocol.http.h1.server;

import io.effi.rpc.common.constant.Constant;
import io.effi.rpc.common.constant.KeyConstant;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.transport.netty.HandlerNames;
import io.effi.rpc.transport.netty.InitializedConfig;
import io.effi.rpc.transport.netty.NettyIdleStateHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;

/**
 * Initializes the channel of Netty for HTTP Codec.
 */
public class Http1ServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    private final InitializedConfig config;

    private final ChannelHandler serverHandler;

    public Http1ServerChannelInitializer(InitializedConfig config) {
        this.config = config;
        this.serverHandler = new Http1ServerHandler(config.url());
    }

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        URL url = config.url();
        NettyIdleStateHandler idleStateHandler = NettyIdleStateHandler.createForServer(url);
        ChannelPipeline pipeline = channel.pipeline();
        int maxReceiveSize = url.getIntParam(KeyConstant.MAX_RECEIVE_SIZE, Constant.DEFAULT_MAX_MESSAGE_SIZE);
        SslContext sslContext = config.sslContext();
        if (sslContext != null) {
            pipeline.addLast(HandlerNames.SSL, sslContext.newHandler(channel.alloc()));
        }
        pipeline.addLast(HandlerNames.IDLE_STATE, idleStateHandler)
                .addLast(HandlerNames.HEARTBEAT, idleStateHandler.handler())
                .addLast("httpServerCodec", new HttpServerCodec())
                .addLast("aggregator", new HttpObjectAggregator(maxReceiveSize))
                .addLast("httpServerHandler", serverHandler)
                .addLast(HandlerNames.ADAPTER, config.handler());
    }
}
