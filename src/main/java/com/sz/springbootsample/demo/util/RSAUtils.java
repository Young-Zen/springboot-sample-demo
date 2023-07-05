package com.sz.springbootsample.demo.util;

import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author Yanghj
 * @date 2023/6/28 11:59
 */
@Slf4j
public class RSAUtils {

    private static final RSA RSA = new RSA();

    static {
        log.info("RSA PublicKey Base64: {}", getPublicKey());
    }

    public static String getPublicKey() {
        return RSA.getPublicKeyBase64();
    }

    public static String encrypt(RSA rsa, byte[] data) {
        byte[] encrypt = rsa.encrypt(data, KeyType.PublicKey);
        return Base64.getEncoder().encodeToString(encrypt);
    }

    public static String encrypt(String plaintext) {
        byte[] encrypt = RSA.encrypt(plaintext.getBytes(StandardCharsets.UTF_8), KeyType.PublicKey);
        return Base64.getEncoder().encodeToString(encrypt);
    }

    public static byte[] decrypt(String ciphertext) {
        byte[] decrypt = RSA.decrypt(Base64.getDecoder().decode(ciphertext.getBytes(StandardCharsets.UTF_8)), KeyType.PrivateKey);
        return decrypt;
    }
}
