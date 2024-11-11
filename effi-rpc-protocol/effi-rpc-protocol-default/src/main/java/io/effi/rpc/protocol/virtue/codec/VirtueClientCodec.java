package io.effi.rpc.protocol.virtue.codec;

import io.effi.rpc.common.constant.Component;
import io.effi.rpc.common.constant.KeyConstants;
import io.effi.rpc.common.exception.UnSupportedDecodeException;
import io.effi.rpc.common.extension.spi.ExtensionLoader;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.common.util.ObjectUtil;
import io.effi.rpc.common.util.bytes.ByteReader;
import io.effi.rpc.common.util.bytes.ByteWriter;
import io.effi.rpc.common.util.bytes.HeapByteWriter;
import io.effi.rpc.common.util.bytes.WrappedByteReader;
import io.effi.rpc.core.Caller;
import io.effi.rpc.core.support.InvocationSupport;
import io.effi.rpc.protocol.virtue.Mode;
import io.effi.rpc.protocol.virtue.ModeContainer;
import io.effi.rpc.protocol.virtue.VirtueProtocol;
import io.effi.rpc.protocol.virtue.VirtueSupport;
import io.effi.rpc.protocol.virtue.envelope.VirtueRequest;
import io.effi.rpc.protocol.virtue.envelope.VirtueResponse;
import io.effi.rpc.transport.codec.AbstractClientCodec;
import io.effi.rpc.transport.compress.Compressor;
import io.effi.rpc.transport.resolver.ReplyResolver;
import io.effi.rpc.transport.Request;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class VirtueClientCodec extends AbstractClientCodec<VirtueRequest, VirtueResponse> implements ReplyResolver<VirtueResponse> {

    private final VirtueProtocol protocol;

    public VirtueClientCodec(VirtueProtocol protocol) {
        this.protocol = protocol;
        this.replyResolver = this;
    }

    @Override
    protected Object encodeRequest(Request request, VirtueRequest virtueRequest) {
        URL url = InvocationSupport.acquireCallerUrl(request.url());
        // message type
        Mode envelopeMode = ModeContainer.getMode(KeyConstants.ENVELOPE, Component.Envelope.REQUEST);
        // compress type
        String compression = url.getParam(KeyConstants.COMPRESSION);
        Mode compressMode = ModeContainer.getMode(KeyConstants.COMPRESSION, compression);
        Compressor compressor = virtueRequest.compressor();
        /*  ---------------- url ------------------  */
        String urlStr = url.toString();
        byte[] urlBytes = compressor.compress(urlStr.getBytes(StandardCharsets.UTF_8));
        /*  ---------------- body ------------------  */
        byte[] message = VirtueSupport.encodeBody(virtueRequest);
        /* write */
        int capacity = VirtueSupport.computeCapacity(urlBytes, message);
        ByteWriter byteWriter = new HeapByteWriter(capacity);
        byteWriter.writeInt(VirtueSupport.MAGIC_REQ);
        byteWriter.writeByte(envelopeMode.type());
        byteWriter.writeByte(compressMode.type());
        byteWriter.writeInt(urlBytes.length);
        byteWriter.writeBytes(urlBytes);
        byteWriter.writeBytes(message);
        return byteWriter.toBytes();
    }

    @Override
    protected VirtueResponse decodeResponse(Object message) {
        if (message instanceof byte[] bytes) {
            ByteReader byteReader = new WrappedByteReader(bytes);
            // magic
            int magic = byteReader.readInt();
            if (magic != VirtueSupport.MAGIC_RES) {
                throw new IllegalArgumentException("Magic is not match");
            }
            // code
            byte code = byteReader.readByte();
            // message type
            Mode envelopeMode = ModeContainer.getMode(KeyConstants.ENVELOPE, byteReader.readByte());
            if (!Objects.equals(envelopeMode.name(), Component.Envelope.RESPONSE)) {
                throw new IllegalArgumentException("Message type must 'response'");
            }
            // compression type
            Mode compressionMode = ModeContainer.getMode(KeyConstants.COMPRESSION, byteReader.readByte());
            Compressor compressor = ExtensionLoader.loadExtension(Compressor.class, compressionMode.name());
            // url
            int urlLength = byteReader.readInt();
            byte[] urlCompressedBytes = byteReader.readBytes(urlLength);
            byte[] urlBytes = compressor.decompress(urlCompressedBytes);
            String urlStr = new String(urlBytes, StandardCharsets.UTF_8);
            URL url = URL.valueOf(urlStr);
            // body
            byte[] bodyBytes = byteReader.readBytes(byteReader.readableBytes());
            return new VirtueResponse(code, url, bodyBytes);
        }
        throw new UnSupportedDecodeException("Unsupported decode type :" + ObjectUtil.simpleClassName(message));
    }

    @Override
    public Object resolve(VirtueResponse response, Caller<?> caller) {
        Object replyValue = null;
        if (response.body() instanceof byte[] bytes) {
            if (response.code() == VirtueResponse.ERROR) {
                replyValue = new String(bytes);
            } else {
                replyValue = VirtueSupport.decodeBody(response.url(), bytes, caller.returnType().type());
            }
            response.body(replyValue);
        }
        return replyValue;
    }
}
