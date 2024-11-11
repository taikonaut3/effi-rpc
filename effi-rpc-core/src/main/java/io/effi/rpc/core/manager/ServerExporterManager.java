package io.effi.rpc.core.manager;

import io.effi.rpc.core.Portal;
import io.effi.rpc.core.ServerExporter;


/**
 * Manage the registration and retrieval of {@link ServerExporter} instances.
 */
public class ServerExporterManager extends AbstractManager<ServerExporter> {
    public ServerExporterManager(Portal portal) {
        super(portal);
    }

}
