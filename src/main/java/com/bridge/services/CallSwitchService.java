package com.bridge.services;

import com.bridge.pojo.EncryptedResponse;
import com.bridge.pojo.RequestInfo;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

import static com.bridge.constants.BridgeConstants.NO_PARSE_TXN;
import static com.bridge.constants.BridgeConstants.NO_PARSING_TXN;

@SuppressWarnings("all")
@Service
@Slf4j
public class CallSwitchService {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    WebClient webClient;

    @Value("${switch.url}")
    String switchUrl;

    public Mono<String> callSwitch(String encryptedRequest, RequestInfo info) {
        String txnId = String.valueOf(ThreadLocalRandom.current().nextInt(10000, 100000));
        String finalUrl = switchUrl.replace("{txnId}", txnId);
        return webClient.post()
                .uri(finalUrl)
                .header("institute", info.getInstitute())
                .header("bc", StringUtils.isBlank(info.getBc()) ? null : info.getBc())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(encryptedRequest)
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> {
                    try {
                        if (NO_PARSING_TXN.contains(info.getTxnName())) {
                            return decryptWithNoParsing(response, info);
                        }
                        return decrypt(response, info);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    private String decrypt(String response, RequestInfo info) throws Exception {
        EncryptedResponse encryptedResponse = mapper.readValue(response, EncryptedResponse.class);
        String decrypted = info.getAesCryptoEngine().getDecrypted(encryptedResponse.getCt());
        Map<Integer, String> map = mapper.readValue(decrypted, new TypeReference<TreeMap<Integer, String>>() {
        });
        String res = mapper.writeValueAsString(map);
        return res;
    }

    private String decryptWithNoParsing(String response, RequestInfo info) throws Exception {
        EncryptedResponse encryptedResponse = mapper.readValue(response, EncryptedResponse.class);
        return info.getAesCryptoEngine().getDecrypted(encryptedResponse.getCt());
    }

    @PostConstruct
    private void printSwitchUrl() {
        log.info("Switch Url {}", switchUrl);
    }
}
