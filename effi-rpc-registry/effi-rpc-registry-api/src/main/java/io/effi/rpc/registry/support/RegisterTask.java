package io.effi.rpc.registry.support;

import io.effi.rpc.common.extension.spi.ExtensionLoader;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.registry.MetaDataRegister;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * Register service task.
 */
@Getter
@Accessors(fluent = true)
public class RegisterTask implements Runnable {

    private static final List<MetaDataRegister> REGISTER_META_DATA = ExtensionLoader.loadExtensions(MetaDataRegister.class);

    private final URL url;

    private final BiConsumer<RegisterTask, Map<String, String>> task;

    @Setter
    private boolean isFirstRun = true;

    public RegisterTask(URL url, BiConsumer<RegisterTask, Map<String, String>> task) {
        this.url = url;
        this.task = task;
        run();
    }

    @Override
    public void run() {
        Map<String, String> metaData = new HashMap<>();
        REGISTER_META_DATA.forEach(metaDataRegister -> metaDataRegister.process(url, metaData));
        task.accept(this, metaData);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
