package io.effi.rpc.transport.server;

import io.effi.rpc.common.exception.BindException;
import io.effi.rpc.common.extension.AttributeKey;
import io.effi.rpc.transport.channel.Channel;
import io.effi.rpc.transport.endpoint.Endpoint;

import java.net.InetSocketAddress;
import java.util.Collection;

/**
 * Server that handles incoming connections and facilitates communication.
 */
public interface Server extends Endpoint {

    AttributeKey<Server> ATTRIBUTE_KEY = AttributeKey.valueOf("server");

    /**
     * Binds the server to the specified host and port,
     * making it ready to accept incoming connections.
     *
     * @throws BindException if an error occurs during the binding process
     */
    void bind() throws BindException;

    /**
     * Returns all channels associated with the server.
     *
     * @return An array of {@link Channel} objects representing
     * all channels associated with the server
     */
    Collection<Channel> channels();

    /**
     * Returns the channel associated with the specified remote address.
     *
     * @param remoteAddress The remote address of the client whose associated
     *                      channel is to be retrieved.
     * @return The {@link Channel} associated with the specified remote address,
     * or null if no channel is found for the given address.
     */
    Channel acquireChannel(InetSocketAddress remoteAddress);

}

