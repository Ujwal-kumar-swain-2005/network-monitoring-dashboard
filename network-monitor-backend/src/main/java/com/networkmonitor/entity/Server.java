package com.networkmonitor.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Represents a monitored server in the network.
 * Tracks hostname, IP address, online/offline status, and last heartbeat.
 */
@Entity
@Table(name = "servers")

public class Server {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String hostname;

    @Column(name = "ip_address", nullable = false)
    private String ipAddress;

    @Column(nullable = false)
    @Builder.Default
    private String status = "ONLINE";

    @Column(name = "last_seen")
    private LocalDateTime lastSeen;

    public Server() {
    }
    public Server(String hostname, String ipAddress, String status, LocalDateTime lastSeen) {
        this.hostname = hostname;
        this.ipAddress = ipAddress;
        this.status = status;
        this.lastSeen = lastSeen;
    }
    public Server(Long id, String hostname, String ipAddress, String status, LocalDateTime lastSeen) {
        this.id = id;
        this.hostname = hostname;
        this.ipAddress = ipAddress;
        this.status = status;
        this.lastSeen = lastSeen;
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
}
