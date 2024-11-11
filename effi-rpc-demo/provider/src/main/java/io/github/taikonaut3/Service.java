package io.github.taikonaut3;

import org.example.model2.ParentObject;

import java.util.List;

public class Service {

    public String helloWorld(String text) {
        return "hello world" + text;
    }

    public List<ParentObject> list(List<ParentObject> list) {
        return ParentObject.getObjList();
    }

    public List<ParentObject> list2(List<ParentObject> list1, List<org.example.model1.ParentObject> list2) {
        //int i=1/0;
        return ParentObject.getObjList("list server2");
    }
}
