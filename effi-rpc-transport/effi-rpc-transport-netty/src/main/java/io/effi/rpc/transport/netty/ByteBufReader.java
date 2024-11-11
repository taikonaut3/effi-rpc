package io.effi.rpc.transport.netty;

import io.effi.rpc.common.util.bytes.ByteReader;
import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;

/**
 * {@link ByteReader} implementation based on {@link ByteBuf}.
 */
public class ByteBufReader implements ByteReader {
    private final ByteBuf byteBuf;

    public ByteBufReader(ByteBuf byteBuf) {
        this.byteBuf = byteBuf;
    }

    @Override
    public byte readByte() {
        return byteBuf.readByte();
    }

    @Override
    public boolean readBoolean() {
        return byteBuf.readBoolean();
    }

    @Override
    public short readShort() {
        return byteBuf.readShort();
    }

    @Override
    public int readInt() {
        return byteBuf.readInt();
    }

    @Override
    public long readLong() {
        return byteBuf.readLong();
    }

    @Override
    public float readFloat() {
        return byteBuf.readFloat();
    }

    @Override
    public double readDouble() {
        return byteBuf.readDouble();
    }

    @Override
    public CharSequence readCharSequence(int length) {
        return readCharSequence(length, Charset.defaultCharset());
    }

    @Override
    public CharSequence readCharSequence(int length, Charset charset) {
        return byteBuf.readCharSequence(length, charset);
    }

    @Override
    public byte[] readBytes(int length) {
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);
        return bytes;
    }

    @Override
    public int readableBytes() {
        return byteBuf.readableBytes();
    }

    @Override
    public byte[] readRemainingBytes() {
        try {
            return ByteReader.super.readRemainingBytes();
        } finally {
            byteBuf.release();
        }
    }
}

