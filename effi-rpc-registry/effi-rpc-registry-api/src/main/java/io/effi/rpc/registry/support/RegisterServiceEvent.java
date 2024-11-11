package io.effi.rpc.registry.support;

import io.effi.rpc.common.url.URL;
import io.effi.rpc.core.Portal;
import io.effi.rpc.core.utils.PortalUtil;
import io.effi.rpc.event.AbstractEvent;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * RegisterService Event.
 */
@Getter
@Accessors(fluent = true)
public class RegisterServiceEvent extends AbstractEvent<RegisterTask> {

    private final URL url;

    private final Portal portal;

    public RegisterServiceEvent(URL url, RegisterTask runnable) {
        super(runnable);
        this.url = url;
        this.portal = PortalUtil.acquirePortal(url);
    }
}
