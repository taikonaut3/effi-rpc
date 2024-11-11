package io.effi.rpc.protocol.support.arg;

import io.effi.rpc.common.url.URL;
import io.effi.rpc.common.util.StringUtil;
import io.effi.rpc.core.Callee;
import io.effi.rpc.core.arg.Argument;
import io.effi.rpc.core.arg.ParamVar;
import io.effi.rpc.core.arg.ParameterMapper;
import io.effi.rpc.transport.Envelope;
import io.effi.rpc.transport.resolver.ArgumentHandler;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * Handle the extraction of URL parameters from requests based on a dynamic parameter variable.
 *
 * @param <REQ> The type of request envelope containing the URL from which parameters are extracted.
 */
public class ParamVarHandler<REQ extends Envelope> implements ArgumentHandler<REQ> {

    private final Function<REQ, URL> acquireUrl;

    public ParamVarHandler(Function<REQ, URL> acquireUrl) {
        this.acquireUrl = acquireUrl;
    }

    @Override
    public boolean isSupportedArgument(Callee<?> callee, ParameterMapper wrapper) {
        return wrapper.argument() instanceof ParamVar<?> paramVar
                && paramVar.get() instanceof Argument.Source source
                && StringUtil.isNotBlank(source.get());
    }

    @Override
    public Object handler(REQ request, Callee<?> callee, ParameterMapper wrapper) {
        @SuppressWarnings("unchecked")
        ParamVar<Argument.Source> argument = (ParamVar<Argument.Source>) wrapper.argument();
        String paramVar = argument.get().get();
        URL requestUrl = acquireUrl.apply(request);
        Map<String, String> requestParams = requestUrl.params();
        for (String paramKey : requestParams.keySet()) {
            if (Objects.equals(paramKey, paramVar)) {
                return requestParams.get(paramKey);
            }
        }
        return null;
    }
}

