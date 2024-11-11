package io.effi.rpc.protocol.handler;

import io.effi.rpc.common.exception.RpcException;
import io.effi.rpc.common.exception.UnSupportedDecodeException;
import io.effi.rpc.protocol.support.ChannelSupport;
import io.effi.rpc.transport.Protocol;
import io.effi.rpc.transport.Response;
import io.effi.rpc.transport.channel.Channel;
import io.effi.rpc.transport.channel.ChannelHandler;
import io.effi.rpc.transport.channel.ChannelMessage;
import io.effi.rpc.transport.codec.ServerCodec;

import java.io.IOException;

/**
 * Converts messages for server using the appropriate
 * encoding and decoding protocols.
 */
public class ServerMessageConverter implements ChannelHandler {

    /**
     * Encodes a {@link Response} message and sends it through the channel.
     *
     * @param msg The {@link ChannelMessage} to be sent.
     * @throws RpcException If encoding or sending the message fails.
     */
    @Override
    public void sent(ChannelMessage msg) throws RpcException {
        if (msg.message() instanceof Response response) {
            Channel channel = msg.channel();
            Protocol protocol = ChannelSupport.acquireProtocol(channel);
            ServerCodec codec = protocol.serverCodec();
            try {
                msg.message(codec.encode(channel, response));
            } catch (IOException e) {
                throw RpcException.wrap(e);
            }
        }
    }

    /**
     * Decodes an incoming message and processes it.
     *
     * @param msg The {@link ChannelMessage} received.
     * @throws RpcException If decoding or processing the message fails,
     *                      excluding unsupported decode exceptions.
     */
    @Override
    public void received(ChannelMessage msg) throws RpcException {
        Channel channel = msg.channel();
        Protocol protocol = ChannelSupport.acquireProtocol(channel);
        ServerCodec codec = protocol.serverCodec();
        try {
            msg.message(codec.decode(channel, msg.message()));
        } catch (IOException e) {
            if (!(e.getCause() instanceof UnSupportedDecodeException)) {
                throw RpcException.wrap(e);
            }
        }
    }
}

