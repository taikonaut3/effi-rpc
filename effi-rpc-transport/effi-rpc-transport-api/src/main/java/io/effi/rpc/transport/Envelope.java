package io.effi.rpc.transport;

import io.effi.rpc.common.url.URL;
import io.effi.rpc.common.url.URLSource;

/**
 * Envelope for exchange messages within the communication framework.
 * <p>
 * An envelope is a structured container that encapsulates the details of a message being
 * exchanged, such as the request URL.
 * </p>
 */
public interface Envelope extends URLSource {

    /**
     * Returns the request URL associated with the message.
     *
     * @return The {@link URL} representing the request destination.
     */
    @Override
    URL url();
}

