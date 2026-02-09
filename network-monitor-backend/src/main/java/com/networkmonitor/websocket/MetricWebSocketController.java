package com.networkmonitor.websocket;

import com.networkmonitor.dto.MetricDTO;
import com.networkmonitor.service.MetricService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

/**
 * WebSocket controller that receives metric data from monitoring agents.
 * 
 * Agents send metrics to /app/metrics via STOMP.
 * This controller delegates processing to MetricService which handles
 * persistence, broadcasting, and alert detection.
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class MetricWebSocketController {

    private final MetricService metricService;

    /**
     * Receives metric data from monitoring agents via STOMP.
     * Message destination: /app/metrics
     */
    @MessageMapping("/metrics")
    public void receiveMetric(MetricDTO metricDTO) {
        log.info("Received metric from {}: CPU={}%, MEM={}%",
                metricDTO.getHostname(), metricDTO.getCpuUsage(), metricDTO.getMemoryUsage());
        metricService.processMetric(metricDTO);
    }
}
