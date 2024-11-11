package io.effi.rpc.common.url;

/**
 * Source that provides a {@link URL}.
 *
 * <p>This interface is designed to be implemented by classes that
 * need to supply a {@link URL}, such as network endpoints, services, or resources.</p>
 */
public interface URLSource {

    /**
     * Returns the {@link URL} associated with this source.
     *
     * @return the {@link URL} associated with this source
     */
    URL url();

}

