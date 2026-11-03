package com.bridge.pojo;

import com.bridge.security.aes.AESCryptoEngine;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RequestInfo {
    String institute;
    String bc;
    String request;
    String api;
    String txnName;
    AESCryptoEngine aesCryptoEngine;
}
