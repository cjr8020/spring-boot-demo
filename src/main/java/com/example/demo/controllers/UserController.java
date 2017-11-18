package com.example.demo.controllers;

import com.example.helper.SpringLoggingHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * In Spring's approach to building RESTful web services, HTTP requests are handled by a controller.
 * These components are easily identified by the @RestController annotation,
 * and the @RestController annotation handles GET requests for '/greeting' by
 * returning a new instance fo the Greeting class.
 */
@RestController
public class UserController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("/users")
    public @ResponseBody String getUsers() {

        log.info("gimme users!");

        SpringLoggingHelper.helpMethod();

        return "{\"users\":[{\"firstname\":\"Richard\", \"lastname\":\"Feynman\"}," +
                "{\"firstname\":\"Marie\",\"lastname\":\"Curie\"}]}";
    }
}
