package io.github.taikonaut3.provider.api;

import io.effi.rpc.common.url.URL;
import io.effi.rpc.core.Callee;
import io.effi.rpc.core.Portal;
import io.effi.rpc.core.RemoteService;
import io.effi.rpc.core.ServerExporter;
import io.effi.rpc.core.arg.Body;
import io.effi.rpc.core.arg.DefaultMethodMapper;
import io.effi.rpc.core.config.ServerConfig;
import io.effi.rpc.protocol.http.h2.Http2Callee;
import io.effi.rpc.protocol.http.h2.Http2ServerConfig;
import io.effi.rpc.protocol.support.CombineRemoteService;
import io.effi.rpc.protocol.support.DefaultPortal;
import io.effi.rpc.protocol.support.DefaultRegistryConfig;
import io.effi.rpc.protocol.support.DefaultServerExporter;
import io.github.taikonaut3.Service;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import static io.effi.rpc.common.constant.Component.Serialization.JSON;

public class Http2Provider {

    public static void main(String[] args) throws IOException {
        // 创建 Scanner 对象，用于从控制台读取输入
        Scanner scanner = new Scanner(System.in);
        // 提示用户输入
        while (true) {
            System.out.print("input: ");
            String line = scanner.nextLine();
            if ("start".equals(line)) {
            } else if ("stop".equals(line)) {
            } else if ("url".equals(line)) {
                URL url = URL.valueOf("http://127.0.0.1/path/llll/?aaaa=111&bbb=222&ccc=333");
                System.out.println(url);
            } else if ("export".equals(line)) {
                export();
            }

        }

    }

    public static void export() {
        ServerConfig h2ServerConfig = Http2ServerConfig.builder()
                .initialWindowSize(65535 * 50)
                .maxConcurrentStreams(1000)
                .build();
        Portal portal = DefaultPortal.builder().build().name("provider");
        DefaultRegistryConfig consulConfig = DefaultRegistryConfig
                .builder()
                .url("consul://127.0.0.1:8500")
                .build();
        DefaultRegistryConfig naocsConfig = DefaultRegistryConfig.builder()
                .url("nacos://127.0.0.1:8848").build();
//        portal.configManager()
//                .registryConfigManager()
//                .registerShared(consulConfig);
        ServerExporter serverExporter = DefaultServerExporter.builder()
                .serverConfig(h2ServerConfig)
                .exportedPort(8090)
                .portal(portal)
                .build();
        Callee<?> callee = createCallee();
        serverExporter.callee(callee);
        serverExporter.registry(consulConfig);
        serverExporter.export();
        portal.start();
    }

    public static Callee<?> createCallee() {
        RemoteService<Service> remoteService = new CombineRemoteService<>("service", new Service());
        DefaultMethodMapper<Service> methodMapper = DefaultMethodMapper
                .builder(remoteService, "list2")
                .parameterType(List.class)
                .mappedParameterType(List.class, Body.IDENTIFY)
                .build();
        return Http2Callee
                .builder(methodMapper)
                .serialization(JSON)
                .build();
    }
}
