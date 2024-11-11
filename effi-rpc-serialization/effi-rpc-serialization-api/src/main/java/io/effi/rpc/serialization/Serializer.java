package io.effi.rpc.serialization;

import io.effi.rpc.common.extension.spi.Extensible;

import java.io.IOException;
import java.lang.reflect.Type;

import static io.effi.rpc.common.constant.Component.Serialization.KRYO;

/**
 * Serializing and deserializing objects.
 */
@Extensible(KRYO)
public interface Serializer {

    /**
     * Serializes the given object into a byte array.
     *
     * @param input The object to serialize.
     * @return A byte array representing the serialized object.
     * @throws IOException if an error occurs during serialization.
     */
    byte[] serialize(Object input) throws IOException;

    /**
     * Deserializes a byte array into an object of the specified type.
     *
     * @param bytes The byte array to deserialize.
     * @param type  The class of the object to deserialize.
     * @param <T>   The type of the object to deserialize.
     * @return The deserialized object of the specified type.
     * @throws IOException if an error occurs during deserialization.
     */
    <T> T deserialize(byte[] bytes, Type type) throws IOException;
}


