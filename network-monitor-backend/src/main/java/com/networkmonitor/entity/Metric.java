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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getServerId() {
        return serverId;
    }

    public void setServerId(Long serverId) {
        this.serverId = serverId;
    }

    public Double getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(Double cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public Double getMemoryUsage() {
        return memoryUsage;
    }

    public void setMemoryUsage(Double memoryUsage) {
        this.memoryUsage = memoryUsage;
    }

    public Long getNetworkIn() {
        return networkIn;
    }

    public void setNetworkIn(Long networkIn) {
        this.networkIn = networkIn;
    }

    public Long getNetworkOut() {
        return networkOut;
    }

    public void setNetworkOut(Long networkOut) {
        this.networkOut = networkOut;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Metric() {
    }

    public Metric(Long serverId, Double cpuUsage, Double memoryUsage, Long networkIn, Long networkOut, LocalDateTime timestamp) {
        this.serverId = serverId;
        this.cpuUsage = cpuUsage;
        this.memoryUsage = memoryUsage;
        this.networkIn = networkIn;
        this.networkOut = networkOut;
        this.timestamp = timestamp;
    }

    public Metric(Long id, Long serverId, Double cpuUsage, Double memoryUsage, Long networkIn, Long networkOut, LocalDateTime timestamp) {
        this.id = id;
        this.serverId = serverId;
        this.cpuUsage = cpuUsage;
        this.memoryUsage = memoryUsage;
        this.networkIn = networkIn;
        this.networkOut = networkOut;
        this.timestamp = timestamp;
    }
}
