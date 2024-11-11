package io.effi.rpc.transport.channel;

import io.effi.rpc.common.exception.NetWorkException;
import io.effi.rpc.common.extension.AbstractAttributes;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.common.util.NetUtil;
import io.effi.rpc.core.Portal;
import io.effi.rpc.core.utils.PortalUtil;

import java.net.InetSocketAddress;

import static io.effi.rpc.common.util.ObjectUtil.simpleClassName;

/**
 * Abstract implementation of {@link Channel}.
 */
public abstract class AbstractChannel extends AbstractAttributes implements Channel {

    private final InetSocketAddress localAddress;

    private final InetSocketAddress remoteAddress;

    private final URL endpointUrl;

    private final Portal portal;

    protected AbstractChannel(InetSocketAddress localAddress, InetSocketAddress remoteAddress, URL endpointUrl) {
        this.endpointUrl = endpointUrl;
        this.localAddress = localAddress;
        this.remoteAddress = remoteAddress;
        this.portal = PortalUtil.acquirePortal(endpointUrl);
    }

    @Override
    public void close() {
        if (isActive()) {
            doClose();
            attributes.clear();
        }
    }

    protected abstract void doClose() throws NetWorkException;

    @Override
    public Portal portal() {
        return portal;
    }

    @Override
    public InetSocketAddress localAddress() {
        return localAddress;
    }

    @Override
    public InetSocketAddress remoteAddress() {
        return remoteAddress;
    }

    @Override
    public URL url() {
        return endpointUrl;
    }

    @Override
    public String toString() {
        return String.format("%s %s connect to %s", simpleClassName(this), NetUtil.toAddress(localAddress), NetUtil.toAddress(remoteAddress));
    }
}
