package io.effi.rpc.core.manager;

import io.effi.rpc.core.Portal;
import io.effi.rpc.core.filter.Filter;

/**
 * Manage the registration and retrieval of {@link Filter} instances.
 */
public class FilterManager extends SharableManager<Filter> {

    public FilterManager(Portal portal) {
        super(portal);
    }

}
