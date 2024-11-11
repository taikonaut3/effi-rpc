package io.effi.rpc.protocol.registry;

import io.effi.rpc.common.constant.Component;
import io.effi.rpc.common.constant.KeyConstant;
import io.effi.rpc.common.extension.spi.Extension;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.registry.MetaDataRegister;

import java.util.Map;

/**
 * Register default meta data to registry.
 */
@Extension(Component.DEFAULT)
public class DefaultMetaDataRegister implements MetaDataRegister {

    @Override
    public void process(URL url, final Map<String, String> metaData) {
        DefaultRegistryMetaData defaultRegistryMetaData = new DefaultRegistryMetaData(url);
        metaData.putAll(defaultRegistryMetaData.toMap());
        metaData.put(KeyConstant.PORTAL, url.getParam(KeyConstant.PORTAL));
        metaData.put(KeyConstant.PROTOCOL, url.protocol());
        metaData.put(KeyConstant.WEIGHT, url.getParam(KeyConstant.WEIGHT));
        metaData.put(KeyConstant.GROUP, url.getParam(KeyConstant.GROUP));
        metaData.put(KeyConstant.VERSION, url.getParam(KeyConstant.VERSION));
    }
}
