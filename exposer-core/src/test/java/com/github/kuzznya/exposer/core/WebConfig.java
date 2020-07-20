package com.github.kuzznya.exposer.core;

import com.github.kuzznya.exposer.EnableExposer;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@Import(ExposerConfig.class)
@EnableExposer
public class WebConfig { }
