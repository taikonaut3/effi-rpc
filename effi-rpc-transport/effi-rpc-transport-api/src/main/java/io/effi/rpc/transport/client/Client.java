package io.effi.rpc.transport.client;

import io.effi.rpc.common.exception.ConnectException;
import io.effi.rpc.transport.channel.Channel;
import io.effi.rpc.transport.endpoint.Endpoint;

/**
 * Client that can connect to a remote endpoint and send messages.
 */
public interface Client extends Endpoint {

    /**
     * Connects to the remote endpoint.
     *
     * @throws ConnectException if the connection cannot be established
     */
    void connect() throws ConnectException;

    /**
     * Returns the {@link Channel} associated with this client.
     *
     * @return the {@link Channel} used by this client
     */
    Channel channel();

    /**
     * Sends a message to the remote endpoint through the underlying channel.
     *
     * @param message the message to send
     */
    default void send(Object message) {
        channel().send(message);
    }

}

