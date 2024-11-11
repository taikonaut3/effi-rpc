package io.github.taikonaut3;

import io.effi.rpc.core.Invocation;
import io.effi.rpc.core.Portal;
import io.effi.rpc.core.filter.Filter;
import io.effi.rpc.core.result.Result;
import io.effi.rpc.metrics.CalleeMetrics;
import org.springframework.stereotype.Component;

import static io.effi.rpc.common.constant.Component.Protocol.*;

/**
 * @Author WenBo Zhou
 * @Date 2024/5/9 19:46
 */
@Component
public class CalleeMetricsExportFilter implements Filter {

    public static CalleeMetrics h2Wrapper;

    public static CalleeMetrics httpWrapper;

    public static CalleeMetrics virtueWrapper;

    public CalleeMetricsExportFilter(Portal portal) {
    }

    @Override
    public Result doFilter(Invocation<Result> invocation) {
        String protocol = invocation.requestUrl().protocol();
        CalleeMetrics calleeMetrics = invocation.invoker().get(CalleeMetrics.ATTRIBUTE_KEY);
        switch (protocol) {
            case H2 -> h2Wrapper = calleeMetrics;
            case HTTPS -> httpWrapper = calleeMetrics;
            case VIRTUE -> virtueWrapper = calleeMetrics;
        }
        return invocation.invoke();
    }
}
