package io.effi.rpc.governance.loadbalance;

import io.effi.rpc.common.extension.spi.Extension;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.core.CallInvocation;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static io.effi.rpc.common.constant.Component.LoadBalance.ROUND_ROBIN;

/**
 * "Random" load balancing strategy:
 * Randomly assign requests to any server in the server cluster,
 * This strategy is simple and fast, but it can lead to an uneven server load.
 */
@Extension(ROUND_ROBIN)
public class RandomLoadBalancer extends AbstractLoadBalancer {

    @Override
    protected URL doChoose(CallInvocation<?> invocation, List<URL> urls) {
        int index = ThreadLocalRandom.current().nextInt(urls.size());
        return urls.get(index);
    }

}
