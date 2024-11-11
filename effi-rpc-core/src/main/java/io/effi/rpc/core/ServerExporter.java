package io.effi.rpc.core;

import io.effi.rpc.core.config.RegistryConfig;
import io.effi.rpc.core.config.ServerConfig;
import io.effi.rpc.core.manager.CalleeManager;
import io.effi.rpc.core.manager.Manager;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * Exposes the service through the specified {@link Portal}.
 */
public interface ServerExporter extends PortalSource, Manager.Key {

    /**
     * Returns the server configuration associated with this exporter.
     *
     * @return a {@link ServerConfig} object containing the configuration details
     * for the server.
     */
    ServerConfig serverConfig();

    /**
     * Returns the exported address of the server.
     *
     * @return an {@link InetSocketAddress} representing the address
     * through which the service is exposed.
     */
    InetSocketAddress exportedAddress();

    /**
     * Registers one or more callee that can be called through this exporter.
     *
     * @param callee an array of {@link Callee} objects to be registered.
     *               Each callee represents a service implementation that can
     *               handle requests.
     * @return the current instance of {@link ServerExporter} for method chaining.
     */
    ServerExporter callee(Callee<?>... callee);

    /**
     * Registers one or more registry configurations for the exporter.
     *
     * @param registryConfigs an array of {@link RegistryConfig} objects to
     *                        be registered with the exporter.
     * @return the current instance of {@link ServerExporter} for method chaining.
     */
    ServerExporter registry(RegistryConfig... registryConfigs);

    /**
     * Returns the list of registered registry configurations.
     *
     * @return a {@link List} of {@link RegistryConfig} representing the
     * registries associated with this exporter.
     */
    List<RegistryConfig> registries();

    /**
     * Returns the manager responsible for managing the exported callee.
     *
     * @return a {@link CalleeManager} for handling the lifecycle and operations
     * of the exported callee.
     */
    CalleeManager calleeManager();

    /**
     * Exports the service and open server, making it available for remote calls.
     */
    void export();

}

