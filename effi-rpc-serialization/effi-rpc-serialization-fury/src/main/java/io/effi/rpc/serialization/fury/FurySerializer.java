package io.effi.rpc.serialization.fury;

import io.effi.rpc.common.constant.Component;
import io.effi.rpc.common.extension.spi.Extension;
import io.effi.rpc.serialization.AbstractSerializer;
import io.fury.Fury;
import io.fury.ThreadSafeFury;
import io.fury.config.Language;

import java.lang.reflect.Type;

/**
 * 测试出 fury的序列化并不理想.
 */
@Extension(Component.Serialization.FURY)
public class FurySerializer extends AbstractSerializer {

    private final ThreadSafeFury fury;

    public FurySerializer() {
        fury = Fury.builder().withLanguage(Language.JAVA)
                .requireClassRegistration(false)
                .buildThreadSafeFury();
    }

    @Override
    protected byte[] doSerialize(Object input) throws Exception {
        return fury.serialize(input);
    }

    @Override
    protected Object doDeserialize(byte[] bytes, Type type) throws Exception {
        return fury.deserialize(bytes);
    }

}
