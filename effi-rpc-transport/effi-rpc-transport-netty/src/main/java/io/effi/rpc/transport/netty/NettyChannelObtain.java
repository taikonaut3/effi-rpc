package io.effi.rpc.transport.netty;

import io.effi.rpc.common.url.URL;
import io.netty.channel.ChannelHandlerContext;

/**
 * Obtain a Netty channel associated with an endpoint URL.
 */
public interface NettyChannelObtain {

    /**
     * Returns the URL of the endpoint associated with this instance.
     *
     * @return the endpoint URL
     */
    URL endpointUrl();

    /**
     * Acquires a Netty channel using the provided {@link ChannelHandlerContext}.
     * Delegates to the static acquire method of NettyChannel with the current context's channel and the endpoint URL.
     *
     * @param ctx the ChannelHandlerContext used to obtain the channel
     * @return the acquired NettyChannel
     */
    default NettyChannel acquireChannel(ChannelHandlerContext ctx) {
        return NettyChannel.acquire(ctx.channel(), endpointUrl());
    }
}

