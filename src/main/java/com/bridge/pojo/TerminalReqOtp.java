package com.bridge.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TerminalReqOtp {
    private String pan;
    private String tid;
    private String stan;
    private String location;
    @JsonProperty("agent-id")
    private String agentId;
    private String purpose;
    private String latitude;
    private String longitude;
}
