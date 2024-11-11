package io.effi.rpc.boot.processor;

import io.effi.rpc.boot.RemoteCallerFactoryBean;
import io.effi.rpc.core.annotation.RemoteService;
import io.effi.rpc.core.config.ClientConfig;
import io.effi.rpc.core.config.RegistryConfig;
import io.effi.rpc.core.config.ServerConfig;
import io.effi.rpc.core.filter.Filter;
import org.springframework.beans.BeansException;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * Preferential Create These Bean.
 */
public abstract class PreferentialCreateConfig extends VirtueAdapterPostProcessor {

    private static final List<Class<?>> PREFERENTIAL_BEAN_TYPES = List.of(
            ServerConfig.class, ClientConfig.class, RegistryConfig.class, Filter.class
    );

    private static boolean isCreate = false;

    @Override
    public Object postProcessBeforeInitialization(Object bean, @NonNull String beanName) throws BeansException {
        if (bean.getClass().isAnnotationPresent(RemoteService.class) || bean instanceof RemoteCallerFactoryBean<?>) {
            createConfigBean();
        }
        return bean;
    }

    protected void createConfigBean() {
        if (!isCreate) {
            for (Class<?> configType : PREFERENTIAL_BEAN_TYPES) {
                beanFactory.getBeansOfType(configType);
            }
            isCreate = true;
        }
    }
}
