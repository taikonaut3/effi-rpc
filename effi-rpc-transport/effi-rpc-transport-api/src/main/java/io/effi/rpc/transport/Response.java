package io.effi.rpc.transport;

import io.effi.rpc.common.constant.KeyConstant;
import io.effi.rpc.common.extension.AttributeKey;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.core.result.Result;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * RPC response, encapsulating the result of an RPC call along with
 * associated message and metadata.
 * <p>
 * This class extends {@link MessageCarrier} to carry the response URL and the
 * message payload. It includes a status code to indicate the outcome of the
 * RPC request.
 * </p>
 */
@Getter
@Accessors(fluent = true)
public class Response extends MessageCarrier {

    public static final AttributeKey<Response> ATTRIBUTE_KEY = AttributeKey.valueOf(KeyConstant.RESPONSE);

    // Response status codes
    public static final byte SUCCESS = 0;

    public static final byte ERROR = -1;

    public static final byte TIMEOUT = 3;

    // Status code for the response
    private byte code;

    // Result of the RPC call, can include any data or exception details
    @Setter
    private Result result;

    /**
     * Constructs a new {@link Response} instance with the specified status code
     * and associated message.
     *
     * @param code    The status code indicating the outcome of the RPC call
     *                (e.g., success, error, timeout).
     * @param url     The URL associated with this response, indicating the
     *                endpoint of the original request.
     * @param message The {@link Envelope} containing the message payload
     *                associated with this response.
     */
    public Response(byte code, URL url, Envelope message) {
        super(url, message);
        url.addParam(KeyConstant.ENVELOPE, KeyConstant.RESPONSE);
        this.code = code;
    }

    /**
     * Constructs a new {@link Response} instance based on the given result and
     * associated message.
     *
     * @param result  The {@link Result} of the RPC call, which may contain data
     *                or exception details.
     * @param message The {@link Envelope} containing the message payload
     *                associated with this response.
     */
    public Response(Result result, Envelope message) {
        super(result.url(), message);
        this.code = result.hasException() ? ERROR : SUCCESS;
        this.result = result;
    }
}

