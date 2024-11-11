package io.effi.rpc.core.manager;

import io.effi.rpc.common.url.URL;
import io.effi.rpc.common.url.URLUtil;
import io.effi.rpc.core.Callee;
import io.effi.rpc.core.Invoker;
import io.effi.rpc.core.Portal;

import java.util.List;

/**
 * Manage the registration and retrieval of {@link Callee} instances.
 */
public class CalleeManager extends AbstractManager<Callee<?>> {

    public CalleeManager(Portal portal) {
        super(portal);
    }

    /**
     * Acquires a callee by its request URL.
     *
     * @param url
     * @return
     * @see Invoker#managerKey()
     */
    public Callee<?> acquire(URL url) {
        return acquire(url.paths());
    }

    private Callee<?> acquire(List<String> paths) {
        return get(URLUtil.toPath(paths));
    }

}

