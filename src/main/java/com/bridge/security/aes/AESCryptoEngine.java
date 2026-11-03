package com.bridge.security.aes;

import lombok.Getter;

import static com.bridge.security.aes.AESAlgorithmConstants.AES_GCM_NO_PADDING;

@Getter
public final class AESCryptoEngine {

    AESCryptoUtils aesCryptoUtils;
    byte[] sKey;
    byte[] iv;

    private AESCryptoEngine(String algorithm, byte[] sKey, byte[] iv) {
        this.sKey = sKey;
        this.iv = iv;
        aesCryptoUtils = new AESCryptoUtils(new AESInfo(algorithm, sKey, iv));
    }

    public static AESCryptoEngine of(byte[] sKey, byte[] iv) {
        return new AESCryptoEngine(AES_GCM_NO_PADDING, sKey, iv);
    }

    public String getDecrypted(String msg) throws Exception {
        return aesCryptoUtils.decrypt(msg);
    }

    public String getEncrypted(String msg) throws Exception {
        return aesCryptoUtils.encrypt(msg);
    }
}
