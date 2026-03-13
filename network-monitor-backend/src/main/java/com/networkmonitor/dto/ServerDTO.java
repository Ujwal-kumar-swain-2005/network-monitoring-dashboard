package com.networkmonitor.dto;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for server information.
 * Includes latest CPU/memory readings for dashboard display.
 */

public class ServerDTO {

    private Long id;
    private String hostname;
    private String ipAddress;
    private String status;
    private LocalDateTime lastSeen;
    private Double cpuUsage;
    private Double memoryUsage;

    public ServerDTO() {
    }
    public ServerDTO(Long id, String hostname, String ipAddress, String status, LocalDateTime lastSeen) {
        this.id = id;
        this.hostname = hostname;
        this.ipAddress = ipAddress;
        this.status = status;
        this.lastSeen = lastSeen;
    }

    public ServerDTO(Long id, String hostname, String ipAddress, String status, LocalDateTime lastSeen, Double cpuUsage, Double memoryUsage) {
        this.id = id;
        this.hostname = hostname;
        this.ipAddress = ipAddress;
        this.status = status;
        this.lastSeen = lastSeen;
        this.cpuUsage = cpuUsage;
        this.memoryUsage = memoryUsage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(LocalDateTime lastSeen) {
        this.lastSeen = lastSeen;
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
}
