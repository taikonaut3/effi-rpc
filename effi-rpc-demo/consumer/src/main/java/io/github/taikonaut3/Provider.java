package io.github.taikonaut3;

import io.effi.rpc.common.url.URL;
import io.effi.rpc.core.annotation.RemoteService;

@RemoteService("345")
public class Provider {

    public static void main(String[] args) {
        URL url = URL.valueOf("xxx://provider/njnk//nkn//");
        System.out.println(url);
    }
}
