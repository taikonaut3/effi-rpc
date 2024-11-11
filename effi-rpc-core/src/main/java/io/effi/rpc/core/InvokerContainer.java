package io.effi.rpc.core;

/**
 * The Caller Container For managing the caller.
 */
public interface InvokerContainer {

    /**
     * Get an array of callers associated with the container.
     *
     * @return all caller instance
     */
    Invoker<?>[] invokers();

    /**
     * Get invokers by protocol.
     *
     * @param protocol
     * @return
     */
    Invoker<?>[] getInvokers(String protocol);

}

