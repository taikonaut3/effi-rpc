package io.effi.rpc.core;

import io.effi.rpc.common.extension.spi.Extensible;

/**
 * Ability to scale over the life cycle of Portal.
 */
@Extensible(lazyLoad = false)
public interface PortalConfiguration {

    /**
     * Portal init before.
     *
     * @param portal
     */
    default void preInit(Portal portal) {

    }

    /**
     * Portal init after.
     *
     * @param portal
     */
    default void postInit(Portal portal) {

    }

    /**
     * Portal start before.
     *
     * @param portal
     */
    default void preStart(Portal portal) {

    }

    /**
     * Portal start after.
     *
     * @param portal
     */
    default void postStart(Portal portal) {

    }

    /**
     * Portal stop before.
     *
     * @param portal
     */
    default void preStop(Portal portal) {
    }

    /**
     * Portal stop after.
     *
     * @param portal
     */
    default void postStop(Portal portal) {
    }

}
