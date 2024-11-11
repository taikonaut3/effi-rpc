package io.effi.rpc.metrics.event;

import io.effi.rpc.metrics.CallerMetrics;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * Callee Metrics Event.
 */
@Getter
@Accessors(fluent = true)
public class CallerMetricsEvent extends MetricsEvent<CallerMetrics> {

    public CallerMetricsEvent(CallerMetrics callerMetrics, boolean hasException, long currentDuration) {
        super(callerMetrics, hasException, currentDuration);

    }

}
