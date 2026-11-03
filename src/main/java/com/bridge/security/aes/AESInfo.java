package com.bridge.security.aes;

@SuppressWarnings("all")
public record AESInfo(String algorithm, byte[] sKey, byte[] iv) {
}
