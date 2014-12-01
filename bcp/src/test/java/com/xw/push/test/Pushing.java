package com.xw.push.test;

import com.xw.push.DeviceType;
import com.xw.push.Pusher;

/**
 * Created by junjie on 11/27/14.
 */
public class Pushing {
    public final void push_unicast_message() {
        Pusher.push_unicast_notification(
                "",
                "",
                DeviceType.ANDROID,
                1L,
                ""
        );
    }
}
