package io.effi.rpc.boot.processor;

import io.effi.rpc.core.filter.Filter;
import org.springframework.beans.BeansException;
import org.springframework.lang.NonNull;

/**
 * Filter PostProcessor.
 */
public class FilterPostProcessor extends VirtueAdapterPostProcessor {

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        if (bean instanceof Filter filter) {
            viceroy().register(beanName, filter);
        }
        return bean;
    }

}
