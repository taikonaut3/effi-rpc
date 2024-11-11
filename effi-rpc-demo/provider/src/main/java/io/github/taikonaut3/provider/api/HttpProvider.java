package io.github.taikonaut3.provider.api;

import io.effi.rpc.core.RemoteService;
import io.effi.rpc.core.ServerExporter;
import io.effi.rpc.core.arg.Body;
import io.effi.rpc.core.arg.DefaultMethodMapper;
import io.effi.rpc.core.arg.MethodMapper;
import io.effi.rpc.protocol.http.h1.Http1Callee;
import io.effi.rpc.protocol.support.*;
import io.github.taikonaut3.Service;

import java.io.IOException;
import java.util.List;

import static io.effi.rpc.common.constant.Component.Serialization.JSON;

public class HttpProvider {

    public static void main(String[] args) throws IOException {
        DefaultPortal portal = DefaultPortal.builder().build();
        portal.name("provider");
        DefaultServerConfig serverConfig = DefaultServerConfig
                .builder()
                .ssl(true)
                .build();
        RemoteService<Service> remoteService = new CombineRemoteService<>("service", new Service());
        MethodMapper<Service> methodMapper = DefaultMethodMapper
                .builder(remoteService, "list2")
                .parameterType(List.class)
                .mappedParameterType(List.class, Body.IDENTIFY)
                .build();
        Http1Callee<Service> callee = Http1Callee.builder(methodMapper)
                .path("hello-list2")
                .serialization(JSON)
                .build();
        ServerExporter serverExporter = DefaultServerExporter.builder()
                .portal(portal)
                .serverConfig(serverConfig)
                .exportedPort(8080)
                .build();
        serverExporter.callee(callee);
        serverExporter.registry(DefaultRegistryConfig.builder().url("consul://127.0.0.1:8500").build());
        portal.start();
    }
}
