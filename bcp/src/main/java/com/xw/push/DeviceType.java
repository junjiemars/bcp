package com.xw.push;

/**
 * Created by junjie on 11/27/14.
 */
public enum DeviceType {
    //device_type => 1: web 2: pc 3:android 4:ios 5:wp
    WEB(1),
    PC(2),
    ANDROID(3),
    IOS(4),
    WP(5)
    ;

    DeviceType(final int t) {
        this._t = t;
    }

    public final int to_int() {
        return (_t);
    }

    private final int _t;
}
