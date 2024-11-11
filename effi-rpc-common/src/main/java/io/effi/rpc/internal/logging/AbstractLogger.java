package io.effi.rpc.internal.logging;

import java.util.Objects;

abstract class AbstractLogger implements Logger {

    @Override
    public void trace(String format, Throwable e, Object... args) {
        if (isTraceEnabled()) {
            String msg = format(format, args);
            trace(msg, e);
        }
    }

    @Override
    public void trace(String format, Object... args) {
        trace(format, null, args);
    }

    @Override
    public void trace(Throwable e) {
        if (isTraceEnabled()) {
            trace(null, e);
        }
    }

    @Override
    public void debug(String format, Throwable e, Object... args) {
        if (isDebugEnabled()) {
            String msg = format(format, args);
            debug(msg, e);
        }
    }

    @Override
    public void debug(String format, Object... args) {
        debug(format, null, args);
    }

    @Override
    public void debug(Throwable e) {
        if (isDebugEnabled()) {
            debug(null, e);
        }
    }

    @Override
    public void info(String format, Throwable e, Object... args) {
        if (isInfoEnabled()) {
            String msg = format(format, args);
            info(msg, e);
        }
    }

    @Override
    public void info(String format, Object... args) {
        info(format, null, args);
    }

    @Override
    public void info(Throwable e) {
        if (isInfoEnabled()) {
            info(null, e);
        }
    }

    @Override
    public void warn(String format, Throwable e, Object... args) {
        if (isWarnEnabled()) {
            String msg = format(format, args);
            warn(msg, e);
        }
    }

    @Override
    public void warn(String format, Object... args) {
        warn(format, null, args);
    }

    @Override
    public void warn(Throwable e) {
        if (isWarnEnabled()) {
            warn(null, e);
        }
    }

    @Override
    public void error(String format, Throwable e, Object... args) {
        if (isErrorEnabled()) {
            String msg = format(format, args);
            error(msg, e);
        }
    }

    @Override
    public void error(String format, Object... args) {
        error(format, null, args);
    }

    @Override
    public void error(Throwable e) {
        if (isErrorEnabled()) {
            error(null, e);
        }
    }

    protected abstract void trace(String msg, Throwable e);

    protected abstract void debug(String msg, Throwable e);

    protected abstract void info(String msg, Throwable e);

    protected abstract void warn(String msg, Throwable e);

    protected abstract void error(String msg, Throwable e);

    protected static String format(String message, Object... args) {
        if (message == null || args == null || args.length == 0) {
            return message;
        }
        StringBuilder result = new StringBuilder(message.length() + args.length * 10);
        int argIndex = 0;
        for (int i = 0; i < message.length(); i++) {
            if (argIndex >= args.length) {
                // If all parameters have been used, append the remaining strings
                result.append(message, i, message.length());
                break;
            }
            char currentChar = message.charAt(i);
            if (currentChar == '{' && i + 1 < message.length() && message.charAt(i + 1) == '}') {
                result.append(Objects.toString(args[argIndex], "null"));
                argIndex++;
                i++; // skip '}'
            } else {
                result.append(currentChar);
            }
        }
        return result.toString();
    }
}
