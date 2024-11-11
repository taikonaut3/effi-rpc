package io.effi.rpc.protocol.virtue.envelope;

import io.effi.rpc.common.url.URL;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * Virtue Response.
 */
@EqualsAndHashCode(callSuper = true)
@Accessors(fluent = true)
@Data
@ToString
public class VirtueResponse extends VirtueEnvelope {

    public static final byte SUCCESS = 0, ERROR = -1, TIMEOUT = 3;

    private byte code;

    public VirtueResponse() {

    }

    public VirtueResponse(byte code, URL url, Object payload) {
        super(url, payload);
        this.code = code;
    }

}
