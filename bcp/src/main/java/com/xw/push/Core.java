package com.xw.push;

/**
 * Created by junjie on 11/27/14.
 */

import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;

import java.nio.charset.Charset;

import static java.lang.System.out;

public final class Core {
    private static final Charset UTF = Charset.forName("UTF-8");

    public static final void main(final String[] args) {
        if (args.length < 2) {
            _help();
            return;
        }

        final LongOpt[] opts = new LongOpt[]{
                new LongOpt("help", LongOpt.NO_ARGUMENT, null, 'h'),
                new LongOpt("device", LongOpt.OPTIONAL_ARGUMENT, null, 'd'),
                new LongOpt("cast", LongOpt.OPTIONAL_ARGUMENT, null, 'c'),
                new LongOpt("api", LongOpt.REQUIRED_ARGUMENT, null, 'a'),
                new LongOpt("secret", LongOpt.REQUIRED_ARGUMENT, null, 's'),
                new LongOpt("channel", LongOpt.OPTIONAL_ARGUMENT, null, 'n'),
                new LongOpt("user", LongOpt.OPTIONAL_ARGUMENT, null, 'u'),
                new LongOpt("type", LongOpt.OPTIONAL_ARGUMENT, null, 't'),
                new LongOpt("message", LongOpt.REQUIRED_ARGUMENT, null, 'm'),
                new LongOpt("cooked", LongOpt.OPTIONAL_ARGUMENT, null, 'k'),
                new LongOpt("cookie", LongOpt.OPTIONAL_ARGUMENT, null, 'i')
        };

        final Getopt g = new Getopt("bcp", args, "hd:c:a:s:n:u:t:m:k:i:;", opts);
        g.setOpterr(true);
        int c;
        DeviceType device = DeviceType.ANDROID;
        CastType cast = CastType.UNICAST;
        String api = null;
        String secret = null;
        Long channel = null;
        String user = null;
        MessageType type = MessageType.MESSAGE;
        String message = null;
        long cooked = 0;
        String cookie = null;

        while ((c = g.getopt()) != -1)
            switch (c) {
                case 'd':
                    device = DeviceType.from_str(g.getOptarg(), DeviceType.ANDROID);
                    break;
                case 'c':
                    cast = CastType.from_str(g.getOptarg(), CastType.UNICAST);
                    break;
                case 'a':
                    api = g.getOptarg();
                    break;
                case 's':
                    secret = g.getOptarg();
                    break;
                case 'n':
                    channel = _from_str(g.getOptarg());
                    break;
                case 'u':
                    user = g.getOptarg();
                    break;
                case 't':
                    type = MessageType.from_str(g.getOptarg(), MessageType.MESSAGE);
                    break;
                case 'm':
                    message = g.getOptarg();
                    break;
                case 'k':
                    cooked = _from_str(g.getOptarg());
                    break;
                case 'i':
                    cookie = g.getOptarg();
                    break;
                case 'h':
                    _help();
                    break;
                case ':':
                    _help(String.format("u need specify an argument for option:%s",
                            g.getOptopt()));
                    break;
                case '?':
                    _help(String.format("the option:%s is invalid",
                            g.getOptopt()));
                default:
                    _help(String.format("cli-parser return:%s", c));
                    break;
            }

        final Options options = new Options(device, cast, api, secret,
                channel, user, type, message, cooked, cookie);
        out.print("REQUEST:\n=========================");
        out.println(options);
        out.println("COOK:\n=========================");
        final String body = _cook(options, message);
        out.println("RESPONSE:\n=========================");


        if (CastType.UNICAST == cast) {
            Pusher.push_unicast_message(
                    options.api(),
                    options.secret(),
                    options.device(),
                    options.channel(),
                    options.user(),
                    options.type(),
                    body,
                    _build_logger()
            );
        } else {
            Pusher.push_broadcast_message(
                    options.api(),
                    options.secret(),
                    options.device(),
                    options.type(),
                    body,
                    _build_logger()
            );
        }
    }

    private static final String _cook(final Options opt, final String s) {
        if (opt.not_cooked()) {
            return (s);
        }

        byte[] coded = null;
        if (opt.coded()) {
            coded = DES.encode(s, opt.cookie(), UTF);
            out.format("\tcoded:\"%s\"->\"%s\"\n", s,
                    new String(coded, UTF));
        }

        String zipped = null;
        if (opt.zipped()) {
            final byte[] z = (Utils.is_null_or_empty(coded)
                    ? s.getBytes(UTF) : coded);
            zipped = Gzip.zip(z, UTF);
            out.format("\tzipped:\"%s\"->\"%s\"\n",
                    new String(coded, UTF), zipped);
        }

        return (zipped);
    }

    private static final Long _from_str(final String s) {
        if (null == s || s.length() == 0) {
            return (null);
        }

        try {
            final Long l = Long.parseLong(s);
            return (l);
        } catch (final NumberFormatException n) {
            // do nothing
        }

        return (null);
    }

    private static final Logger _build_logger() {
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

    private static final void _help() {
        _help(null);
    }

    private static final void _help(final String m) {
        if (null != m) {
            out.println(m);
        }
        out.println("bcp [-d|--device] [-c|cast] " +
                "\n\t[-a|--api] [-s|--secret] " +
                "\n\t[-c|--channel] [-m|--message]");
        out.println("\t--device: android/1, ios/4");
        out.println("\t--cast: unicast/0, broadcast/1");
        out.println("\t--api: api key");
        out.println("\t--secret: secret key");
        out.println("\t--channel: channel id");
        out.println("\t--user: user id");
        out.println("\t--message: message body");
        out.println("\t--cooked: not cooke(0), coded(1) zipped(2)");
        out.println("\t--cookie: coded key");
    }
}
