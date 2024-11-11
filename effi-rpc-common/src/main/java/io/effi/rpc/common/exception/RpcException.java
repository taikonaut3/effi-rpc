package io.effi.rpc.common.exception;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.concurrent.ExecutionException;

/**
 * Rpc Exception.
 */
public class RpcException extends RuntimeException {

    public RpcException(String msg, Throwable e) {
        super(msg, e);
    }

    public RpcException(String msg) {
        super(msg);
    }

    public RpcException(Throwable e) {
        super(e);
    }

    /**
     * Reverse wrap exception until the root exception is found.
     *
     * @param wrapped
     * @return
     */
    public static RpcException wrap(Throwable wrapped) {
        while (true) {
            switch (wrapped) {
                case InvocationTargetException e -> wrapped = e.getTargetException();
                case UndeclaredThrowableException e -> wrapped = e.getUndeclaredThrowable();
                case ExecutionException e -> wrapped = e.getCause();
                case RpcException e -> {
                    return e;
                }
                default -> {
                    return new RpcException(wrapped.getMessage(), wrapped);
                }
            }
        }
    }
}
