package io.github.taikonaut3;

import io.effi.rpc.core.Portal;
import jakarta.annotation.Resource;
import org.example.model2.ParentObject;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ProviderController {

    private String name;

    @Resource
    private Portal portal;

    @GetMapping("hello")
    public String hello(@RequestParam("world") String world) {
        return "hello" + world;
    }

    @PostMapping("hello/list")
    public List<ParentObject> list(@RequestBody List<ParentObject> list) {
        return ParentObject.getObjList();
    }

    @PostMapping("hello/list/{id}/name/{name1}")
    public String path(@PathVariable("id") String id, @PathVariable("name1") String name) {
        return id + name;
    }

    @GetMapping("calleeMetrics")
    public Map<String, String> calleeMetrics() {
        LinkedHashMap<String, String> result = new LinkedHashMap<>();
//        portal.configManager().calleeManager().values().forEach(invoker -> {
//                CalleeMetrics calleeMetrics = invoker.get(CalleeMetrics.ATTRIBUTE_KEY);
//                result.put(GenerateUtil.generateCalleeMapping(invoker.url().protocol(), invoker.url().path()), calleeMetrics.toString());
//
//        });
        return result;
    }
}
