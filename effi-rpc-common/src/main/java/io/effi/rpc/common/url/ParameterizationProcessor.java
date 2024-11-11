//package io.effi.rpc.common.url;
//
//import com.sun.source.tree.ClassTree;
//import com.sun.source.util.Trees;
//
//import javax.annotation.processing.*;
//import javax.lang.model.SourceVersion;
//import javax.lang.model.element.Element;
//import javax.lang.model.element.TypeElement;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Set;
//
//@SupportedAnnotationTypes("io.effi.rpc.common.url.Parameter")
//@SupportedSourceVersion(SourceVersion.RELEASE_21)
//public class ParameterizationProcessor extends AbstractProcessor {
//
//    private Map<TypeElement, Map<String, String>> map;
//
//    private Trees trees;
//
//    @Override
//    public synchronized void init(ProcessingEnvironment processingEnv) {
//        super.init(processingEnv);
//        map = new HashMap<>();
//        trees = Trees.instance(processingEnv);
//    }
//
//    @Override
//    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
//        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Parameter.class);
//        for (Element element : elements) {
//            TypeElement classElement = (TypeElement) element.getEnclosingElement();
//            Parameter parameter = element.getAnnotation(Parameter.class);
//            String fieldName = element.getSimpleName().toString();
//            Map<String, String> fieldMap = map.computeIfAbsent(classElement, k -> new HashMap<>());
//            fieldMap.put(parameter.value(), fieldName);
//        }
//
//        for (TypeElement typeElement : map.keySet()) {
//            Map<String, String> filedMap = map.get(typeElement);
//            for (Map.Entry<String, String> entry : filedMap.entrySet()) {
//                String key = entry.getKey();
//                String fieldName = entry.getValue();
//            }
//            ClassTree tree = trees.getTree(typeElement);
//            // 生成一个getParamMap的方法插入到这个ast中,key是上面key，value是this.上面的filedName
//        }
//        return false;
//    }
//
//
//}
//
