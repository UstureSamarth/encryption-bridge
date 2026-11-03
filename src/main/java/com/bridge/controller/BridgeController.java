package com.bridge.controller;

import com.bridge.pojo.RequestInfo;
import com.bridge.pojo.login.LoginRequest;
import com.bridge.services.CallSwitchService;
import com.bridge.services.EncryptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

import static com.bridge.constants.BridgeConstants.*;
import static com.bridge.error.Checker.*;
import static com.bridge.helper.RequestHelper.*;
import static com.bridge.helper.TxnNameHelper.getTxnName;

@Slf4j
@RestController
@RequestMapping("/api/fi/{institute}/v2.5")
@SuppressWarnings("all")
public class BridgeController {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    EncryptService encryptService;

    @Autowired
    CallSwitchService callSwitchService;

    @PostMapping("/call")
    public Mono<ResponseEntity<String>> handleRequest(@RequestBody Map<Integer, String> requestBody,
                                                      @RequestHeader Map<String, String> headers,
                                                      ServerHttpRequest fullRequest) throws Exception {
        checkNull(requestBody, "Request body is missing");
        return processRequest(requestBody, headers, fullRequest, getTxnName(requestBody), headers.get("bc"));
    }

    @PostMapping("/aeps/generate-login-otp")
    public Mono<ResponseEntity<String>> handleGenOtpRequest(@RequestBody LoginRequest requestBody,
                                                            @RequestHeader Map<String, String> headers,
                                                            ServerHttpRequest fullRequest) throws Exception {
        checkNull(requestBody, "Request body is missing");
        return processRequest(requestBody, headers, fullRequest, GENERATE_OTP, null);
    }

    @PostMapping("/aeps/2fa-auth-login")
    public Mono<ResponseEntity<String>> handle2FALogin(@RequestBody Map<Integer, String> requestBody,
                                                       @RequestHeader Map<String, String> headers,
                                                       ServerHttpRequest fullRequest) throws Exception {
        checkNull(requestBody, "Request body is missing");
        return handleRequest(requestBody, headers, fullRequest);
    }

    @PostMapping("/aeps/validate-login-otp")
    public Mono<ResponseEntity<String>> handleValOtpRequest(@RequestBody Map<Integer, String> requestBody,
                                                            @RequestHeader Map<String, String> headers,
                                                            ServerHttpRequest fullRequest) throws Exception {
        return handleRequest(requestBody, headers, fullRequest);
    }

    @PostMapping("/get-bank-list")
    public Mono<ResponseEntity<String>> handleBankList(@RequestHeader Map<String, String> headers,
                                                       ServerHttpRequest fullRequest) throws Exception {
        return processRequest(null, headers, fullRequest, BANK_LIST, null);
    }

    @PostMapping("/bfd")
    public Mono<ResponseEntity<String>> handleBFD(@RequestBody String requestBody, @RequestHeader Map<String, String> headers,
                                                  ServerHttpRequest fullRequest) throws Exception {
        return processRequest(requestBody, headers, fullRequest, BFD, headers.get("bc"));
    }

    @PostMapping("/aeps/auth")
    public Mono<ResponseEntity<String>> handleAepsAuth(@RequestBody String requestBody, @RequestHeader Map<String, String> headers,
                                                       ServerHttpRequest fullRequest) throws Exception {
        return processRequest(requestBody, headers, fullRequest, AEPS_AUTH, headers.get("bc"));
    }

    @PostMapping("/demographic/auth")
    public Mono<ResponseEntity<String>> handleDemoAuth(@RequestBody String requestBody, @RequestHeader Map<String, String> headers,
                                                       ServerHttpRequest fullRequest) throws Exception {
        return processRequest(requestBody, headers, fullRequest, DEMO_AUTH, headers.get("bc"));
    }

    @PostMapping("/ekyc/gen-otp")
    public Mono<ResponseEntity<String>> handleEkycGenerateOtp(@RequestBody String requestBody, @RequestHeader Map<String, String> headers,
                                                              ServerHttpRequest fullRequest) throws Exception {
        return processRequest(requestBody, headers, fullRequest, EKYC_GEN_OTP, headers.get("bc"));
    }


    public Mono<ResponseEntity<String>> processRequest(Object requestBody, Map<String, String> headers,
                                                       ServerHttpRequest fullRequest, String txnName,
                                                       String bc) throws Exception {
        long start = System.nanoTime();
        String institute = validateInstituteHeader(headers);
        String api = fullRequest.getURI().getPath();
        String id = getRequestId();
        log.info("{}", getRequestReceivedInfo(id, institute, bc, api, txnName));
        String finalRequest = getFinalRequest(txnName, requestBody);
        return processSwitchCall(finalRequest, institute, bc, api, txnName).doOnSuccess(response -> {
            log.info("{}", getTimeTaken(id, start));
        });
    }

    public Mono<ResponseEntity<String>> processSwitchCall(String request, String institute, String bc, String api, String txnName) throws Exception {
        RequestInfo requestInfo = getRequestInfo(request, institute, bc, api, txnName);
        String encryptedRequest = encryptService.getEncryptedRequest(requestInfo);
        checkBlankJson(encryptedRequest, "Encrypted Request is blank");
        MediaType contentType = XML_TXN.contains(txnName)
                ? MediaType.APPLICATION_XML
                : MediaType.APPLICATION_JSON;
        return callSwitchService.callSwitch(encryptedRequest, requestInfo)
                .map(response -> ResponseEntity.ok()
                        .contentType(contentType)
                        .body(response));
    }

    public String getFinalRequest(String txnName, Object requestBody) {
        if (XML_TXN.contains(txnName)) {
            return (String) requestBody;
        }
        if (NO_BODY_TXN.contains(txnName)) {
            return "{}";
        }
        return mapper.writeValueAsString(requestBody);
    }

}

