package com.example.demo;

import com.example.helper.SpringLoggingHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * In Spring's approach to building RESTful web services, HTTP requests are handled by a controller.
 * These components are easily identified by the @RestController annotation,
 * and the @RestController annotation handles GET requests for '/greeting' by
 * returning a new instance fo the Greeting class.
 */
@SpringBootApplication
@EnableAutoConfiguration
public class DemoApplication {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public static void main(String[] args) {
        SpringLoggingHelper.helpMethod();
        SpringApplication.run(DemoApplication.class, args);
    }
}
