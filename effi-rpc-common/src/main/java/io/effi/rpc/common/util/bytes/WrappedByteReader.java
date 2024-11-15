package io.effi.rpc.common.util.bytes;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Use HeapByteBuffer asReadOnlyBuffer.
 */
public class WrappedByteReader implements ByteReader {

    private final ByteBuffer buffer;

    public WrappedByteReader(byte[] bytes) {
        buffer = ByteBuffer.wrap(bytes);
        buffer.asReadOnlyBuffer();
    }

    @Override
    public byte readByte() {
        return buffer.get();
    }

    @Override
    public boolean readBoolean() {
        return buffer.get() != 0;
    }

    @Override
    public short readShort() {
        return buffer.getShort();
    }

    @Override
    public int readInt() {
        return buffer.getInt();
    }

    @Override
    public long readLong() {
        return buffer.getLong();
    }

    @Override
    public float readFloat() {
        return buffer.getFloat();
    }

    @Override
    public double readDouble() {
        return buffer.getDouble();
    }

    @Override
    public CharSequence readCharSequence(int length) {
        return readCharSequence(length, StandardCharsets.UTF_8);
    }

    @Override
    public CharSequence readCharSequence(int length, Charset charset) {
        return new String(readBytes(length), charset);
    }

    @Override
    public byte[] readBytes(int length) {
        byte[] bytes = new byte[length];
        buffer.get(bytes);
        return bytes;
    }

    @Override
    public int readableBytes() {
        return buffer.remaining();
    }
}
