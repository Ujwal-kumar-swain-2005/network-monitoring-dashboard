package com.networkmonitor.service;

import com.networkmonitor.dto.MetricDTO;
import com.networkmonitor.entity.Metric;
import com.networkmonitor.entity.Server;
import com.networkmonitor.repository.MetricRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Core service for processing, persisting, and broadcasting metrics.
 * 
 * Flow: Agent → WebSocket → processMetric() → DB + broadcast + alert check
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MetricService {

    private final MetricRepository metricRepository;
    private final ServerService serverService;
    private final AlertService alertService;
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Processes an incoming metric from a monitoring agent.
     * 1. Upserts the server record
     * 2. Persists the metric
     * 3. Broadcasts to WebSocket subscribers
     * 4. Checks alert thresholds
     */
    @Transactional
    public void processMetric(MetricDTO dto) {
        // Register or update the server
        Server server = serverService.upsertServer(dto.getHostname(), dto.getIpAddress());

        // Persist metric data
        Metric metric = Metric.builder()
                .serverId(server.getId())
                .cpuUsage(dto.getCpuUsage())
                .memoryUsage(dto.getMemoryUsage())
                .networkIn(dto.getNetworkIn())
                .networkOut(dto.getNetworkOut())
                .timestamp(dto.getTimestamp() != null ? dto.getTimestamp() : LocalDateTime.now())
                .build();
        metricRepository.save(metric);

        // Enrich DTO with server ID and broadcast to all connected dashboards
        dto.setServerId(server.getId());
        messagingTemplate.convertAndSend("/topic/metrics", dto);

        // Check if thresholds are exceeded and trigger alerts
        alertService.checkThresholds(server, dto);

        log.debug("Processed metric for {} — CPU: {}%, MEM: {}%",
                dto.getHostname(), dto.getCpuUsage(), dto.getMemoryUsage());
    }

    /**
     * Returns recent metrics for a specific server.
     */
    public List<MetricDTO> getRecentMetrics(Long serverId, int limit) {
        return metricRepository.findRecentByServerId(serverId, PageRequest.of(0, limit))
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Returns metrics for a server within a specific time range.
     */
    public List<MetricDTO> getMetricsByTimeRange(Long serverId, LocalDateTime start, LocalDateTime end) {
        return metricRepository.findByServerIdAndTimestampBetweenOrderByTimestampAsc(serverId, start, end)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private MetricDTO toDTO(Metric metric) {
        return MetricDTO.builder()
                .serverId(metric.getServerId())
                .cpuUsage(metric.getCpuUsage())
                .memoryUsage(metric.getMemoryUsage())
                .networkIn(metric.getNetworkIn())
                .networkOut(metric.getNetworkOut())
                .timestamp(metric.getTimestamp())
                .build();
    }
}
