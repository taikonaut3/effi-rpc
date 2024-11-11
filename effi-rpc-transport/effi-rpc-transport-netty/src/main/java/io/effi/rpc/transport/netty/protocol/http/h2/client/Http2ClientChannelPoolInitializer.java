package io.effi.rpc.transport.netty.protocol.http.h2.client;

import io.effi.rpc.transport.netty.InitializedConfig;
import io.effi.rpc.transport.netty.ProtocolSupport;
import io.netty.channel.Channel;
import io.netty.channel.pool.AbstractChannelPoolHandler;

/**
 * HttpClient ChannelInitializer.
 */
public class Http2ClientChannelPoolInitializer extends AbstractChannelPoolHandler {

    private final InitializedConfig config;

    private final Http2ClientHandler handler;

    public Http2ClientChannelPoolInitializer(InitializedConfig config) {
        this.config = config;
        this.handler = new Http2ClientHandler(config);
    }

    @Override
    public void channelCreated(Channel channel) throws Exception {
        ProtocolSupport.configHttp2ClientPipeline(channel, config, handler);
    }

}
