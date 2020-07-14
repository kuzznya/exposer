package com.kuzznya.exposer_sample;

import com.kuzznya.exposer.EnableExposer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableExposer
public class ExposerSampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExposerSampleApplication.class, args);
    }

}
