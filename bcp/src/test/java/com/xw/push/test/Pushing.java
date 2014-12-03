package com.xw.push.test;

import com.xw.push.DeviceType;
import com.xw.push.Logger;
import com.xw.push.MessageType;
import com.xw.push.Pusher;

/**
 * Created by junjie on 11/27/14.
 */
import static java.lang.System.out;

public class Pushing {
    private static final String apiKey = "q7CIgfACcd9glR9csquSUKRP";
    private static final String secretKey = "mvdVD66Ym3gZwlEdxIqHBqrWGNMAMKBH";
    private static final Long channelId = 4428138971497670592L;
    private static final String userId = "870066080831560238";

    public final void push_unicast_message() {
        // 单播消息/通知
        Pusher.push_unicast_message(
                apiKey,
                secretKey,
                DeviceType.ANDROID,
                channelId,
                userId,
                MessageType.MESSAGE,
                "Hello, World!",
                _build_logger()
        );

        // 多播消息/通知
        Pusher.push_broadcast_message(
                apiKey,
                secretKey,
                DeviceType.ANDROID,
                MessageType.MESSAGE,
                "Hello, Broadcast!",
                _build_logger());

    }


    private final Logger _build_logger() {
        final Logger l = new Logger() {

            @Override
            public void log_channel(String e) {
                out.println(e);
            }

            @Override
            public void log_client_exception(String e) {
                out.println(e);
            }

            @Override
            public void log_server_exception(long requestId, int errorId, String errorMessage) {
                out.format("requestId:%s errorId:%s errorMessage:%s\n",
                        requestId, errorId, errorMessage);
            }
        };

        return (l);
    }
}
