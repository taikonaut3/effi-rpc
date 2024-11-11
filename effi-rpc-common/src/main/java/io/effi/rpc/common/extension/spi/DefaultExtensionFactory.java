package io.effi.rpc.common.extension.spi;

import io.effi.rpc.common.util.ReflectionUtil;

/**
 * Default implementation of {@link ExtensionFactory}.
 */
public class DefaultExtensionFactory implements ExtensionFactory {

    @Override
    public <T> T getInstance(Class<T> type) {
        return ReflectionUtil.newInstance(type);
    }
}
