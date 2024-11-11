package io.effi.rpc.governance.loadbalance;

import io.effi.rpc.common.constant.KeyConstant;
import io.effi.rpc.common.extension.spi.Extension;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.common.util.AtomicUtil;
import io.effi.rpc.core.CallInvocation;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static io.effi.rpc.common.constant.Component.LoadBalance.ROUND_ROBIN;

/**
 * "RoundRobin" load balancing strategy:
 * Requests are assigned to each server in sequence,
 * Each request is assigned in the order of the server list, and starts again at the end of the list,
 * This strategy applies to cases where the server performance is equivalent.
 */
@Extension(ROUND_ROBIN)
public class RoundRobinLoadBalancer extends AbstractLoadBalancer {

    @Override
    protected URL doChoose(CallInvocation<?> invocation, List<URL> urls) {
        AtomicInteger lastIndex = invocation.caller().get(KeyConstant.LAST_CALL_INDEX);
        int current = AtomicUtil.updateAtomicInteger(lastIndex, old -> (old + 1) % urls.size());
        return urls.get(current);
    }

}
