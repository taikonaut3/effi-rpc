package io.github.taikonaut3;

import io.effi.rpc.core.annotation.RemoteService;
import io.effi.rpc.core.arg.annotation.Body;
import io.effi.rpc.protocol.http.annotation.HttpCallable;
import io.effi.rpc.protocol.http.h2.config.Http2Callable;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import org.example.MyBean;
import org.example.model2.ParentObject;

import java.util.List;

@RemoteService("345")
//@Service
public class Provider {


    @Http2Callable(path = "/http2Test")
    public List<ParentObject> http2cTest(@Body List<ParentObject> list) {
        return ParentObject.getObjList("list http2 server");
    }

    @Http2Callable(path = "/http2Test/{id}")
    public List<ParentObject> http2Test(@Body List<ParentObject> list,
                                        @PathParam("id") String id,
                                        @HeaderParam("token") String token,
                                        @QueryParam("ccc") String ccc,
                                        @BeanParam MyBean myBean) {
        return ParentObject.getObjList("list server2");
    }

    @HttpCallable(path = "/httpTest")
    public List<ParentObject> httpTest(@Body List<ParentObject> list) {
        return ParentObject.getObjList("list http server");
    }

}
