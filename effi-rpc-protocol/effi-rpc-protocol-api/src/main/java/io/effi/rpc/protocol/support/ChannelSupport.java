package io.effi.rpc.protocol.support;

import io.effi.rpc.common.extension.spi.ExtensionLoader;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.transport.Protocol;
import io.effi.rpc.transport.channel.Channel;

/**
 * Utility class providing channel-related support functions.
 */
public class ChannelSupport {

    /**
     * Retrieves the protocol instance associated with the given channel.
     *
     * @param channel the channel from which to acquire the protocol
     * @return the protocol instance associated with the channel's URL
     */
    public static Protocol acquireProtocol(Channel channel) {
        URL url = channel.url();
        return ExtensionLoader.loadExtension(Protocol.class, url.protocol());
    }
}

