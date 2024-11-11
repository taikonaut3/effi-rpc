package io.effi.rpc.transport.netty.protocol.http.h2.server;

import io.effi.rpc.common.exception.BindException;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.transport.channel.ChannelHandler;
import io.effi.rpc.transport.netty.server.NettyServer;

/**
 * Http2 Server.
 */
public class NettyHttp2Server extends NettyServer {

    public NettyHttp2Server(URL url, ChannelHandler handler) throws BindException {
        super(url, handler);
    }
}
