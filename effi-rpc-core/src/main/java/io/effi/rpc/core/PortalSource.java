package io.effi.rpc.core;

/**
 * Source that provides a {@link Portal}.
 *
 * <p>This interface is designed to be implemented by classes that
 * need to supply a {@link Portal}</p>
 */
public interface PortalSource {

    /**
     * Returns the {@link Portal} associated with this source.
     *
     * @return the {@link Portal} associated with this source
     */
    Portal portal();
}
