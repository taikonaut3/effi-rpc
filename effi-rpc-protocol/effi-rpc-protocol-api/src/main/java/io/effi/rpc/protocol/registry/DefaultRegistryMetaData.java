package io.effi.rpc.protocol.registry;

import com.sun.management.OperatingSystemMXBean;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.core.Portal;
import io.effi.rpc.core.ServerExporter;
import io.effi.rpc.core.utils.PortalUtil;
import io.effi.rpc.protocol.support.DefaultServerExporter;
import io.effi.rpc.transport.server.Server;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.lang.management.ManagementFactory;
import java.util.Map;

/**
 * SystemInfo.
 */
@Data
@ToString
@Accessors(fluent = true)
public class DefaultRegistryMetaData {

    private static final OperatingSystemMXBean OS_BEAN = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

    private double cpuUsage;

    private double memoryUsage;

    private long connections;

    private int services;

    private double loadAverage;

    public DefaultRegistryMetaData() {

    }

    public DefaultRegistryMetaData(URL url) {
        Portal portal = PortalUtil.acquirePortal(url);
        // Get the server CPU usage
        cpuUsage = round(OS_BEAN.getCpuLoad());
        loadAverage = round(OS_BEAN.getSystemLoadAverage());
        // Get the server memory usage
        long totalMemory = OS_BEAN.getTotalMemorySize();
        long freeMemory = OS_BEAN.getFreeMemorySize();
        long usedMemory = totalMemory - freeMemory;
        memoryUsage = round((double) usedMemory / totalMemory);
        ServerExporter serverExporter = portal.serverExporterManager().get(url.uri());
        services = serverExporter.calleeManager().values().size();
        if (serverExporter instanceof DefaultServerExporter defaultServerExporter) {
            Server server = defaultServerExporter.server();
            if (server != null) {
                connections = server.channels().size();
            }
        }
    }

    /**
     * Convert map to DefaultRegistryMetaData.
     *
     * @param map
     * @return
     */
    public static DefaultRegistryMetaData valueOf(Map<String, String> map) {
        DefaultRegistryMetaData defaultRegistryMetaData = new DefaultRegistryMetaData();
        defaultRegistryMetaData.cpuUsage(Double.parseDouble(map.get("cpuUsage")));
        defaultRegistryMetaData.memoryUsage(Double.parseDouble(map.get("memoryUsage")));
        defaultRegistryMetaData.connections(Long.parseLong(map.get("connections")));
        defaultRegistryMetaData.services(Integer.parseInt(map.get("services")));
        defaultRegistryMetaData.loadAverage(Double.parseDouble(map.get("loadAverage")));
        return defaultRegistryMetaData;
    }

    private static double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    /**
     * SystemInfo to map.
     *
     * @return
     */
    public Map<String, String> toMap() {
        return Map.of(
                "cpuUsage", String.valueOf(cpuUsage),
                "memoryUsage", String.valueOf(memoryUsage),
                "connections", String.valueOf(connections),
                "services", String.valueOf(services),
                "loadAverage", String.valueOf(loadAverage)
        );
    }
}
