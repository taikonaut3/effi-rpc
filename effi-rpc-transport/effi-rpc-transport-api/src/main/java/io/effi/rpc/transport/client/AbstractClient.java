package io.effi.rpc.transport.client;

import io.effi.rpc.common.constant.Constant;
import io.effi.rpc.common.constant.KeyConstant;
import io.effi.rpc.common.exception.ConnectException;
import io.effi.rpc.common.exception.NetWorkException;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.common.util.NetUtil;
import io.effi.rpc.internal.logging.Logger;
import io.effi.rpc.internal.logging.LoggerFactory;
import io.effi.rpc.transport.channel.Channel;
import io.effi.rpc.transport.channel.ChannelHandler;
import io.effi.rpc.transport.endpoint.AbstractEndpoint;

import static io.effi.rpc.common.util.ObjectUtil.simpleClassName;

/**
 * Abstract implementation of {@link Client}.
 */
public abstract class AbstractClient extends AbstractEndpoint implements Client {

    protected int connectTimeout;

    protected Channel channel;

    protected ChannelHandler channelHandler;

    protected boolean isInit = false;

    protected AbstractClient(URL endpointUrl, ChannelHandler channelHandler) throws ConnectException {
        super(endpointUrl);
        this.channelHandler = channelHandler;
        this.connectTimeout = endpointUrl.getIntParam(KeyConstant.CONNECT_TIMEOUT, Constant.DEFAULT_CONNECT_TIMEOUT);
        try {
            connect();
            Logger logger = LoggerFactory.getLogger(this.getClass());
            logger.debug("Create <{}>{} succeeded,connect to remoteAddress: {}", endpointUrl.protocol(), simpleClassName(this), address());
        } catch (Throwable e) {
            throw new ConnectException(String.format("Create <%s>%s failed,connect to remoteAddress: %s", endpointUrl.protocol(), simpleClassName(this), address()), e);
        }
    }

    @Override
    public void connect() throws ConnectException {
        if (isActive()) {
            return;
        }
        // When reconnecting, there is no need to initialize again
        if (!isInit) {
            doInit();
            isInit = true;
        }
        doConnect();
    }

    @Override
    public void close() throws NetWorkException {
        try {
            channel.close();
        } catch (Throwable e) {
            throw new NetWorkException(e);
        }
    }

    @Override
    public Channel channel() {
        return channel;
    }

    @Override
    public String toString() {
        return simpleClassName(this) + " connect to " + NetUtil.toAddress(inetSocketAddress());
    }

    protected abstract void doInit() throws ConnectException;

    protected abstract void doConnect() throws ConnectException;

}
