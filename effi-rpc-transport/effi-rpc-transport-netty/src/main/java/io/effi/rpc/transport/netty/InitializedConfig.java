package io.effi.rpc.transport.netty;

import io.effi.rpc.common.constant.KeyConstant;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.common.url.URLType;
import io.effi.rpc.transport.http.HttpVersion;
import io.effi.rpc.transport.util.HttpUtil;
import io.netty.channel.ChannelHandler;
import io.netty.handler.ssl.SslContext;
import lombok.Getter;
import lombok.experimental.Accessors;

import static io.effi.rpc.transport.netty.protocol.http.SslContextFactory.acquireForClient;
import static io.effi.rpc.transport.netty.protocol.http.SslContextFactory.acquireForServer;
import static io.netty.handler.ssl.ApplicationProtocolNames.HTTP_1_1;
import static io.netty.handler.ssl.ApplicationProtocolNames.HTTP_2;

/**
 * Endpoint's initialize configuration.
 */
@Getter
@Accessors(fluent = true)
public class InitializedConfig {

    private final URL url;

    private final ChannelHandler handler;

    private final SslContext sslContext;

    public InitializedConfig(URL url, ChannelHandler handler) {
        this.url = url;
        this.handler = handler;
        boolean sslEnabled = url.getBooleanParam(KeyConstant.SSL, false);
        this.sslContext = sslEnabled ? acquireSslContext(url) : null;
    }

    private SslContext acquireSslContext(URL url) {
        HttpVersion httpVersion = HttpUtil.acquireHttpVersion(url);
        return switch (httpVersion) {
            case HTTP_1_1 -> URLType.SERVER.valid(url) ? acquireForServer(HTTP_1_1) : acquireForClient(HTTP_1_1);
            case HTTP_2_0 -> URLType.SERVER.valid(url) ? acquireForServer(HTTP_2) : acquireForClient(HTTP_2);
            default -> null;
        };
    }

}
