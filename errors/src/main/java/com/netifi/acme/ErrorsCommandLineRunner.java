package com.netifi.acme;

import com.netifi.spring.core.annotation.Group;
import org.springframework.boot.CommandLineRunner;

import java.time.Duration;

public class ErrorsCommandLineRunner implements CommandLineRunner {
  @Group("netifi.acme.aggregator")
  AggregatorServiceClient client;

  @Override
  public void run(String... args) throws Exception {
    client
        .aggregateStream(
            AggregateStreamRequest.newBuilder().setWindowInMillis(250).setWindowSize(100).build())
        .take(Duration.ofSeconds(5))
        .toStream()
        .forEach(
            aggregateTotal -> {
              System.out.println(aggregateTotal.toString());
            });
  }
}
