package io.github.taikonaut3.filter;

import io.effi.rpc.core.Invocation;
import io.effi.rpc.core.Portal;
import io.effi.rpc.core.filter.Filter;
import org.example.Result;
import org.springframework.stereotype.Component;

/**
 * @Author WenBo Zhou
 * @Date 2024/3/26 15:44
 */
@Component
public class CallerResultFilter implements Filter {

    public CallerResultFilter(Portal portal) {

    }

    @Override
    public io.effi.rpc.core.result.Result doFilter(Invocation<io.effi.rpc.core.result.Result> invocation) {
        invocation.rebase(() -> {
            Object t = invocation.invoke();
            return new Result("000", t, "world");
        });
        return invocation.invoke();
    }
}
