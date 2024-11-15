package io.effi.rpc.common.exception;

/**
 * Bind Exception.
 */
public class BindException extends NetWorkException {

    public BindException(String msg, Throwable e) {
        super(msg, e);
    }

    public BindException(String msg) {
        super(msg);
    }

    public BindException(Throwable msg) {
        super(msg);
    }

}
