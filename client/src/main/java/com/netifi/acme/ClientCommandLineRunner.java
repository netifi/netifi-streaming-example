package com.netifi.acme;

import com.netifi.spring.core.annotation.Group;
import org.springframework.boot.CommandLineRunner;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicLong;

public class ClientCommandLineRunner implements CommandLineRunner {
  @Group("netifi.acme.aggregator")
  AggregatorServiceClient client;

  @Group("netifi.acme.errors")
  ErrorServiceClient errorServiceClient;

  @Override
  public void run(String... args) throws Exception {
  
    System.out.println();
    System.out.println();
    System.out.println("Getting Errors for the Last 15 seconds");
    System.out.println();
    System.out.println();
    
    /*
    int total =
        client
            .aggregateStream(
                AggregateStreamRequest.newBuilder()
                    .setWindowInMillis(1000)
                    .setWindowSize(10_000)
                    .build())
            .take(Duration.ofSeconds(15))
            .reduce(
                new AtomicInteger(),
                (o, aggregateTotal) -> {
                  o.getAndAdd(aggregateTotal.getErrors());
                  return o;
                })
            .retry()
            .block()
            .get();
     */

    int total =
        errorServiceClient
            .countErrors(
                ErrorsRequest.newBuilder()
                    .setWindowInMillis(1000)
                    .setWindowSize(10_000)
                    .setDuration(5_000)
                    .build())
            .block()
            .getTotal();

    System.out.println();
    System.out.println();
    System.out.println("Total Errors -> " + total);
    System.out.println();
    System.out.println();

    System.exit(0);
  }
}
