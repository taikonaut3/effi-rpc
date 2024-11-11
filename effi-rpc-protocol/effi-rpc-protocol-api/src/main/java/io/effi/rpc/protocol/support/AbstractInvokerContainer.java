package io.effi.rpc.protocol.support;

import io.effi.rpc.core.Invoker;
import io.effi.rpc.core.InvokerContainer;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Abstract CallerContainer.
 */
@Accessors(fluent = true)
public abstract class AbstractInvokerContainer implements InvokerContainer {

    protected Map<String, Invoker<?>> invokers = new HashMap<>();

    public void addInvoker(String key, Invoker<?> invoker) {
        invokers.put(key, invoker);
    }

    public Invoker<?> getInvoker(String key) {
        return invokers.get(key);
    }

    @Override
    public Invoker<?>[] invokers() {
        return invokers.values().toArray(Invoker[]::new);
    }

    @Override
    public Invoker<?>[] getInvokers(String protocol) {
        return invokers.values().stream()
                .filter(invoker -> Objects.equals(invoker.url().protocol(), protocol))
                .toArray(Invoker[]::new);
    }

}
