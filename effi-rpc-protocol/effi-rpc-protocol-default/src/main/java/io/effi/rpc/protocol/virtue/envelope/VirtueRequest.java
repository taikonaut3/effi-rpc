package io.effi.rpc.protocol.virtue.envelope;

import io.effi.rpc.common.url.URL;
import lombok.ToString;

/**
 * Virtue Request.
 */
@ToString
public class VirtueRequest extends VirtueEnvelope {

    public VirtueRequest() {

    }

    public VirtueRequest(URL url, Object body) {
        super(url, body);
    }

}
