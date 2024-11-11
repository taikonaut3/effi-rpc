package io.effi.rpc.governance.loadbalance;

import io.effi.rpc.common.exception.RpcException;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.common.util.CollectionUtil;
import io.effi.rpc.core.CallInvocation;

import java.util.List;

/**
 * Abstract implementation of {@link LoadBalancer}.
 */
public abstract class AbstractLoadBalancer implements LoadBalancer {

    @Override
    public URL choose(CallInvocation<?> invocation, List<URL> urls) {
        if (CollectionUtil.isEmpty(urls)) {
            throw new RpcException("Not available service");
        }
        if (urls.size() == 1) {
            return urls.get(0);
        }
        return doChoose(invocation, urls);
    }

    protected abstract URL doChoose(CallInvocation<?> invocation, List<URL> urls);

}
