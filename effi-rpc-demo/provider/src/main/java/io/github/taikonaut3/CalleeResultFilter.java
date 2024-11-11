package io.github.taikonaut3;

import io.effi.rpc.core.Invocation;
import io.effi.rpc.core.filter.Filter;
import io.effi.rpc.core.result.Result;
import org.springframework.stereotype.Component;

/**
 * @Author WenBo Zhou
 * @Date 2024/3/26 15:41
 */
@Component
public class CalleeResultFilter implements Filter {
    @Override
    public Result doFilter(Invocation<Result> invocation) {
        return invocation.invoke();
    }
}
