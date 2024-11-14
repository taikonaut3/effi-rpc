package io.effi.rpc.core;

import io.effi.rpc.common.exception.RpcException;
import io.effi.rpc.common.url.URL;

import java.lang.reflect.Type;
import java.util.function.Supplier;

/**
 * Encapsulates the invocation of an RPC call, capturing the caller's {@link Caller} URL
 * and the parameters for the call. This core class facilitates the invocation process,
 * allowing for diverse invocation behaviors through {@link #invoke()}, which initiates
 * the actual RPC call. The {@link #rebase(Supplier)} enables dynamic modification of
 * the invocation behavior.
 * <p>
 * For clients, an instance of this class is created at the start of the RPC call and
 * persists until the server successfully parses the response, covering the entire duration
 * of the RPC interaction
 * {@link io.effi.rpc.transport.ProtocolAdapter#createInvocation(Caller, Object[])}.
 * <p>
 * For servers, this class can parse the parameters required by the caller from the request,
 * according to the method parameter mapping of the callee {@link Callee}. It utilizes the
 * {@link #invoke()} method to call the service method
 * {@link io.effi.rpc.transport.ProtocolAdapter#createInvocation(Portal, URL, Callee, Object[])}.
 * <p>
 * And can invoke various components in the framework, such as Invoke the next filter.
 *
 * @param <T> {@link  #invoke()} method return type
 * @see CallInvocation
 * @see ReceiveInvocation
 */
public interface Invocation<T> extends PortalSource {

    /**
     * Returns the URL of the request associated with this invocation.
     *
     * @return
     */
    URL requestUrl();

    /**
     * Returns the arguments of the invocation.
     *
     * @return an array of arguments passed to the invocation.
     */
    Object[] args();

    /**
     * Returns the return type of the invocation.
     *
     * @return the expected return type as a {@link Type} object, similar to {@link Invoker#returnType()}.
     */
    Type returnType();

    /**
     * Returns the associated invoker for this invocation.
     *
     * @return the invoker responsible for handling this invocation.
     */
    Invoker<?> invoker();

    /**
     * Executes the invocation and returns the result.
     *
     * @return the result of the invocation, which can be null.
     * @throws RpcException if an error occurs during the invocation process.
     */
    T invoke() throws RpcException;

    /**
     * Reconfigures the current invocation behavior.
     *
     * @param invoke a supplier that provides a new invocation behavior.
     * @param <R>    the return type of the new invocation behavior.
     * @return current {@link Invocation} instance with the updated behavior.
     */
    <R> Invocation<R> rebase(Supplier<R> invoke);

    /**
     * Sets the arguments for the invocation.
     *
     * @param args
     */
    void args(Object... args);
}
