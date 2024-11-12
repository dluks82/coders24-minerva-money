package dev.dluks.minervamoney.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/")
public class HealthCheckController {

    @GetMapping
    public ResponseEntity<Map<String, Object>> getApiStatus() {
        Map<String, Object> status = new HashMap<>();

        status.put("status", "UP");
        status.put("timestamp", LocalDateTime.now());
        status.put("service", "Minerva Money API");
        status.put("documentation", "/docs");

        return ResponseEntity.ok(status);
    }
}
