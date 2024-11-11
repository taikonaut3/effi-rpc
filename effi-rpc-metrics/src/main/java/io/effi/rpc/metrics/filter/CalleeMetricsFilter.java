package io.effi.rpc.metrics.filter;

import io.effi.rpc.core.Invocation;
import io.effi.rpc.core.Invoker;
import io.effi.rpc.core.Portal;
import io.effi.rpc.core.filter.Filter;
import io.effi.rpc.core.manager.FilterManager;
import io.effi.rpc.core.result.Result;
import io.effi.rpc.metrics.CalleeMetrics;
import io.effi.rpc.metrics.MetricsSupport;
import io.effi.rpc.metrics.event.CalleeMetricsEvent;

/**
 * Callee Metrics Filter.
 */
public class CalleeMetricsFilter implements Filter {

    public CalleeMetricsFilter(Portal portal) {
        FilterManager manager = portal.filterManager();
    }

    @Override
    public Result doFilter(Invocation<Result> invocation) {
        Invoker<?> invoker = invocation.invoker();
        CalleeMetrics calleeMetrics = invoker.get(CalleeMetrics.ATTRIBUTE_KEY);
        long start = MetricsSupport.recordStartTime(invocation);
        Result result = invocation.invoke();
        long end = MetricsSupport.recordEndTime(invocation);
        long currentDuration = (end - start) / 1_000_000;
        invocation.portal().publishEvent(new CalleeMetricsEvent(calleeMetrics, result.hasException(), currentDuration));
        return result;

    }
}
