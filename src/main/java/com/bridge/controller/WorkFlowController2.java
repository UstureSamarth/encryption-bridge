package com.bridge.controller;

import com.bridge.workflow.WorkflowContext;
import com.bridge.workflow.WorkflowExecutor2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@SuppressWarnings("all")
@RestController
@RequiredArgsConstructor
public class WorkFlowController2 {

    private final WorkflowExecutor2 workflowExecutor;

    @GetMapping("/select")
    public ResponseEntity<Map<String, Object>> test() {
        WorkflowContext context = new WorkflowContext();
        workflowExecutor.execute("f1", context);
        Map<String, Object> response = new HashMap<>();
        response.put("status", "OK");
        response.put("message", "WorkflowSelector completed");
        return ResponseEntity.ok(response);
    }
}
