package io.effi.rpc.transport.codec;

import io.effi.rpc.common.url.URL;
import io.effi.rpc.core.ReplyFuture;
import io.effi.rpc.core.caller.Caller;
import io.effi.rpc.core.result.Result;
import io.effi.rpc.core.support.InvocationSupport;
import io.effi.rpc.transport.Envelope;
import io.effi.rpc.transport.Request;
import io.effi.rpc.transport.Response;
import io.effi.rpc.transport.channel.Channel;
import io.effi.rpc.transport.resolver.ReplyResolver;

import java.io.IOException;

/**
 * Abstract implementation of {@link ClientCodec}.
 *
 * @param <REQ>
 * @param <RESP>
 */
public abstract class AbstractClientCodec<REQ extends Envelope, RESP extends Envelope> implements ClientCodec {

    protected ReplyResolver<RESP> replyResolver;

    @SuppressWarnings("unchecked")
    @Override
    public Object encode(Channel channel, Request request) throws IOException {
        try {
            Envelope message = request.message();
            REQ req = (REQ) message;
            return encodeRequest(request, req);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    @Override
    public Response decode(Channel channel, Object message) throws IOException {
        RESP resp = decodeResponse(message);
        URL requestUrl = resp.url();
        Caller<?> caller = findCaller(requestUrl);
        Result result = null;
        if (caller != null) {
            result = replyResolver.resolve(resp, caller);
        }
        if (result == null) result = new Result(requestUrl, null);
        return new Response(result, resp);
    }

    protected abstract Object encodeRequest(Request request, REQ req) throws IOException;

    protected abstract RESP decodeResponse(Object message);

    protected Caller<?> findCaller(URL url) {
        URL callerUrl = InvocationSupport.acquireCallerUrl(url);
        ReplyFuture future = ReplyFuture.acquireFuture(callerUrl);
        if (future != null) {
            return future.invocation().caller();
        }
        return null;
    }
}
