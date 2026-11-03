package com.bridge.workflow;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@SuppressWarnings("unused")
public final class WorkflowContext {
    private final Map<String, Object> map;
    private final StringBuilder sb;

    @Getter
    private final CompletableFuture<ResponseEntity<?>> future;

    public WorkflowContext() {
        this.map = new HashMap<>();
        this.sb = new StringBuilder();
        this.future = new CompletableFuture<>();
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) map.get(key);
    }

    public String getString(String key) {
        return (String) map.get(key);
    }

    public void put(String key, Object value) {
        map.put(key, value);
    }

    public boolean contains(String key) {
        return map.containsKey(key);
    }

    public Object remove(String key) {
        return map.remove(key);
    }

    public void log(String msg) {
        sb.append(msg).append("\n");
    }

    public String getContextLogs() {
        return sb.toString();
    }

    public String getContextValues() {
        StringBuilder builder = new StringBuilder();
        builder.append("\n");
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            builder.append(entry.getKey())
                    .append(" = ")
                    .append(entry.getValue())
                    .append("\n");
        }
        return builder.toString();
    }
}
