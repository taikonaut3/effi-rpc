package io.effi.rpc.transport.netty.client;

import io.effi.rpc.common.url.URL;
import io.effi.rpc.transport.channel.ChannelHandler;
import io.effi.rpc.transport.netty.PeerChannelDuplexHandler;

import static io.netty.channel.ChannelHandler.Sharable;

/**
 * Base channel handler for netty client.
 */
@Sharable
public final class NettyClientChannelHandler extends PeerChannelDuplexHandler {

    public NettyClientChannelHandler(ChannelHandler channelHandler, URL endpointUrl) {
        super(channelHandler, endpointUrl);
    }
}
