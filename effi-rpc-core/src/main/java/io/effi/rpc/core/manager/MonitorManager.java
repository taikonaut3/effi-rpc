package io.effi.rpc.core.manager;

import io.effi.rpc.core.RemoteCaller;
import io.effi.rpc.core.RemoteService;

import java.util.ArrayList;
import java.util.List;

/**
 * Monitor Manager.
 */
public class MonitorManager {

    private List<RemoteService<?>> remoteServices = new ArrayList<>();

    private List<RemoteCaller<?>> remoteCallers = new ArrayList<>();

}
