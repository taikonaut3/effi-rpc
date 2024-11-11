package io.effi.rpc.metrics.filter;

import io.effi.rpc.core.Invocation;
import io.effi.rpc.core.Invoker;
import io.effi.rpc.core.Portal;
import io.effi.rpc.core.filter.ReplyFilter;
import io.effi.rpc.core.manager.FilterManager;
import io.effi.rpc.core.result.Result;
import io.effi.rpc.metrics.CallerMetrics;
import io.effi.rpc.metrics.MetricsSupport;
import io.effi.rpc.metrics.event.CallerMetricsEvent;

/**
 * Callee Metrics Filter.
 */
public class CallerMetricsFilter implements ReplyFilter {

    public CallerMetricsFilter(Portal portal) {
        FilterManager manager = portal.filterManager();
    }

    @Override
    public Result doFilter(Invocation<Result> invocation) {
        long start = MetricsSupport.getStartTime(invocation);
        long end = MetricsSupport.recordEndTime(invocation);
        long currentDuration = (end - start) / 1_000_000;
        Invoker<?> invoker = invocation.invoker();
        CallerMetrics callerMetrics = invoker.get(CallerMetrics.ATTRIBUTE_KEY);
        Result result = invocation.invoke();
        invocation.portal().publishEvent(new CallerMetricsEvent(callerMetrics, result.hasException(), currentDuration));
        return result;

    }
}
