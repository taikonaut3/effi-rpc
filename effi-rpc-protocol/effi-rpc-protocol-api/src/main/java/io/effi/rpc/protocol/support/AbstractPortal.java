package io.effi.rpc.protocol.support;

import io.effi.rpc.common.constant.Constant;
import io.effi.rpc.common.constant.KeyConstant;
import io.effi.rpc.common.constant.Platform;
import io.effi.rpc.common.extension.AbstractAttributes;
import io.effi.rpc.common.extension.RpcContext;
import io.effi.rpc.common.extension.Scheduler;
import io.effi.rpc.common.extension.spi.ExtensionLoader;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.common.util.StringUtil;
import io.effi.rpc.core.Lifecycle;
import io.effi.rpc.core.Portal;
import io.effi.rpc.core.PortalConfiguration;
import io.effi.rpc.core.manager.*;
import io.effi.rpc.event.EventDispatcher;
import io.effi.rpc.event.EventDispatcherFactory;
import io.effi.rpc.governance.router.Router;
import io.effi.rpc.internal.logging.Logger;
import io.effi.rpc.internal.logging.LoggerFactory;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Abstract implementation of {@link Portal}.
 */
@Getter
@Accessors(fluent = true)
public class AbstractPortal extends AbstractAttributes implements Portal {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected String name;

    protected URL url;

    protected Environment environment;

    protected List<PortalConfiguration> configurations;

    protected CallerManager callerManager;

    protected RegistryConfigManager registryConfigManager;

    protected ServerExporterManager serverExporterManager;

    protected FilterManager filterManager;

    protected RouterConfigManager routerConfigManager;

    protected MonitorManager monitorManager;

    protected Scheduler scheduler;

    protected EventDispatcher eventDispatcher;

    protected volatile Lifecycle.State state;

    public AbstractPortal(URL url) {
        this.url = url;
        this.environment = new Environment();
        this.callerManager = new CallerManager(this);
        this.registryConfigManager = new RegistryConfigManager(this);
        this.serverExporterManager = new ServerExporterManager(this);
        this.filterManager = new FilterManager(this);
        this.routerConfigManager = new RouterConfigManager(this);
        this.monitorManager = new MonitorManager();
        this.configurations = ExtensionLoader.loadExtensions(PortalConfiguration.class);
        init();
    }

    @Override
    public Portal name(String name) {
        this.name = name;
        return this;
    }

    @Override
    public void init() {
        if (state == null) {
            for (PortalConfiguration configuration : configurations) {
                configuration.preInit(this);
            }
            initPortalComponents();
            for (PortalConfiguration configuration : configurations) {
                configuration.postInit(this);
            }
            state = State.INITIALIZED;
        } else {
            throw new IllegalStateException("init() can only be invoked when constructing");
        }
    }

    private void initPortalComponents() {
        EventDispatcherFactory eventDispatcherFactory = ExtensionLoader.loadExtension(EventDispatcherFactory.class);
        this.eventDispatcher = eventDispatcherFactory.create(url);
        this.scheduler = ExtensionLoader.loadExtension(Scheduler.class);
        String routerName = url.getParam(KeyConstant.ROUTER, Constant.DEFAULT_ROUTER);
        Router router = ExtensionLoader.loadExtension(Router.class, routerName);
        set(Router.ATTRIBUTE_KEY, router);
    }

    @Override
    public synchronized void start() {
        if (state == State.INITIALIZED || state == State.STOPPED) {
            if (StringUtil.isBlank(name)) {
                logger.warn("Portal name is blank");
            }
            for (PortalConfiguration configuration : configurations) {
                configuration.preStart(this);
            }
            for (PortalConfiguration configuration : configurations) {
                configuration.postStart(this);
            }
            Portals.register(this);
            state = State.STARTED;
            logger.info("Effi RPC started v{}", Platform.virtueVersion());
        } else {
            throw new IllegalStateException("start() can only be invoked when State is (INITIALIZED|STOPPED)");
        }
    }

    @Override
    public synchronized void stop() {
        if (state == State.STARTED) {
            RpcContext.currentContext().set(Portal.KEY, this);
            for (PortalConfiguration configuration : configurations) {
                configuration.preStop(this);
            }
            for (PortalConfiguration configuration : configurations) {
                configuration.postStop(this);
            }
            eventDispatcher.close();
            scheduler.close();
            state = State.STOPPED;
            logger.info("Effi RPC stopped");
        } else {
            throw new IllegalStateException("start() can only be invoked when State is STARTED");
        }
    }

}
