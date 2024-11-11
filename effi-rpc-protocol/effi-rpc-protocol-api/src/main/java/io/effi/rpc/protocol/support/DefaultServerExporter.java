package io.effi.rpc.protocol.support;

import io.effi.rpc.common.extension.collection.LazyList;
import io.effi.rpc.common.extension.spi.ExtensionLoader;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.common.url.URLType;
import io.effi.rpc.common.util.AssertUtil;
import io.effi.rpc.common.util.CollectionUtil;
import io.effi.rpc.common.util.ObjectUtil;
import io.effi.rpc.core.Callee;
import io.effi.rpc.core.Portal;
import io.effi.rpc.core.ServerExporter;
import io.effi.rpc.core.config.RegistryConfig;
import io.effi.rpc.core.config.ServerConfig;
import io.effi.rpc.core.manager.CalleeManager;
import io.effi.rpc.core.utils.PortalUtil;
import io.effi.rpc.internal.logging.Logger;
import io.effi.rpc.internal.logging.LoggerFactory;
import io.effi.rpc.protocol.support.builder.ServerExportBuilder;
import io.effi.rpc.registry.RegistryFactory;
import io.effi.rpc.registry.RegistryService;
import io.effi.rpc.transport.Protocol;
import io.effi.rpc.transport.server.Server;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Default implementation of the {@link ServerExporter} that exports the server configuration
 * and registers it with registries.
 */
public class DefaultServerExporter implements ServerExporter {

    private static final Logger logger = LoggerFactory.getLogger(DefaultServerExporter.class);

    private final ServerConfig serverConfig;

    private final InetSocketAddress exportedAddress;

    private final URL exportedUrl;

    protected final Portal portal;

    protected final CalleeManager calleeManager;

    protected final List<RegistryConfig> registryConfigs = new LazyList<>(ArrayList::new);

    private volatile Server server;

    DefaultServerExporter(URL exportedUrl, InetSocketAddress exportedAddress, ServerConfig serverConfig, Portal portal) {
        this.exportedUrl = exportedUrl;
        this.serverConfig = serverConfig;
        this.exportedAddress = exportedAddress;
        this.portal = portal;
        this.calleeManager = new CalleeManager(portal);
        List<RegistryConfig> sharedRegistryConfigs = portal.registryConfigManager().sharedValues();
        sharedRegistryConfigs.forEach(this::registry);
        portal.serverExporterManager().register(this);
    }

    /**
     * Builder method for creating instances of {@link DefaultServerExporter}.
     */
    public static DefaultServerExporterBuilder builder() {
        return new DefaultServerExporterBuilder();
    }

    @Override
    public void export() {
        if (server == null) {
            synchronized (this) {
                if (server == null) {
                    doRegister();
                    server = openServer();
                    logger.info("Opened <{}>{}, bind port(s) {}, use config: {}",
                            server.url().protocol(),
                            ObjectUtil.simpleClassName(server),
                            server.port(),
                            server.url()
                    );
                }
            }
        }
    }

    @Override
    public ServerExporter callee(Callee<?>... callee) {
        if (CollectionUtil.isNotEmpty(callee)) {
            for (Callee<?> ce : callee) {
                if (exportedUrl.protocol().equals(ce.url().protocol())) {
                    calleeManager.register(ce);
                }
            }
        }
        return this;
    }

    @Override
    public ServerExporter registry(RegistryConfig... registryConfigs) {
        if (CollectionUtil.isNotEmpty(registryConfigs)) {
            CollectionUtil.addUnique(this.registryConfigs, registryConfigs);
        }
        return this;
    }

    @Override
    public CalleeManager calleeManager() {
        return calleeManager;
    }

    @Override
    public List<RegistryConfig> registries() {
        return registryConfigs;
    }

    @Override
    public ServerConfig serverConfig() {
        return serverConfig;
    }

    @Override
    public InetSocketAddress exportedAddress() {
        return exportedAddress;
    }

    @Override
    public Portal portal() {
        return portal;
    }

    @Override
    public String managerKey() {
        return exportedUrl.authority();
    }

    /**
     * Returns the server instance.
     */
    public Server server() {
        return server;
    }

    /**
     * Opens a new server instance using the provided protocol and configuration.
     */
    protected Server openServer() {
        Protocol protocol = ExtensionLoader.loadExtension(Protocol.class, exportedUrl.protocol());
        URL serverUrl = URL.builder()
                .type(URLType.SERVER)
                .protocol(exportedUrl.protocol())
                .address(exportedAddress)
                .params(serverConfig.url().params())
                .build();
        PortalUtil.setPortal(serverUrl, portal);
        return protocol.openServer(serverUrl, portal);
    }

    /**
     * Registers the server with the specified registries.
     */
    protected void doRegister() {
        if (CollectionUtil.isEmpty(registryConfigs)) {
            logger.warn("No globally available RegistryConfig(s)");
        } else {
            for (RegistryConfig registryConfig : registryConfigs) {
                URL registryConfigUrl = registryConfig.url().replicate();
                RegistryFactory registryFactory = ExtensionLoader.loadExtension(RegistryFactory.class, registryConfigUrl.protocol());
                RegistryService registryService = registryFactory.acquire(registryConfigUrl);
                registryService.register(exportedUrl);
            }
        }
    }

    /**
     * Builder class for constructing {@link DefaultServerExporter} instances.
     */
    public static class DefaultServerExporterBuilder extends ServerExportBuilder<DefaultServerExporter, DefaultServerExporterBuilder> {

        @Override
        public DefaultServerExporter build() {
            AssertUtil.notNull(serverConfig, "server config cannot be null");
            AssertUtil.notNull(exportedAddress, "exported address cannot be null");
            AssertUtil.notNull(portal, "portal cannot be null");
            URL exportedUrl = URL.builder()
                    .type(URLType.SERVER)
                    .protocol(serverConfig.url().protocol())
                    .address(exportedAddress)
                    .params(parameterization())
                    .build();
            PortalUtil.setPortal(exportedUrl, portal);
            return new DefaultServerExporter(exportedUrl, exportedAddress, serverConfig, portal);
        }
    }
}

