package com.bridge.security.rsa;

@SuppressWarnings("all")
public interface RSAAlgorithmConstants {
    String RSA_RAW = "RSA";
    String RSA_ECB_PKCS1_PADDING = "RSA/ECB/PKCS1Padding";
    String RSA_ECB_OAEP_SHA1_MGF1 = "RSA/ECB/OAEPWithSHA1AndMGF1Padding";
    String RSA_ECB_OAEP_SHA256_MGF1 = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";
    String RSA_ECB_OAEP_SHA384_MGF1 = "RSA/ECB/OAEPWithSHA-384AndMGF1Padding";
    String RSA_ECB_OAEP_SHA512_MGF1 = "RSA/ECB/OAEPWithSHA-512AndMGF1Padding";
}

