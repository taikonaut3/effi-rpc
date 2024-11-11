package io.effi.rpc.core;

/**
 * Wrapper service.
 * <p>
 * Allow interaction with a remote service by providing methods to access
 * the service instance, its type, and invoke methods on it. The service
 * can have multiple callee, each representing a specific endpoint for
 * the service.
 *
 * @param <T> The type of the remote service interface.
 */
public interface RemoteService<T> extends InvokerContainer {

    /**
     * Returns the target instance of the remote service.
     *
     * @return the target instance of the remote service.
     */
    T target();

    /**
     * Returns the class type of the remote service.
     *
     * @return the Class object representing the type of the remote service.
     */
    Class<T> targetType();

    /**
     * Returns the name of the remote service.
     *
     * @return the name of the remote service.
     */
    String name();

    /**
     * Invokes the specified method on the remote service.
     *
     * @param callee the callee representing the method to invoke.
     * @param args   the arguments to pass to the method.
     * @param <R>    {@link Callee#invoke(Object...)} return type
     * @return the result of the method invocation.
     */
    <R> R invokeCallee(Callee<T> callee, Object... args);

    /**
     * Adds a callee to the remote service.
     *
     * @param callee the callee to be added.
     * @return the updated remote service instance.
     */
    RemoteService<T> addCallee(Callee<?> callee);

    /**
     * Retrieves the callee for the specified protocol and path.
     *
     * @param protocol the protocol used for the callee.
     * @param path     the path used for the callee.
     * @return the callee associated with the given protocol and path.
     */
    Callee<?> getCallee(String protocol, String path);
}

