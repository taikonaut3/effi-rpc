package io.effi.rpc.transport.codec;

import io.effi.rpc.common.exception.ResourceException;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.core.Callee;
import io.effi.rpc.core.Portal;
import io.effi.rpc.core.ServerExporter;
import io.effi.rpc.transport.Envelope;
import io.effi.rpc.transport.Protocol;
import io.effi.rpc.transport.Request;
import io.effi.rpc.transport.Response;
import io.effi.rpc.transport.channel.Channel;
import io.effi.rpc.transport.resolver.ArgumentResolver;

import java.io.IOException;

/**
 * Abstract implementation of {@link ServerCodec}.
 *
 * @param <RESP>
 * @param <REQ>
 */
public abstract class AbstractServerCodec<RESP extends Envelope, REQ extends Envelope> implements ServerCodec {

    protected Protocol protocol;

    protected ArgumentResolver<REQ> argumentResolver;

    @SuppressWarnings("unchecked")
    @Override
    public Object encode(Channel channel, Response response) throws IOException {
        try {
            Envelope message = response.message();
            RESP resp = (RESP) message;
            return encodeResponse(response, resp);
        } catch (Throwable e) {
            throw new IOException(e);
        }
    }

    @Override
    public Request decode(Channel channel, Object message) throws IOException {
        try {
            REQ req = decodeRequest(message);
            URL requestUrl = req.url();
            Portal portal = channel.portal();
            Callee<?> callee = findCallee(requestUrl, portal);
            Object[] args = argumentResolver.resolve(req, callee);
            var invocation = protocol.createInvocation(channel, requestUrl, callee, args);
            return new Request(invocation, req);
        } catch (Throwable e) {
            throw new IOException(e);
        }
    }

    protected Callee<?> findCallee(URL url, Portal portal) {
        ServerExporter serverExporter = portal.serverExporterManager().get(url.authority());
        Callee<?> callee = serverExporter.calleeManager().acquire(url);
        if (callee == null) {
            throw new ResourceException("Can't find callee<" + url.protocol() + ">['" + url.path() + "']");
        }
        return callee;
    }

    protected abstract Object encodeResponse(Response response, RESP resp) throws Throwable;

    protected abstract REQ decodeRequest(Object message) throws Throwable;

}
