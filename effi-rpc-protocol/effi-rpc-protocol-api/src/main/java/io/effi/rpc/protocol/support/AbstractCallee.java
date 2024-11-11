package io.effi.rpc.protocol.support;

import io.effi.rpc.common.constant.KeyConstant;
import io.effi.rpc.common.exception.RpcException;
import io.effi.rpc.common.extension.Ordered;
import io.effi.rpc.common.extension.TypeToken;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.common.url.URLType;
import io.effi.rpc.common.util.CollectionUtil;
import io.effi.rpc.common.util.DateUtil;
import io.effi.rpc.core.Callee;
import io.effi.rpc.core.Invocation;
import io.effi.rpc.core.ReceiveInvocation;
import io.effi.rpc.core.RemoteService;
import io.effi.rpc.core.arg.MethodMapper;
import io.effi.rpc.core.arg.ParameterMapper;
import io.effi.rpc.core.filter.ChosenFilter;
import io.effi.rpc.core.filter.Filter;
import io.effi.rpc.core.filter.ReplyFilter;
import io.effi.rpc.core.result.Result;
import io.effi.rpc.internal.logging.Logger;
import io.effi.rpc.internal.logging.LoggerFactory;
import io.effi.rpc.metrics.CalleeMetrics;
import io.effi.rpc.protocol.support.builder.CalleeBuilder;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract implementation of {@link Callee}.
 *
 * @param <T>
 */
@Getter
@Accessors(fluent = true)
public abstract class AbstractCallee<T> extends AbstractInvoker<Object> implements Callee<T> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractCallee.class);

    protected MethodMapper<T> methodMapper;

    protected String desc;

    @SuppressWarnings("unchecked")
    protected AbstractCallee(URL url, CalleeBuilder<?, ?> builder) {
        super(url, builder);
        this.methodMapper = (MethodMapper<T>) builder.methodMapper();
        this.desc = builder.desc();
        this.returnType = new TypeToken<>(method().getGenericReturnType()) {};
        set(CalleeMetrics.ATTRIBUTE_KEY, new CalleeMetrics());
        remoteService().addCallee(this);
    }

    @Override
    public Result receive(ReceiveInvocation<?> invocation) {
        URL url = invocation.requestUrl();
        List<Filter> filters = new ArrayList<>(this.filters);
        List<ReplyFilter> replyFilters = new ArrayList<>(this.replyFilters);
        orderedFilters(invocation, filters, replyFilters);
        Invocation<Result> filterInvocation = invocation.rebase(() -> {
            Object returnValue = null;
            try {
                if (URLType.CALLER.valid(url)) {
                    long timeout = url.getLongParam(KeyConstant.TIMEOUT);
                    String timestamp = url.getParam(KeyConstant.TIMESTAMP);
                    LocalDateTime localDateTime = DateUtil.parse(timestamp);
                    long margin = Duration.between(localDateTime, LocalDateTime.now()).toMillis();
                    if (margin < timeout) {
                        returnValue = invoke(invocation.args());
                        long invokeAfterMargin = Duration.between(localDateTime, LocalDateTime.now()).toMillis();
                        if (invokeAfterMargin > timeout) {
                            returnValue = null;
                        }
                    }
                } else {
                    returnValue = invoke(invocation.args());
                }
            } catch (RpcException e) {
                returnValue = e;
            }
            url.addParams(this.url.params());
            Result result = new Result(url, returnValue);
            Invocation<Result> replyFilterInvocation = invocation.rebase(() -> result);
            return filterChain.execute(replyFilterInvocation, Ordered.order(replyFilters));
        });
        return filterChain.execute(filterInvocation, Ordered.order(filters));
    }

    protected void orderedFilters(Invocation<?> invocation, List<Filter> filters, List<ReplyFilter> replyFilters) {
        List<Filter> sharedFilters = invocation.portal().filterManager().sharedValues();
        for (Filter sharedFilter : sharedFilters) {
            if (!(sharedFilter instanceof ReplyFilter) && !(sharedFilter instanceof ChosenFilter)) {
                CollectionUtil.addUnique(filters, sharedFilter);
            } else if (sharedFilter instanceof ReplyFilter replyFilter) {
                CollectionUtil.addUnique(replyFilters, replyFilter);
            }
        }
    }

    @Override
    public RemoteService<T> remoteService() {
        return methodMapper.remoteService();
    }

    @Override
    public Method method() {
        return methodMapper.method();
    }

    @Override
    public ParameterMapper[] parameterMappers() {
        return methodMapper.parameterMappers();
    }

    @Override
    public Object invoke(Object... args) throws RpcException {
        try {
            return remoteService().invokeCallee(this, args);
        } catch (Exception e) {
            logger.error("{} invoke failed", e, this);
            throw RpcException.wrap(e);
        }
    }
}
