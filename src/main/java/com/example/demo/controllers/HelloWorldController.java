package com.example.demo.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  @RequestMapping("/")
  public String hello() {
    log.info("say hello.. ");
    return "hello world";
  }
}
