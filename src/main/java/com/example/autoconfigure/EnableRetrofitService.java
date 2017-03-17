package com.example.autoconfigure;

import com.example.autoconfigure.retrofit.RetrofitAutoConfigurationRegistrar;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;
import retrofit2.Retrofit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(RetrofitAutoConfigurationRegistrar.class)
@ConditionalOnClass(Retrofit.class)
//@ConditionalOnBean(Converter.Factory.class)
public @interface EnableRetrofitService {

    @AliasFor("basePackage")
    String value() default "";

    @AliasFor("value")
    String basePackage() default "";

}
