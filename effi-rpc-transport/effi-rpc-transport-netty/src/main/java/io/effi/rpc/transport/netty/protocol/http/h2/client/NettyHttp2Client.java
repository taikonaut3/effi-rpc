package io.effi.rpc.transport.netty.protocol.http.h2.client;

import io.effi.rpc.common.exception.ConnectException;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.transport.channel.Channel;
import io.effi.rpc.transport.channel.ChannelHandler;
import io.effi.rpc.transport.netty.NettySupport;
import io.effi.rpc.transport.netty.client.NettyClient;
import io.netty.handler.codec.http2.Http2StreamChannelBootstrap;

/**
 * Http2Client Base on Netty.
 */
public class NettyHttp2Client extends NettyClient {

    private Http2StreamChannelBootstrap streamChannelBootstrap;

    public NettyHttp2Client(URL url, ChannelHandler channelHandler) throws ConnectException {
        super(url, channelHandler);

    }

    @Override
    protected void doConnect() throws ConnectException {
        super.doConnect();
        streamChannelBootstrap = channel.attr(NettySupport.H2_STREAM_BOOTSTRAP_KEY).get();
    }

    @Override
    public Channel channel() {
        return NettySupport.acquireStreamChannel(streamChannelBootstrap, url, connectTimeout);
    }

}
