package com.bridge.security.aes;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public final class AESCryptoUtils {

    private final AESInfo aesInfo;

    public AESCryptoUtils(AESInfo aesInfo) {
        this.aesInfo = aesInfo;
        validateKeyAndIv();
    }

    public String encrypt(String msg) throws Exception {
        Cipher cipher = createCipher(Cipher.ENCRYPT_MODE);
        return Base64.getEncoder().encodeToString(
                cipher.doFinal(msg.getBytes(StandardCharsets.UTF_8))
        );
    }

    public String decrypt(String msg) throws Exception {
        Cipher cipher = createCipher(Cipher.DECRYPT_MODE);
        return new String(
                cipher.doFinal(Base64.getDecoder().decode(msg)),
                StandardCharsets.UTF_8
        );
    }

    private Cipher createCipher(int mode) throws Exception {
        Cipher cipher = Cipher.getInstance(aesInfo.algorithm());
        SecretKeySpec keySpec = new SecretKeySpec(aesInfo.sKey(), "AES");

        if (isGCM()) {
            GCMParameterSpec gcmSpec = new GCMParameterSpec(128, aesInfo.iv());
            cipher.init(mode, keySpec, gcmSpec);
        } else if (isECB()) {
            cipher.init(mode, keySpec);
        } else {
            IvParameterSpec ivSpec = new IvParameterSpec(aesInfo.iv());
            cipher.init(mode, keySpec, ivSpec);
        }
        return cipher;
    }

    private void validateKeyAndIv() {
        int keySize = aesInfo.sKey().length;

        if (!(keySize == 16 || keySize == 24 || keySize == 32)) {
            throw new IllegalArgumentException("Invalid AES key size.");
        }

        if (!isGCM() && !isECB() && aesInfo.iv().length != 16) {
            throw new IllegalArgumentException("IV must be 16 bytes for CBC/CFB/OFB.");
        }
    }

    private boolean isGCM() {
        return aesInfo.algorithm() != null && aesInfo.algorithm().contains("GCM");
    }

    private boolean isECB() {
        return aesInfo.algorithm() != null && aesInfo.algorithm().contains("ECB");
    }
}
