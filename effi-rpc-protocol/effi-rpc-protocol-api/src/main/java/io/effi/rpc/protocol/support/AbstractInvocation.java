package io.effi.rpc.protocol.support;

import io.effi.rpc.common.exception.RpcException;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.common.url.URLType;
import io.effi.rpc.common.util.AssertUtil;
import io.effi.rpc.core.Invocation;
import io.effi.rpc.core.Invoker;
import io.effi.rpc.core.Portal;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.lang.reflect.Type;
import java.util.function.Supplier;

/**
 * Abstract implementation of {@link Invocation}.
 *
 * @param <T>
 */
@Getter
@Accessors(fluent = true)
public class AbstractInvocation<T> implements Invocation<T> {

    protected URL requestUrl;

    protected Portal portal;

    protected Invoker<?> invoker;

    protected Object[] args;

    protected Type returnType;

    protected Supplier<?> invoke;

    protected AbstractInvocation(Portal portal, URL requestUrl, Invoker<?> invoker, Object[] args) {
        this.portal = AssertUtil.notNull(portal, "portal cannot be null");
        this.invoker = AssertUtil.notNull(invoker, "invoker cannot be null");
        AssertUtil.condition(requestUrl != null && URLType.REQUEST.valid(requestUrl), "invalid request url");
        this.requestUrl = requestUrl;
        this.args = args;
        this.returnType = invoker.returnType().type();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T invoke() throws RpcException {
        if (invoke != null) {
            return (T) invoke.get();
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R> Invocation<R> rebase(Supplier<R> invoke) {
        this.invoke = invoke;
        return (Invocation<R>) this;
    }

    @Override
    public void args(Object... args) {
        this.args = args;
    }

    @Override
    public Object[] args() {
        return args;
    }
}
