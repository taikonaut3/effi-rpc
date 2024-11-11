package io.effi.rpc.protocol.support;

import io.effi.rpc.common.extension.AbstractAttributes;
import io.effi.rpc.common.extension.TypeToken;
import io.effi.rpc.common.extension.collection.LazyList;
import io.effi.rpc.common.extension.spi.ExtensionLoader;
import io.effi.rpc.common.url.QueryPath;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.common.util.AssertUtil;
import io.effi.rpc.common.util.CollectionUtil;
import io.effi.rpc.common.util.ObjectUtil;
import io.effi.rpc.core.Invoker;
import io.effi.rpc.core.filter.Filter;
import io.effi.rpc.core.filter.FilterChain;
import io.effi.rpc.core.filter.ReplyFilter;
import io.effi.rpc.protocol.support.builder.InvokerBuilder;
import io.effi.rpc.transport.Protocol;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract implementation of {@link Invoker}.
 *
 * @param <R>
 */
@Getter
@Accessors(fluent = true)
public abstract class AbstractInvoker<R> extends AbstractAttributes implements Invoker<R> {

    protected List<Filter> filters = new LazyList<>(ArrayList::new);

    protected List<ReplyFilter> replyFilters = new LazyList<>(ArrayList::new);

    protected FilterChain filterChain = ExtensionLoader.loadExtension(FilterChain.class);

    protected URL url;

    protected QueryPath queryPath;

    protected TypeToken<?> returnType;

    protected Protocol protocol;

    protected AbstractInvoker(URL url, InvokerBuilder<?, ?> builder) {
        this.url = AssertUtil.notNull(url, "url cannot be null");
        this.queryPath = builder.queryPath();
        this.returnType = builder.returnType();
        this.protocol = ExtensionLoader.loadExtension(Protocol.class, url.protocol());
        addFilter(builder.filters().toArray(Filter[]::new));
    }

    @Override
    public void addFilter(Filter... filters) {
        if (CollectionUtil.isNotEmpty(filters)) {
            for (Filter filter : filters) {
                if (filter instanceof ReplyFilter replyFilter) {
                    CollectionUtil.addUnique(replyFilters, replyFilter);
                } else {
                    CollectionUtil.addUnique(this.filters, filter);
                }
            }
        }
    }

    @Override
    public String toString() {
        return ObjectUtil.simpleClassName(this) + "<" + url.uri() + ">";
    }
}
