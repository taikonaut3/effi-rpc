package io.effi.rpc.boot;

import io.effi.rpc.common.constant.Component;
import io.effi.rpc.common.constant.KeyConstant;
import io.effi.rpc.core.Portal;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.client.serviceregistry.RegistrationLifecycle;

/**
 * Adapts to the registry of SpringCloud.
 *
 * @param <R>
 */
public class VirtueRegistrationLifecycle<R extends Registration> implements RegistrationLifecycle<R> {

    private final Portal portal;

    public VirtueRegistrationLifecycle(Portal portal) {
        this.portal = portal;
    }

    @Override
    public void postProcessBeforeStartRegister(R registration) {
        registration.getMetadata().put(KeyConstant.PROTOCOL, Component.Protocol.HTTP);
    }

    @Override
    public void postProcessAfterStartRegister(R registration) {

    }

    @Override
    public void postProcessBeforeStopRegister(R registration) {

    }

    @Override
    public void postProcessAfterStopRegister(R registration) {

    }

}
