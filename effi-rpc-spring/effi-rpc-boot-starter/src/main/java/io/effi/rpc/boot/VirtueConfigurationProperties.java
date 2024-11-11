package io.effi.rpc.boot;

import io.effi.rpc.core.Portal;
import io.effi.rpc.core.config.RegistryConfig;
import jakarta.annotation.Resource;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * Virtue springboot config.
 */
@ConfigurationProperties(prefix = "virtue")
public class VirtueConfigurationProperties {

    @Resource
    private Portal portal;

    public String getApplicationName() {
        return portal.name();
    }

    public void setApplicationName(String name) {
        portal.name(name);
    }

//    public List<ServerConfig> getServerConfigs() {
//        return portal.configManager().serverConfigManager().values().stream().toList();
//    }
//
//    public void setServerConfigs(List<ServerConfig> serverConfigs) {
//        serverConfigs.forEach(serverConfig -> portal.configManager().serverConfigManager().register(serverConfig));
//    }
//
//    public List<ClientConfig> getClientConfigs() {
//        return portal.configManager().clientConfigManager().values().stream().toList();
//    }
//
//    public void setClientConfigs(List<ClientConfig> clientConfigs) {
//        clientConfigs.forEach(clientConfig -> portal.configManager().clientConfigManager().register(clientConfig));
//    }

    public List<RegistryConfig> getRegistryConfigs() {
        return portal.registryConfigManager().values().stream().toList();
    }

    public void setRegistryConfigs(List<RegistryConfig> registryConfigs) {
        registryConfigs.forEach(registryConfig -> portal.registryConfigManager().register(registryConfig));
    }

}
