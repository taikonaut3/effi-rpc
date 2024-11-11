package io.effi.rpc.core.manager;

import io.effi.rpc.core.Portal;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract implementation of {@link Manager}.
 *
 * @param <T> the type of values managed
 */
public abstract class AbstractManager<T> implements Manager<T> {

    protected final Portal portal;

    protected final Map<String, T> map = new HashMap<>();

    protected AbstractManager(Portal portal) {
        this.portal = portal;
    }

    @Override
    public void register(String key, T value) {
        map.put(key, value);
    }

    @Override
    public void remove(String key) {
        map.remove(key);
    }

    public T get(String key) {
        return map.get(key);
    }

    @Override
    public Portal portal() {
        return portal;
    }

    @Override
    public Collection<T> values() {
        return map.values();
    }

    @Override
    public void clear() {
        map.clear();
    }

}
