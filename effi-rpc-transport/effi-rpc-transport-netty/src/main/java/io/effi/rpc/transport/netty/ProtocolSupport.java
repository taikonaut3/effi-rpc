package io.effi.rpc.transport.netty;

import io.effi.rpc.common.constant.Constant;
import io.effi.rpc.common.constant.KeyConstant;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.common.url.URLType;
import io.effi.rpc.transport.client.Client;
import io.effi.rpc.transport.netty.client.NettyClient;
import io.effi.rpc.transport.netty.client.NettyPoolClient;
import io.effi.rpc.transport.netty.protocol.http.h1.client.Http1ClientChannelPoolHandler;
import io.effi.rpc.transport.netty.protocol.http.h1.client.NettyHttp1Client;
import io.effi.rpc.transport.netty.protocol.http.h1.server.Http1ServerChannelInitializer;
import io.effi.rpc.transport.netty.protocol.http.h1.server.NettyHttp1Server;
import io.effi.rpc.transport.netty.protocol.http.h2.client.Http2ClientChannelInitializer;
import io.effi.rpc.transport.netty.protocol.http.h2.client.Http2ClientChannelPoolInitializer;
import io.effi.rpc.transport.netty.protocol.http.h2.client.NettyHttp2Client;
import io.effi.rpc.transport.netty.protocol.http.h2.client.NettyHttp2PoolClient;
import io.effi.rpc.transport.netty.protocol.http.h2.server.Http2ServerChannelInitializer;
import io.effi.rpc.transport.netty.protocol.http.h2.server.NettyHttp2Server;
import io.effi.rpc.transport.netty.protocol.tcp.TcpChannelInitializer;
import io.effi.rpc.transport.netty.protocol.tcp.TcpClientChannelPoolHandler;
import io.effi.rpc.transport.netty.server.NettyServer;
import io.effi.rpc.transport.server.Server;
import io.netty.channel.*;
import io.netty.channel.pool.ChannelPoolHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http2.*;
import io.netty.handler.ssl.SslContext;

import static io.effi.rpc.common.constant.Component.Protocol.*;

/**
 * Initializer SocketChannel for protocol.
 */
public class ProtocolSupport {

    /**
     * Builds {@link ChannelInitializer} for client.
     *
     * @param config
     * @return
     */
    public static ChannelInitializer<SocketChannel> buildClientChannelInitializer(InitializedConfig config) {
        URL url = config.url();
        String protocol = url.protocol();
        // http1.1 use HttpChannelPoolHandler init channel
        return switch (protocol) {
            case H2, H2C -> new Http2ClientChannelInitializer(config);
            default -> new TcpChannelInitializer(config, false);
        };
    }

    /**
     * Builds {@link ChannelInitializer} for server.
     *
     * @param config
     * @return
     */
    public static ChannelInitializer<SocketChannel> buildServerChannelInitializer(InitializedConfig config) {
        URL url = config.url();
        String protocol = url.protocol();
        return switch (protocol) {
            case HTTP, HTTPS -> new Http1ServerChannelInitializer(config);
            case H2, H2C -> new Http2ServerChannelInitializer(config);
            default -> new TcpChannelInitializer(config, true);
        };
    }

    /**
     * Builds {@link ChannelPoolHandler} for client.
     *
     * @param config
     * @param client
     * @return
     */
    public static ChannelPoolHandler buildClientChannelPoolHandler(InitializedConfig config, NettyPoolClient client) {
        URL url = config.url();
        String protocol = url.protocol();
        return switch (protocol) {
            case HTTP, HTTPS -> new Http1ClientChannelPoolHandler(config, client);
            case H2, H2C -> new Http2ClientChannelPoolInitializer(config);
            default -> new TcpClientChannelPoolHandler(config);
        };
    }

    /**
     * Builds server.
     *
     * @param url
     * @param handler
     * @return
     */
    public static Server bindServer(URL url, io.effi.rpc.transport.channel.ChannelHandler handler) {
        String protocol = url.protocol();
        return switch (protocol) {
            case HTTP, HTTPS -> new NettyHttp1Server(url, handler);
            case H2, H2C -> new NettyHttp2Server(url, handler);
            default -> new NettyServer(url, handler);
        };
    }

    /**
     * Builds client.
     *
     * @param url
     * @param handler
     * @return
     */
    public static Client connectClient(URL url, io.effi.rpc.transport.channel.ChannelHandler handler) {
        String protocol = url.protocol();
        int maxConnections = url.getIntParam(KeyConstant.MAX_CONNECTIONS, 1);
        return switch (protocol) {
            case HTTP, HTTPS -> new NettyHttp1Client(url, handler);
            case H2, H2C -> maxConnections == 1
                    ? new NettyHttp2Client(url, handler)
                    : new NettyHttp2PoolClient(url, handler);
            default -> maxConnections == 1
                    ? new NettyClient(url, handler)
                    : new NettyPoolClient(url, handler);
        };
    }

    /**
     * Configures Http2 client pipeline.
     *
     * @param channel
     * @param config
     * @param handler
     */
    public static void configHttp2ClientPipeline(Channel channel, InitializedConfig config, ChannelHandler handler) {
        ChannelPipeline pipeline = channel.pipeline();
        SslContext sslContext = config.sslContext();
        NettyIdleStateHandler idleStateHandler = NettyIdleStateHandler.createForClient(config.url());
        if (sslContext != null) {
            pipeline.addLast(HandlerNames.SSL, sslContext.newHandler(channel.alloc()));
        }
        final Http2FrameCodec http2FrameCodec = Http2FrameCodecBuilder.forClient()
                // this is the default, but shows it can be changed.
                .initialSettings(buildHttp2Settings(config))
                .build();
        pipeline.addLast(HandlerNames.IDLE_STATE, idleStateHandler)
                .addLast(HandlerNames.HEARTBEAT, idleStateHandler.handler())
                .addLast("http2ClientFrameCodec", http2FrameCodec)
                // this parameter ChannelInboundHandlerAdapter is Invalid for client
                .addLast(new Http2MultiplexHandler(new ChannelInboundHandlerAdapter()));
        var streamChannelBootstrap = new Http2StreamChannelBootstrap(channel);
        streamChannelBootstrap.handler(handler);
        channel.attr(NettySupport.H2_STREAM_BOOTSTRAP_KEY).set(streamChannelBootstrap);
    }

    /**
     * Builds Http2Settings.
     *
     * @param config
     * @return
     */
    public static Http2Settings buildHttp2Settings(InitializedConfig config) {
        URL url = config.url();
        int initialWindows = url.getIntParam(KeyConstant.INITIAL_WINDOW_SIZE,
                Constant.DEFAULT_INITIAL_WINDOW_SIZE);
        int maxConcurrentStreams = url.getIntParam(KeyConstant.MAX_CONCURRENT_STREAMS,
                Constant.DEFAULT_MAX_CONCURRENT_STREAMS);
        int maxFrameSize = url.getIntParam(KeyConstant.MAX_FRAME_SIZE,
                Constant.DEFAULT_MAX_FRAME_SIZE);
        int maxHeaderListSize = url.getIntParam(KeyConstant.MAX_HEADER_LIST_SIZE,
                Constant.DEFAULT_MAX_HEADER_LIST_SIZE);
        int headerTableSize = url.getIntParam(KeyConstant.HEADER_TABLE_SIZE,
                Constant.DEFAULT_MAX_HEADER_TABLE_SIZE);
        Http2Settings settings = new Http2Settings();
        settings.initialWindowSize(initialWindows);
        settings.maxConcurrentStreams(maxConcurrentStreams);
        settings.maxFrameSize(maxFrameSize);
        settings.maxHeaderListSize(maxHeaderListSize);
        settings.headerTableSize(headerTableSize);
        if (URLType.CLIENT.valid(config.url())) {
            boolean pushEnabled = url.getBooleanParam(KeyConstant.PUSH_ENABLED, false);
            settings.pushEnabled(pushEnabled);
        }
        return settings;
    }

}