package com.example.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * this class doesn't do anything .. except emitting logging statements.. .. but it will help
 * understand logging across different packages
 */
public class SpringLoggingHelper {

  private static final Logger logger = LoggerFactory.getLogger(SpringLoggingHelper.class);

  /**
   * Helper method.
   */
  public static void helpMethod() {
    logger.debug("This is a debug message");
    logger.info("This is an info message");
    logger.warn("This is a warn message");
    logger.error("This is an error message");
  }
}