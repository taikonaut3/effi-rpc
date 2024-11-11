package io.effi.rpc.transport.codec;

import io.effi.rpc.transport.Request;
import io.effi.rpc.transport.Response;

/**
 * Encode server {@link Response} and decode client {@link Request}.
 */
public interface ServerCodec extends Codec<Response, Request> {

}
