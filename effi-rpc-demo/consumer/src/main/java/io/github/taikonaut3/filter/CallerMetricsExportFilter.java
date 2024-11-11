package io.github.taikonaut3.filter;

import io.effi.rpc.core.Invocation;
import io.effi.rpc.core.Portal;
import io.effi.rpc.core.filter.Filter;
import io.effi.rpc.core.result.Result;
import io.effi.rpc.metrics.CallerMetrics;
import org.springframework.stereotype.Component;

import static io.effi.rpc.common.constant.Component.Protocol.*;

/**
 * @Author WenBo Zhou
 * @Date 2024/5/9 19:46
 */
@Component
public class CallerMetricsExportFilter implements Filter {

    public static CallerMetrics h2Wrapper = new CallerMetrics();

    public static CallerMetrics httpWrapper = new CallerMetrics();

    public static CallerMetrics virtueWrapper = new CallerMetrics();

    public CallerMetricsExportFilter(Portal portal) {
    }

    @Override
    public Result doFilter(Invocation<Result> invocation) {
        String protocol = invocation.requestUrl().protocol();
        CallerMetrics callerMetrics = invocation.invoker().get(CallerMetrics.ATTRIBUTE_KEY);
        switch (protocol) {
            case H2 -> h2Wrapper = callerMetrics;
            case HTTPS -> httpWrapper = callerMetrics;
            case VIRTUE -> virtueWrapper = callerMetrics;
        }
        return invocation.invoke();
    }
}
