package io.effi.rpc.transport.netty.protocol.http.h1.server;

import io.effi.rpc.common.exception.BindException;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.transport.channel.ChannelHandler;
import io.effi.rpc.transport.netty.server.NettyServer;

/**
 * Http Server.
 */
public class NettyHttp1Server extends NettyServer {

    public NettyHttp1Server(URL url, ChannelHandler handler) throws BindException {
        super(url, handler);
    }
}
