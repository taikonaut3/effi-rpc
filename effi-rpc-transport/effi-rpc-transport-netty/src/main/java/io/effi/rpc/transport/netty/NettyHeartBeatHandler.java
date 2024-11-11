package io.effi.rpc.transport.netty;

import io.effi.rpc.common.constant.KeyConstant;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.event.Event;
import io.effi.rpc.transport.channel.Channel;
import io.effi.rpc.transport.event.IdleEvent;
import io.effi.rpc.transport.event.RefreshHeartBeatCountEvent;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import java.util.concurrent.atomic.AtomicInteger;

import static io.netty.channel.ChannelHandler.Sharable;

/**
 * Netty HeartBeatHandler Adapter.
 */
@Sharable
public class NettyHeartBeatHandler extends ChannelInboundHandlerAdapter implements NettyChannelObtain {

    private final URL endpointUrl;

    private final Boolean isServer;

    public NettyHeartBeatHandler(URL endpointUrl, boolean isServer) {
        this.endpointUrl = endpointUrl;
        this.isServer = isServer;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
        NettyChannel nettyChannel = acquireChannel(ctx);
        publishRefreshHeartbeatCountEvent(nettyChannel);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        NettyChannel.remove(ctx.channel());
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        NettyChannel nettyChannel = acquireChannel(ctx);
        if (evt instanceof IdleStateEvent event) {
            IdleState state = event.state();
            AtomicInteger idleTimes = switch (state) {
                case ALL_IDLE -> nettyChannel.get(KeyConstant.ALL_IDLE_TIMES);
                case READER_IDLE -> isServer ? nettyChannel.get(KeyConstant.READER_IDLE_TIMES) : null;
                case WRITER_IDLE -> !isServer ? nettyChannel.get(KeyConstant.WRITE_IDLE_TIMES) : null;
            };
            if (idleTimes != null) {
                idleTimes.incrementAndGet();
            }
            if (state == IdleState.ALL_IDLE
                    || (isServer && state == IdleState.READER_IDLE)
                    || (!isServer && state == IdleState.WRITER_IDLE)) {
                nettyChannel.portal().publishEvent(new IdleEvent(nettyChannel));
            }
        }
        super.userEventTriggered(ctx, evt);
    }

    private void publishRefreshHeartbeatCountEvent(Channel channel) {
        Event<?> event = isServer
                ? RefreshHeartBeatCountEvent.buildForServer(channel)
                : RefreshHeartBeatCountEvent.buildForClient(channel);
        channel.portal().publishEvent(event);
    }

    @Override
    public URL endpointUrl() {
        return endpointUrl;
    }
}
