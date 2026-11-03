package com.bridge.services;

import com.bridge.security.rsa.RSACryptoEngine;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

@Slf4j
@Service
public class RSAKeyStoreService {

    @Getter
    private final Map<String, RSACryptoEngine> rsaEngineMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void loadKeys() {
        try {
            Path keyDir = Paths.get("keys");
            if (!Files.exists(keyDir)) {
                log.error("Keys directory not found: {}", keyDir.toAbsolutePath());
                return;
            }
            try (Stream<Path> paths = Files.walk(keyDir, 1)) {
                paths.filter(Files::isRegularFile)
                        .filter(path -> path.getFileName().toString().endsWith(".publickey"))
                        .forEach(this::loadKeyFile);
            }
            log.info("Loaded {} RSA public keys", rsaEngineMap.size());
        } catch (Exception e) {
            log.error("Failed to load RSA keys", e);
        }
    }

    private void loadKeyFile(Path path) {
        try {
            String fileName = path.getFileName().toString();
            String keyName = fileName.substring(0, fileName.indexOf(".publickey"));
            String publicKey = Files.readString(path, StandardCharsets.UTF_8);
            RSACryptoEngine engine = RSACryptoEngine.of(publicKey);
            if (engine == null) {
                log.error("Failed to create RSACryptoEngine for file: {}", fileName);
                return;
            }
            rsaEngineMap.put(keyName, engine);
            log.info("Loaded RSA key: {}", keyName);
        } catch (IOException e) {
            log.error("Error reading key file: {}", path, e);
        }
    }

    public RSACryptoEngine getEngine(String keyName) {
        return rsaEngineMap.get(keyName);
    }

    public Map<String, RSACryptoEngine> getAll() {
        return Collections.unmodifiableMap(rsaEngineMap);
    }
}
