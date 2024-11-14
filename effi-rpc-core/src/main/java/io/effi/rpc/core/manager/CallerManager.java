package io.effi.rpc.core.manager;

import io.effi.rpc.core.Caller;
import io.effi.rpc.core.Portal;

/**
 * Manage the registration and retrieval of {@link Caller} instances.
 */
public class CallerManager extends AbstractManager<Caller<?>> {
    public CallerManager(Portal portal) {
        super(portal);
    }
}
