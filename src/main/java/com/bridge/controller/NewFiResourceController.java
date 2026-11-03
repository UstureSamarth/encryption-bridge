package com.bridge.controller;

import com.bridge.pojo.TerminalReqOtp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

import static com.bridge.constants.BridgeConstants.AEPS_GEN_OTP;

@Slf4j
@RestController
@RequestMapping("/api/fi/{institute}/aeps")
@SuppressWarnings("all")
public class NewFiResourceController {

    @Autowired
    BridgeController bridgeController;


    @PostMapping("/otp/generate")
    public Mono<ResponseEntity<String>> handleSeedingGenerateOtp(@RequestBody TerminalReqOtp requestBody, @RequestHeader Map<String, String> headers,
                                                                 ServerHttpRequest fullRequest) throws Exception {
        return bridgeController.processRequest(requestBody, headers, fullRequest, AEPS_GEN_OTP, headers.get("bc"));
    }

}

