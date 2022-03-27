package com.assignment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CountryServiceApplication {

    public static void main(final String[] args) {
        final SpringApplication app = new SpringApplication(CountryServiceApplication.class);
        // Need to uncomment below if startup actuator endpoint need to be enabled.
        // app.setApplicationStartup(new BufferingApplicationStartup(2048));
        app.run(args);
    }

}
