package io.effi.rpc.transport.codec;

import io.effi.rpc.transport.Request;
import io.effi.rpc.transport.Response;

/**
 * Encode client {@link Request} and decode server {@link Response}.
 */
public interface ClientCodec extends Codec<Request, Response> {

}

