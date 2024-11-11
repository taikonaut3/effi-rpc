package io.effi.rpc.protocol.support;

import io.effi.rpc.common.constant.Platform;
import io.effi.rpc.common.executor.RpcThreadPool;
import io.effi.rpc.common.extension.spi.ExtensionLoader;
import io.effi.rpc.core.Portal;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Manage all {@link Portal}.
 */
public class Portals {

    private static boolean REGISTERED_HOOK = false;

    private static final Map<String, Portal> PORTALS = new HashMap<>();

    /**
     * Acquires a {@link Portal} by name.
     *
     * @param name
     * @return
     */
    public static Portal acquire(String name) {
        return PORTALS.get(name);
    }

    /**
     * Returns all {@link Portal}.
     *
     * @return
     */
    public static Collection<Portal> portals() {
        return PORTALS.values();
    }

    /**
     * Registers a {@link Portal}.
     *
     * @param portal
     */
    public static void register(Portal portal) {
        PORTALS.put(portal.name(), portal);
        if (!REGISTERED_HOOK) {
            Platform.registerShutdownHook(Portals::stop);
            REGISTERED_HOOK = true;
        }
    }

    private static void stop() {
        Platform.jvmShuttingDown();
        for (Portal portal : portals()) {
            portal.stop();
        }
        RpcThreadPool.clear();
//        RpcFuture.clear();
        ExtensionLoader.clearLoader();
    }

}
