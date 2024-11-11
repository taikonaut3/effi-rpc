package io.effi.rpc.boot;

import io.effi.rpc.core.Portal;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * Virtue Stater for Spring Boot.
 */
public class SpringBootVirtueStarter implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Portal portal = event.getApplicationContext().getBean(Portal.class);
        portal.start();
    }

}
