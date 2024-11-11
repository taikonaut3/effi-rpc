package io.effi.rpc.governance.loadbalance;

import io.effi.rpc.common.extension.spi.Extensible;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.core.CallInvocation;

import java.util.List;

import static io.effi.rpc.common.constant.Component.LoadBalance.RANDOM;
import static io.effi.rpc.common.constant.KeyConstant.LOAD_BALANCE;

/**
 * Load balancing strategies for selecting a URL from a list of available URLs
 * based on the context of the invocation.
 */
@Extensible(value = RANDOM, key = LOAD_BALANCE)
public interface LoadBalancer {

    /**
     * Selects a URL for a given invocation from a list of available URLs.
     * Different implementations may employ various strategies for selection.
     *
     * @param invocation the context for the selection process
     * @param urls       a list of available URLs to choose from
     * @return the selected URL
     */
    URL choose(CallInvocation<?> invocation, List<URL> urls);
}



