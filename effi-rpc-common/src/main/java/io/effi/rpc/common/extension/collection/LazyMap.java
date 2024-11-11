package io.effi.rpc.common.extension.collection;

import io.effi.rpc.common.extension.LazyInitializer;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Lazily initialized {@link Map} implementation.
 * The underlying map is created only when necessary, which can save memory and
 * improve performance in scenarios where the map may not be used immediately.
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 */
public class LazyMap<K, V> extends LazyInitializer<Map<K, V>> implements Map<K, V> {

    public LazyMap(Supplier<Map<K, V>> supplier) {
        super(supplier);
    }

    @Override
    public V put(K key, V value) {
        return get(false).put(key, value);
    }

    @Override
    public V get(Object key) {
        return instance == null ? null : instance.get(key);
    }

    @Override
    public V remove(Object key) {
        return instance == null ? null : instance.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        get(false).putAll(m);
    }

    @Override
    public void clear() {
        if (instance != null) {
            instance.clear();
        }
    }

    @Override
    public boolean containsKey(Object key) {
        return instance != null && instance.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return instance != null && instance.containsValue(value);
    }

    @Override
    public Set<K> keySet() {
        return instance == null ? Set.of() : instance.keySet();
    }

    @Override
    public Collection<V> values() {
        return instance == null ? List.of() : instance.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return instance == null ? Set.of() : instance.entrySet();
    }

    @Override
    public int size() {
        return instance == null ? 0 : instance.size();
    }

    @Override
    public boolean isEmpty() {
        return instance == null || instance.isEmpty();
    }

    @Override
    public V getOrDefault(Object key, V defaultValue) {
        return instance == null ? defaultValue : instance.getOrDefault(key, defaultValue);
    }

    @Override
    public V computeIfAbsent(K key, @NotNull Function<? super K, ? extends V> mappingFunction) {
        return get(false).computeIfAbsent(key, mappingFunction);
    }

    @Override
    public V computeIfPresent(K key, @NotNull BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        return get(false).computeIfPresent(key, remappingFunction);
    }
}
