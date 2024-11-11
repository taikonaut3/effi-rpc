package io.effi.rpc.common.exception;

/**
 * UnSupportedDecodeException.
 */
public class UnSupportedDecodeException extends RpcException {
    public UnSupportedDecodeException(String msg, Throwable e) {
        super(msg, e);
    }

    public UnSupportedDecodeException(String msg) {
        super(msg);
    }

    public UnSupportedDecodeException(Throwable e) {
        super(e);
    }
}
