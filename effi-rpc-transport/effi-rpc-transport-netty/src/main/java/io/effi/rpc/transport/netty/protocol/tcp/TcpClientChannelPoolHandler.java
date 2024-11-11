package io.effi.rpc.transport.netty.protocol.tcp;

import io.effi.rpc.common.url.URL;
import io.effi.rpc.transport.netty.NettyIdleStateHandler;
import io.effi.rpc.transport.netty.InitializedConfig;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.pool.AbstractChannelPoolHandler;

import static io.effi.rpc.transport.netty.NettyIdleStateHandler.createForClient;

/**
 * Initializes the channel of Netty for Custom Codec.
 */
public class TcpClientChannelPoolHandler extends AbstractChannelPoolHandler {

    private final InitializedConfig config;

    public TcpClientChannelPoolHandler(InitializedConfig config) {
        this.config = config;
    }

    @Override
    public void channelCreated(Channel channel) throws Exception {
        URL url = config.url();
        NettyIdleStateHandler idleStateHandler = createForClient(url);
        ChannelPipeline pipeline = channel.pipeline();
        TcpCodec tcpCodec = new TcpCodec(url, true);
        pipeline.addLast("decoder", tcpCodec.getDecoder())
                .addLast("encoder", tcpCodec.getEncoder())
                .addLast("idleState", idleStateHandler)
                .addLast("heartbeat", idleStateHandler.handler())
                .addLast(config.handler());
    }
}
