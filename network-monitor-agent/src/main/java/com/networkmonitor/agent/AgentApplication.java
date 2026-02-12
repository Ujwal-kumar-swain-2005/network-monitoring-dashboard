package com.networkmonitor.agent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Network Monitor Agent application.
 * Collects system metrics using OSHI and sends them to the backend
 * via WebSocket at a configurable interval (default: 3 seconds).
 */
@SpringBootApplication
@EnableScheduling
public class AgentApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgentApplication.class, args);
    }
}
