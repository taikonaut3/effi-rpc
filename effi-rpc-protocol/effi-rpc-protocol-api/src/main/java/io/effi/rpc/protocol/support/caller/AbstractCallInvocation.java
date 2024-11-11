package io.effi.rpc.protocol.support.caller;

import io.effi.rpc.common.constant.KeyConstant;
import io.effi.rpc.common.url.*;
import io.effi.rpc.common.util.CollectionUtil;
import io.effi.rpc.common.util.StringUtil;
import io.effi.rpc.core.CallInvocation;
import io.effi.rpc.core.ReplyFuture;
import io.effi.rpc.core.arg.Argument;
import io.effi.rpc.core.arg.ParamVar;
import io.effi.rpc.core.arg.PathVar;
import io.effi.rpc.core.caller.Caller;
import io.effi.rpc.core.utils.PortalUtil;
import io.effi.rpc.protocol.support.AbstractInvocation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Abstract implementation of {@link CallInvocation}.
 *
 * @param <T>
 */
public abstract class AbstractCallInvocation<T> extends AbstractInvocation<T> implements CallInvocation<T> {

    private final URL callerUrl;

    private final URL clientUrl;

    private final ReplyFuture future;

    protected AbstractCallInvocation(ReplyFuture future, Caller<?> caller, Object[] args) {
        super(caller.portal(), buildRequestUrl(caller), caller, args);
        this.future = future;
        this.clientUrl = caller.clientConfig().url().replicate();
        PortalUtil.setPortal(this.clientUrl, portal);
        this.callerUrl = caller.url().replicate();
        handleCallerArgs();
        requestUrl.set(KeyConstant.CALLER_URL, callerUrl);
        future.currentInvocation(this);
    }

    private static URL buildRequestUrl(Caller<?> caller) {
        URL callerUrl = caller.url();
        QueryPath queryPath = caller.queryPath();
        URLBuilder urlBuilder = URL.builder()
                .type(URLType.REQUEST)
                .protocol(callerUrl.protocol())
                .address(callerUrl.address());
        if (queryPath != null) {
            urlBuilder.paths(queryPath.paths()).params(queryPath.queryParams());
        }
        return urlBuilder.build();
    }

    protected void handleCallerArgs() {
        Map<String, String> pathVars = new HashMap<>();
        Map<String, String> paramVars = new HashMap<>();
        if (CollectionUtil.isNotEmpty(args)) {
            for (Object arg : args) {
                if (arg instanceof PathVar<?> pathVar && pathVar.get() instanceof Argument.Target target) {
                    paramVars.putAll(target.get());
                }
                if (arg instanceof ParamVar<?> paramVar && paramVar.get() instanceof Argument.Target target) {
                    paramVars.putAll(target.get());
                }
            }
        }
        if (CollectionUtil.isNotEmpty(pathVars)) {
            List<String> paths = requestUrl.paths();
            for (int i = 0; i < paths.size(); i++) {
                String path = paths.get(i);
                String var = URLUtil.getVar(path);
                if (!StringUtil.isBlank(var)) {
                    paths.add(i, pathVars.getOrDefault(var, path));
                }
            }
        }
        if (CollectionUtil.isNotEmpty(paramVars)) {
            requestUrl.addParams(paramVars);
        }
    }

    @Override
    public Caller<?> caller() {
        return (Caller<?>) invoker;
    }

    @Override
    public URL clientUrl() {
        return clientUrl;
    }

    @Override
    public URL callerUrl() {
        return callerUrl;
    }

    @Override
    public ReplyFuture future() {
        return future;
    }
}
