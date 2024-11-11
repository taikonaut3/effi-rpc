package io.effi.rpc.transport.netty.protocol.http.h2.client;

import io.effi.rpc.common.exception.ConnectException;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.transport.channel.ChannelHandler;
import io.effi.rpc.transport.netty.NettyChannel;
import io.effi.rpc.transport.netty.NettySupport;
import io.effi.rpc.transport.netty.client.NettyPoolClient;
import io.netty.channel.Channel;
import io.netty.handler.codec.http2.Http2StreamChannelBootstrap;

/**
 * Http2 Pool Client.
 */
public class NettyHttp2PoolClient extends NettyPoolClient {

    public NettyHttp2PoolClient(URL url, ChannelHandler channelHandler) throws ConnectException {
        super(url, channelHandler);
    }

    @Override
    protected void doSend(Channel channel, Object message) {
        Http2StreamChannelBootstrap streamChannelBootstrap = channel.attr(NettySupport.H2_STREAM_BOOTSTRAP_KEY).get();
        NettyChannel nettyChannel = NettySupport.acquireStreamChannel(streamChannelBootstrap, url, connectTimeout);
        nettyChannel.send(message);
        release(channel);
    }
}
