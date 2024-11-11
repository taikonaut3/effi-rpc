package io.effi.rpc.common.constant;

import io.effi.rpc.common.extension.AttributeKey;
import io.effi.rpc.common.url.URL;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Key constant.
 */
public interface KeyConstant {

    String UNIQUE_ID = "uniqueId";

    String NAME = "name";

    String SERVICE_NAME = "serviceName";

    String SERVICE = "service";

    String PROTOCOL = "protocol";

    String BUFFER_SIZE = "bufferSize";

    String ONEWAY = "oneway";

    String RETRY_INTERVAL = "retryInterval";

    String HEALTH_CHECK_INTERVAL = "healthCheckInterval";

    String GROUP = "group";

    String VERSION = "version";

    String WEIGHT = "weight";

    String TIMEOUT = "timeout";


    String KEEP_ALIVE_TIMEOUT = "keepAliveTimeout";

    String SPARE_CLOSE_TIMES = "spareCloseTimes";

    String HEARTBEAT_LOG_ENABLE = "heartbeatLogEnable";

    String SO_BACKLOG = "soBacklog";

    String MAX_CONNECTIONS = "maxConnections";

    String MAX_UN_CONNECTIONS = "maxUnConnections";

    String MAX_THREADS = "maxThreads";

    String PROXY = "proxy";

    String TRANSPORTER = "transporter";

    String REQUEST_CONTEXT = "requestContext";

    String RESPONSE_CONTEXT = "responseContext";


    String SERIALIZATION = "serialization";

    String LOAD_BALANCE = "loadBalance";

    String SERVICE_DISCOVERY = "serviceDiscovery";

    String ROUTER = "router";

    String RETRIES = "retries";

    String REQUEST = "request";

    String RESPONSE = "response";

    String FAULT_TOLERANCE = "faultTolerance";

    String TIMESTAMP = "timestamp";

    String MAX_RECEIVE_SIZE = "maxReceiveSize";

    String CLIENT_MAX_RECEIVE_SIZE = "clientMaxReceiveSize";

    String MAX_HEADER_SIZE = "maxHeaderSize";

    String SSL = "ssl";

    String CONNECT_TIMEOUT = "connectTimeout";

    String COMPRESSION = "compression";

    String KEEPALIVE = "keepAlive";

    String GLOBAL = "global";

    String URL = "url";

    String ENVELOPE = "envelope";

    String RESPONSE_CODE = "responseCode";


    String PASSWORD = "password";

    String ENABLE_HEALTH_CHECK = "enableHealthCheck";

    String USERNAME = "username";

    String SESSION_TIMEOUT = "sessionTimeout";


    String HTTP_METHOD = "httpMethod";

    String PORTAL = "portal";

    String HEADER_TABLE_SIZE = "headerTableSize";

    String PUSH_ENABLED = "pushEnabled";

    String MAX_CONCURRENT_STREAMS = "maxConcurrentStreams";

    String INITIAL_WINDOW_SIZE = "initialWindowSize";

    String MAX_FRAME_SIZE = "maxFrameSize";

    String MAX_HEADER_LIST_SIZE = "maxHeaderListSize";

    String SUBSCRIBES = "subscribes";

    String CA_PATH = "virtue.h2.ca.path";

    String CLIENT_CERT_PATH = "virtue.http.client.cert.path";

    String CLIENT_KEY_PATH = "virtue.http.client.key.path";

    String SERVER_CERT_PATH = "virtue.http.server.cert.path";

    String SERVER_KEY_PATH = "virtue.http.server.key.path";

    AttributeKey<AtomicInteger> ALL_IDLE_TIMES = AttributeKey.valueOf("allIdleTimes");

    AttributeKey<AtomicInteger> WRITE_IDLE_TIMES = AttributeKey.valueOf("writeIdleTimes");

    AttributeKey<AtomicInteger> READER_IDLE_TIMES = AttributeKey.valueOf("readeIdleTimes");

    AttributeKey<AtomicInteger> LAST_CALL_INDEX = AttributeKey.valueOf("lastCallIndex");

    AttributeKey<Throwable> CALL_EXCEPTION = AttributeKey.valueOf("callException");

    AttributeKey<URL> CALLER_URL = AttributeKey.valueOf("callerUrl");


}
