package io.effi.rpc.transport;

import io.effi.rpc.common.url.URL;
import io.effi.rpc.core.*;
import io.effi.rpc.core.caller.Caller;
import io.effi.rpc.transport.channel.Channel;

/**
 * Handle invocation-related operations in the RPC protocol.
 */
public interface ProtocolAdapter {

    /**
     * Creates an {@link CallInvocation} object when the RPC call starts.
     *
     * @param future The {@link ReplyFuture} associated with the RPC call.
     * @param caller The {@link Caller} instance representing the entity initiating the RPC call.
     * @param args   The arguments passed to the RPC call, encapsulated in an {@link Object} array.
     * @param <T>    The type of the return value expected from the invocation.
     * @return An {@link Invocation} object containing details about the RPC call.
     */
    <T> CallInvocation<T> createInvocation(ReplyFuture future, Caller<?> caller, Object[] args);

    /**
     * Creates an {@link ReceiveInvocation} object after the server has decoded the request.
     *
     * @param channel    The {@link Channel} instance representing the communication channel.
     * @param requestUrl The {@link URL} representing the endpoint of the request.
     * @param callee     The {@link Callee} instance representing the target of the RPC call.
     * @param args       The arguments extracted from the decoded request, encapsulated in an {@link Object} array.
     * @param <T>        The type of the return value expected from the invocation.
     * @return An {@link Invocation} object containing details about the decoded RPC call.
     */
    <T> ReceiveInvocation<T> createInvocation(Channel channel, URL requestUrl, Callee<?> callee, Object[] args);
}

