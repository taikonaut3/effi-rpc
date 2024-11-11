package io.effi.rpc.protocol.support.arg;

import io.effi.rpc.common.url.URL;
import io.effi.rpc.common.url.URLUtil;
import io.effi.rpc.common.util.StringUtil;
import io.effi.rpc.core.Callee;
import io.effi.rpc.core.arg.Argument;
import io.effi.rpc.core.arg.ParameterMapper;
import io.effi.rpc.core.arg.PathVar;
import io.effi.rpc.transport.Envelope;
import io.effi.rpc.transport.resolver.ArgumentHandler;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * Handle the extraction of path variables from request URLs, dynamically matching
 * the variables in the target URL path.
 *
 * @param <REQ> The type of request envelope containing the URL with path variables.
 */
public class PathVarHandler<REQ extends Envelope> implements ArgumentHandler<REQ> {

    private final Function<REQ, URL> acquireUrl;

    public PathVarHandler(Function<REQ, URL> acquireUrl) {
        this.acquireUrl = acquireUrl;
    }

    @Override
    public boolean isSupportedArgument(Callee<?> callee, ParameterMapper wrapper) {
        return wrapper.argument() instanceof PathVar<?> pathVar
                && pathVar.get() instanceof Argument.Source source
                && StringUtil.isNotBlank(source.get());
    }

    @Override
    public Object handler(REQ request, Callee<?> callee, ParameterMapper wrapper) {
        @SuppressWarnings("unchecked")
        PathVar<Argument.Source> argument = (PathVar<Argument.Source>) wrapper.argument();
        String pathVar = argument.get().get();
        URL requesUrl = acquireUrl.apply(request);
        List<String> requestUrlPaths = requesUrl.paths();
        List<String> calleeUrlPaths = callee.url().paths();
        for (int i = 0; i < calleeUrlPaths.size(); i++) {
            String calleeUrlPath = calleeUrlPaths.get(i);
            String var = URLUtil.getVar(calleeUrlPath);
            if (StringUtil.isNotBlank(var) && Objects.equals(var, pathVar)) {
                return requestUrlPaths.get(i);
            }
        }
        return null;
    }

}
