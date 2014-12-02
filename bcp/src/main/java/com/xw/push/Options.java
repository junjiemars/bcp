package com.xw.push;

/**
 * Created by junjie on 12/2/14.
 */
public final class Options {
        public Options(final DeviceType device,
                   final CastType cast,
                   final String api,
                   final String secret,
                   final Long channel,
                   final String user,
                   final MessageType type,
                   final String message) {
        _device = device;
        _cast = cast;
        _api = api;
        _secret = secret;
        _channel = channel;
        _user = user;
        _type = type;
        _message = message;
    }

    public final DeviceType device() {
        return (_device);
    }

    public final CastType cast() {
        return (_cast);
    }

    public final String api() {
        return (_api);
    }

    public final String secret() {
        return (_secret);
    }

    public final Long channel() {
        return (_channel);
    }

    public final String user() { return (_user); }

    public final MessageType type() { return (_type); }

    public final String message() {
        return (_message);
    }

    @Override
    public String toString() {
        final String s = String.format("\n\tdeviceType:%s" +
                "\n\tcast:%s" +
                "\n\tapiKey:%s" +
                "\n\tsecretKey:%s" +
                "\n\tchannelId:%s" +
                "\n\tuserId:%s" +
                "\n\tmessageType:%s" +
                "\n\tmessage:%s",
                String.format("%s(%s)", _device, _device.to_int()),
                String.format("%s(%s)", _cast, _cast.to_int()),
                _api,
                _secret,
                _channel,
                _user,
                String.format("%s(%s)", _type, _type.to_int()),
                _message);
        return (s);
    }

    public final DeviceType _device;
    private final CastType _cast;
    private final String _api;
    private final String _secret;
    private final Long _channel;
    private final String _user;
    private final MessageType _type;
    private final String _message;
}
