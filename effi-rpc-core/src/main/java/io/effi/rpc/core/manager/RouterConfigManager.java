package io.effi.rpc.core.manager;

import io.effi.rpc.core.Portal;
import io.effi.rpc.core.config.RouterConfig;
import org.intellij.lang.annotations.Language;

/**
 * RouterConfig Manager.
 */
public class RouterConfigManager extends AbstractManager<RouterConfig> {
    public RouterConfigManager(Portal portal) {
        super(portal);
    }

    /**
     * Register a new router config bye call url regex and target url regex.
     *
     * @param urlRegex
     * @param targetRegex
     */
    public void register(@Language("RegExp") String urlRegex, @Language("RegExp") String targetRegex) {
        RouterConfig routerConfig = new RouterConfig(urlRegex).match(targetRegex);
        register(urlRegex, routerConfig);
    }
}
