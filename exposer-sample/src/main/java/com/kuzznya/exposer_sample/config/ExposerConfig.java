package com.kuzznya.exposer_sample.config;

import com.github.kuzznya.exposer.core.config.ExposerConfiguration;
import com.github.kuzznya.exposer.core.config.ExposerConfigurer;
import com.kuzznya.exposer_sample.model.User;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestMethod;

@Configuration
@Profile("code")
public class ExposerConfig implements ExposerConfigurer {

    @Override
    public ExposerConfiguration configureExposer() {
        return ExposerConfiguration.builder()
                .route("/test")
                    .bean("TestService")
                    .route("/v1")
                        .endpoint(RequestMethod.GET, "getValue").and()
                        .endpoint(RequestMethod.POST, "setValue").register()
                        .and()
                    .route("/v2")
                        .endpoint(RequestMethod.POST, "setValue")
                            .param("value", "params['val']")
                            .register()
                        .and()
                    .route("/v3")
                        .endpoint(RequestMethod.POST, "setValue")
                            .param("value", "bodyData['value']")
                            .register()
                        .and()
                    .and()
                .route("/users")
                    .bean("UserService")
                    .endpoint(RequestMethod.POST, "createUser")
                        .requestBodyClass(User.class)
                        .param("user", "body")
                        .and()
                    .endpoint(RequestMethod.GET, "findUserByEmail")
                        .param("emails", "params['email']")
                        .register()
                    .route("/{username}")
                        .endpoint(RequestMethod.GET, "getUser")
                            .param("username", "pathVars['username']")
                            .and()
                        .endpoint(RequestMethod.DELETE, "deleteUser")
                            .param("username", "pathVars['username']")
                            .register()
                        .add()
                    .add()
                .configure();
    }
}
