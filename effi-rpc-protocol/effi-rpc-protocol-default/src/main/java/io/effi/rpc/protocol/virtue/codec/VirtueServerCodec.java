package io.effi.rpc.protocol.virtue.codec;

import io.effi.rpc.common.constant.Component;
import io.effi.rpc.common.constant.KeyConstants;
import io.effi.rpc.common.exception.UnSupportedDecodeException;
import io.effi.rpc.common.extension.spi.ExtensionLoader;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.common.util.bytes.ByteReader;
import io.effi.rpc.common.util.bytes.ByteWriter;
import io.effi.rpc.common.util.bytes.HeapByteWriter;
import io.effi.rpc.common.util.bytes.WrappedByteReader;
import io.effi.rpc.core.Callee;
import io.effi.rpc.core.arg.ParameterMapper;
import io.effi.rpc.protocol.support.arg.ParamVarHandler;
import io.effi.rpc.protocol.support.arg.PathVarHandler;
import io.effi.rpc.protocol.virtue.Mode;
import io.effi.rpc.protocol.virtue.VirtueSupport;
import io.effi.rpc.protocol.virtue.envelope.VirtueRequest;
import io.effi.rpc.protocol.virtue.envelope.VirtueResponse;
import io.effi.rpc.transport.codec.AbstractServerCodec;
import io.effi.rpc.transport.compress.Compressor;
import io.effi.rpc.transport.resolver.ArgumentResolver;
import io.effi.rpc.transport.resolver.BodyArgumentHandler;
import io.effi.rpc.protocol.virtue.ModeContainer;
import io.effi.rpc.protocol.virtue.VirtueProtocol;
import io.effi.rpc.transport.Response;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class VirtueServerCodec extends AbstractServerCodec<VirtueResponse, VirtueRequest> implements BodyArgumentHandler<VirtueRequest> {

    public VirtueServerCodec(VirtueProtocol protocol) {
        this.protocol = protocol;
        this.argumentResolver = new ArgumentResolver<>() {
            @Override
            protected void initialize() {
                add(new ParamVarHandler<>(VirtueRequest::url));
                add(new PathVarHandler<>(VirtueRequest::url));
                add(VirtueServerCodec.this);
            }
        };
    }

    @Override
    protected Object encodeResponse(Response response, VirtueResponse virtueResponse) throws Throwable {
        URL url = response.url();
        // message type
        Mode envelopeMode = ModeContainer.getMode(KeyConstants.ENVELOPE, Component.Envelope.RESPONSE);
        // compress type
        String compression = url.getParam(KeyConstants.COMPRESSION);
        Mode compressMode = ModeContainer.getMode(KeyConstants.COMPRESSION, compression);
        Compressor compressor = virtueResponse.compressor();
        /*  ---------------- url ------------------  */
        String urlStr = url.toString();
        byte[] urlBytes = compressor.compress(urlStr.getBytes(StandardCharsets.UTF_8));
        /*  ---------------- body ------------------  */
        byte[] message = VirtueSupport.encodeBody(virtueResponse);
        /* write */
        int capacity = VirtueSupport.computeCapacity(urlBytes, message);
        ByteWriter byteWriter = new HeapByteWriter(capacity);
        byteWriter.writeInt(VirtueSupport.MAGIC_RES);
        byteWriter.writeByte(response.code());
        byteWriter.writeByte(envelopeMode.type());
        byteWriter.writeByte(compressMode.type());
        byteWriter.writeInt(urlBytes.length);
        byteWriter.writeBytes(urlBytes);
        byteWriter.writeBytes(message);
        return byteWriter.toBytes();
    }

    @Override
    protected VirtueRequest decodeRequest(Object message) throws Throwable {
        if (message instanceof byte[] bytes) {
            ByteReader byteReader = new WrappedByteReader(bytes);
            // magic
            int magic = byteReader.readInt();
            if (magic != VirtueSupport.MAGIC_REQ) {
                throw new IllegalArgumentException("Magic is not match");
            }
            Mode envelopeMode = ModeContainer.getMode(KeyConstants.ENVELOPE, byteReader.readByte());
            if (!Objects.equals(envelopeMode.name(), Component.Envelope.REQUEST)) {
                throw new IllegalArgumentException("Message type must 'request'");
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
            return new VirtueRequest(url, bodyBytes);
        }
        throw new UnSupportedDecodeException("");
    }

    @Override
    public Object handler(VirtueRequest request, Callee<?> callee, ParameterMapper wrapper) {
        Object body = null;
        if (request.body() instanceof byte[] unDecodeBody) {
            body = VirtueSupport.decodeBody(request.url(), unDecodeBody, wrapper.parameter().getParameterizedType());
            request.body(body);
        }
        return body;
    }
}
