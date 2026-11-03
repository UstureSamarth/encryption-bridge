package com.bridge.security.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EncryptHelper {

    private static final SecureRandom random = new SecureRandom();

    private static String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_";
        StringBuilder sb = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    private static byte[] generateRandomSalt() {
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    public static byte[] getSecretKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        final String password = generateRandomPassword();
        final int iterations = 65536;
        final int keyLength = 256;
        byte[] salt = generateRandomSalt();
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, keyLength);
        byte[] sk = factory.generateSecret(spec).getEncoded();
        return new SecretKeySpec(sk, "AES").getEncoded();
    }

    public static byte[] getIV(boolean isGCM) {
        byte[] iv;
        if (isGCM) {
            iv = new byte[12];
        } else {
            iv = new byte[16];
        }
        random.nextBytes(iv);
        return new IvParameterSpec(iv).getIV();
    }
}
