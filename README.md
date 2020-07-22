# Exposer
Spring Boot API bootstrap creator without Controllers

Required Java version: 11

Project consists of 3 modules:
* Core module with Spring dependencies
* Spring Boot autoconfigure module
* Spring Boot Starter module

[![Core (Maven Central)](https://img.shields.io/maven-central/v/com.github.kuzznya/exposer-core.svg?label=Core%20(Maven%20Central))](https://search.maven.org/search?q=g:%22com.github.kuzznya%22%20AND%20a:%22exposer-core%22)  
[![Maven Central](https://img.shields.io/maven-central/v/com.github.kuzznya/exposer-spring-boot-autoconfigure.svg?label=Autoconfigure%20(Maven%20Central))](https://search.maven.org/search?q=g:%22com.github.kuzznya%22%20AND%20a:%22exposer-spring-boot-autoconfigure%22)  
[![Maven Central](https://img.shields.io/maven-central/v/com.github.kuzznya/exposer-spring-boot-starter.svg?label=Starter%20(Maven%20Central))](https://search.maven.org/search?q=g:%22com.github.kuzznya%22%20AND%20a:%22exposer-spring-boot-starter%22)

[![codecov](https://codecov.io/gh/kuzznya/exposer/branch/master/graph/badge.svg)](https://codecov.io/gh/kuzznya/exposer)

The aim of this project is to provide a convenient way to configure API
with Spring Boot

This project also allows developers to manage API through 
Spring Cloud Config when using Spring Cloud technologies

## Getting started

1. Add Exposer Spring Boot Starter & Spring Boot Starter Web dependencies to your project

with Maven:
```xml
<dependencies>
    <dependency>
        <groupId>com.github.kuzznya</groupId>
        <artifactId>exposer-spring-boot-starter</artifactId>
        <version>1.1.2</version>
    </dependency>
    
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    
    <!-- ... -->
</dependencies>
```

with Gradle:
```groovy
implementation 'com.github.kuzznya:exposer-spring-boot-starter:1.1.2'
implementation 'org.springframework.boot:spring-boot-starter-web'
```

2. Add `@EnableExposer` annotation on Spring Boot Application class

```java
import com.github.kuzznya.exposer.EnableExposer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableExposer
public class ExposerSampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExposerSampleApplication.class, args);
    }

}
```

3. Configure your API endpoints - see next section

## Configuration

There are two types of configuration

#### 1. Through config file

**Subroute config:**

| Property    | Required | Description                    |
|-------------|----------|--------------------------------|
| `path`      | true     | Relative path of this subroute |
| `routes`    | false    | Nested routes of this subroute |
| `endpoints` | false    | Endpoints of this subroute     |
| `bean`      | false    | Bean that you want to expose   |

**Endpoint config:**

| Property      | Required | Description                               |
|---------------|----------|-------------------------------------------|
| `http-method` | true     | HTTP method of endpoint (GET, POST, etc.) |
| `bean-method` | true     | Name of respective bean method            |
| `bean`        | false    | Bean that you want to expose              |
| `params`      | false    | Bean method params mapping                |

Endpoint `params` config:

To map request param to method argument, you need to set

* in application.yml:

```yaml
  params:
    - <arg_name>: ?<param_name>
```

e.g.:
```yaml
  endpoints:
    - http-method: GET
      bean-method: setValue
      params:
        value: ?val
```

* or in application.properties:

```properties
<...>.params.<arg_name>=?<param_name>
```

e.g.:
```properties
exposer.routes[0].endpoints[0].params.value=?val
```


Without question mark before the param value 
it will be set as a constant value for the argument

If you don't define the endpoint params, 
method params names will be used as request params

Full sample `application.yml`:

```yaml
exposer:
  bean: TestService2
  routes:
    - path: /test
      routes:
        - path: /v1
          bean: TestService
          endpoints:
            - http-method: GET
              bean-method: getValue
            - http-method: POST
              bean-method: setValue
        - path: /v2
          bean: TestService
          endpoints:
            - http-method: GET
              bean-method: setValue
              params:
                value: ?val
      endpoints:
        - http-method: GET
          bean-method: joinTwoArgs
          params:
            arg1: ?val
            arg2: tst
```

Full sample `application.properties`:

```properties
exposer.bean=TestService2
exposer.routes[0].path=/test


exposer.routes[0].routes[0].path=/v1
exposer.routes[0].routes[0].bean=TestService

exposer.routes[0].routes[0].endpoints[0].http-method=GET
exposer.routes[0].routes[0].endpoints[0].bean-method=getValue

exposer.routes[0].routes[0].endpoints[1].http-method=POST
exposer.routes[0].routes[0].endpoints[1].bean-method=setValue


exposer.routes[0].routes[1].path=/v2
exposer.routes[0].routes[1].bean=TestService

exposer.routes[0].routes[1].endpoints[0].http-method=GET
exposer.routes[0].routes[1].endpoints[0].bean-method=getValue
exposer.routes[0].routes[1].endpoints[0].params.value=?val


exposer.routes[0].endpoints[0].http-method=GET
exposer.routes[0].endpoints[0].bean-method=joinTwoArgs
exposer.routes[0].endpoints[0].params.arg1=?val
exposer.routes[0].endpoints[0].params.arg2=tst
```

#### 2. Through code with bean creation

```java
@Configuration
public class ExposerConfig implements ExposerConfigurer {
    @Override
    public ExposerConfiguration configureExposer() {
        return ExposerConfiguration.builder()
                .bean("TestService2")
                .route("/test")
                    .route("/v1")
                        .bean("TestService")
                        .endpoint(RequestMethod.GET, "getValue").and()
                        .endpoint(RequestMethod.POST, "setValue").register()
                    .and()
                    .route("/v2")
                        .endpoint(RequestMethod.GET, "setValue")
                            .bean("TestService").param("value", "?val")
                            .register()
                    .and()
                    .endpoint(RequestMethod.GET, "joinTwoArgs")
                        .param("arg1", "?val").param("arg2", "tst")
                        .register()
                    .add()
                .configure();
    }
}
```

## Sample project

See [exposer-sample](./exposer-sample) project in this repository

Sample config code, `application.yml` & `application.properties` belong to this project
(but defined for corresponding Spring profiles `code`, `yml` & `prop`)

## License

The project is licensed under MIT License

## Contributing

Feel free to open issues and pull requests on GitHub. This is my first public project, 
so please don’t expect enterprise grade support.
