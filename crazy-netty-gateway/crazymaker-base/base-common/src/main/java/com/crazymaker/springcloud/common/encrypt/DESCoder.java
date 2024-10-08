package com.crazymaker.springcloud.common.encrypt;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

@SuppressWarnings("restriction")
public class DESCoder {
    public static final String KEY_ALGORITHM = "DES";
    public static final String CIPHER_ALIGORITHM = "DES/ECB/PKCS5Padding";

    public DESCoder() {
    }

    public static void generate(String data) {
        Encoder encoder = Base64.getEncoder();
        ;

        try {
            String key = encoder.encodeToString(initKey());
            String result = encrypt(data, key);
            System.out.println("the key is :" + key);
            System.out.println("this datas is :" + result);
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }

    public static byte[] initKey() throws Exception {
        KeyGenerator kg = KeyGenerator.getInstance("DES");
        kg.init(56);
        SecretKey secretKey = kg.generateKey();
        return secretKey.getEncoded();
    }

    public static String encrypt(String data, String key) throws Exception {
        Encoder encoder = Base64.getEncoder();
        Decoder decoder = Base64.getDecoder();
        Key k = toKey(decoder.decode(key));
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(1, k);
        return encoder.encodeToString(cipher.doFinal(data.getBytes()));
    }

    public static String decrypt(String data, String key) throws Exception {
        Decoder decoder = Base64.getDecoder();
        Key k = toKey(decoder.decode(key));
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(2, k);
        return new String(cipher.doFinal(decoder.decode(data)), "UTF-8");
    }

    private static Key toKey(byte[] key) throws Exception {
        DESKeySpec dks = new DESKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        return keyFactory.generateSecret(dks);
    }
}
