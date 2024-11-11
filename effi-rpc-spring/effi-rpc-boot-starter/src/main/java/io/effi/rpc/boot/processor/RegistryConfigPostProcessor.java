package io.effi.rpc.boot.processor;

import io.effi.rpc.core.config.RegistryConfig;
import org.springframework.beans.BeansException;
import org.springframework.lang.NonNull;

/**
 * RegistryConfig PostProcessor.
 */
public class RegistryConfigPostProcessor extends VirtueAdapterPostProcessor {

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        if (bean instanceof RegistryConfig config) {
            viceroy().register(config);
        }
        return bean;
    }

}
