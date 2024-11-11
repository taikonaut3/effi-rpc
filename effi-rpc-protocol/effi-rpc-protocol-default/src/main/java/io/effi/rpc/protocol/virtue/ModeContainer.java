package io.effi.rpc.protocol.virtue;

import io.effi.rpc.common.constant.Component;
import io.effi.rpc.common.constant.KeyConstants;
import io.effi.rpc.common.exception.ResourceException;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Store mapping: string -> byte.
 */
public class ModeContainer {

    private static final Map<String, List<ModeBean>> modeMap = new ConcurrentHashMap<>();

    static {
        put(KeyConstants.SERIALIZATION, Component.Serialization.JDK, (byte) 1);
        put(KeyConstants.SERIALIZATION, Component.Serialization.JSON, (byte) 2);
        put(KeyConstants.SERIALIZATION, Component.Serialization.KRYO, (byte) 3);
        put(KeyConstants.SERIALIZATION, Component.Serialization.FURY, (byte) 4);
        put(KeyConstants.SERIALIZATION, Component.Serialization.MSGPACK, (byte) 5);
        put(KeyConstants.ENVELOPE, Component.Envelope.REQUEST, (byte) 1);
        put(KeyConstants.ENVELOPE, Component.Envelope.RESPONSE, (byte) 2);
        put(KeyConstants.ENVELOPE, Component.Envelope.HEARTBEAT, (byte) 3);
        put(KeyConstants.ENVELOPE, Component.Envelope.ERROR, (byte) -1);
        put(KeyConstants.PROTOCOL, Component.Protocol.VIRTUE, (byte) 1);
        put(KeyConstants.COMPRESSION, Component.Compression.GZIP, (byte) 1);
    }

    public static Mode getMode(String key, String name) {
        List<ModeBean> values = modeMap.get(key);
        for (ModeBean value : values) {
            if (name.equals(value.name)) {
                return value;
            }
        }
        throw new ResourceException("Can't find Key: " + key + " name is " + name);
    }

    public static Mode getMode(String key, byte type) {
        List<ModeBean> values = modeMap.get(key);
        for (ModeBean value : values) {
            if (type == value.type) {
                return value;
            }
        }
        throw new ResourceException("Can't find Key: " + key + " type is " + type);
    }

    /**
     * Put mode into container.
     *
     * @param key
     * @param name
     * @param type
     */
    public static void put(String key, String name, byte type) {
        List<ModeBean> values = modeMap.computeIfAbsent(key, k -> new LinkedList<>());
        ModeBean modeBean = new ModeBean(name, type);
        values.add(modeBean);
    }

    /**
     * Get mode map.
     *
     * @return
     */
    public static Map<String, List<ModeBean>> modeMap() {
        return modeMap;
    }

    /**
     * Mode Wrapper.
     *
     * @param name
     * @param type
     */
    public record ModeBean(String name, byte type) implements Mode {

    }

}
