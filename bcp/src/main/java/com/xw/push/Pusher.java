package com.xw.push;

import com.baidu.yun.channel.auth.ChannelKeyPair;
import com.baidu.yun.channel.client.BaiduChannelClient;
import com.baidu.yun.channel.exception.ChannelClientException;
import com.baidu.yun.channel.exception.ChannelServerException;
import com.baidu.yun.channel.model.PushBroadcastMessageRequest;
import com.baidu.yun.channel.model.PushBroadcastMessageResponse;
import com.baidu.yun.channel.model.PushUnicastMessageRequest;
import com.baidu.yun.channel.model.PushUnicastMessageResponse;
import com.baidu.yun.core.log.YunLogEvent;
import com.baidu.yun.core.log.YunLogHandler;

/**
 * Just one static class for pushing.
 * @author junjiemars@mail.xwtec.cn
  */
public final class Pusher {
    private Pusher(){}

    /**
     * 单播推送消息
     * @param apiKey 应用标识，终端上的绑定和服务端推送消息时都要用到
     * @param secretKey 应用私钥，服务端推送消息时用到
     * @param deviceType 设备类型标识
     * @param channelId 推送通道ID，通常指一个终端，如一台android系统手机。客户端绑定调用返回值中可获得
     * @param messageType 消息类型
     * @param content 要推送的消息体内容
     * @param logger 日志处理器, 可为null
     * @return 成功后返回:>0, 失败后返回:0
     */
    public static final int push_unicast_message(
            final String apiKey,
            final String secretKey,
            final DeviceType deviceType,
            final long channelId,
            final String userId,
            final MessageType messageType,
            final String content,
            final Logger logger) {

        final BaiduChannelClient client = _build_channel_client(
                apiKey, secretKey);
        _mount_logger(client, logger);
        final PushUnicastMessageRequest request = _build_unicast_request(
                deviceType,
                channelId,
                userId,
                content,
                messageType);

        try {
            final PushUnicastMessageResponse response =
                    client.pushUnicastMessage(request);
            final int c = response.getSuccessAmount();
            return (c);
        } catch (final ChannelClientException e) {
            _mount_logger(logger, e);
        } catch (final ChannelServerException e) {
            _mount_logger(logger, e);
        }

        return (RETURN_FAILED);
    }

    /**
     * 多播推送消息
     * @param apiKey 应用标识，终端上的绑定和服务端推送消息时都要用到
     * @param secretKey 应用私钥，服务端推送消息时用到
     * @param deviceType 设备类型标识
     * @param messageType 消息类型标识
     * @param content 要推送的消息体内容，需要按规范构建
     * @param logger 日志处理器, 可为null
     * @return 成功后返回:>0, 失败后返回:0
     */
    public static final int push_broadcast_message(
            final String apiKey,
            final String secretKey,
            final DeviceType deviceType,
            final MessageType messageType,
            final String content,
            final Logger logger) {

        final BaiduChannelClient client = _build_channel_client(
                apiKey, secretKey);
        _mount_logger(client, logger);
        final PushBroadcastMessageRequest request = _build_broadcast_request(
                deviceType,
                messageType,
                content
        );

        try {
            final PushBroadcastMessageResponse response =
                    client.pushBroadcastMessage(request);
            final int c = response.getSuccessAmount();
            return (c);
        } catch (final ChannelClientException e) {
            _mount_logger(logger, e);
        } catch (final ChannelServerException e) {
            _mount_logger(logger, e);
        }

        return (RETURN_FAILED);
    }

    private static final PushUnicastMessageRequest _build_unicast_request(
            final DeviceType deviceType,
            final long channelId,
            final String userId,
            final String content,
            final MessageType messageType) {

        final PushUnicastMessageRequest request = new PushUnicastMessageRequest();
        request.setDeviceType(deviceType.to_int());
        request.setMessageType(messageType.to_int());
        request.setMessage(content);
        request.setChannelId(channelId);
        request.setUserId(userId);
        //request.setDeployStatus(2); /* 1:dev, 2:pro */

        return (request);
    }

    private static final PushBroadcastMessageRequest _build_broadcast_request(
            final DeviceType deviceType,
            final MessageType messageType,
            final String content) {
        final PushBroadcastMessageRequest request = new PushBroadcastMessageRequest();
        request.setDeviceType(deviceType.to_int());
        request.setMessageType(messageType.to_int());
        request.setMessage(content);
        request.setDeployStatus(2); /* 1:dev, 2:pro */

        return (request);
    }

    private static final BaiduChannelClient _build_channel_client(
            final String apiKey,
            final String secretKey) {

        final ChannelKeyPair p = new ChannelKeyPair(apiKey, secretKey);
        final BaiduChannelClient c = new BaiduChannelClient(p);
        return (c);
    }

    private static final void _mount_logger(
            final BaiduChannelClient c,
            final Logger logger) {
        if (null == logger) {
            return;
        }

        c.setChannelLogHandler(new YunLogHandler() {
            public void onHandle(YunLogEvent event) {
                logger.log_channel(event.getMessage());
            }
        });
    }

    private static final void _mount_logger(
            final Logger logger,
            final ChannelClientException e) {
        if (null == logger) {
            return;
        }

        logger.log_client_exception(e.getMessage());
    }

    private static final void _mount_logger(
            final Logger logger,
            final ChannelServerException e) {
        if (null == logger) {
            return;
        }

        logger.log_channel_exception(e.getRequestId(),
                e.getErrorCode(), e.getMessage());
    }


    private static final int RETURN_FAILED = (0);
}
