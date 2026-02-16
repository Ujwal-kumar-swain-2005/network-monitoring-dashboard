package com.networkmonitor.agent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Scheduled task that collects system metrics at a fixed interval
 * and sends them to the backend via WebSocket.
 * 
 * Default interval: 3000ms (configurable via monitor.agent.collect-interval-ms)
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class MetricScheduler {

    private final MetricCollector metricCollector;
    private final AgentWebSocketClient webSocketClient;

    /**
     * Collects system metrics and sends them to the backend.
     * Runs every 3 seconds by default.
     */
    @Scheduled(fixedDelayString = "${monitor.agent.collect-interval-ms:3000}")
    public void collectAndSend() {
        try {
            MetricPayload payload = metricCollector.collect();
            webSocketClient.sendMetric(payload);

            log.info("Metric sent — Host: {}, CPU: {}%, MEM: {}%, NetIn: {}, NetOut: {}",
                    payload.getHostname(),
                    payload.getCpuUsage(),
                    payload.getMemoryUsage(),
                    payload.getNetworkIn(),
                    payload.getNetworkOut());

        } catch (Exception e) {
            log.error("Failed to collect/send metrics: {}", e.getMessage(), e);
        }
    }
}
