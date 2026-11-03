package com.bridge.security.rsa;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static com.bridge.security.rsa.RSAAlgorithmConstants.RSA_ECB_OAEP_SHA1_MGF1;

@Slf4j
public final class RSACryptoEngine {

    RSACryptoUtils rsaCryptoUtils;

    private RSACryptoEngine(String key) throws GeneralSecurityException {
        rsaCryptoUtils = RSACryptoUtils.forPublicKey(RSA_ECB_OAEP_SHA1_MGF1, key);
    }

    public static RSACryptoEngine of(String key) {
        try {
            return new RSACryptoEngine(key);
        } catch (Exception e) {
            log.error("error with key {}", key, e);
        }
        return null;
    }

    public String getEncrypted(byte[] msg) throws IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        return rsaCryptoUtils.encryptUsingRSA(msg);
    }
}
