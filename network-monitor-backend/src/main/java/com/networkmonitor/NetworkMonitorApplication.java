package com.networkmonitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main entry point for the Network Monitor Backend application.
 * Enables scheduling for periodic server health checks.
 */
@SpringBootApplication
@EnableScheduling
public class NetworkMonitorApplication {

    public static void main(String[] args) {
        SpringApplication.run(NetworkMonitorApplication.class, args);
    }
}
