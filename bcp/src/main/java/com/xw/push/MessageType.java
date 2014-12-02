package com.xw.push;

/**
 * Created by junjie on 12/2/14.
 */
public enum MessageType {
    MESSAGE(0),
    NOTIFICATION(1)
    ;

    MessageType(final int v) {
        _v = v;
    }

    public final int to_int() {
        return (_v);
    }

    public static final MessageType from_str(final String s, final MessageType d) {
        if (null == s || s.length() == 0) {
            return (d);
        }

        try {
            final MessageType t = MessageType.valueOf(s.toUpperCase());
            return (t);
        } catch (final IllegalArgumentException a) {
            // do nothing
        }

        return (d);
    }
    private final int _v;
}
