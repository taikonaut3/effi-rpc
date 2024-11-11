package io.effi.rpc.protocol.support;

import io.effi.rpc.common.url.URL;
import io.effi.rpc.protocol.support.builder.PortalBuilder;

/**
 * Default implementation of a {@link io.effi.rpc.core.Portal}.
 * This class provides the functionality for creating and configuring a portal instance.
 */
public class DefaultPortal extends AbstractPortal {

    DefaultPortal(URL url) {
        super(url);
    }

    /**
     * Creates a builder for constructing a {@link DefaultPortal} instance.
     *
     * @return A new {@link DefaultPortalBuilder}.
     */
    public static DefaultPortalBuilder builder() {
        return new DefaultPortalBuilder();
    }

    /**
     * Builder class for constructing a {@link DefaultPortal} instance.
     */
    public static class DefaultPortalBuilder extends PortalBuilder<DefaultPortal, DefaultPortalBuilder> {

        @Override
        protected DefaultPortal buildPortal(URL url) {
            return new DefaultPortal(url);
        }
    }
}

