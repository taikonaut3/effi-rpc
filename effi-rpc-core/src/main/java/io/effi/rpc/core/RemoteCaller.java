package io.effi.rpc.core;

/**
 * Remote caller for invoking remote service methods.
 *
 * @param <T> The type of the remote service interface.
 */
public interface RemoteCaller<T> extends InvokerContainer {

    /**
     * Gets the target interface of the remote service.
     *
     * @return the target interface
     */
    Class<T> targetInterface();

    /**
     * Gets a Proxy instance of the remote service.
     *
     * @return proxy instance
     */
    T get();
}

