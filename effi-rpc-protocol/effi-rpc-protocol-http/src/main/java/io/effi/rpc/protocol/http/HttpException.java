package io.effi.rpc.protocol.http;

import io.effi.rpc.common.exception.RpcException;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * Http Exception.
 */
@Getter
@Accessors(fluent = true)
public class HttpException extends RpcException {

    private final int statusCode;

    public HttpException(int statusCode, String msg) {
        super(msg);
        this.statusCode = statusCode;
    }
}
