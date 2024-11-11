package event;

import io.effi.rpc.event.disruptor.DisruptorEventDispatcher;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author WenBo Zhou
 * @Date 2024/4/18 16:15
 */
public class EventTest {

    @Test
    public void test() throws IOException {
        DisruptorEventDispatcher dispatcher = new DisruptorEventDispatcher();
        dispatcher.registerListener(Event1.class, new Event1listener());

        ExecutorService executorService = Executors.newFixedThreadPool(200);
        for (int i = 0; i < 200; i++) {
            int finalI = i;
            executorService.execute(() -> {
                dispatcher.publish(new Event1("hello " + finalI));
            });
        }
        int read = System.in.read();
    }

}
