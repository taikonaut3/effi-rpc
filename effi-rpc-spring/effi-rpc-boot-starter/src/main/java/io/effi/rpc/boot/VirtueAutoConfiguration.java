package io.effi.rpc.boot;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * Virtue auto configuration.
 */
@EnableConfigurationProperties
public class VirtueAutoConfiguration {


    @Bean
    public VirtueConfigurationProperties virtueConfigurationProperties() {
        return new VirtueConfigurationProperties();
    }

    @Bean
    public SpringBootVirtueStarter springBootVirtueStarter() {
        return new SpringBootVirtueStarter();
    }

}
