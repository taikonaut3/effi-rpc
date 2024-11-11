package io.effi.rpc.core.manager;

import io.effi.rpc.common.extension.collection.LazyList;
import io.effi.rpc.common.util.CollectionUtil;
import io.effi.rpc.core.Portal;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles shared values of type {@link T},
 * Registering and accessing shared values in a portal context.
 *
 * @param <T> the type of values managed by this class
 */
public class SharableManager<T> extends AbstractManager<T> {

    protected List<T> sharedValues = new LazyList<>(ArrayList::new);

    protected SharableManager(Portal portal) {
        super(portal);
    }

    /**
     * Registers one or more shared values. If a value is already present,
     * it will not be added again.
     *
     * @param values the values to be registered as shared.
     */
    @SafeVarargs
    public final void registerShared(T... values) {
        CollectionUtil.addUnique(sharedValues, values);
    }

    /**
     * Returns the list of shared values.
     *
     * @return a {@link List} of shared values managed by this manager
     */
    public List<T> sharedValues() {
        return sharedValues;
    }
}

