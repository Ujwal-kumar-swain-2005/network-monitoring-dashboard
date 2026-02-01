package com.networkmonitor.service;

import com.networkmonitor.dto.AlertDTO;
import com.networkmonitor.dto.MetricDTO;
import com.networkmonitor.entity.Alert;
import com.networkmonitor.entity.Server;
import com.networkmonitor.repository.AlertRepository;
import com.networkmonitor.repository.ServerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service responsible for detecting threshold violations and creating alerts.
 * Monitors CPU usage, memory usage, and broadcasts alerts to connected dashboards.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AlertService {

    private final AlertRepository alertRepository;
    private final ServerRepository serverRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Value("${monitoring.alerts.cpu-threshold:90.0}")
    private double cpuThreshold;

    @Value("${monitoring.alerts.memory-threshold:85.0}")
    private double memoryThreshold;

    /**
     * Checks incoming metric data against configured thresholds.
     * Creates and broadcasts alerts when thresholds are exceeded.
     */
    public void checkThresholds(Server server, MetricDTO metric) {
        // CPU threshold check
        if (metric.getCpuUsage() != null && metric.getCpuUsage() > cpuThreshold) {
            createAlert(server, "CPU_HIGH",
                    String.format("CPU usage on %s reached %.1f%% (threshold: %.1f%%)",
                            server.getHostname(), metric.getCpuUsage(), cpuThreshold),
                    metric.getCpuUsage() > 95 ? "CRITICAL" : "WARNING");
        }

        // Memory threshold check
        if (metric.getMemoryUsage() != null && metric.getMemoryUsage() > memoryThreshold) {
            createAlert(server, "MEMORY_HIGH",
                    String.format("Memory usage on %s reached %.1f%% (threshold: %.1f%%)",
                            server.getHostname(), metric.getMemoryUsage(), memoryThreshold),
                    metric.getMemoryUsage() > 95 ? "CRITICAL" : "WARNING");
        }
    }

    /**
     * Creates an alert, persists it, and broadcasts to connected dashboards.
     */
    private void createAlert(Server server, String type, String message, String severity) {
        Alert alert = Alert.builder()
                .serverId(server.getId())
                .type(type)
                .message(message)
                .severity(severity)
                .createdAt(LocalDateTime.now())
                .build();

        alertRepository.save(alert);

        // Broadcast alert to all connected dashboard clients
        AlertDTO alertDTO = toDTO(alert, server.getHostname());
        messagingTemplate.convertAndSend("/topic/alerts", alertDTO);

        log.warn("Alert [{}] {} — {}", severity, type, message);
    }

    /**
     * Returns all alerts, most recent first.
     */
    public List<AlertDTO> getRecentAlerts(int limit) {
        return alertRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(0, limit))
                .stream()
                .map(alert -> {
                    String hostname = serverRepository.findById(alert.getServerId())
                            .map(Server::getHostname)
                            .orElse("Unknown");
                    return toDTO(alert, hostname);
                })
                .collect(Collectors.toList());
    }

    /**
     * Returns alerts for a specific server.
     */
    public List<AlertDTO> getAlertsByServerId(Long serverId) {
        return alertRepository.findByServerIdOrderByCreatedAtDesc(serverId)
                .stream()
                .map(alert -> {
                    String hostname = serverRepository.findById(alert.getServerId())
                            .map(Server::getHostname)
                            .orElse("Unknown");
                    return toDTO(alert, hostname);
                })
                .collect(Collectors.toList());
    }

    private AlertDTO toDTO(Alert alert, String hostname) {
        return AlertDTO.builder()
                .id(alert.getId())
                .serverId(alert.getServerId())
                .serverHostname(hostname)
                .type(alert.getType())
                .message(alert.getMessage())
                .severity(alert.getSeverity())
                .createdAt(alert.getCreatedAt())
                .build();
    }
}
