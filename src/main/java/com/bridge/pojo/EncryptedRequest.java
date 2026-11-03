package com.bridge.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EncryptedRequest {
    String sk;
    String iv;
    String api;
    String ct;
    String version;
}
