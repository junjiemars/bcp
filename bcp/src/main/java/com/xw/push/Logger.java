package com.xw.push;

/**
 * Created by junjie on 12/2/14.
 */

public abstract class Logger {
    public abstract void log_channel(String e);
    public abstract void log_client_exception(final String e);
    public abstract void log_server_exception(
            final long requestId,
            final int errorId,
            final String errorMessage);
}
