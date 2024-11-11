package io.effi.rpc.protocol.support.builder;

import io.effi.rpc.common.constant.Constant;
import io.effi.rpc.common.constant.KeyConstant;
import io.effi.rpc.common.extension.ChainBuilder;
import io.effi.rpc.common.url.Parameter;
import io.effi.rpc.common.url.Parameterization;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.core.Portal;

/**
 * Builder for configuring {@link Portal} instances.
 *
 * @param <T> The type of {@link Portal}.
 * @param <C> The type of the builder.
 */
public abstract class PortalBuilder<T extends Portal, C extends PortalBuilder<T, C>>
        implements ChainBuilder<T, C>, Parameterization {

    /**
     * Name of the portal.
     */
    protected String name;

    /**
     * Transporter type for the portal.
     */
    @Parameter(KeyConstant.TRANSPORTER)
    protected String transporter = Constant.DEFAULT_TRANSPORTER;

    /**
     * Router used in the portal.
     */
    @Parameter(KeyConstant.ROUTER)
    protected String router = Constant.DEFAULT_ROUTER;

    /**
     * Buffer size for events.
     */
    @Parameter(KeyConstant.BUFFER_SIZE)
    protected int eventBufferSize = Constant.DEFAULT_BUFFER_SIZE;

    /**
     * Number of event subscriptions.
     */
    @Parameter(KeyConstant.SUBSCRIBES)
    protected int eventSubscribes = Constant.DEFAULT_SUBSCRIBES;

    /**
     * Builds and returns the configured {@link Portal} instance.
     *
     * @return Configured {@link Portal} instance.
     */
    @Override
    public T build() {
        URL url = URL.builder()
                .protocol("portal")
                .address(Constant.DEFAULT_ADDRESS)
                .params(parameterization())
                .build();
        return buildPortal(url);
    }

    /**
     * Constructs the {@link Portal} instance using the specified URL.
     *
     * @param url Configuration URL.
     * @return New {@link Portal} instance.
     */
    protected abstract T buildPortal(URL url);
}

