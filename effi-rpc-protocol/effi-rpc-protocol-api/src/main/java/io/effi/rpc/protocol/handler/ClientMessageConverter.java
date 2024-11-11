package io.effi.rpc.protocol.handler;

import io.effi.rpc.common.exception.RpcException;
import io.effi.rpc.common.exception.UnSupportedDecodeException;
import io.effi.rpc.protocol.support.ChannelSupport;
import io.effi.rpc.transport.Protocol;
import io.effi.rpc.transport.Request;
import io.effi.rpc.transport.channel.Channel;
import io.effi.rpc.transport.channel.ChannelHandler;
import io.effi.rpc.transport.channel.ChannelMessage;
import io.effi.rpc.transport.codec.ClientCodec;

import java.io.IOException;

/**
 * Converts messages for client using the appropriate
 * encoding and decoding protocols.
 */
public class ClientMessageConverter implements ChannelHandler {

    /**
     * Encodes a {@link Request} message and sends it through the channel.
     *
     * @param msg The {@link ChannelMessage} to be sent.
     * @throws RpcException If encoding or sending the message fails.
     */
    @Override
    public void sent(ChannelMessage msg) throws RpcException {
        if (msg.message() instanceof Request request) {
            Channel channel = msg.channel();
            Protocol protocol = ChannelSupport.acquireProtocol(channel);
            ClientCodec codec = protocol.clientCodec();
            try {
                msg.message(codec.encode(channel, request));
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
        ClientCodec codec = protocol.clientCodec();
        try {
            msg.message(codec.decode(channel, msg.message()));
        } catch (IOException e) {
            if (!(e.getCause() instanceof UnSupportedDecodeException)) {
                throw RpcException.wrap(e);
            }
        }
    }
}
