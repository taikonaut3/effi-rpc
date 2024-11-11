package io.effi.rpc.serialization;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Abstract implementation of {@link Serializer}.
 */
public abstract class AbstractSerializer implements Serializer {

    @Override
    public byte[] serialize(Object input) throws IOException {
        if (input == null) {
            return new byte[0];
        }
        try {
            return doSerialize(input);
        } catch (Exception e) {
            throw new IOException("Failed to serialize object " + input.getClass(), e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T deserialize(byte[] bytes, Type type) throws IOException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        try {
            return (T) doDeserialize(bytes, type);
        } catch (Exception e) {
            throw new IOException("Failed to deserialize bytes to " + type, e);
        }
    }

    protected abstract byte[] doSerialize(Object input) throws Exception;

    protected abstract Object doDeserialize(byte[] bytes, Type type) throws Exception;

}

