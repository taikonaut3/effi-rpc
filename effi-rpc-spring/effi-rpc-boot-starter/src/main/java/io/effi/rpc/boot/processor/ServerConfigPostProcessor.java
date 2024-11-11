package io.effi.rpc.boot.processor;

import io.effi.rpc.core.config.ServerConfig;
import org.springframework.beans.BeansException;
import org.springframework.lang.NonNull;

/**
 * ServerConfig PostProcessor.
 */
public class ServerConfigPostProcessor extends VirtueAdapterPostProcessor {

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        if (bean instanceof ServerConfig config) {
            //viceroy().register(config);
        }
        return bean;
    }

}
