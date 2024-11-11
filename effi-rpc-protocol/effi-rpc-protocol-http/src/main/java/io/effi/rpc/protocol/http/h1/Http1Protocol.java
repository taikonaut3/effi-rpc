package io.effi.rpc.protocol.http.h1;

import io.effi.rpc.common.constant.Component;
import io.effi.rpc.common.extension.spi.Extension;
import io.effi.rpc.protocol.http.HttpProtocol;
import io.effi.rpc.transport.http.HttpVersion;

/**
 * Http protocol.
 */
@Extension({Component.Protocol.HTTP, Component.Protocol.HTTPS})
public class Http1Protocol extends HttpProtocol {
    public Http1Protocol() {
        super(HttpVersion.HTTP_1_1);
    }

}
