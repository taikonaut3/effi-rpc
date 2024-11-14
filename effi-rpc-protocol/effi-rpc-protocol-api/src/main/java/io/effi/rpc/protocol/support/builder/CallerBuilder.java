package io.effi.rpc.protocol.support.builder;

import io.effi.rpc.common.constant.Constant;
import io.effi.rpc.common.constant.KeyConstant;
import io.effi.rpc.common.extension.TypeToken;
import io.effi.rpc.common.url.Parameter;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.common.url.URLBuilder;
import io.effi.rpc.common.url.URLType;
import io.effi.rpc.core.Caller;
import io.effi.rpc.core.Locator;
import io.effi.rpc.core.Portal;
import io.effi.rpc.core.config.ClientConfig;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * Builder for creating {@link Caller} instances,defining settings for caller.
 *
 * @param <T> The type of {@link Caller}.
 * @param <C> The type of this builder.
 */
@Getter
@Accessors(fluent = true)
public abstract class CallerBuilder<T extends Caller<?>, C extends CallerBuilder<T, C>>
        extends InvokerBuilder<T, C> {

    /**
     * Service locator for finding available services.
     */
    protected Locator locator;

    /**
     * Configuration for the client connection.
     */
    protected ClientConfig clientConfig;

    /**
     * Entry point for network communication.
     */
    protected Portal portal;

    /**
     * Service group identifier.
     */
    @Parameter(KeyConstant.GROUP)
    protected String group = Constant.DEFAULT_GROUP;

    /**
     * Retry count for failed calls.
     */
    @Parameter(KeyConstant.RETRIES)
    protected int retries = Constant.DEFAULT_RETRIES;

    /**
     * Load balancing strategy for service selection.
     */
    @Parameter(KeyConstant.LOAD_BALANCE)
    protected String loadBalance = Constant.DEFAULT_LOAD_BALANCE;

    /**
     * Method for discovering services.
     */
    @Parameter(KeyConstant.SERVICE_DISCOVERY)
    protected String serviceDiscovery = Constant.DEFAULT_SERVICE_DISCOVERY;

    /**
     * Routing strategy for determining service paths.
     */
    @Parameter(KeyConstant.ROUTER)
    protected String router = Constant.DEFAULT_ROUTER;

    /**
     * Strategy for handling call failures.
     */
    @Parameter(KeyConstant.FAULT_TOLERANCE)
    protected String faultTolerance = Constant.DEFAULT_FAULT_TOLERANCE;

    /**
     * Maximum time to wait for a call, in milliseconds.
     */
    @Parameter(KeyConstant.TIMEOUT)
    protected int timeout = Constant.DEFAULT_TIMEOUT;

    /**
     * Specifies if the call is one-way (no response expected).
     */
    @Parameter(KeyConstant.ONEWAY)
    protected boolean oneWay;

    /**
     * Constructs a builder with a specified return type.
     *
     * @param returnType
     */
    protected CallerBuilder(TypeToken<?> returnType) {
        this.returnType = returnType;
    }

    /**
     * Sets the communication portal.
     *
     * @param portal Portal instance to use.
     * @return This builder.
     */
    public C portal(Portal portal) {
        this.portal = portal;
        return returnChain();
    }

    /**
     * Sets client configuration.
     *
     * @param clientConfig Client configuration to apply.
     * @return This builder.
     */
    public C clientConfig(ClientConfig clientConfig) {
        this.clientConfig = clientConfig;
        return returnChain();
    }

    /**
     * Sets service locator.
     *
     * @param locator Locator instance to use.
     * @return This builder.
     */
    public C locator(Locator locator) {
        this.locator = locator;
        return returnChain();
    }

    /**
     * Sets service group.
     *
     * @param group Group identifier.
     * @return This builder.
     */
    public C group(String group) {
        this.group = group;
        return returnChain();
    }

    /**
     * Sets retry attempts.
     *
     * @param retries Number of retries.
     * @return This builder.
     */
    public C retries(int retries) {
        this.retries = retries;
        return returnChain();
    }

    /**
     * Sets load balancing strategy.
     *
     * @param loadBalance Load balancing strategy.
     * @return This builder.
     */
    public C loadBalance(String loadBalance) {
        this.loadBalance = loadBalance;
        return returnChain();
    }

    /**
     * Sets service discovery method.
     *
     * @param serviceDiscovery Service discovery method.
     * @return This builder.
     */
    public C serviceDiscovery(String serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
        return returnChain();
    }

    /**
     * Sets routing strategy.
     *
     * @param router Routing strategy to use.
     * @return This builder.
     */
    public C router(String router) {
        this.router = router;
        return returnChain();
    }

    /**
     * Sets fault tolerance strategy.
     *
     * @param faultTolerance Fault tolerance method.
     * @return This builder.
     */
    public C faultTolerance(String faultTolerance) {
        this.faultTolerance = faultTolerance;
        return returnChain();
    }

    /**
     * Sets call timeout.
     *
     * @param timeout Timeout in milliseconds.
     * @return This builder.
     */
    public C timeout(int timeout) {
        this.timeout = timeout;
        return returnChain();
    }

    /**
     * Specifies if the call is one-way.
     *
     * @param oneWay True for one-way call; false otherwise.
     * @return This builder.
     */
    public C oneWay(boolean oneWay) {
        this.oneWay = oneWay;
        return returnChain();
    }

    @Override
    protected URL buildUrl() {
        URLBuilder urlBuilder = URL.builder()
                .type(URLType.CALLER)
                .protocol(protocol())
                .address(Constant.DEFAULT_ADDRESS)
                .params(parameterization());
        if (queryPath != null) {
            urlBuilder.paths(queryPath.paths());
        }
        return urlBuilder.build();
    }
}


