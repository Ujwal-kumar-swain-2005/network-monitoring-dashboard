package com.networkmonitor.controller;

import com.networkmonitor.dto.ServerDTO;
import com.networkmonitor.service.ServerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for server management endpoints.
 */
@RestController
@RequestMapping("/api/servers")
@RequiredArgsConstructor
public class ServerController {
    @Autowired
    private  ServerService serverService;

    /**
     * GET /api/servers — List all monitored servers with latest metrics.
     */
    @GetMapping
    public ResponseEntity<List<ServerDTO>> getAllServers() {
        return ResponseEntity.ok(serverService.getAllServers());
    }

    /**
     * GET /api/servers/{id} — Get a specific server by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ServerDTO> getServerById(@PathVariable Long id) {
        return ResponseEntity.ok(serverService.getServerById(id));
    }
}
