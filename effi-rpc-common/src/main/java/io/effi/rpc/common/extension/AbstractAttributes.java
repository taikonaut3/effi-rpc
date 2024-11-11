package io.effi.rpc.common.extension;

import io.effi.rpc.common.extension.collection.LazyMap;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * Abstract implementation of {@link Attributes}.
 */

@SuppressWarnings("unchecked")
public abstract class AbstractAttributes implements Attributes {

    protected Map<AttributeKey<?>, Object> attributes = new LazyMap<>(ConcurrentHashMap::new);

    @Override
    public <T> T get(AttributeKey<T> key) {
        return (T) attributes.get(key);
    }

    @Override
    public <T> T computeIfAbsent(AttributeKey<T> key, Supplier<T> creator) {
        return (T) attributes.computeIfAbsent(key, k -> creator.get());
    }

    @Override
    public <T> T set(AttributeKey<T> key, T value) {
        return (T) attributes.computeIfAbsent(key, k -> value);
    }

    @Override
    public Attributes remove(AttributeKey<?> key) {
        attributes.remove(key);
        return this;
    }

    @Override
    public void clear() {
        attributes.clear();
    }
}
