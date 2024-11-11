package io.effi.rpc.core.utils;

import io.effi.rpc.common.constant.KeyConstant;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.core.Portal;

/**
 * Utility class for portal.
 */
public class PortalUtil {

    /**
     * Sets the portal object in the URL and adds relevant portal parameters.
     *
     * @param url    the URL to update with portal details
     * @param portal the portal to set in the URL
     */
    public static void setPortal(URL url, Portal portal) {
        url.set(Portal.KEY, portal);
        url.addParam(KeyConstant.PORTAL, portal.name());
        url.addParam(KeyConstant.SERVICE_NAME, portal.name());
    }

    /**
     * Retrieves the portal associated with the URL.
     *
     * @param url the URL to retrieve the portal from
     * @return the portal associated with the URL
     */
    public static Portal acquirePortal(URL url) {
        return url.get(Portal.KEY);
    }
}

