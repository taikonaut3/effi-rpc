package io.effi.rpc.boot.processor;

import io.effi.rpc.boot.VirtueRegistrationLifecycle;
import org.springframework.beans.BeansException;
import org.springframework.cloud.client.serviceregistry.AbstractAutoServiceRegistration;
import org.springframework.lang.NonNull;

/**
 * ServiceRegistration PostProcessor.
 */
public class ServiceRegistrationPostProcessor extends VirtueAdapterPostProcessor {

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        if (bean instanceof AbstractAutoServiceRegistration<?> autoServiceRegistration) {
            autoServiceRegistration.addRegistrationLifecycle(new VirtueRegistrationLifecycle<>(viceroy()));
        }
        return bean;
    }

}
