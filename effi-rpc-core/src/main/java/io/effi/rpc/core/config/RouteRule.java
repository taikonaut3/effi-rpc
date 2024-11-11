package io.effi.rpc.core.config;

import io.effi.rpc.common.url.URL;
import io.effi.rpc.core.Invocation;

import java.util.List;

/**
 * Defines a routing rule to filter and select target URLs that meet specified invocation conditions.
 */
@FunctionalInterface
public interface RouteRule {

    /**
     * Filters the list of URLs based on invocation details, returning URLs that meet the criteria.
     *
     * @param invocation the invocation context with request parameters and metadata
     * @param urls       the list of available URLs to be filtered
     * @return a list of URLs that match the routing rule; returns an empty list if none match
     */
    List<URL> execute(Invocation<?> invocation, List<URL> urls);
}

