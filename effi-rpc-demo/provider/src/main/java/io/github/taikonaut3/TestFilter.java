package io.github.taikonaut3;

import io.effi.rpc.common.extension.RpcContext;
import io.effi.rpc.core.Invocation;
import io.effi.rpc.core.filter.Filter;
import io.effi.rpc.core.result.Result;
import org.springframework.stereotype.Component;

/**
 * @Author WenBo Zhou
 * @Date 2024/3/15 16:15
 */
@Component
public class TestFilter implements Filter {
    @Override
    public Result doFilter(Invocation<Result> invocation) {
        try {
            return invocation.invoke();
        } finally {
            RpcContext.responseContext().set("123", "456");
        }
    }
}
