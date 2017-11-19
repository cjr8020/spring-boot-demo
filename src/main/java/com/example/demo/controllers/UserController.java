package com.example.demo.controllers;

import com.example.demo.user.ApplicationUser;
import com.example.demo.user.ApplicationUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

  private static final Logger log = LoggerFactory.getLogger(UserController.class);

  private ApplicationUserRepository applicationUserRepository;
  private BCryptPasswordEncoder binCryptPasswordEncoder;

  public UserController(ApplicationUserRepository applicationUserRepository,
      BCryptPasswordEncoder binCryptPasswordEncoder) {
    this.applicationUserRepository = applicationUserRepository;
    this.binCryptPasswordEncoder = binCryptPasswordEncoder;
  }

  /**
   * User self service.
   * @param user user self-registration
   */
  @PostMapping("/sign-up")
  public void signUp(@RequestBody ApplicationUser user) {
    log.info("User {} signed up.", user.getUsername());
    user.setPassword(binCryptPasswordEncoder.encode(user.getPassword()));
    applicationUserRepository.save(user);
  }
}
