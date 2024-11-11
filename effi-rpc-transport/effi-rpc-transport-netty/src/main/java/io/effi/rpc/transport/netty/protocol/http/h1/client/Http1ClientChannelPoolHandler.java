package io.effi.rpc.transport.netty.protocol.http.h1.client;

import io.effi.rpc.common.constant.Constant;
import io.effi.rpc.common.constant.KeyConstant;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.transport.netty.HandlerNames;
import io.effi.rpc.transport.netty.InitializedConfig;
import io.effi.rpc.transport.netty.NettyIdleStateHandler;
import io.effi.rpc.transport.netty.client.NettyPoolClient;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.pool.AbstractChannelPoolHandler;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.ssl.SslContext;

/**
 * HttpClient ChannelInitializer.
 */
public class Http1ClientChannelPoolHandler extends AbstractChannelPoolHandler {

    private final InitializedConfig config;

    private final ChannelHandler clientHandler;

    public Http1ClientChannelPoolHandler(InitializedConfig config, NettyPoolClient client) {
        this.config = config;
        this.clientHandler = new Http1ClientHandler(client);
    }

    @Override
    public void channelCreated(Channel channel) throws Exception {
        URL url = config.url();
        NettyIdleStateHandler idleStateHandler = NettyIdleStateHandler.createForClient(url);
        ChannelPipeline pipeline = channel.pipeline();
        int maxReceiveSize = url.getIntParam(KeyConstant.CLIENT_MAX_RECEIVE_SIZE, Constant.DEFAULT_MAX_MESSAGE_SIZE);
        SslContext sslContext = config.sslContext();
        if (sslContext != null) {
            pipeline.addLast(HandlerNames.SSL, sslContext.newHandler(channel.alloc()));
        }
        pipeline.addLast(HandlerNames.IDLE_STATE, idleStateHandler)
                .addLast(HandlerNames.HEARTBEAT, idleStateHandler.handler())
                .addLast("httpClientCodec", new HttpClientCodec())
                .addLast("aggregator", new HttpObjectAggregator(maxReceiveSize))
                .addLast("httpClientHandler", clientHandler)
                .addLast(HandlerNames.ADAPTER, config.handler());
    }

}
