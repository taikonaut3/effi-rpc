package io.effi.rpc.core.result;

import io.effi.rpc.common.url.URL;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Represents the result of an invocation, which can either contain a value or an exception.
 */
@Data
@Accessors(fluent = true)
public class Result {

    private URL url;

    private Object value;

    private Throwable exception;

    /**
     * Constructs a result with a URL and either a value or an exception.
     *
     * @param url   The URL associated with the result.
     * @param value The result value, or an exception if the invocation failed.
     */
    public Result(URL url, Object value) {
        this.url = url;
        if (value instanceof Throwable e) {
            this.exception = e;
        } else {
            this.value = value;
        }
    }

    /**
     * Checks if the result contains an exception.
     *
     * @return {@code true} if the result contains an exception, {@code false} otherwise.
     */
    public boolean hasException() {
        return exception != null;
    }

}

