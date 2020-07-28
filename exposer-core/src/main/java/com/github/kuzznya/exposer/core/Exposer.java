package com.github.kuzznya.exposer.core;

import com.github.kuzznya.exposer.core.config.ExposerConfiguration;
import com.github.kuzznya.exposer.core.config.ExposerConfigurer;
import com.github.kuzznya.exposer.core.model.Endpoint;
import org.springframework.context.ApplicationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@EnableWebMvc
public class Exposer {
    private final RequestMappingHandlerMapping handlerMapping;
    private final ExposerConfiguration exposerConfiguration;
    private final ApplicationContext context;

    private final ParameterNameDiscoverer paramsDiscoverer = new DefaultParameterNameDiscoverer();

    private final List<RequestMappingInfo> mappings = new ArrayList<>();

    public Exposer(RequestMappingHandlerMapping handlerMapping,
                   ExposerConfigurer exposerConfigurer,
                   ApplicationContext context) {
        this.handlerMapping = handlerMapping;
        this.exposerConfiguration = exposerConfigurer.configureExposer();
        this.context = context;
    }

    @PostConstruct
    public void setUpEndpoints() throws NoSuchMethodException {
        for (Endpoint endpoint : exposerConfiguration.getEndpoints()) {
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

            EndpointHandler handler = new EndpointHandler(service, serviceMethod, endpoint.getParamsMapping(),
                    paramsDiscoverer);

            RequestMappingInfo mappingInfo = RequestMappingInfo
                    .paths(endpoint.getPath())
                    .methods(endpoint.getHttpMethod())
                    .params(
                            endpoint.getParamsMapping() != null ?
                                    endpoint.getRequestParams().toArray(String[]::new) :
                                    Optional.ofNullable(paramsDiscoverer.getParameterNames(serviceMethod))
                                            .orElse(new String[0])
                    )
                    .build();

            handlerMapping.registerMapping(
                    mappingInfo,
                    handler,
                    EndpointHandler.class.getDeclaredMethod("handle", MultiValueMap.class, Map.class)
            );

            mappings.add(mappingInfo);
        }
    }

    @PreDestroy
    public void unregisterEndpoints() {
        mappings.forEach(handlerMapping::unregisterMapping);
    }

}
