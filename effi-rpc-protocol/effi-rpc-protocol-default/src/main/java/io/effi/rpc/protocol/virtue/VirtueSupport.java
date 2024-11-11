package io.effi.rpc.protocol.virtue;

import io.effi.rpc.common.constant.KeyConstants;
import io.effi.rpc.common.extension.spi.ExtensionLoader;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.serialization.Serializer;
import io.effi.rpc.transport.compress.Compressor;
import io.effi.rpc.protocol.virtue.envelope.VirtueEnvelope;

import java.lang.reflect.Type;

public class VirtueSupport {

    public static final int MAGIC_REQ = 888;

    public static final int MAGIC_RES = 999;

    public static byte[] encodeBody(VirtueEnvelope virtueEnvelope) {
        Serializer serializer = virtueEnvelope.serializer();
        Compressor compressor = virtueEnvelope.compressor();
        byte[] bytes = serializer.serialize(virtueEnvelope.body());
        return compressor.compress(bytes);
    }

    public static Object decodeBody(URL url, byte[] bodyBytes, Type bodyType) {
        String serializationName = url.getParam(KeyConstants.SERIALIZATION);
        String compressionName = url.getParam(KeyConstants.COMPRESSION);
        Serializer serializer = ExtensionLoader.loadExtension(Serializer.class, serializationName);
        Compressor compressor = ExtensionLoader.loadExtension(Compressor.class, compressionName);
        byte[] decompress = compressor.decompress(bodyBytes);
        return serializer.deserialize(decompress, bodyType);
    }

    public static int computeCapacity(byte[] urlBytes, byte[] bodyBytes) {
        int totalLength = urlBytes.length + bodyBytes.length + 10;
        int nearestPowerOfTwo = 1;
        while (nearestPowerOfTwo < totalLength) {
            nearestPowerOfTwo <<= 1;
        }
        // todo 优化
        return nearestPowerOfTwo;
    }
}
