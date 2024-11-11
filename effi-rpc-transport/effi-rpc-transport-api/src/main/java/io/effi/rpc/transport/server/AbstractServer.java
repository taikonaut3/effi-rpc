package io.effi.rpc.transport.server;

import io.effi.rpc.common.exception.BindException;
import io.effi.rpc.common.exception.NetWorkException;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.common.util.NetUtil;
import io.effi.rpc.transport.channel.Channel;
import io.effi.rpc.transport.channel.ChannelHandler;
import io.effi.rpc.transport.channel.ChannelHandlerChain;
import io.effi.rpc.transport.channel.DefaultChannelHandlerChain;
import io.effi.rpc.transport.endpoint.AbstractEndpoint;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Abstract server.
 */
public abstract class AbstractServer extends AbstractEndpoint implements Server {

    protected ChannelHandler channelHandler;

    protected Map<String, Channel> activeChannels;

    private boolean isInit = false;

    protected AbstractServer(URL endpointUrl, ChannelHandler channelHandler) throws BindException {
        super(endpointUrl);
        this.activeChannels = new ConcurrentHashMap<>();
        this.channelHandler = buildChannelHandler(channelHandler);
        try {
            bind();
            url.set(Server.ATTRIBUTE_KEY, this);
        } catch (Throwable e) {
            throw new BindException(String.format("Create <%s>server failed,bind's port(s): %s", endpointUrl.protocol(), port()), e);
        }
    }

    @Override
    public void bind() throws BindException {
        if (isActive()) {
            return;
        }
        if (!isInit) {
            doInit();
            isInit = true;
        }
        doBind();
    }

    @Override
    public void close() {
        for (Channel channel : channels()) {
            try {
                channel.close();
            } catch (Throwable e) {
                throw new NetWorkException(e);
            }
        }
        try {
            doClose();
        } catch (Throwable e) {
            throw new NetWorkException(e);
        }
    }

    @Override
    public Collection<Channel> channels() {
        return activeChannels.values();
    }

    @Override
    public Channel acquireChannel(InetSocketAddress remoteAddress) {
        for (Channel channel : channels()) {
            if (NetUtil.isSameAddress(channel.remoteAddress(), remoteAddress)) {
                return channel;
            }
        }
        return null;
    }

    private ChannelHandler buildChannelHandler(ChannelHandler handler) {
        ChannelManageHandler channelManageHandler = new ChannelManageHandler(this.activeChannels);
        ChannelHandlerChain handlerChain = new DefaultChannelHandlerChain();
        handlerChain.addLast(channelManageHandler).addLast(handler);
        return handlerChain;
    }

    protected abstract void doInit() throws BindException;

    protected abstract void doBind() throws BindException;

    protected abstract void doClose() throws NetWorkException;

}
