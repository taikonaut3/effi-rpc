package io.effi.rpc.common.url;

/**
 * Factory for the SPI instance created from the url configuration.
 *
 * @param <T> service type
 */
public interface ServiceFactory<T> {

    /**
     * Acquires the instance by the url.
     *
     * @param url
     * @return instance
     */
    T acquire(URL url);
}