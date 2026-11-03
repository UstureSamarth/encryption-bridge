package com.bridge.controller;

import com.bridge.workflow.WorkflowContext;
import com.bridge.workflow.WorkflowExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
@SuppressWarnings("all")
@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class WorkFlowController {

    private final WorkflowExecutor workflowExecutor;

    public WorkFlowController(WorkflowExecutor workflowExecutor) {
        this.workflowExecutor = workflowExecutor;
    }

    @GetMapping("/test")
    public ResponseEntity<Map<String, Object>> test() {
        WorkflowContext context = new WorkflowContext();
        workflowExecutor.execute("test", context);
        Map<String, Object> response = new HashMap<>();
        response.put("status", "OK");
        response.put("message", "Workflow completed");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/test3")
    public ResponseEntity<Map<String, Object>> test3() {
        WorkflowContext context = new WorkflowContext();
        workflowExecutor.execute("test3", context);
        Map<String, Object> response = new HashMap<>();
        response.put("status", "OK");
        response.put("message", "Workflow test3 completed");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/test2")
    public Mono<ResponseEntity<?>> test2() {
        WorkflowContext context = new WorkflowContext();
        workflowExecutor.executeAsync("test2", context);
        return Mono.fromFuture(context.getFuture().orTimeout(10, TimeUnit.SECONDS))
                .onErrorResume(TimeoutException.class, ex -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("status", "TIMEOUT");
                    response.put("message", "Workflow timeout");
                    return Mono.just(ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT)
                            .body(response)
                    );
                });
    }
}
