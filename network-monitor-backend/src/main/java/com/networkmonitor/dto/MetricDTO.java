package com.networkmonitor.dto;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for metric data.
 * Used for both incoming agent data and outgoing WebSocket broadcasts.
 */
public class MetricDTO {

    private Long serverId;
    private String hostname;
    private String ipAddress;
    private Double cpuUsage;
    private Double memoryUsage;
    private Long networkIn;
    private Long networkOut;
    private LocalDateTime timestamp;

    public MetricDTO() {
    }
    public MetricDTO(Long serverId, Double cpuUsage, Double memoryUsage,
                     Long networkIn, Long networkOut, LocalDateTime timestamp) {
        this.serverId = serverId;
        this.cpuUsage = cpuUsage;
        this.memoryUsage = memoryUsage;
        this.networkIn = networkIn;
        this.networkOut = networkOut;
        this.timestamp = timestamp;
    }


    public MetricDTO(Long serverId, String hostname, String ipAddress, Double cpuUsage, Double memoryUsage, Long networkIn, Long networkOut, LocalDateTime timestamp) {
        this.serverId = serverId;
        this.hostname = hostname;
        this.ipAddress = ipAddress;
        this.cpuUsage = cpuUsage;
        this.memoryUsage = memoryUsage;
        this.networkIn = networkIn;
        this.networkOut = networkOut;
        this.timestamp = timestamp;
    }

    public Long getServerId() {
        return serverId;
    }

    public void setServerId(Long serverId) {
        this.serverId = serverId;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
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
}
