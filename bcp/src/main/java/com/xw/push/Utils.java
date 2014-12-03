package com.xw.push;

/**
 * Created by junjie on 12/2/14.
 */
public final class Utils {
    private Utils() {}

    public static final boolean is_null_or_empty(final String s) {
        return (null == s || s.length() == 0);
    }
    public static final boolean is_null_or_empty(final byte[] b) {
        return (null == b || b.length == 0);
    }
}
