package com.bridge.services;

import com.bridge.pojo.EncryptedRequest;
import com.bridge.pojo.RequestInfo;
import com.bridge.security.aes.AESCryptoEngine;
import com.bridge.security.rsa.RSACryptoEngine;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static com.bridge.error.Checker.checkNull;
import static com.bridge.security.util.EncryptHelper.getIV;
import static com.bridge.security.util.EncryptHelper.getSecretKey;

@Slf4j
@Service
public class EncryptService {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    RSAKeyStoreService rsaKeyStoreService;

    public String getEncryptedRequest(RequestInfo info) throws Exception {
        RSACryptoEngine rsaCryptoEngine = getRsaCryptoEngine(info.getInstitute(), info.getBc());
        AESCryptoEngine aesCryptoEngine = getAesCryptoEngine();
        String sKey = rsaCryptoEngine.getEncrypted(aesCryptoEngine.getSKey());
        String iv = rsaCryptoEngine.getEncrypted(aesCryptoEngine.getIv());
        String content = aesCryptoEngine.getEncrypted(info.getRequest());
        String api = aesCryptoEngine.getEncrypted(info.getApi());
        info.setAesCryptoEngine(aesCryptoEngine);
        EncryptedRequest encryptedRequest = EncryptedRequest.builder()
                .sk(sKey)
                .iv(iv)
                .ct(content)
                .api(api)
                .version("1.1")
                .build();
        return mapper.writeValueAsString(encryptedRequest);
    }

    private RSACryptoEngine getRsaCryptoEngine(String institute, String bc) {
        String key = (StringUtils.isBlank(bc) ? institute : bc + "_" + institute);
        RSACryptoEngine engine = rsaKeyStoreService.getEngine(key);
        checkNull(engine, "public key " + key + " not found");
        return engine;
    }

    private AESCryptoEngine getAesCryptoEngine() throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] sk = getSecretKey();
        byte[] iv = getIV(true);
        return AESCryptoEngine.of(sk, iv);
    }
}
