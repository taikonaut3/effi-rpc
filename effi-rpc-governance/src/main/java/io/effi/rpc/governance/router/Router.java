package io.effi.rpc.governance.router;

import io.effi.rpc.common.constant.Component;
import io.effi.rpc.common.constant.KeyConstant;
import io.effi.rpc.common.extension.AttributeKey;
import io.effi.rpc.common.extension.spi.Extensible;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.core.CallInvocation;

import java.util.List;

/**
 * Routes a list of URLs based on the provided invocation context.
 */
@Extensible(Component.DEFAULT)
public interface Router {

    AttributeKey<Router> ATTRIBUTE_KEY = AttributeKey.valueOf(KeyConstant.ROUTER);

    /**
     * Routes the list of URLs according to the given invocation context.
     *
     * @param invocation the context for routing decisions
     * @param urls       the list of URLs to be routed
     * @return a list of routed URLs
     */
    List<URL> route(CallInvocation<?> invocation, List<URL> urls);
}


