package io.effi.rpc.protocol.virtue;

import io.effi.rpc.common.constant.Component;
import io.effi.rpc.common.extension.Builder;
import io.effi.rpc.core.arg.MethodMapper;
import io.effi.rpc.protocol.support.AbstractCallee;

public class VirtueCallee<T> extends AbstractCallee<T, VirtueCallee.VirtueCalleeBuilder<T>> {
    VirtueCallee(VirtueCalleeBuilder<T> virtueCalleeBuilder) {
        super(virtueCalleeBuilder);
    }


    public static <T> VirtueCalleeBuilder<T> builder(MethodMapper<T> methodMapper) {
        return new VirtueCalleeBuilder<>(methodMapper);
    }

    public static class VirtueCalleeBuilder<T> extends AbstractCallee<T, VirtueCalleeBuilder<T>> implements Builder<VirtueCallee<T>> {

         VirtueCalleeBuilder(MethodMapper<T> methodMapper) {
            super(methodMapper);
        }

        @Override
        public VirtueCallee<T> build() {
            protocol(Component.Protocol.VIRTUE);
            return new VirtueCallee<>(this);
        }

    }
}
