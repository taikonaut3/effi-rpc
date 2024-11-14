package io.effi.rpc.transport.netty.protocol.http.h1.client;

import io.effi.rpc.common.exception.ConnectException;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.transport.channel.ChannelHandler;
import io.effi.rpc.transport.netty.client.NettyPoolClient;

/**
 * Http Client.
 */
public class NettyHttp1Client extends NettyPoolClient {

    public NettyHttp1Client(URL url, ChannelHandler channelHandler) throws ConnectException {
        super(url, channelHandler);
    }

    @Override
    public void send(Object message) {
        channel().send(message);
    }

}
