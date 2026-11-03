package com.bridge.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

import static com.bridge.constants.BridgeConstants.*;

@Slf4j
@RestController
@RequestMapping("/api/fi/{institute}/aadhar-seeding")
@SuppressWarnings("all")
public class SeedingController {

    @Autowired
    BridgeController bridgeController;

    @PostMapping("/bio")
    public Mono<ResponseEntity<String>> handleSeedingBio(@RequestBody String requestBody, @RequestHeader Map<String, String> headers,
                                                         ServerHttpRequest fullRequest) throws Exception {
        return bridgeController.processRequest(requestBody, headers, fullRequest, SEEDING_BIO, null);
    }

    @PostMapping("/otp/generate")
    public Mono<ResponseEntity<String>> handleSeedingGenerateOtp(@RequestBody String requestBody, @RequestHeader Map<String, String> headers,
                                                                 ServerHttpRequest fullRequest) throws Exception {
        return bridgeController.processRequest(requestBody, headers, fullRequest, SEEDING_GEN_OTP, null);
    }

    @PostMapping("/otp/validate")
    public Mono<ResponseEntity<String>> handleSeedingValidateOtp(@RequestBody String requestBody, @RequestHeader Map<String, String> headers,
                                                                 ServerHttpRequest fullRequest) throws Exception {
        return bridgeController.processRequest(requestBody, headers, fullRequest, SEEDING_VAL_OTP, null);
    }
}

