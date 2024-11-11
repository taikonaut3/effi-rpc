package io.effi.rpc.registry;

import io.effi.rpc.common.constant.KeyConstant;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.common.util.CollectionUtil;
import io.effi.rpc.common.util.StringUtil;
import io.effi.rpc.core.Portal;
import io.effi.rpc.core.utils.PortalUtil;
import io.effi.rpc.registry.support.RegisterServiceEvent;
import io.effi.rpc.registry.support.RegisterTask;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;


/**
 * Base class for implementing registry services.
 * <p>Handles service registration and discovery, including health checks.</p>
 */
public abstract class AbstractRegistryService implements RegistryService {

    protected final Map<String, List<String>> discoverHealthServices = new ConcurrentHashMap<>();
    protected final Map<String, URL> registeredUrls = new ConcurrentHashMap<>();
    protected URL registryUrl;
    protected boolean enableHealthCheck;

    protected AbstractRegistryService(URL url) {
        this.registryUrl = url;
        this.enableHealthCheck = url.getBooleanParam(KeyConstant.ENABLE_HEALTH_CHECK, true);
        connect(url);
    }

    @Override
    public void register(URL url) {
        Portal portal = PortalUtil.acquirePortal(url);
        if (!registeredUrls.containsKey(url.authority())) {
            var task = buildRegisterTask(url);
            RegisterTask registerTask = new RegisterTask(url, task);
            RegisterServiceEvent event = new RegisterServiceEvent(url, registerTask);
            portal.publishEvent(event);
            registeredUrls.put(url.authority(), url);
        }
    }

    @Override
    public List<URL> discover(URL url) {
        String serviceName = serviceName(url);
        List<URL> urls;
        // Check the cache for available services
        List<String> serverUrls = discoverHealthServices.get(serviceName);
        if (CollectionUtil.isEmpty(serverUrls)) {
            synchronized (serviceName) {
                serverUrls = discoverHealthServices.get(serviceName);
                if (CollectionUtil.isEmpty(serverUrls)) {
                    boolean noSubscribe = serverUrls == null;
                    // Discover available services from the registry
                    urls = doDiscover(url);
                    serverUrls = urls.stream().map(URL::toString).toList();
                    // Subscribe to services if needed
                    if (noSubscribe) subscribeService(url);
                    discoverHealthServices.put(serviceName, serverUrls);
                }
            }
        }
        urls = serverUrls.stream()
                .map(URL::valueOf)
                .filter(serverUrl -> serverUrl.protocol().equalsIgnoreCase(url.protocol()))
                .toList();

        return urls;
    }

    @Override
    public void close() {
        registeredUrls.values().forEach(this::deregister);
        discoverHealthServices.clear();
        registeredUrls.clear();
    }

    protected String instanceId(URL url) {
        return "<" + url.protocol() + ">" + url.address();
    }

    protected String serviceName(URL url) {
        String serviceName = url.getParam(KeyConstant.SERVICE_NAME);
        if (StringUtil.isBlank(serviceName)) {
            throw new IllegalArgumentException("service name cannot be blank");
        }
        return serviceName;
    }

    protected abstract void subscribeService(URL url);

    protected abstract BiConsumer<RegisterTask, Map<String, String>> buildRegisterTask(URL url);

    protected abstract List<URL> doDiscover(URL url);
}

