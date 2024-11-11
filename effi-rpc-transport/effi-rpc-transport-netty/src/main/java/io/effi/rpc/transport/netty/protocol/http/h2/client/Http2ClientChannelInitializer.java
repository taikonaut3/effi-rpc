package io.effi.rpc.transport.netty.protocol.http.h2.client;

import io.effi.rpc.transport.netty.InitializedConfig;
import io.effi.rpc.transport.netty.ProtocolSupport;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * Initializes the channel of Netty for HTTP2 Codec.
 */
public class Http2ClientChannelInitializer extends ChannelInitializer<SocketChannel> {

    private final InitializedConfig config;

    private final Http2ClientHandler handler;

    public Http2ClientChannelInitializer(InitializedConfig config) {
        this.config = config;
        this.handler = new Http2ClientHandler(config);
    }

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        ProtocolSupport.configHttp2ClientPipeline(channel, config, handler);
    }
}
