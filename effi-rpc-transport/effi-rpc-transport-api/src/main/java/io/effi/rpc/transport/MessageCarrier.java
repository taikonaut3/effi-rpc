package io.effi.rpc.transport;

import io.effi.rpc.common.url.URL;

/**
 * Carrier for messages, encapsulating a request URL and the associated message.
 * <p>
 * This class implements the {@link Envelope} interface, allowing it to provide the request URL
 * associated with the message. It serves as a wrapper for an exchange message, enabling the
 * transmission of both the URL and the message content together.
 * </p>
 */
public class MessageCarrier implements Envelope {

    protected URL requestUrl;

    protected Envelope message;

    /**
     * Constructs a new {@link MessageCarrier} with the specified request URL and message.
     *
     * @param requestUrl The URL associated with the request.
     * @param message The envelope message to be carried.
     */
    public MessageCarrier(URL requestUrl, Envelope message) {
        this.requestUrl = requestUrl;
        this.message = message;
    }

    /**
     * Returns the request URL associated with this message carrier.
     *
     * @return The {@link URL} associated with the request.
     */
    @Override
    public URL url() {
        return requestUrl;
    }

    /**
     * Returns the message contained within this carrier.
     *
     * @return The {@link Envelope} message carried by this instance.
     */
    public Envelope message() {
        return message;
    }
}

