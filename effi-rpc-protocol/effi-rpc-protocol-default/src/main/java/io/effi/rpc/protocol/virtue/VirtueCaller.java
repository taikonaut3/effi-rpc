package io.effi.rpc.protocol.virtue;

import io.effi.rpc.common.constant.Component;
import io.effi.rpc.common.extension.Builder;
import io.effi.rpc.common.extension.TypeToken;
import io.effi.rpc.protocol.support.AbstractCaller;

public class VirtueCaller<R> extends AbstractCaller<R, VirtueCaller.VirtueCallerBuilder<R>> {

    VirtueCaller(VirtueCallerBuilder<R> builder) {
        super(builder);
    }

    public static <R> VirtueCallerBuilder<R> builder(TypeToken<R> returnType) {
        return new VirtueCallerBuilder<>(returnType);
    }

    public static class VirtueCallerBuilder<R> extends AbstractCaller<R, VirtueCallerBuilder<R>> implements Builder<VirtueCaller<R>> {

        VirtueCallerBuilder(TypeToken<R> returnType) {
            super(returnType);
        }

        @Override
        public VirtueCaller<R> build() {
            protocol(Component.Protocol.VIRTUE);
            return new VirtueCaller<>(this);
        }
    }
}
