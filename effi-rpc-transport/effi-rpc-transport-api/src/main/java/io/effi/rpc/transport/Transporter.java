package io.effi.rpc.transport;

import io.effi.rpc.common.extension.resoruce.Cleanable;
import io.effi.rpc.common.extension.spi.Extensible;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.transport.channel.ChannelHandler;
import io.effi.rpc.transport.client.Client;
import io.effi.rpc.transport.server.Server;

import java.util.Collection;

import static io.effi.rpc.common.constant.Component.Transport.NETTY;

/**
 * Manage the creation and retrieval of client and server connections.
 * Provides access to active clients and servers, and includes cleanup functionality.
 */
@Extensible(NETTY)
public interface Transporter extends Cleanable {

    /**
     * Acquires or creates a client connection for the specified URL and assigns
     * a handler to manage channel events.
     *
     * @param url     the target URL for the client connection
     * @param handler the channel handler for managing client channel events
     * @return a client connection associated with the given URL and handler
     */
    Client acquireClient(URL url, ChannelHandler handler);

    /**
     * Acquires or creates a server connection for the specified URL and assigns
     * a handler to manage channel events.
     *
     * @param url     the target URL for the server connection
     * @param handler the channel handler for managing server channel events
     * @return a server connection associated with the given URL and handler
     */
    Server acquireServer(URL url, ChannelHandler handler);

    /**
     * Retrieves all active client connections managed by this transporter.
     *
     * @return a collection of active client connections
     */
    Collection<Client> clients();

    /**
     * Retrieves all active server connections managed by this transporter.
     *
     * @return a collection of active server connections
     */
    Collection<Server> servers();
}


