package com.networkmonitor.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Stores a point-in-time metric snapshot for a server.
 * Includes CPU usage, memory usage, and network I/O counters.
 */
@Entity
@Table(name = "metrics", indexes = {
    @Index(name = "idx_metrics_server_id", columnList = "server_id"),
    @Index(name = "idx_metrics_timestamp", columnList = "timestamp")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Metric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "server_id", nullable = false)
    private Long serverId;

    @Column(name = "cpu_usage", nullable = false)
    private Double cpuUsage;

    @Column(name = "memory_usage", nullable = false)
    private Double memoryUsage;

    @Column(name = "network_in")
    private Long networkIn;

    @Column(name = "network_out")
    private Long networkOut;

    @Column(nullable = false)
    private LocalDateTime timestamp;
}
