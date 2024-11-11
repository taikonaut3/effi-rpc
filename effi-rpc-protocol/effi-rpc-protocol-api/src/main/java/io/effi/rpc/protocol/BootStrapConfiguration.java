package io.effi.rpc.protocol;

import io.effi.rpc.common.extension.spi.Extension;
import io.effi.rpc.core.Portal;
import io.effi.rpc.core.PortalConfiguration;
import io.effi.rpc.core.ServerExporter;
import io.effi.rpc.event.EventDispatcher;
import io.effi.rpc.internal.logging.Logger;
import io.effi.rpc.internal.logging.LoggerFactory;
import io.effi.rpc.metrics.CalleeMetrics;
import io.effi.rpc.metrics.CallerMetrics;
import io.effi.rpc.metrics.event.CalleeMetricsEvent;
import io.effi.rpc.metrics.event.CalleeMetricsEventListener;
import io.effi.rpc.metrics.event.CallerMetricsEvent;
import io.effi.rpc.metrics.event.CallerMetricsEventListener;
import io.effi.rpc.metrics.filter.CalleeMetricsFilter;
import io.effi.rpc.metrics.filter.CallerMetricsFilter;
import io.effi.rpc.protocol.event.*;
import io.effi.rpc.registry.support.RegisterServiceEvent;
import io.effi.rpc.registry.support.RegisterServiceEventListener;
import io.effi.rpc.transport.event.IdleEvent;
import io.effi.rpc.transport.event.IdleEventListener;
import io.effi.rpc.transport.event.RefreshHeartBeatCountEvent;
import io.effi.rpc.transport.event.RefreshHeartBeatCountEventListener;

/**
 * Registry Global Default Listeners and Open Server.
 */
@Extension("bootstrapConfiguration")
public class BootStrapConfiguration implements PortalConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(BootStrapConfiguration.class);

    @Override
    public void postInit(Portal portal) {
        // register default event listeners
        EventDispatcher eventDispatcher = portal.eventDispatcher();
        eventDispatcher.registerListener(RequestEvent.class, new RequestEventListener());
        eventDispatcher.registerListener(ResponseEvent.class, new ResponseEventListener());
        eventDispatcher.registerListener(ClientHandlerExceptionEvent.class, new ClientHandlerExceptionListener());
        eventDispatcher.registerListener(ServerHandlerExceptionEvent.class, new ServerHandlerExceptionListener());
        eventDispatcher.registerListener(RefreshHeartBeatCountEvent.class, new RefreshHeartBeatCountEventListener());
        eventDispatcher.registerListener(IdleEvent.class, new IdleEventListener());
        eventDispatcher.registerListener(SendMessageEvent.class, new SendMessageEventListener());
        eventDispatcher.registerListener(RegisterServiceEvent.class, new RegisterServiceEventListener());
        eventDispatcher.registerListener(CallerMetricsEvent.class, new CallerMetricsEventListener());
        eventDispatcher.registerListener(CalleeMetricsEvent.class, new CalleeMetricsEventListener());
        eventDispatcher.registerListener(FaultToleranceEvent.class, new FaultToleranceEventListener());

        // register default filter
        portal.register(CalleeMetrics.ATTRIBUTE_KEY.toString(), new CalleeMetricsFilter(portal));
        portal.register(CallerMetrics.ATTRIBUTE_KEY.toString(), new CallerMetricsFilter(portal));
    }

    @Override
    public void postStart(Portal portal) {
        portal.serverExporterManager().values().forEach(ServerExporter::export);
    }
}
