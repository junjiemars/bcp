package com.xw.push;

/**
 * Created by junjie on 12/2/14.
 */
public enum CastType {
    UNICAST(0),
    BROADCAST(1);

    CastType(final int v) {
        _v = v;
    }

    public final int to_int() {
        return (_v);
    }

    public static final CastType from_str(final String s, final CastType d) {
        if (null == s || s.length() == 0) {
            return (d);
        }

        try {
            final CastType t = CastType.valueOf(s.toUpperCase());
            return (t);
        } catch (final IllegalArgumentException a) {

        }

        return (d);
    }

    private final int _v;
}
