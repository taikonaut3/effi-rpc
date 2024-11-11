package io.effi.rpc.transport.http;

import io.effi.rpc.common.url.URL;
import io.effi.rpc.transport.util.HttpUtil;

/**
 * Default implementation of {@link HttpRequest}.
 *
 * @param <BODY>
 */
public class DefaultHttpRequest<BODY> extends DefaultHttpEnvelope<BODY> implements HttpRequest<BODY> {

    public DefaultHttpRequest(HttpVersion version, HttpMethod method, URL url, HttpHeaders headers, BODY body) {
        super(version, method, url, headers, body);
        HttpUtil.setContentLength(headers, body);
    }

}
