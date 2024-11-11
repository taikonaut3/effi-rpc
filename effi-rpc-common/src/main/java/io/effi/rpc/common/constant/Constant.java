package io.effi.rpc.common.constant;

import io.effi.rpc.common.util.NetUtil;

import static io.effi.rpc.common.constant.Component.*;

/**
 * Default constant.
 */
public interface Constant {

    String EXTENSION_NAME = "io.effi.rpc.common.extension.spi.Extension";

    int DEFAULT_IO_THREADS = Math.min(Runtime.getRuntime().availableProcessors() + 1, 32);

    int DEFAULT_CPU_THREADS = Runtime.getRuntime().availableProcessors();

    int DEFAULT_MAX_IO_THREADS = Runtime.getRuntime().availableProcessors() * 5;

    int DEFAULT_MAX_CPU_THREADS = Runtime.getRuntime().availableProcessors() * 2;

    int DEFAULT_CONNECT_TIMEOUT = 6 * 1000;

    int DEFAULT_MAX_CONNECT_TIMEOUT = 10 * 1000;

    String DEFAULT_ADDRESS = NetUtil.toAddress(NetUtil.defaultHost(), -1);

    int DEFAULT_TIMEOUT = 3000;

    int DEFAULT_KEEP_ALIVE_TIMEOUT = 6000;

    int DEFAULT_MAX_UN_CONNECTIONS = 1024;

    int DEFAULT_MAX_CONCURRENT_STREAMS = 100;

    int DEFAULT_INITIAL_WINDOW_SIZE = 65535;

    int DEFAULT_MAX_HEADER_TABLE_SIZE = 16 * 1024;

    int DEFAULT_MAX_HEADER_LIST_SIZE = 8192;
    int DEFAULT_MAX_FRAME_SIZE = 16 * 1024;

    int DEFAULT_SESSION_TIMEOUT = 60 * 1000;

    int DEFAULT_INTERVAL = 1000;

    int DEFAULT_HEALTH_CHECK_INTERVAL = 5000;

    int DEFAULT_RETRIES = 3;

    int DEFAULT_CAPACITY = Runtime.getRuntime().availableProcessors() * 100;

    int DEFAULT_KEEPALIVE = 60;

    int DEFAULT_PROTOCOL_PORT = 2333;

    int DEFAULT_HTTP_PORT = 9090;

    int DEFAULT_MAX_MESSAGE_SIZE = 1024 * 32;

    String DEFAULT_SERIALIZATION = Serialization.KRYO;

    int DEFAULT_MAX_HEADER_SIZE = 10000;

    int DEFAULT_BUFFER_SIZE = 512;

    int DEFAULT_SUBSCRIBES = DEFAULT_CPU_THREADS;

    int DEFAULT_SPARE_CLOSE_TIMES = 3;

    int DEFAULT_CLIENT_MAX_CONNECTIONS = 3;

    String DEFAULT_VERSION = "1.0.0";

    String DEFAULT_GROUP = DEFAULT;

    int DEFAULT_WEIGHT = 0;

    String DEFAULT_ROUTER = DEFAULT;

    String DEFAULT_SERVICE_DISCOVERY = DEFAULT;

    String DEFAULT_PROXY = ProxyFactory.JDK;

    String DEFAULT_REGISTRY = Registry.CONSUL;

    String DEFAULT_FAULT_TOLERANCE = FaultTolerance.FAIL_FAST;

    String DEFAULT_LOAD_BALANCE = LoadBalance.ROUND_ROBIN;

    String DEFAULT_EVENT_DISPATCHER = EventDispatcher.DISRUPTOR;

    String DEFAULT_TRANSPORTER = Transport.NETTY;

    String SPI_FIX_PATH = "META-INF/services/";

    String INTERNAL_CERTS_PATH = "META-INF/virtue/internal/certs/";

    String PENDING_ADDRESS = "pending.address";

    String ENABLED_ADDRESS = "enabled.address";

    String DEFAULT_COMPRESSION = Compression.GZIP;

}
