package io.effi.rpc.transport.codec;

import io.effi.rpc.transport.channel.Channel;

import java.io.IOException;

/**
 * Encode and Decode data in the channel.
 *
 * @param <I> The type of message to be encoded
 * @param <O> The decoded type
 */
public interface Codec<I, O> {

    /**
     * Encodes a message into a format suitable for transmission over a channel.
     *
     * @param channel The channel used for communication.
     * @param message The message to encode.
     * @return The encoded message as an Object.
     * @throws IOException if an error occurs during encoding.
     */
    Object encode(Channel channel, I message) throws IOException;

    /**
     * Decodes a received message back into its original format.
     *
     * @param channel The channel used for communication.
     * @param message The encoded message to decode.
     * @return The decoded message of type.
     * @throws IOException if an error occurs during decoding.
     */
    O decode(Channel channel, Object message) throws IOException;

}

