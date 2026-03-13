package com.networkmonitor.agent;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Collects system metrics using the OSHI library.
 * Gathers CPU usage, memory usage, and network I/O statistics.
 */
@Component
@Slf4j
public class MetricCollector {
    private static final Logger log =  LoggerFactory.getLogger(MetricCollector.class);
    private final SystemInfo systemInfo = new SystemInfo();
    private long[] previousTicks;
    private long previousNetIn = 0;
    private long previousNetOut = 0;
    private boolean initialized = false;

    @Value("${monitor.agent.hostname:}")
    private String configuredHostname;

    /**
     * Collects a snapshot of current system metrics.
     * 
     * @return MetricPayload containing CPU, memory, and network data
     */
    public MetricPayload collect() {
        HardwareAbstractionLayer hardware = systemInfo.getHardware();
        CentralProcessor processor = hardware.getProcessor();
        GlobalMemory memory = hardware.getMemory();

        // --- CPU Usage ---
        double cpuUsage = 0.0;
        if (previousTicks == null) {
            previousTicks = processor.getSystemCpuLoadTicks();
            try {
                Thread.sleep(1000); // Need a small delay for accurate first reading
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        cpuUsage = processor.getSystemCpuLoadBetweenTicks(previousTicks) * 100.0;
        previousTicks = processor.getSystemCpuLoadTicks();

        // --- Memory Usage ---
        long totalMemory = memory.getTotal();
        long availableMemory = memory.getAvailable();
        double memoryUsage = ((totalMemory - availableMemory) * 100.0) / totalMemory;

        // --- Network I/O (delta since last collection) ---
        long currentNetIn = 0;
        long currentNetOut = 0;
        List<NetworkIF> networkIFs = hardware.getNetworkIFs();
        for (NetworkIF net : networkIFs) {
            net.updateAttributes();
            currentNetIn += net.getBytesRecv();
            currentNetOut += net.getBytesSent();
        }

        long deltaNetIn = 0;
        long deltaNetOut = 0;
        if (initialized) {
            deltaNetIn = currentNetIn - previousNetIn;
            deltaNetOut = currentNetOut - previousNetOut;
        }
        previousNetIn = currentNetIn;
        previousNetOut = currentNetOut;
        initialized = true;

        MetricPayload payload = new MetricPayload(
                getHostname(),
                getIpAddress(),
                Math.round(cpuUsage * 100.0) / 100.0,
                Math.round(memoryUsage * 100.0) / 100.0,
                deltaNetIn,
                deltaNetOut,
                LocalDateTime.now()
        );

        log.debug("Collected metrics — CPU: {}%, MEM: {}%, NetIn: {}, NetOut: {}",
                payload.getCpuUsage(), payload.getMemoryUsage(),
                payload.getNetworkIn(), payload.getNetworkOut());

        return payload;
    }

    /**
     * Gets the hostname, using configured value or auto-detecting from system.
     */
    private String getHostname() {
        if (configuredHostname != null && !configuredHostname.isEmpty()) {
            return configuredHostname;
        }
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            log.warn("Could not determine hostname, using 'unknown'");
            return "unknown";
        }
    }

    /**
     * Gets the primary IP address of this machine.
     */
    private String getIpAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            return "127.0.0.1";
        }
    }
}
