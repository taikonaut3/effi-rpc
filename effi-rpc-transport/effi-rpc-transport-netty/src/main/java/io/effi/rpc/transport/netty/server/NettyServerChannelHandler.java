package io.effi.rpc.transport.netty.server;

import io.effi.rpc.common.url.URL;
import io.effi.rpc.transport.channel.ChannelHandler;
import io.effi.rpc.transport.netty.PeerChannelDuplexHandler;

import static io.netty.channel.ChannelHandler.Sharable;

/**
 * Base channelHandler for netty server.
 */
@Sharable
public final class NettyServerChannelHandler extends PeerChannelDuplexHandler {

    public NettyServerChannelHandler(ChannelHandler channelHandler, URL endpointUrl) {
        super(channelHandler, endpointUrl);
    }

}
