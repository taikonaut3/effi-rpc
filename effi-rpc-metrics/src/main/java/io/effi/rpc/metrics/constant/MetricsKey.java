package io.effi.rpc.metrics.constant;

import io.effi.rpc.common.extension.AttributeKey;

/**
 * Metrics Key.
 */
public class MetricsKey {

    public static final AttributeKey<Long> START_TIME = AttributeKey.valueOf("startTime");

    public static final AttributeKey<Long> END_TIME = AttributeKey.valueOf("endTime");

}
