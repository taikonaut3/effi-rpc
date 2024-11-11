package io.github.taikonaut3.consumer.api;

import io.effi.rpc.common.extension.TypeToken;
import io.effi.rpc.core.arg.Body;
import io.effi.rpc.protocol.http.h1.Http1Caller;
import io.effi.rpc.protocol.http.h1.Http1CallerBuilder;
import io.effi.rpc.protocol.support.DefaultClientConfig;
import io.effi.rpc.protocol.support.DefaultPortal;
import io.effi.rpc.protocol.support.DefaultRegistryConfig;
import io.effi.rpc.protocol.support.RegistryLocator;
import io.effi.rpc.transport.http.HttpMethod;
import org.example.model1.ParentObject;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static io.effi.rpc.common.constant.Component.Serialization.JSON;

public class HttpConsumer {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        DefaultPortal portal = DefaultPortal.builder().build();
        Http1CallerBuilder<List<ParentObject>> builder = Http1Caller.builder(new TypeToken<>() {});
        Http1Caller<List<ParentObject>> caller = builder
                .method(HttpMethod.POST)
                .serialization(JSON)
                .path("hello-list2?aaa=111")
                .portal(portal)
                .clientConfig(DefaultClientConfig.builder().ssl(true).build())
                .locator(new RegistryLocator("provider",
                        List.of(DefaultRegistryConfig.builder().url("consul://127.0.0.1:8500").build())))
                .build();
        List<org.example.model2.ParentObject> list1 = org.example.model2.ParentObject.getObjList("list1");
        List<org.example.model2.ParentObject> list2 = org.example.model2.ParentObject.getObjList("list2");
        DefaultClientConfig clientConfig = DefaultClientConfig.builder().build();
        ExecutorService executorService = Executors.newFixedThreadPool(200);
        for (int i = 0; i < 1000; i++) {
            executorService.execute(() -> {
                List<ParentObject> list = caller.blockingCall(clientConfig, Body.wrap(list1), list2);
                System.out.println(list);
            });
        }
    }
}
