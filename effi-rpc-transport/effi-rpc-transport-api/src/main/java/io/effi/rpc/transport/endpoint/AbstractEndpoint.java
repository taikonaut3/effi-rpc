package io.effi.rpc.transport.endpoint;

import io.effi.rpc.common.url.URL;
import io.effi.rpc.core.Portal;
import io.effi.rpc.core.utils.PortalUtil;

import java.net.InetSocketAddress;

/**
 * Abstract implementation of {@link Endpoint}.
 */
public abstract class AbstractEndpoint implements Endpoint {

    protected InetSocketAddress address;

    protected URL url;

    protected Portal portal;

    protected AbstractEndpoint(URL url) {
        this.url = url;
        this.address = new InetSocketAddress(url.host(), url.port());
        this.portal = PortalUtil.acquirePortal(url);
    }

    @Override
    public Portal portal() {
        return portal;
    }

    @Override
    public URL url() {
        return url;
    }

    @Override
    public String host() {
        return url.host();
    }

    @Override
    public int port() {
        return url.port();
    }

    @Override
    public InetSocketAddress inetSocketAddress() {
        return address;
    }

}
