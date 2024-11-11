package io.effi.rpc.transport.netty;

import io.effi.rpc.common.extension.spi.Extension;
import io.effi.rpc.core.Portal;
import io.effi.rpc.core.PortalConfiguration;
import io.effi.rpc.transport.netty.client.NettyClient;

/**
 * Close netty client event loop group.
 */
@Extension("clientEventGroupCloser")
public class ClientEventGroupCloser implements PortalConfiguration {

    @Override
    public void postStop(Portal portal) {
        NettyClient.closeNioEventLoopGroup();
    }
}
