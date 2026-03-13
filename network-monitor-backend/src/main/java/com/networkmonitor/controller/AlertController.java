package com.networkmonitor.controller;

import com.networkmonitor.dto.AlertDTO;
import com.networkmonitor.service.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for alert endpoints.
 */
@RestController
@RequestMapping("/api/alerts")

public class AlertController {
    @Autowired
    private  AlertService alertService;


    @GetMapping
    public ResponseEntity<List<AlertDTO>> getRecentAlerts(
            @RequestParam(defaultValue = "50") int limit) {
        return ResponseEntity.ok(alertService.getRecentAlerts(limit));
    }


    @GetMapping("/server/{serverId}")
    public ResponseEntity<List<AlertDTO>> getAlertsByServer(@PathVariable Long serverId) {
        return ResponseEntity.ok(alertService.getAlertsByServerId(serverId));
    }
}
