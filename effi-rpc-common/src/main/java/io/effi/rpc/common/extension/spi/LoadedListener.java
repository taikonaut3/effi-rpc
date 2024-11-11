package io.effi.rpc.common.extension.spi;

/**
 * SPI listener, invoked when an extension instance is created.
 * This allows for post-creation actions or additional setup.
 *
 * @param <T> the service type for the extension
 */
@FunctionalInterface
public interface LoadedListener<T> {

    /**
     * Invoked when the extension instance is created.
     * This method can be used to perform operations or track the created service instance.
     *
     * @param service the created instance of the service
     */
    void onLoaded(T service);

}

