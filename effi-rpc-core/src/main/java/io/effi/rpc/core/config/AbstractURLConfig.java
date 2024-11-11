package io.effi.rpc.core.config;

import io.effi.rpc.common.url.URL;

/**
 * Abstract base class for URL config.
 */
public abstract class AbstractURLConfig implements URLConfig {

    protected String name;

    protected URL url;

    public AbstractURLConfig(String name, URL url) {
        this.name = name;
        this.url = url;
    }

    @Override
    public URL url() {
        return url;
    }

    @Override
    public String name() {
        return name;
    }

}
