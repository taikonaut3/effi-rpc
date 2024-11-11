package serialization;

import io.effi.rpc.metrics.CallerMetrics;
import io.effi.rpc.serialization.json.JacksonSerializer;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * @Author WenBo Zhou
 * @Date 2024/5/31 14:45
 */
public class SerializationTest {

    @Test
    public void jsonTest() throws IOException {
        JacksonSerializer jacksonSerializer = new JacksonSerializer();
        byte[] bytes = jacksonSerializer.serialize(new CallerMetrics());
        String str = new String(bytes);
        System.out.println(str);
    }
}
