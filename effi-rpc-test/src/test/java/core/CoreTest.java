package core;

import io.effi.rpc.core.Portal;
import io.effi.rpc.core.manager.Environment;
import io.effi.rpc.internal.logging.Logger;
import io.effi.rpc.internal.logging.LoggerFactory;
import org.junit.jupiter.api.Test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author WenBo Zhou
 * @Date 2024/3/19 16:14
 */
public class CoreTest {

    private final Map<String, String> map = new ConcurrentHashMap<>();

    public static boolean match(String str, String pattern) {
        // 将模式中的 {} 替换为正则表达式的匹配规则，其中 \\{([^{}]*)\\} 表示匹配 { 和 } 之间的任意内容
        String regex = pattern.replaceAll("\\{([^{}]*)\\}", "([^/]+)")
                .replaceAll("\\{([^{}]*)$", "\\\\$0")
                .replaceAll("(?<!\\})\\}$", "\\\\$0");

        // 使用正则表达式进行匹配
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    @Test
    public void test1() {
        Environment environment = new Environment();
        environment.set("name", "zwb")
                .set("abc", "123");
        String input = "dasdada-${name}-fasd-${name}-asda${abc}----${abc}-------${123}";
        System.out.println(environment.replace(input));
    }

    @Test
    public void test2() {
        ExecutorService executorService = Executors.newFixedThreadPool(200);
        for (int i = 0; i < 1000; i++) {
            executorService.execute(() -> {
                System.out.println(get("111"));
            });
        }
    }

    @Test
    public void test3() {
        Logger logger = LoggerFactory.getLogger(Portal.class);
        System.out.println(logger);
    }

    @Test
    public void test4() {
        TypeToken<List<String>> listTypeToken = TypeToken.<List<String>>get();
        System.out.println(listTypeToken);

    }

    private String get(String key) {
        String value = map.get(key);
        if (value == null) {
            synchronized (this) {
                if (map.get(key) == null) {
                    value = "111";
                    map.put(key, value);
                }
            }
        }
        return value;
    }

    public static abstract class TypeToken<T> {
        private final Type type;

        // 构造函数：使用匿名类捕获泛型类型
        protected TypeToken() {
            this.type = getSuperclassTypeParameter();
        }

        public Type type() {
            return type;
        }

        /**
         * 静态方法，用于生成 TypeToken 实例，并捕获泛型类型。
         *
         * @param <T> 泛型类型
         * @return 捕获的 TypeToken
         */
        public static <T> TypeToken<T> get() {
            return new TypeToken<T>() {
            };
        }

        /**
         * 获取父类的泛型参数
         */
        private Type getSuperclassTypeParameter() {
            Type superclass = getClass().getGenericSuperclass();
            if (superclass instanceof ParameterizedType) {
                return ((ParameterizedType) superclass).getActualTypeArguments()[0];
            } else {
                throw new IllegalArgumentException("Invalid TypeToken construction.");
            }
        }
    }

}
