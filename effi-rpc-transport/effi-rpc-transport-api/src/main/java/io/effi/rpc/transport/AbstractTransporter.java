package io.effi.rpc.transport;

import io.effi.rpc.common.constant.KeyConstant;
import io.effi.rpc.common.exception.BindException;
import io.effi.rpc.common.exception.ConnectException;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.transport.channel.ChannelHandler;
import io.effi.rpc.transport.client.Client;
import io.effi.rpc.transport.server.Server;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Abstract implementation of {@link Transporter}.
 */
public abstract class AbstractTransporter implements Transporter {

    private final Map<String, Client> clients = new ConcurrentHashMap<>();

    private final Map<String, Server> servers = new ConcurrentHashMap<>();

    @Override
    public Client acquireClient(URL url, ChannelHandler handler) {
        String key = url.getParam(KeyConstant.NAME, url.protocol());
        Client client = clients.computeIfAbsent(key, k -> connect(url, handler));
        if (!client.isActive()) {
            client.connect();
        }
        return client;
    }

    @Override
    public Server acquireServer(URL url, ChannelHandler handler) {
        String key = url.getParam(KeyConstant.NAME, url.authority());
        return servers.computeIfAbsent(key, k -> bind(url, handler));
    }

    @Override
    public Collection<Client> clients() {
        return clients.values();
    }

    @Override
    public Collection<Server> servers() {
        return servers.values();
    }

    @Override
    public synchronized void clear() {
        clients().forEach(Client::close);
        servers().forEach(Server::close);
        clients.clear();
        servers.clear();
    }

    protected abstract Client connect(URL url, ChannelHandler handler) throws ConnectException;

    protected abstract Server bind(URL url, ChannelHandler handler) throws BindException;

}
