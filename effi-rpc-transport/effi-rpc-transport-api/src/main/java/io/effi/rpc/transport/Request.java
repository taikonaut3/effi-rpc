package io.effi.rpc.transport;

import io.effi.rpc.common.constant.KeyConstant;
import io.effi.rpc.common.extension.AttributeKey;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.core.Invocation;
import io.effi.rpc.core.support.InvocationSupport;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * RPC request, encapsulating the invocation details and associated message.
 * <p>
 * This class extends {@link MessageCarrier} to carry the request URL and the message
 * payload. It also includes information about whether the request is a one-way call.
 * </p>
 */
@Getter
@Accessors(fluent = true)
public class Request extends MessageCarrier {

    public static final AttributeKey<Request> ATTRIBUTE_KEY = AttributeKey.valueOf(KeyConstant.REQUEST);

    // Indicates if this request is a one-way call (no response expected)
    private boolean oneway = false;

    // The invocation details associated with this request
    private final Invocation<?> invocation;

    /**
     * Constructs a new {@link Request} instance.
     *
     * @param invocation The {@link Invocation} object containing details about the method
     *                   being invoked, including the target method and its parameters.
     * @param message    The {@link Envelope} containing the message payload associated
     *                   with this request.
     */
    public Request(Invocation<?> invocation, Envelope message) {
        super(invocation.requestUrl(), message);
        this.invocation = invocation;

        // Acquire the caller's URL and check if the request is one-way
        URL callerUrl = InvocationSupport.acquireCallerUrl(requestUrl);
        if (callerUrl != null) {
            oneway = callerUrl.getBooleanParam(KeyConstant.ONEWAY);
        }
    }
}

