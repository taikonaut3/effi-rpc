package io.effi.rpc.transport.netty;

import io.effi.rpc.common.exception.BindException;
import io.effi.rpc.common.exception.ConnectException;
import io.effi.rpc.common.extension.spi.Extension;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.transport.AbstractTransporter;
import io.effi.rpc.transport.channel.ChannelHandler;
import io.effi.rpc.transport.client.Client;
import io.effi.rpc.transport.server.Server;

import static io.effi.rpc.common.constant.Component.Transport.NETTY;

/**
 * Base on netty's transporter.
 */
@Extension(NETTY)
public class NettyTransporter extends AbstractTransporter {

    @Override
    public Server bind(URL endpointUrl, ChannelHandler handler) throws BindException {
        return ProtocolSupport.bindServer(endpointUrl, handler);
    }

    @Override
    public Client connect(URL endpointUrl, ChannelHandler handler) throws ConnectException {
        return ProtocolSupport.connectClient(endpointUrl, handler);
    }
}
