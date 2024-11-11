package io.effi.rpc.transport.http;

import io.effi.rpc.common.url.URL;
import io.effi.rpc.transport.util.HttpUtil;

/**
 * Default implementation of {@link HttpResponse}.
 *
 * @param <BODY>
 */
public class DefaultHttpResponse<BODY> extends DefaultHttpEnvelope<BODY> implements HttpResponse<BODY> {
    private final int statusCode;

    public DefaultHttpResponse(HttpVersion version, HttpMethod method, URL url, int statusCode, HttpHeaders headers, BODY body) {
        super(version, method, url, headers, body);
        this.statusCode = statusCode;
        HttpUtil.setContentLength(headers, body);
    }

    @Override
    public int statusCode() {
        return statusCode;
    }
}
