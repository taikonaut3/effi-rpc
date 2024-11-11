package io.effi.rpc.transport.http;

/**
 * HTTP response.
 *
 * @param <BODY> the type of the body content of the HTTP response
 */
public interface HttpResponse<BODY> extends HttpEnvelope<BODY> {

    /**
     * Returns the HTTP response status code.
     *
     * @return the status code of the HTTP response
     */
    int statusCode();

    /**
     * Creates a new instance of {@link HttpResponseBuilder}, which is used to construct
     * an {@link HttpResponse} instance.
     *
     * @param <BODY> the type of the body content for the response being built
     * @return a new instance of {@link HttpResponseBuilder}
     */
    static <BODY> HttpResponseBuilder<BODY> builder() {
        return new HttpResponseBuilder<>();
    }
}

