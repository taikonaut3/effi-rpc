package io.effi.rpc.core.config;

import io.effi.rpc.common.constant.Constant;
import io.effi.rpc.common.url.Parameterization;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Configuration for current Viceroy.
 */
@Data
@Accessors(fluent = true, chain = true)
public class ApplicationConfig implements Parameterization {

    /**
     * The name of the application.
     */
    private String name;

    /**
     * The transport framework used by the application for communication.
     * The default value is {@link Constant#DEFAULT_TRANSPORTER}.
     */
    private String transport = Constant.DEFAULT_TRANSPORTER;

    /**
     * The router used by the application to manage routing of calls.
     * The default value is {@link Constant#DEFAULT_ROUTER}.
     */
    private String router = Constant.DEFAULT_ROUTER;

    /**
     * Configuration for event dispatching in the application.
     * This contains the settings for handling event-driven processes within the system.
     */
    private EventDispatcherConfig eventDispatcherConfig = new EventDispatcherConfig();

    /**
     * Default constructor.
     * Initializes the {@link ApplicationConfig} with default settings.
     */
    public ApplicationConfig() {
        // No additional initialization required, using default values.
    }

    /**
     * Constructs an {@link ApplicationConfig} with a specified application name.
     *
     * @param name the name of the application
     */
    public ApplicationConfig(String name) {
        this.name = name;
    }
}

