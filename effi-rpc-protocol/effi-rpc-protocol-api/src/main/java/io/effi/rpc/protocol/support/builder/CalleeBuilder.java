package io.effi.rpc.protocol.support.builder;

import io.effi.rpc.common.constant.Constant;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.common.url.URLType;
import io.effi.rpc.common.util.CollectionUtil;
import io.effi.rpc.core.Callee;
import io.effi.rpc.core.arg.MethodMapper;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * Builder for creating {@link Callee} instances,defining settings for callee.
 *
 * @param <T> The type of {@link Callee}.
 * @param <C> The type of the builder.
 */
@Getter
@Accessors(fluent = true)
public abstract class CalleeBuilder<T extends Callee<?>, C extends CalleeBuilder<T, C>>
        extends InvokerBuilder<T, C> {

    /**
     * Mapper for the callee's methods.
     */
    protected MethodMapper<?> methodMapper;

    /**
     * Description of the callee.
     */
    protected String desc;

    protected CalleeBuilder(MethodMapper<?> methodMapper) {
        this.methodMapper = methodMapper;
    }

    /**
     * Sets callee description.
     *
     * @param desc
     * @return
     */
    public C desc(String desc) {
        this.desc = desc;
        return returnChain();
    }

    @Override
    protected URL buildUrl() {
        if (queryPath == null || CollectionUtil.isEmpty(queryPath.paths())) {
            path(methodMapper.remoteService().name() + "/" + methodMapper.method().getName());
        }
        return URL.builder()
                .type(URLType.CALLEE)
                .protocol(protocol())
                .address(Constant.DEFAULT_ADDRESS)
                .path(queryPath.path())
                .params(parameterization())
                .build();
    }
}
