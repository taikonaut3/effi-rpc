package io.effi.rpc.protocol.protocol;

import io.effi.rpc.common.constant.Constant;
import io.effi.rpc.common.constant.KeyConstant;
import io.effi.rpc.common.constant.Platform;
import io.effi.rpc.common.extension.spi.ExtensionLoader;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.core.CallInvocation;
import io.effi.rpc.core.Invocation;
import io.effi.rpc.core.Portal;
import io.effi.rpc.core.ReceiveInvocation;
import io.effi.rpc.core.arg.Body;
import io.effi.rpc.core.result.Result;
import io.effi.rpc.protocol.handler.*;
import io.effi.rpc.transport.*;
import io.effi.rpc.transport.channel.Channel;
import io.effi.rpc.transport.channel.ChannelHandler;
import io.effi.rpc.transport.channel.ChannelHandlerChain;
import io.effi.rpc.transport.channel.DefaultChannelHandlerChain;
import io.effi.rpc.transport.client.Client;
import io.effi.rpc.transport.codec.ClientCodec;
import io.effi.rpc.transport.codec.ServerCodec;
import io.effi.rpc.transport.server.Server;
import lombok.experimental.Accessors;

/**
 * Abstract implementation of {@link Protocol}.
 *
 * @param <REQ>
 * @param <RESP>
 */
@Accessors(fluent = true)
public abstract class AbstractProtocol<REQ extends Envelope, RESP extends Envelope> implements Protocol {

    protected static final String SERVER_INVOKE_EXCEPTION = "Server invoke exception: ";

    protected String protocol;

    protected ServerCodec serverCodec;

    protected ClientCodec clientCodec;

    protected ChannelHandler clientHandler;

    protected ChannelHandler serverHandler;

    protected AbstractProtocol(String protocol) {
        this(protocol, null, null);
    }

    protected AbstractProtocol(String protocol, ServerCodec serverCodec, ClientCodec clientCodec) {
        this(protocol, serverCodec, clientCodec, defaultClientChannelHandler(), defaultServerChannelHandler());
    }

    protected AbstractProtocol(String protocol, ServerCodec serverCodec, ClientCodec clientCodec, ChannelHandler clientHandler, ChannelHandler serverHandler) {
        this.protocol = protocol;
        this.serverCodec = serverCodec;
        this.clientCodec = clientCodec;
        this.clientHandler = clientHandler;
        this.serverHandler = serverHandler;
    }

    @Override
    public Client openClient(URL url, Portal portal) {
        return transporter(portal).acquireClient(url, clientHandler);
    }

    @Override
    public Server openServer(URL url, Portal portal) {
        return transporter(portal).acquireServer(url, serverHandler);
    }

    @Override
    public void sendRequest(CallInvocation<?> invocation) {
        if (!Platform.isJvmShuttingDown()) {
            REQ request = createRequest(invocation);
            Client client = openClient(invocation.clientUrl(), invocation.portal());
            client.send(new Request(invocation, request));
        } else {
            invocation.future().remove();
        }
    }

    @Override
    public void sendResponse(Channel channel, ReceiveInvocation<?> invocation, Result result) {
        RESP response = createResponse(invocation, result);
        channel.send(new Response(result, response));
    }

    @Override
    public ServerCodec serverCodec() {
        return serverCodec;
    }

    @Override
    public ClientCodec clientCodec() {
        return clientCodec;
    }

    @Override
    public String protocol() {
        return protocol;
    }

    protected Transporter loadTransporter(String transport) {
        return ExtensionLoader.loadExtension(Transporter.class, transport);
    }

    protected abstract REQ createRequest(CallInvocation<?> invocation);

    protected abstract RESP createResponse(ReceiveInvocation<?> invocation, Result result);

    protected Transporter transporter(Portal portal) {
        String transport = portal.url().getParam(KeyConstant.TRANSPORTER, Constant.DEFAULT_TRANSPORTER);
        return loadTransporter(transport);
    }

    protected Object findBody(Invocation<?> invocation) {
        for (Object arg : invocation.args()) {
            if (arg instanceof Body<?> body && body.get() != null) {
                return body.get();
            }
        }
        return null;
    }

    private static ChannelHandler defaultClientChannelHandler() {
        ChannelHandlerChain chain = new DefaultChannelHandlerChain();
        chain.addLast(new RpcFutureBinderHandler())
                .addLast(new ClientMessageConverter())
                .addLast(new ClientChannelHandler());
        return chain;
    }

    private static ChannelHandler defaultServerChannelHandler() {
        ChannelHandlerChain chain = new DefaultChannelHandlerChain();
        chain.addLast(new ServerMessageConverter())
                .addLast(new ServerChannelHandler());
        return chain;
    }
}
