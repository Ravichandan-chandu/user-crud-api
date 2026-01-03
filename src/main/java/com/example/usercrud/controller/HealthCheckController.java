package com.example.usercrud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
public class HealthCheckController {

    private final DataSource dataSource;

    @Autowired
    public HealthCheckController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> healthStatus = new HashMap<>();
        healthStatus.put("status", "UP");
        healthStatus.put("application", "User CRUD API");
        
        // Check database connection
        try (Connection connection = dataSource.getConnection()) {
            healthStatus.put("database", "Connected");
            healthStatus.put("databaseProductName", connection.getMetaData().getDatabaseProductName());
        } catch (Exception e) {
            healthStatus.put("database", "Disconnected");
            healthStatus.put("error", e.getMessage());
            return ResponseEntity.status(503).body(healthStatus);
        }
        
        return ResponseEntity.ok(healthStatus);
    }
}
