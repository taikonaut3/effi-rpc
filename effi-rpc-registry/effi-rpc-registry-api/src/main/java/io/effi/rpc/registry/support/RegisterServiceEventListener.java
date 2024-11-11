package io.effi.rpc.registry.support;

import io.effi.rpc.core.Portal;
import io.effi.rpc.event.EventListener;
import io.effi.rpc.internal.logging.Logger;
import io.effi.rpc.internal.logging.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * RegisterService EventListener.
 */
public class RegisterServiceEventListener implements EventListener<RegisterServiceEvent> {

    private static final Logger logger = LoggerFactory.getLogger(RegisterServiceEventListener.class);

    @Override
    public void onEvent(RegisterServiceEvent event) {
        Portal portal = event.portal();
        // todo tcp config?
        portal.scheduler().addPeriodic(event.source(), 5, 5, TimeUnit.SECONDS);
        String protocol = event.url().protocol();
        String address = event.url().address();
        logger.info("The <{}>{} service register is executed every 5s", protocol, address);
    }
}
