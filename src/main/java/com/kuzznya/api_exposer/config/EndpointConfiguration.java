package com.kuzznya.api_exposer.config;

import com.kuzznya.api_exposer.model.Endpoint;
import com.kuzznya.api_exposer.model.ExposerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableConfigurationProperties(ExposerProperties.class)
public class EndpointConfiguration {
    private final RequestMappingHandlerMapping handlerMapping;
    private final ExposerProperties properties;
    private final ApplicationContext context;

    public EndpointConfiguration(RequestMappingHandlerMapping handlerMapping, ExposerProperties properties, ApplicationContext context) {
        this.handlerMapping = handlerMapping;
        this.properties = properties;
        this.context = context;
    }

    @PostConstruct
    public void init() throws NoSuchMethodException {
        for (Endpoint endpoint : properties.getEndpoints()) {
            String beanName = endpoint.getBeanName();
            beanName = Character.toLowerCase(beanName.charAt(0)) + beanName.substring(1);
            String methodName = endpoint.getBeanMethod();

            Object service = context.getBean(beanName);
            Method serviceMethod = List.of(service.getClass().getDeclaredMethods())
                    .stream()
                    .filter((method -> method.getName().equals(methodName)))
                    .findAny()
                    .orElseThrow(NoSuchMethodException::new);

            serviceMethod.setAccessible(true);

            EndpointHandler handler = new EndpointHandler(service, serviceMethod, endpoint.getParamsMapping());

            handlerMapping.registerMapping(
                    RequestMappingInfo
                            .paths(endpoint.getPath())
                            .methods(endpoint.getHttpMethod())
                            .params(
                                    endpoint.getParamsMapping() != null ?
                                            endpoint.getRequestParams().toArray(String[]::new) :
                                            Arrays.stream(serviceMethod.getParameters())
                                                    .map(Parameter::getName)
                                                    .toArray(String[]::new)
                            )
                            .produces(MediaType.APPLICATION_JSON_VALUE)
                            .build(),
                    handler,
                    EndpointHandler.class.getDeclaredMethod("handle", MultiValueMap.class)
            );
        }
    }

}
