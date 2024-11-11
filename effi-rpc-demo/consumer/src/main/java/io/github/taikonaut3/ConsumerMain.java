package io.github.taikonaut3;

import io.effi.rpc.boot.EnableViceroy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableViceroy(scanBasePackages = "io.github.taikonaut3")
public class ConsumerMain {
    public static void main(String[] args) throws Exception {
//        ConfigurableApplicationContext context = SpringApplication.run(ConsumerMain.class, args);
//        Consumer consumer = context.getBean("consumer", Consumer.class);
//        ExecutorService executorService = Executors.newFixedThreadPool(5);
//        for (int i = 0; i < 10; i++) {
//            executorService.execute(() -> {
//                Message message = new Message();
//                message.setDate(new Date());
//                message.setName("client" + message.getDate().toString());
//                Message message1 = consumer.exchangeMessage(message);
//                System.out.println(message1);
//            });
//
//        }
        SpringApplication.run(ConsumerMain.class, args);
        //simpleTest();
    }

//    public static void simpleTest() throws Exception {
//        Virtue virtue = Virtue.getDefault();
//        virtue.wrap("provider",new Provider());
//        Consumer consumer = virtue.application(new ApplicationConfig("consumer"))
//                .register(new RegistryConfig("consul://127.0.0.1:8500"))
//                .register("filter1", new Filter1())
//                .register("filter2", new Filter2())
//                .register("testFilter", new TestFilter(virtue))
//                .register("callerResultFilter", new CallerResultFilter(virtue))
//                .router("^virtue://.*/345/list", ":2333")
//                //.register(new RegistryConfig("nacos://127.0.0.1:8848"))
//                .proxy(Consumer.class)
//        virtue.start();
//        //List<ParentObject> httplist = consumer.http2Test(ParentObject.getObjList("client list"));
//        Message message = new Message();
//        message.setDate(new Date());
//        message.setName("client" + message.getDate().toString());
//        Message message1 = consumer.exchangeMessage(message);
//        System.out.println(message1);
//        ExecutorService executorService = Executors.newFixedThreadPool(200);
//        for (int i = 0; i < 200; i++) {
//            executorService.execute(() -> {
//                long start = System.currentTimeMillis();
//                List<ParentObject> list = consumer.list(ParentObject.getObjList("client list 1"), ParentObject.getObjList("client list 2"));
//                long end = System.currentTimeMillis();
//                System.out.println("virtue 耗时:" + (end - start) + list);
//                start = System.currentTimeMillis();
//                //List<ParentObject> clientList = consumer.http2Test(ParentObject.getObjList("client list"));
//                end = System.currentTimeMillis();
//                //System.out.println("h2 耗时:" + (end - start) + clientList);
//            });
//        }
//        String hello = consumer.hello("world");
//        String hello1 = consumer.hello("world2");
//        System.out.println(hello + hello1);
//        CompletableFuture<String> future = consumer.helloAsync("worldAsync");
//        String s = null;
//        try {
//            s = future.get();
//        } catch (InterruptedException | ExecutionException e) {
//            throw new RuntimeException(e);
//        }
//        System.out.println(s);
//        System.in.read();
//    }



//    @Bean
//    public RegistryConfig nacosRegistryConfig() {
//        RegistryConfig registryConfig = new RegistryConfig();
//        registryConfig.type(NACOS);
//        registryConfig.host("127.0.0.1");
//        registryConfig.port(8848);
//        return registryConfig;
//    }


}