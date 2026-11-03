package com.bridge.security.rsa;


import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import static com.bridge.security.rsa.RSAAlgorithmConstants.RSA_RAW;

public final class RSACryptoUtils {

    private final String algorithm;
    private final Key key;

    private RSACryptoUtils(boolean isPrivate, String algorithm, String key) throws GeneralSecurityException {
        this.algorithm = algorithm;
        this.key = isPrivate ? loadPrivateKey(key) : loadPublicKey(key);
    }

    public static RSACryptoUtils forPublicKey(String algorithm, String key) throws GeneralSecurityException {
        return new RSACryptoUtils(false, algorithm, key);
    }

    public static RSACryptoUtils forPrivateKey(String algorithm, String key) throws GeneralSecurityException {
        return new RSACryptoUtils(true, algorithm, key);
    }

    public String encryptUsingRSA(byte[] msg) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return Base64.getEncoder().encodeToString(cipher.doFinal(msg));
    }

    public static PrivateKey loadPrivateKey(String key) throws GeneralSecurityException {
        key = key.replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");
        byte[] decoded = Base64.getDecoder().decode(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_RAW);
        return keyFactory.generatePrivate(keySpec);
    }

    public static PublicKey loadPublicKey(String key) throws GeneralSecurityException {
        key = key.replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");
        byte[] decoded = Base64.getDecoder().decode(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_RAW);
        return keyFactory.generatePublic(keySpec);
    }
}
