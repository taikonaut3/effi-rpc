package io.effi.rpc.protocol.support.builder;

import io.effi.rpc.common.constant.Constant;
import io.effi.rpc.common.constant.KeyConstant;
import io.effi.rpc.common.extension.ChainBuilder;
import io.effi.rpc.common.extension.TypeToken;
import io.effi.rpc.common.extension.collection.LazyList;
import io.effi.rpc.common.url.Parameter;
import io.effi.rpc.common.url.Parameterization;
import io.effi.rpc.common.url.QueryPath;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.common.util.CollectionUtil;
import io.effi.rpc.core.Invoker;
import io.effi.rpc.core.filter.Filter;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder for configuring {@link Invoker} instances.
 *
 * @param <T> The type of {@link Invoker}.
 * @param <C> The type of the builder.
 */
@Getter
@Accessors(fluent = true)
public abstract class InvokerBuilder<T extends Invoker<?>, C extends InvokerBuilder<T, C>>
        implements ChainBuilder<T, C>, Parameterization {

    /**
     * Filters applied to the invoker's operations.
     */
    protected List<Filter> filters = new LazyList<>(ArrayList::new);

    /**
     * Return type of the invoker.
     */
    protected TypeToken<?> returnType;

    /**
     * Path used for the invoker's query.
     */
    protected QueryPath queryPath;

    /**
     * Serialization format for the invoker.
     */
    @Parameter(KeyConstant.SERIALIZATION)
    protected String serialization = Constant.DEFAULT_SERIALIZATION;

    /**
     * Compression method used by the invoker.
     */
    @Parameter(KeyConstant.COMPRESSION)
    protected String compression = Constant.DEFAULT_COMPRESSION;

    /**
     * Sets the compression method.
     *
     * @param compression Compression type.
     * @return This builder.
     */
    public C compression(String compression) {
        this.compression = compression;
        return returnChain();
    }

    /**
     * Sets the serialization format.
     *
     * @param serialization Serialization format.
     * @return This builder.
     */
    public C serialization(String serialization) {
        this.serialization = serialization;
        return returnChain();
    }

    /**
     * Sets the query path for the invoker.
     *
     * @param path Query path string.
     * @return This builder.
     */
    public C path(String path) {
        this.queryPath = QueryPath.valueOf(path);
        return returnChain();
    }

    /**
     * Adds filters to the invoker, avoiding duplicates.
     *
     * @param filters Filters to add.
     * @return This builder.
     */
    public C addFilter(Filter... filters) {
        if (CollectionUtil.isNotEmpty(filters)) {
            for (Filter filter : filters) {
                CollectionUtil.addToList(this.filters, (oldFilter, newFilter) -> oldFilter == newFilter, filter);
            }
        }
        return returnChain();
    }

    /**
     * Builds and returns the configured {@link Invoker} instance.
     *
     * @return Configured {@link Invoker} instance.
     */
    @Override
    public T build() {
        return build(buildUrl());
    }

    /**
     * Defines the protocol used by the invoker.
     *
     * @return Protocol string.
     */
    protected abstract String protocol();

    /**
     * Builds the URL for the invoker configuration.
     *
     * @return Configured URL.
     */
    protected abstract URL buildUrl();

    /**
     * Constructs the invoker instance with the given URL.
     *
     * @param url URL for the invoker.
     * @return New {@link Invoker} instance.
     */
    protected abstract T build(URL url);
}

