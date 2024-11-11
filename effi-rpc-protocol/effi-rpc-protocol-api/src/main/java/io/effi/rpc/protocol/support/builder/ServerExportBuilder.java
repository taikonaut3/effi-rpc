package io.effi.rpc.protocol.support.builder;

import io.effi.rpc.common.constant.Constant;
import io.effi.rpc.common.constant.KeyConstant;
import io.effi.rpc.common.constant.SystemKey;
import io.effi.rpc.common.extension.ChainBuilder;
import io.effi.rpc.common.url.Parameter;
import io.effi.rpc.common.url.Parameterization;
import io.effi.rpc.common.util.NetUtil;
import io.effi.rpc.common.util.StringUtil;
import io.effi.rpc.core.Portal;
import io.effi.rpc.core.ServerExporter;
import io.effi.rpc.core.config.ServerConfig;

import java.net.InetSocketAddress;

/**
 * Builder for configuring {@link ServerExporter} instances.
 *
 * @param <T> The type of {@link ServerExporter}.
 * @param <C> The type of the builder.
 */
public abstract class ServerExportBuilder<T extends ServerExporter, C extends ServerExportBuilder<T, C>>
        implements ChainBuilder<T, C>, Parameterization {

    /**
     * Group name for the server export.
     */
    @Parameter(KeyConstant.GROUP)
    protected String group = Constant.DEFAULT_GROUP;

    /**
     * Version of the server export.
     */
    @Parameter(KeyConstant.VERSION)
    protected String version = Constant.DEFAULT_VERSION;

    /**
     * Weight of the server export (used for load balancing).
     */
    @Parameter(KeyConstant.WEIGHT)
    protected int weight = Constant.DEFAULT_WEIGHT;

    /**
     * Address where the server is exported.
     */
    protected InetSocketAddress exportedAddress;

    /**
     * Configuration for the server.
     */
    protected ServerConfig serverConfig;

    /**
     * The portal associated with the exporter.
     */
    protected Portal portal;

    /**
     * Sets the server configuration for the exporter.
     *
     * @param serverConfig The configuration for the server.
     * @return The current builder instance for method chaining.
     */
    public C serverConfig(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
        return returnChain();
    }

    /**
     * Sets the portal for the exporter.
     *
     * @param portal The portal that will be associated with the exporter.
     * @return The current builder instance for method chaining.
     */
    public C portal(Portal portal) {
        this.portal = portal;
        return returnChain();
    }

    /**
     * Sets the group for the server export.
     *
     * @param group Group name.
     * @return This builder.
     */
    public C group(String group) {
        this.group = group;
        return returnChain();
    }

    /**
     * Sets the version for the server export.
     *
     * @param version Version string.
     * @return This builder.
     */
    public C version(String version) {
        this.version = version;
        return returnChain();
    }

    /**
     * Sets the weight for the server export (used for load balancing).
     *
     * @param weight Weight value.
     * @return This builder.
     */
    public C weight(int weight) {
        this.weight = weight;
        return returnChain();
    }

    /**
     * Sets the exported address from a string representation (e.g., "127.0.0.1:8080").
     *
     * @param exportedAddress String representation of the exported address.
     * @return This builder.
     */
    public C exportedAddress(String exportedAddress) {
        return exportedAddress(NetUtil.toInetSocketAddress(exportedAddress));
    }

    /**
     * Sets the exported address using an IP and port.
     *
     * @param ip   IP address.
     * @param port Port number.
     * @return This builder.
     */
    public C exportedAddress(String ip, int port) {
        if (NetUtil.isValidIP(ip) && NetUtil.isValidPort(port)) {
            return exportedAddress(NetUtil.toAddress(ip, port));
        }
        return returnChain();
    }

    /**
     * Sets the exported address using an {@link InetSocketAddress}.
     *
     * @param address {@link InetSocketAddress} for the exported address.
     * @return This builder.
     */
    public C exportedAddress(InetSocketAddress address) {
        this.exportedAddress = address;
        return returnChain();
    }

    /**
     * Sets the exported address using a port number and automatically determines the IP address.
     *
     * @param port Port number.
     * @return This builder.
     * @throws IllegalArgumentException if the port is invalid.
     */
    public C exportedPort(int port) {
        if (NetUtil.isValidPort(port)) {
            String configIp = System.getProperty(SystemKey.LOCAL_IP);
            String ip = StringUtil.isBlankOrDefault(configIp, NetUtil.defaultHost());
            return exportedAddress(ip, port);
        }
        throw new IllegalArgumentException("Invalid port:" + port);
    }
}

