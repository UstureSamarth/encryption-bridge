package com.bridge.security.aes;

@SuppressWarnings("all")
public interface AESAlgorithmConstants {
    String AES_RAW = "AES";
    String AES_ECB_PKCS5_PADDING = "AES/ECB/PKCS5Padding";
    String AES_CBC_PKCS5_PADDING = "AES/CBC/PKCS5Padding";
    String AES_CFB_NO_PADDING = "AES/CFB/NoPadding";
    String AES_CFB_PKCS5_PADDING = "AES/CFB/PKCS5Padding";
    String AES_OFB_NO_PADDING = "AES/OFB/NoPadding";
    String AES_OFB_PKCS5_PADDING = "AES/OFB/PKCS5Padding";
    String AES_GCM_NO_PADDING = "AES/GCM/NoPadding";
    String AES_WRAP = "AESWrap";

    //require msg of length multiple of 16
    String AES_CBC_NO_PADDING = "AES/CBC/NoPadding";
    String AES_ECB_NO_PADDING = "AES/ECB/NoPadding";

}

