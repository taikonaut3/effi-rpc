package io.effi.rpc.common.exception;

/**
 * UnSupportedEncodeException.
 */
public class UnSupportedEncodeException extends RpcException {
    public UnSupportedEncodeException(String msg, Throwable e) {
        super(msg, e);
    }

    public UnSupportedEncodeException(String msg) {
        super(msg);
    }

    public UnSupportedEncodeException(Throwable e) {
        super(e);
    }
}
