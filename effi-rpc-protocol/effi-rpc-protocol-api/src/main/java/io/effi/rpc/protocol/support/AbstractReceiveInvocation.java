package io.effi.rpc.protocol.support;

import io.effi.rpc.common.url.URL;
import io.effi.rpc.core.Callee;
import io.effi.rpc.core.ReceiveInvocation;
import io.effi.rpc.transport.channel.Channel;

/**
 * Abstract implementation of {@link ReceiveInvocation}.
 *
 * @param <T>
 */
public class AbstractReceiveInvocation<T> extends AbstractInvocation<T> implements ReceiveInvocation<T> {

    protected Channel channel;

    protected URL serverUrl;

    protected AbstractReceiveInvocation(Channel channel, URL requestUrl, Callee<?> callee, Object[] args) {
        super(channel.portal(), requestUrl, callee, args);
        this.channel = channel;
        this.serverUrl = channel.url();
    }

    @Override
    public Callee<?> callee() {
        return (Callee<?>) invoker;
    }

    @Override
    public URL serverUrl() {
        return serverUrl;
    }

    /**
     * Returns the channel that received the request.
     *
     * @return
     */
    public Channel channel() {
        return channel;
    }
}
