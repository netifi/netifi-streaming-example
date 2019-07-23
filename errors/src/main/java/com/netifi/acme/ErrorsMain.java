package com.netifi.acme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ErrorsMain {
  public static void main(String... args) {
    SpringApplication.run(ErrorsMain.class, args);
  }

  @Bean
  public ErrorsCommandLineRunner schedulerRunner() {
    return new ErrorsCommandLineRunner();
  }
}
