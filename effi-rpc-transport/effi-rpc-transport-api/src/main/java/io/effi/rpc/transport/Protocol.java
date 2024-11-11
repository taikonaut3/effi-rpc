package io.effi.rpc.transport;

import io.effi.rpc.common.constant.Component;
import io.effi.rpc.common.extension.spi.Extensible;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.core.CallInvocation;
import io.effi.rpc.core.Invocation;
import io.effi.rpc.core.Portal;
import io.effi.rpc.core.ReceiveInvocation;
import io.effi.rpc.core.config.ClientConfig;
import io.effi.rpc.core.config.ServerConfig;
import io.effi.rpc.core.result.Result;
import io.effi.rpc.transport.channel.Channel;
import io.effi.rpc.transport.client.Client;
import io.effi.rpc.transport.codec.ClientCodec;
import io.effi.rpc.transport.codec.ServerCodec;
import io.effi.rpc.transport.server.Server;

/**
 * Communication protocol within the system.
 */
@Extensible(Component.Protocol.VIRTUE)
public interface Protocol extends ProtocolAdapter {

    /**
     * Returns the name of this protocol.
     *
     * @return A {@link String} representing the name of the protocol.
     */
    String protocol();

    /**
     * Sends a request using the specified invocation.
     *
     * @param invocation The {@link Invocation} object containing request details.
     * @return An {@link RpcFuture} representing the asynchronous result of the request.
     */
    void sendRequest(CallInvocation<?> invocation);

    /**
     * Sends a response back to the client through the specified channel.
     *
     * @param channel    The {@link Channel} through which the response is sent.
     * @param invocation The original {@link Invocation} object associated with the request.
     * @param result     The {@link Result} object containing the response data.
     */
    void sendResponse(Channel channel, ReceiveInvocation<?> invocation, Result result);

    /**
     * Opens a client instance, potentially reusing an existing one if available.
     *
     * @param url    The core {@link ClientConfig} for the client to be opened.
     * @param portal The {@link Portal} associated with the client.
     * @return A {@link Client} instance associated with the specified configuration.
     */
    Client openClient(URL url, Portal portal);

    /**
     * Opens a new server instance based on the provided configuration.
     *
     * @param url    The core {@link ServerConfig} for the server to be opened.
     * @param portal The {@link Portal} associated with the client.
     * @return A {@link Server} instance associated with the specified configuration.
     */
    Server openServer(URL url, Portal portal);

    /**
     * Returns the server codec used for encoding and decoding messages on the server side.
     *
     * @return A {@link ServerCodec} instance for server-side message handling.
     */
    ServerCodec serverCodec();

    /**
     * Returns the client codec used for encoding and decoding messages on the client side.
     *
     * @return A {@link ClientCodec} instance for client-side message handling.
     */
    ClientCodec clientCodec();

}

