package com.github.kuzznya.exposer.sample;

import com.github.kuzznya.exposer.EnableExposer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableExposer
public class ExposerCoreSampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExposerCoreSampleApplication.class, args);
    }

}
