package io.github.taikonaut3;

import io.effi.rpc.core.annotation.RemoteCaller;
import io.effi.rpc.core.arg.annotation.Body;
import io.effi.rpc.protocol.http.annotation.HttpCall;
import io.effi.rpc.protocol.http.h2.config.Http2Call;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import org.example.MyBean;
import org.example.model1.ParentObject;

import java.util.List;

@RemoteCaller(value = "provider")
public interface Consumer {

    @Http2Call(path = "/http2Test/{id}?aaa=123&bbb=321")
    List<ParentObject> http2Test(@PathParam("id") String id,
                                 @HeaderParam("token") String token,
                                 @QueryParam("ccc") String ccc,
                                 @BeanParam MyBean myBean,
                                 @Body List<ParentObject> list);

    @Http2Call(path = "/http2Test")
    List<ParentObject> http2Test(@Body List<ParentObject> list);

    @HttpCall(path = "httpTest")
    List<ParentObject> httpTest(@Body List<ParentObject> list);

}
