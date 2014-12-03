package com.xw.push;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import static java.lang.System.out;

/**
 * @author Think
 * @author junjie
 * @since 2014/12/03
 */
public final class DES {
    private DES() {}
    private static byte[] iv = {1, 2, 3, 4, 5, 6, 7, 8};

    public static final byte[] encode(
            final String s,
            final String k,
            final Charset charset) {
        if (Utils.is_null_or_empty(s) || Utils.is_null_or_empty(k)) {
            return (null);
        }

        try {
            final IvParameterSpec zeroIv = new IvParameterSpec(iv);
            final SecretKeySpec key = new SecretKeySpec(k.getBytes(), "DES");
            final Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
            final byte[] coded = cipher.doFinal(s.getBytes(charset));
            //final String c = Base64.encode(coded);
            return (coded);
        } catch (final Exception e) {
            out.println(e);
        }

        return (null);
    }


    public static final String decode(
            String s,
            String k,
            String charset) throws Exception {


        byte[] byteMi = com.xw.push.Base64.decode(s);

        IvParameterSpec zeroIv = new IvParameterSpec(iv);
        SecretKeySpec key = new SecretKeySpec(k.getBytes(), "DES");
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
        byte decryptedData[] = cipher.doFinal(byteMi);
        return new String(decryptedData, charset);
    }

}
