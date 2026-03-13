package com.networkmonitor.controller;

import com.networkmonitor.dto.MetricDTO;
import com.networkmonitor.service.MetricService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * REST controller for metric data endpoints.
 */
@RestController
@RequestMapping("/api/metrics")
@RequiredArgsConstructor
public class MetricController {
    @Autowired
    private  MetricService metricService;

    /**
     * GET /api/metrics/server/{serverId}?limit=50 — Get recent metrics for a server.
     */
    @GetMapping("/server/{serverId}")
    public ResponseEntity<List<MetricDTO>> getRecentMetrics(
            @PathVariable Long serverId,
            @RequestParam(defaultValue = "50") int limit) {
        return ResponseEntity.ok(metricService.getRecentMetrics(serverId, limit));
    }

    /**
     * GET /api/metrics/server/{serverId}/range?start=...&end=... — Get metrics in time range.
     */
    @GetMapping("/server/{serverId}/range")
    public ResponseEntity<List<MetricDTO>> getMetricsByRange(
            @PathVariable Long serverId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(metricService.getMetricsByTimeRange(serverId, start, end));
    }
}
