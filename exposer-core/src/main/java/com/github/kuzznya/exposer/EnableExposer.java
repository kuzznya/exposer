package com.github.kuzznya.exposer;

import com.github.kuzznya.exposer.core.Exposer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ComponentScan("com.github.kuzznya.exposer")
@Import(Exposer.class)
public @interface EnableExposer { }
