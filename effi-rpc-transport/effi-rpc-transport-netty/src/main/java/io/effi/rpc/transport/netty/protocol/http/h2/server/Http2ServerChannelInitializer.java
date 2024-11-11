package io.effi.rpc.transport.netty.protocol.http.h2.server;

import io.effi.rpc.common.url.URL;
import io.effi.rpc.transport.netty.HandlerNames;
import io.effi.rpc.transport.netty.InitializedConfig;
import io.effi.rpc.transport.netty.NettyIdleStateHandler;
import io.effi.rpc.transport.netty.ProtocolSupport;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http2.Http2FrameCodecBuilder;
import io.netty.handler.ssl.SslContext;

/**
 * Initializes the channel of Netty for HTTP2 Codec.
 */
public class Http2ServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    private final InitializedConfig config;

    public Http2ServerChannelInitializer(InitializedConfig config) {
        this.config = config;
    }

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        URL url = config.url();
        ChannelPipeline pipeline = channel.pipeline();
        NettyIdleStateHandler idleStateHandler = NettyIdleStateHandler.createForServer(url);
        SslContext sslContext = config.sslContext();
        if (sslContext != null) {
            pipeline.addLast(HandlerNames.SSL, sslContext.newHandler(channel.alloc()));
        }
        pipeline.addLast(HandlerNames.IDLE_STATE, idleStateHandler)
                .addLast(HandlerNames.HEARTBEAT, idleStateHandler.handler())
                .addLast("http2FrameServerCodec", Http2FrameCodecBuilder.forServer()
                        .initialSettings(ProtocolSupport.buildHttp2Settings(config))
                        .build())
                .addLast("http2serverHandler", new Http2ServerHandler(config))
                .addLast(HandlerNames.ADAPTER, config.handler());
    }

}