package io.effi.rpc.core.annotation;

import io.effi.rpc.common.constant.Constant;

import java.lang.annotation.*;

/**
 * Client reflect parameter configuration.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Options {

    /**
     * The group.
     */
    String group() default Constant.DEFAULT_GROUP;

    /**
     * The default is false,Unit: ms.
     */
    int timeout() default Constant.DEFAULT_TIMEOUT;

    /**
     * Used {@link io.effi.rpc.governance.discovery.ServiceDiscovery}.
     * Default is "default" {@link io.effi.rpc.governance.discovery.ServiceDiscovery}.
     */
    String serviceDiscovery() default Constant.DEFAULT_SERVICE_DISCOVERY;

    /**
     * Used {@link io.effi.rpc.governance.router.Router}.
     * Default is "default" {@link io.effi.rpc.governance.router.DefaultRouter}.
     */
    String router() default Constant.DEFAULT_ROUTER;

    /**
     * Used {@link io.effi.rpc.governance.loadbalance.LoadBalance}.
     * Default is "random" {@link io.effi.rpc.governance.loadbalance.RandomLoadBalance}.
     */
    String loadBalance() default Constant.DEFAULT_LOAD_BALANCE;

    /**
     * Used {@link io.effi.rpc.governance.faulttolerance.FaultTolerance}.
     * Default is "failFast" {@link io.effi.rpc.governance.faulttolerance.FailFast}.
     */
    String faultTolerance() default Constant.DEFAULT_FAULT_TOLERANCE;

    /**
     * If {@link Options#faultTolerance()} is "failRetry"{@link io.effi.rpc.governance.faulttolerance.FailRetry},
     * Number of retries called when an exception occurred.
     */
    int retires() default Constant.DEFAULT_RETRIES;

    /**
     * Precise direct connection to the url address.
     */
    String url() default "";

    /**
     * Determines whether to reuse the client configuration from the current protocol.
     */
    boolean multiplex() default true;

    /**
     * When {@link Options#multiplex()} is false,
     * Capable to configure tcp clients,Then a client will be created.
     */
    String client() default "";

    /**
     * Supports multiple registry configs.
     */
    String[] registries() default {};

}

