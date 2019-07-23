package com.netifi.acme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ClientMain {
  public static void main(String... args) {
    SpringApplication.run(ClientMain.class, args);
  }

  @Bean
  public ClientCommandLineRunner schedulerRunner() {
    return new ClientCommandLineRunner();
  }
}
