package io.effi.rpc.protocol.http.h2;

import io.effi.rpc.common.extension.spi.Extension;
import io.effi.rpc.protocol.http.HttpProtocol;
import io.effi.rpc.transport.http.HttpVersion;

import static io.effi.rpc.common.constant.Component.Protocol.H2;
import static io.effi.rpc.common.constant.Component.Protocol.H2C;

/**
 * Http2 Protocol.
 */
@Extension({H2, H2C})
public class Http2Protocol extends HttpProtocol {

    public Http2Protocol() {
        super(HttpVersion.HTTP_2_0);
    }

}
