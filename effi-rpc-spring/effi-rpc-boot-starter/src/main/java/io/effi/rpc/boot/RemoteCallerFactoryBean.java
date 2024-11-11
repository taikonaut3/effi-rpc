package io.effi.rpc.boot;

import io.effi.rpc.core.Portal;
import io.effi.rpc.core.RemoteCaller;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.FactoryBean;

/**
 * RemoteCaller FactoryBean.
 *
 * @param <T>
 */
public class RemoteCallerFactoryBean<T> implements FactoryBean<T> {

    private final Class<T> interfaceType;

    private BeanFactory beanFactory;

    private RemoteCaller<T> remoteCaller;

    public RemoteCallerFactoryBean(Class<T> interfaceType) {
        this.interfaceType = interfaceType;
    }

    @Resource
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T getObject() throws Exception {
        if (remoteCaller == null) {
            Portal portal = beanFactory.getBean(Portal.class);
//            remoteCaller = virtue.proxy(interfaceType).acquireRemoteCaller(interfaceType);
//            var annotation = interfaceType.getAnnotation(annotation.core.io.effi.protocol.RemoteCaller.class);
//            if (interfaceType.isAssignableFrom(annotation.fallback())) {
//                remoteCaller.fallBacker((T) beanFactory.getBean(annotation.fallback()));
//            }
        }
        return remoteCaller.get();
    }

    @Override
    public Class<?> getObjectType() {
        return interfaceType;
    }

}
