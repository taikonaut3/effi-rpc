package io.effi.rpc.transport.channel;

import io.effi.rpc.common.exception.NetWorkException;
import io.effi.rpc.common.extension.Attributes;
import io.effi.rpc.common.extension.resoruce.Closeable;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.common.url.URLSource;
import io.effi.rpc.core.PortalSource;

import java.net.InetSocketAddress;

/**
 * Communication channel for sending messages.
 */
public interface Channel extends URLSource, PortalSource, Attributes, Closeable {

    /**
     * Returns the local address to which this channel is bound.
     *
     * @return the {@link InetSocketAddress} representing the local address
     */
    InetSocketAddress localAddress();

    /**
     * Returns the remote address to which this channel is connected.
     *
     * @return the {@link InetSocketAddress} representing the remote address
     */
    InetSocketAddress remoteAddress();

    /**
     * Returns the URL of the endpoint associated with this channel.
     *
     * @return the {@link URL} representing the endpoint
     */
    @Override
    URL url();

    /**
     * Sends a message through this channel.
     *
     * @param message the message to be sent through the channel
     * @throws NetWorkException if there is an error during message transmission
     */
    void send(Object message) throws NetWorkException;
}

