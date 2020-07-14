package com.kuzznya.exposer;

import com.kuzznya.exposer.config.EndpointConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(EndpointConfiguration.class)
public @interface EnableExposer { }
