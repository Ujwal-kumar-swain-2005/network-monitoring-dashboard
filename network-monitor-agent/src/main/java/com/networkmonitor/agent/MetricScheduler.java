package com.networkmonitor.agent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Scheduled task that collects system metrics at a fixed interval
 * and sends them to the backend via WebSocket.
 * 
 * Default interval: 3000ms (configurable via monitor.agent.collect-interval-ms)
 */
@Component
@Slf4j
public class MetricScheduler {
    private static final Logger log =  LoggerFactory.getLogger(MetricScheduler.class);
    private final MetricCollector metricCollector;
    private final AgentWebSocketClient webSocketClient;

    public MetricScheduler(MetricCollector metricCollector, AgentWebSocketClient webSocketClient) {
        this.metricCollector = metricCollector;
        this.webSocketClient = webSocketClient;
    }

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
