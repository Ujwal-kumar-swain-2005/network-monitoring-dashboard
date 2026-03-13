package com.networkmonitor.service;

import com.networkmonitor.dto.MetricDTO;
import com.networkmonitor.entity.Metric;
import com.networkmonitor.entity.Server;
import com.networkmonitor.repository.MetricRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
    private Logger log;
    @Autowired
    private  MetricRepository metricRepository;
    @Autowired
    private  ServerService serverService;
    @Autowired
    private  AlertService alertService;
    @Autowired
    private  SimpMessagingTemplate messagingTemplate;

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
        Metric metric = new Metric(
                server.getId(),
                dto.getCpuUsage(),
                dto.getMemoryUsage(),
                dto.getNetworkIn(),
                dto.getNetworkOut(),
                dto.getTimestamp() != null ? dto.getTimestamp() : LocalDateTime.now()
        );

        metricRepository.save(metric);
        metricRepository.save(metric);


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
        MetricDTO dto = new MetricDTO(
                metric.getServerId(),
                metric.getCpuUsage(),
                metric.getMemoryUsage(),
                metric.getNetworkIn(),
                metric.getNetworkOut(),
                metric.getTimestamp()
        );

        return dto;
    }
}
