package io.effi.rpc.transport.server;

import io.effi.rpc.common.exception.RpcException;
import io.effi.rpc.common.util.AssertUtil;
import io.effi.rpc.common.util.NetUtil;
import io.effi.rpc.transport.channel.Channel;
import io.effi.rpc.transport.channel.ChannelHandler;

import java.util.Map;

/**
 * Manage active Channel connections for {@link Server}.
 */
public class ChannelManageHandler implements ChannelHandler {

    private final Map<String, Channel> activeChannels;

    public ChannelManageHandler(Map<String, Channel> activeChannels) {
        this.activeChannels = AssertUtil.notNull(activeChannels, "activeChannels cannot be null");
    }

    @Override
    public void connected(Channel channel) throws RpcException {
        String remoteAddress = NetUtil.toAddress(channel.remoteAddress());
        activeChannels.put(remoteAddress, channel);
    }

    @Override
    public void disconnected(Channel channel) throws RpcException {
        String remoteAddress = NetUtil.toAddress(channel.remoteAddress());
        activeChannels.remove(remoteAddress);
    }
}

