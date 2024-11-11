package io.effi.rpc.metrics.event;

import io.effi.rpc.common.util.AtomicUtil;
import io.effi.rpc.metrics.CalleeMetrics;
import io.effi.rpc.event.EventListener;

import java.util.concurrent.atomic.LongAdder;

/**
 * CalleeMetricsEvent Listener.
 */
public class CalleeMetricsEventListener implements EventListener<CalleeMetricsEvent> {

    @Override
    public void onEvent(CalleeMetricsEvent event) {
        CalleeMetrics calleeMetrics = event.source();
        calleeMetrics.requestCount().increment();
        if (event.hasException()) {
            calleeMetrics.failureCount().increment();
        } else {
            calleeMetrics.successCount().increment();
            long currentDuration = event.currentDuration();
            AtomicUtil.updateAtomicLong(calleeMetrics.maxResponseTime(), old -> Math.max(old, currentDuration));
            AtomicUtil.updateAtomicLong(calleeMetrics.minResponseTime(), old -> old == 0 ? currentDuration : Math.min(old, currentDuration));
            AtomicUtil.updateAtomicReference(calleeMetrics.averageResponseTime(), old -> {
                LongAdder successCount = calleeMetrics.successCount();
                double totalSuccessTime = (successCount.doubleValue() - 1) * old;
                return (totalSuccessTime + currentDuration) / successCount.doubleValue();
            });
        }
    }
}
