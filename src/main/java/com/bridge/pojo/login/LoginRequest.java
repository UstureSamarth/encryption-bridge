package com.bridge.pojo.login;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;


@Getter
public class LoginRequest {

    private String imei;

    @JsonProperty("userName")
    @JsonAlias({"user-id"})
    private String user;

    @JsonProperty("terminal-id")
    private String terminalId;
}
