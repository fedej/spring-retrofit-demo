package com.example.autoconfigure.retrofit;

import com.example.autoconfigure.EnableRetrofitService;
import lombok.extern.java.Log;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.ClassUtils;
import retrofit2.Converter;
import retrofit2.Retrofit;

import java.util.Map;

@Log
public class RetrofitAutoConfigurationRegistrar implements ImportBeanDefinitionRegistrar,
        BeanFactoryAware {

    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void registerBeanDefinitions(final AnnotationMetadata importingClassMetadata,
                                        final BeanDefinitionRegistry registry) {

        final Map<String, Object> annotationAttributes = importingClassMetadata
                .getAnnotationAttributes(EnableRetrofitService.class.getName());

        final String basePackage = (String) annotationAttributes
                .getOrDefault("basePackage", "");

        final ConfigurableListableBeanFactory beanFactory =
                (ConfigurableListableBeanFactory) this.beanFactory;
        final Converter.Factory factory = beanFactory.getBean(Converter.Factory.class);

        new RetrofitComponentProvider()
                .findCandidateComponents(basePackage)
                .stream()
                .map(BeanDefinition::getBeanClassName)
                .forEach(className -> {
                    try {
                        final Class<?> beanClass = ClassUtils.forName(className, ClassUtils.getDefaultClassLoader());
                        final RetrofitService annotation = AnnotationUtils.getAnnotation(beanClass, RetrofitService.class);
                        final String baseUrl = beanFactory.getBean(Environment.class).getProperty(annotation.urlProperty());
                        final Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(baseUrl)
                                .addConverterFactory(factory)
                                .build();
                        beanFactory.registerSingleton(className, retrofit.create(beanClass));
                    } catch (ClassNotFoundException e) {
                        log.severe(() -> "Error creating Retrofit service");
                    }
                });
    }

    static final class RetrofitComponentProvider
            extends ClassPathScanningCandidateComponentProvider {

        RetrofitComponentProvider() {
            super(false);
            super.addIncludeFilter(new AnnotationTypeFilter(RetrofitService.class));
        }

        @Override
        protected boolean isCandidateComponent(final AnnotatedBeanDefinition beanDefinition) {
            return beanDefinition.getMetadata().isInterface();
        }

        @Override
        public void addIncludeFilter(final TypeFilter includeFilter) {

        }
    }
}