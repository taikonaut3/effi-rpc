package io.effi.rpc.core.caller;

import io.effi.rpc.common.exception.RpcException;

import java.util.concurrent.CompletableFuture;

public interface UnaryCaller<R> extends Caller<CompletableFuture<R>> {

    /**
     * Initiates an asynchronous RPC call with the specified client configuration and arguments.
     *
     * @param args the arguments to pass to the remote service.
     * @return a {@link CompletableFuture} that will contain the result of the call.
     * @throws RpcException if an error occurs during the call.
     */
    CompletableFuture<R> call(Object... args) throws RpcException;

    /**
     * Initiates a synchronous RPC call with the specified arguments.
     *
     * @param args the arguments to pass to the remote service.
     * @return the result of the call.
     * @throws RpcException if an error occurs during the call.
     */
    default R blockingCall(Object... args) throws RpcException {
        try {
            return call(args).join();
        } catch (Exception e) {
            throw RpcException.wrap(e);
        }
    }

}
