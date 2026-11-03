package com.bridge.controller;

import com.bridge.TestService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("reload")
public class ReloadController {

    private final TestService service;

    @GetMapping("/test-service")
    public ResponseEntity<String> reloadTestService() {
        service.reload();
        return ResponseEntity.ok("reloaded TestService");
    }
}
