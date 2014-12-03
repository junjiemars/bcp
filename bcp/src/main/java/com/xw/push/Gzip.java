package com.xw.push;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import static java.lang.System.out;

/**
 * @author junjie
 * @since 12/3/14
 */
public final class Gzip {
    private Gzip() {}

    public static final String zip(final byte[] b, final Charset charset) {
        if (Utils.is_null_or_empty(b)) {
            return (null);
        }

        final ByteArrayOutputStream o = new ByteArrayOutputStream();
        final GZIPOutputStream gzip;
        try {
            gzip = new GZIPOutputStream(o);
            gzip.write(b);
            gzip.close();
            o.flush();
            final byte[] bb = o.toByteArray();
            o.close();

            final String z = Base64.encode(bb);
            return (z);
        } catch (IOException e) {
            out.println(e);
        }

        return (null);
    }

    public static final String unzip(final String s, final Charset charset) {
        if (Utils.is_null_or_empty(s)) {
            return (null);
        }

        final byte[] b = s.getBytes(charset);
        final ByteArrayOutputStream o = new ByteArrayOutputStream();
        final ByteArrayInputStream i = new ByteArrayInputStream(b);

        try {
            final GZIPInputStream unzip = new GZIPInputStream(i);
            final byte[] buffer = new byte[512];
            int n = 0;
            while (0 <= (n = unzip.read(buffer))) {
                o.write(buffer, 0, n);
            }
            unzip.close();
            final String u = o.toString(charset.name());
            o.close();

            return (u);
        } catch (final IOException e) {
            out.println(e);
        }

        return null;
    }
}
