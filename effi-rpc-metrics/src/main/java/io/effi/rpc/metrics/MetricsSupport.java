package io.effi.rpc.metrics;

import io.effi.rpc.common.url.URL;
import io.effi.rpc.core.Invocation;
import io.effi.rpc.metrics.constant.MetricsKey;

/**
 * Utility for recording and retrieving invocation metrics.
 */
public class MetricsSupport {

    /**
     * Records the start time of an invocation in nanoseconds.
     *
     * @param invocation The invocation to record the start time for.
     * @return The recorded start time in nanoseconds.
     */
    public static long recordStartTime(Invocation<?> invocation) {
        URL url = invocation.requestUrl();
        long start = System.nanoTime();
        url.set(MetricsKey.START_TIME, start);
        return start;
    }

    /**
     * Records the end time of an invocation in nanoseconds.
     *
     * @param invocation The invocation to record the end time for.
     * @return The recorded end time in nanoseconds.
     */
    public static long recordEndTime(Invocation<?> invocation) {
        URL url = invocation.requestUrl();
        long end = System.nanoTime();
        url.set(MetricsKey.END_TIME, end);
        return end;
    }

    /**
     * Retrieves the recorded start time of an invocation.
     *
     * @param invocation The invocation to retrieve the start time for.
     * @return The start time in nanoseconds.
     */
    public static long getStartTime(Invocation<?> invocation) {
        return invocation.requestUrl().get(MetricsKey.START_TIME);
    }
}

