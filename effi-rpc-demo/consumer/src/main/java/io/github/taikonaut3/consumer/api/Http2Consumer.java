package io.github.taikonaut3.consumer.api;

import io.effi.rpc.common.extension.TypeToken;
import io.effi.rpc.core.arg.Argument;
import io.effi.rpc.core.arg.Body;
import io.effi.rpc.core.arg.ParamVar;
import io.effi.rpc.metrics.filter.CallerMetricsFilter;
import io.effi.rpc.protocol.http.h2.Http2Caller;
import io.effi.rpc.protocol.http.h2.Http2CallerBuilder;
import io.effi.rpc.protocol.http.h2.Http2ClientConfig;
import io.effi.rpc.protocol.support.DefaultPortal;
import io.effi.rpc.protocol.support.DefaultRegistryConfig;
import io.effi.rpc.protocol.support.RegistryLocator;
import io.effi.rpc.transport.http.HttpMethod;
import org.example.model1.ParentObject;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static io.effi.rpc.common.constant.Component.Serialization.JSON;

public class Http2Consumer {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        DefaultPortal portal = DefaultPortal.builder().build();
        portal.registerShared(DefaultRegistryConfig.builder().url("consul://127.0.0.1:8500").build());
        Http2ClientConfig clientConfig = Http2ClientConfig.builder()
                .maxConnections(1)
                .initialWindowSize(65535 * 50)
                .maxConcurrentStreams(1000)
                .build();
        Http2CallerBuilder<List<ParentObject>> builder = Http2Caller.builder(new TypeToken<>() {});
        Http2Caller<List<ParentObject>> caller = builder
                .method(HttpMethod.POST)
                .serialization(JSON)
                .path("service/list2")
                .portal(portal)
                .clientConfig(clientConfig)
                .locator(new RegistryLocator("provider"))
                .addFilter(new CallerMetricsFilter(portal))
                //.directUrl("127.0.0.1:8088")
                .build();
        List<org.example.model2.ParentObject> list1 = org.example.model2.ParentObject.getObjList("list1");
        List<org.example.model2.ParentObject> list2 = org.example.model2.ParentObject.getObjList("list2");
        ParamVar<Argument.Target> paramVar = ParamVar.target(Map.of("zzz", "123456哈哈哈哈"));
        ExecutorService executorService = Executors.newFixedThreadPool(200);
        for (int i = 0; i < 1000; i++) {
            executorService.execute(() -> {
                List<ParentObject> list = caller.blockingCall(Body.wrap(list1), list2, paramVar);
                System.out.println(list);
            });
        }
    }
}
