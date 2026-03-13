package com.networkmonitor.service;

import com.networkmonitor.dto.ServerDTO;
import com.networkmonitor.entity.Metric;
import com.networkmonitor.entity.Server;
import com.networkmonitor.repository.MetricRepository;
import com.networkmonitor.repository.ServerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service layer for server management.
 * Handles server registration, status updates, and health checks.
 */
@Service
@Slf4j
public class ServerService {
    @Autowired
    private  ServerRepository serverRepository;
    @Autowired
    private  MetricRepository metricRepository;
    private static final Logger log =  LoggerFactory.getLogger(ServerService.class);
    @Value("${monitoring.server.timeout-seconds:30}")
    private int timeoutSeconds;

    /**
     * Registers a new server or updates an existing one.
     * Called each time a metric is received from an agent.
     */
    @Transactional
    public Server upsertServer(String hostname, String ipAddress) {
        Optional<Server> existing = serverRepository.findByHostname(hostname);

        if (existing.isPresent()) {
            Server server = existing.get();
            server.setIpAddress(ipAddress);
            server.setStatus("ONLINE");
            server.setLastSeen(LocalDateTime.now());
            return serverRepository.save(server);
        }

        Server newServer = new Server(
                hostname,
                ipAddress,
                "ONLINE",
                LocalDateTime.now()
        );

        log.info("New server registered: {} ({})", hostname, ipAddress);
        return serverRepository.save(newServer);
    }

    /**
     * Returns all servers with their latest metric readings.
     */
    public List<ServerDTO> getAllServers() {
        return serverRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Returns a single server with its latest metric readings.
     */
    public ServerDTO getServerById(Long id) {
        Server server = serverRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Server not found: " + id));
        return toDTO(server);
    }

    /**
     * Periodically checks for servers that haven't sent metrics recently
     * and marks them as OFFLINE.
     */
    @Scheduled(fixedRate = 15000)
    @Transactional
    public void checkServerHealth() {
        LocalDateTime threshold = LocalDateTime.now().minusSeconds(timeoutSeconds);
        List<Server> staleServers = serverRepository.findByLastSeenBefore(threshold);

        for (Server server : staleServers) {
            if ("ONLINE".equals(server.getStatus())) {
                log.warn("Server {} marked as OFFLINE (last seen: {})", 
                        server.getHostname(), server.getLastSeen());
                server.setStatus("OFFLINE");
                serverRepository.save(server);
            }
        }
    }

    /**
     * Converts a Server entity to a ServerDTO, enriching it with the latest metrics.
     */
    private ServerDTO toDTO(Server server) {
        ServerDTO dto = new ServerDTO(
                server.getId(),
                server.getHostname(),
                server.getIpAddress(),
                server.getStatus(),
                server.getLastSeen()
        );

        // Attach latest metric data if available
        metricRepository.findLatestByServerId(server.getId())
                .ifPresent(metric -> {
                    dto.setCpuUsage(metric.getCpuUsage());
                    dto.setMemoryUsage(metric.getMemoryUsage());
                });

        return dto;
    }
}
