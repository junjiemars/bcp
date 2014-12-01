package com.xw.push;

import com.baidu.yun.channel.auth.ChannelKeyPair;
import com.baidu.yun.channel.client.BaiduChannelClient;
import com.baidu.yun.channel.exception.ChannelClientException;
import com.baidu.yun.channel.exception.ChannelServerException;
import com.baidu.yun.channel.model.PushUnicastMessageRequest;
import com.baidu.yun.channel.model.PushUnicastMessageResponse;
import com.baidu.yun.core.log.YunLogEvent;
import com.baidu.yun.core.log.YunLogHandler;

import static java.lang.System.out;

/**
 * Just one static class for pushing.
 * @author junjiemars@mail.xwtec.cn
  */
public final class Pusher {
    private Pusher(){}

    /**
     * 单播推送通知
     * @param apiKey 应用标识，终端上的绑定和服务端推送消息时都要用到
     * @param secretKey 应用私钥，服务端推送消息时用到
     * @param deviceType 设备类型标识
     * @param channelId 推送通道ID，通常指一个终端，如一台android系统手机。客户端绑定调用返回值中可获得
     * @param content 要推送的消息体内容
     * @return 成功后返回:>0, 失败后返回:0
     */
    public static final int push_unicast_notification(
            final String apiKey,
            final String secretKey,
            final DeviceType deviceType,
            final long channelId,
            final String content) {

        final int c = push_unicast_notification(
                apiKey, secretKey, deviceType, channelId,
                null, /* userId */
                content
        );
        return (c);
    }
    public static final int push_unicast_notification(
            final String apiKey,
            final String secretKey,
            final DeviceType deviceType,
            final long channelId,
            final String userId,
            final String content) {
        final BaiduChannelClient client = _build_channel_client(
                apiKey, secretKey
        );
        _set_logger(client);
        final PushUnicastMessageRequest request = _build_unicast_request(
                deviceType, channelId, userId, content,
                1 /* unicast notification */
        );

        try {
            final PushUnicastMessageResponse response = client.pushUnicastMessage(request);
            int c = response.getSuccessAmount();
            return (c);
        } catch (final ChannelClientException e) {
            e.printStackTrace();
        } catch (final ChannelServerException e) {
            out.println(String.format(
                    "request_id: %d, error_code: %d, error_message: %s",
                    e.getRequestId(), e.getErrorCode(), e.getErrorMsg()));
        }

        return (0);
    }

    public final int push_unicast_message(
            final String apiKey,
            final String secretKey,
            final DeviceType deviceType,
            final long channelId,
            final String content) {
        final int c = push_unicast_message(
                apiKey, secretKey, deviceType, channelId,
                null, /* userId */
                content
        );
        return (c);
    }

    public final int push_unicast_message(
            final String apiKey,
            final String secretKey,
            final DeviceType deviceType,
            final long channelId,
            final String userId,
            final String content) {

        final BaiduChannelClient client = _build_channel_client(
                apiKey, secretKey);
        _set_logger(client);
        final PushUnicastMessageRequest request = _build_unicast_request(
                deviceType, channelId, userId, content);

        try {
            final PushUnicastMessageResponse response =
                    client.pushUnicastMessage(request);
            final int c = response.getSuccessAmount();
            return (c);
        } catch (final ChannelClientException e) {
            e.printStackTrace();
        } catch (final ChannelServerException e) {
            out.println(String.format(
                    "request_id: %d, error_code: %d, error_message: %s",
                    e.getRequestId(), e.getErrorCode(), e.getErrorMsg()));
        }

        return (0);
    }

    private static final PushUnicastMessageRequest _build_unicast_request(
            final DeviceType deviceType,
            final long channelId,
            final String userId,
            final String content) {
        final PushUnicastMessageRequest request = _build_unicast_request(
                deviceType, channelId, userId, content,
                0 /* messageType */
        );
        return (request);
    }

    private static final PushUnicastMessageRequest _build_unicast_request(
            final DeviceType deviceType,
            final long channelId,
            final String userId,
            final String content,
            final int messageType) {
        final PushUnicastMessageRequest request = new PushUnicastMessageRequest();
        request.setDeviceType(deviceType.to_int());
        request.setChannelId(channelId);
        request.setUserId(userId);

        request.setMessageType(messageType);
        request.setMessage(content);

        return (request);
    }

    private static final BaiduChannelClient _build_channel_client(
            final String apiKey,
            final String secretKey) {

        final ChannelKeyPair p = new ChannelKeyPair(apiKey, secretKey);
        final BaiduChannelClient c = new BaiduChannelClient(p);
        return (c);
    }

    private static final void _set_logger(final BaiduChannelClient c) {
        c.setChannelLogHandler(new YunLogHandler() {
            public void onHandle(YunLogEvent event) {
                out.println(event.getMessage());
            }
        });
    }
}
