package com.networkmonitor.controller;

import com.networkmonitor.dto.AlertDTO;
import com.networkmonitor.service.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for alert endpoints.
 */
@RestController
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
public class AlertController {
    
    private final AlertService alertService;

    /**
     * GET /api/alerts?limit=50 — Get recent alerts across all servers.
     */
    @GetMapping
    public ResponseEntity<List<AlertDTO>> getRecentAlerts(
            @RequestParam(defaultValue = "50") int limit) {
        return ResponseEntity.ok(alertService.getRecentAlerts(limit));
    }

    /**
     * GET /api/alerts/server/{serverId} — Get alerts for a specific server.
     */
    @GetMapping("/server/{serverId}")
    public ResponseEntity<List<AlertDTO>> getAlertsByServer(@PathVariable Long serverId) {
        return ResponseEntity.ok(alertService.getAlertsByServerId(serverId));
    }
}
