package io.effi.rpc.boot.processor;

import io.effi.rpc.boot.EnableViceroy;
import io.effi.rpc.common.util.ReflectionUtil;
import io.effi.rpc.core.annotation.RemoteService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.lang.NonNull;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * RemoteService PostProcessor.
 */
public class RemoteServicePostProcessor extends PreferentialCreateConfig implements BeanDefinitionRegistryPostProcessor {

    private static final List<Class<? extends Annotation>> EXPORT_ANNOTATION_TYPES = List.of(
            RemoteService.class
    );

    private final String[] exportPackages;

    public RemoteServicePostProcessor(EnableViceroy enableViceroy) {
        ArrayList<String> list = new ArrayList<>();
        Collections.addAll(list, enableViceroy.scanBasePackages());
        Collections.addAll(list, enableViceroy.serverScan());
        this.exportPackages = list.toArray(new String[0]);
    }

    @Override
    public void postProcessBeanDefinitionRegistry(@NonNull BeanDefinitionRegistry registry) throws BeansException {
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(registry);
        for (Class<? extends Annotation> annotationType : EXPORT_ANNOTATION_TYPES) {
            scanner.addIncludeFilter(new AnnotationTypeFilter(annotationType));
        }
        for (String exportPackage : exportPackages) {
            Set<BeanDefinition> beanDefinitions = scanner.findCandidateComponents(exportPackage);
            for (BeanDefinition beanDefinition : beanDefinitions) {
                String className = beanDefinition.getBeanClassName();
                assert className != null;
                Class<?> type = ClassUtils.resolveClassName(className, null);
                if (type.isAnnotationPresent(RemoteService.class)) {
                    RemoteService service = type.getAnnotation(RemoteService.class);
                    registry.registerBeanDefinition(service.value(), beanDefinition);
                }
            }
        }
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, @NonNull String beanName) throws BeansException {
        if (ReflectionUtil.getTargetClass(bean.getClass()).isAnnotationPresent(RemoteService.class)) {
            // viceroy().wrap(beanName, bean);
        }
        return bean;
    }

}
