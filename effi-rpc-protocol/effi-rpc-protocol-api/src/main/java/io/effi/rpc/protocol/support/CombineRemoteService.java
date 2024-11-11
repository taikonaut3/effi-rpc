package io.effi.rpc.protocol.support;

import io.effi.rpc.common.util.AssertUtil;
import io.effi.rpc.common.util.GenerateUtil;
import io.effi.rpc.common.util.ReflectionUtil;
import io.effi.rpc.core.Callee;
import io.effi.rpc.core.RemoteService;
import io.effi.rpc.protocol.support.reflect.MethodAccess;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

/**
 * Default RemoteService Impl.
 *
 * @param <T> target type
 */
@ToString
public class CombineRemoteService<T> extends AbstractInvokerContainer implements RemoteService<T> {

    private final Class<T> targetType;

    private final T target;

    private final MethodAccess methodAccess;

    private final Map<Callee<?>, Integer> methodIndex;

    private final String name;

    @SuppressWarnings("unchecked")
    public CombineRemoteService(String name, T target) {
        this.name = AssertUtil.notBlank(name, "name cannot be blank");
        this.target = AssertUtil.notNull(target, "target cannot be null");
        this.targetType = (Class<T>) ReflectionUtil.getTargetClass(target.getClass());
        this.methodIndex = new HashMap<>();
        this.methodAccess = MethodAccess.get(targetType);
    }


    @Override
    public Callee<?> getCallee(String protocol, String path) {
        String key = GenerateUtil.generateCalleeMapping(protocol, path);
        return (Callee<?>) getInvoker(key);
    }

    @Override
    public T target() {
        return target;
    }

    @Override
    public Class<T> targetType() {
        return targetType;
    }

    @Override
    public String name() {
        return name;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <R> R invokeCallee(Callee<T> callee, Object... args) {
        Integer methodIndex = this.methodIndex.get(callee);
        return (R) methodAccess.invoke(target, methodIndex, args);
    }

    @Override
    public RemoteService<T> addCallee(Callee<?> callee) {
        String key = GenerateUtil.generateCalleeMapping(callee.url().protocol(), callee.url().path());
        addInvoker(key, callee);
        methodIndex.put(callee, methodAccess.getIndex(callee.method().getName(), callee.method().getParameterTypes()));
        return this;
    }
}
