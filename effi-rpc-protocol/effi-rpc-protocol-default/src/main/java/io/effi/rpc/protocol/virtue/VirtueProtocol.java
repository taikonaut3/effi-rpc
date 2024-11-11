package io.effi.rpc.protocol.virtue;

import io.effi.rpc.common.constant.Component;
import io.effi.rpc.common.constant.KeyConstants;
import io.effi.rpc.common.extension.spi.Extension;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.common.util.DateUtil;
import io.effi.rpc.core.Callee;
import io.effi.rpc.core.Caller;
import io.effi.rpc.core.Invocation;
import io.effi.rpc.core.result.Result;
import io.effi.rpc.core.support.InvocationSupport;
import io.effi.rpc.protocol.protocol.AbstractProtocol;
import io.effi.rpc.protocol.protocol.MultiplexPeers;
import io.effi.rpc.protocol.virtue.codec.VirtueServerCodec;
import io.effi.rpc.protocol.virtue.envelope.VirtueRequest;
import io.effi.rpc.protocol.virtue.envelope.VirtueResponse;
import io.effi.rpc.protocol.virtue.codec.VirtueClientCodec;

import java.time.LocalDateTime;

/**
 * Virtue Protocol implementation.
 */
@Extension(Component.Protocol.VIRTUE)
public final class VirtueProtocol extends AbstractProtocol<VirtueRequest, VirtueResponse> {

    public VirtueProtocol() {
        super(Component.Protocol.VIRTUE, new MultiplexPeers());
        serverCodec = new VirtueServerCodec(this);
        clientCodec = new VirtueClientCodec(this);
    }

    @Override
    public <T> Invocation<T> createInvocation(URL clientUrl, Caller<?> caller, Object[] args) {
        return new VirtueInvocation<>(clientUrl,caller, args);
    }

    @Override
    public <T> Invocation<T> createInvocation(URL url, Callee<?> callee, Object[] args) {
        return new VirtueInvocation<>(url, callee, args);
    }

    @Override
    protected VirtueRequest createRequest(Invocation<?> invocation) {
        URL url = invocation.url();
        String timestamp = DateUtil.format(LocalDateTime.now());
        url.addParam(KeyConstants.TIMESTAMP, timestamp);
        return new VirtueRequest(url, findBody(invocation));
    }

    @Override
    protected VirtueResponse createResponse(Invocation<?> invocation, Result result) {
        Object value = result.value();
        byte code = VirtueResponse.SUCCESS;
        if (result.hasException()) {
            value = SERVER_INVOKE_EXCEPTION + result.exception().getMessage();
            code = VirtueResponse.ERROR;
        }
        URL requestUrl = InvocationSupport.acquireRequestUrl(result.url());
        return new VirtueResponse(code, requestUrl, value);
    }

}
