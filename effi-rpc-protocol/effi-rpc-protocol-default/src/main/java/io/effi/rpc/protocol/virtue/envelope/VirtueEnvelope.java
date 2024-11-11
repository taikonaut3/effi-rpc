package io.effi.rpc.protocol.virtue.envelope;

import io.effi.rpc.common.constant.KeyConstants;
import io.effi.rpc.common.extension.spi.ExtensionLoader;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.serialization.Serializer;
import io.effi.rpc.transport.compress.Compressor;
import io.effi.rpc.transport.Envelope;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Virtue Envelope.
 */
@Getter
@Setter
@Accessors(fluent = true)
public abstract class VirtueEnvelope implements Envelope {

    private Object body;

    private URL url;

    protected VirtueEnvelope() {
    }

    protected VirtueEnvelope(URL url, Object body) {
        url(url);
        body(body);
    }

    /**
     * Get serializer.
     *
     * @return
     */
    public Serializer serializer() {
        return ExtensionLoader.loadExtension(Serializer.class, url.getParam(KeyConstants.SERIALIZATION));
    }

    /**
     * Get compression.
     *
     * @return
     */
    public Compressor compressor() {
        return ExtensionLoader.loadExtension(Compressor.class, url.getParam(KeyConstants.COMPRESSION));
    }

}
