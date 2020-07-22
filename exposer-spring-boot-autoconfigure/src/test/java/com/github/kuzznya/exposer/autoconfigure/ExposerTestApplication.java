package com.github.kuzznya.exposer.autoconfigure;

import com.github.kuzznya.exposer.EnableExposer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableExposer
public class ExposerTestApplication {
    public static void main(String... args) {
        SpringApplication.run(ExposerTestApplication.class, args);
    }
}
