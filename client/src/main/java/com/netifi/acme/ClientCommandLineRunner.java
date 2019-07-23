package com.netifi.acme;

import com.netifi.spring.core.annotation.Group;
import io.netty.buffer.Unpooled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ClientCommandLineRunner implements CommandLineRunner {
  @Autowired
  @Qualifier("localErrorService")
  ErrorService errorService;

  @Override
  public void run(String... args) throws Exception {

    System.out.println();
    System.out.println();
    System.out.println("Getting Errors for the Last 15 seconds");
    System.out.println();
    System.out.println();

    int total =
        errorService
            .countErrors(
                ErrorsRequest.newBuilder()
                    .setWindowInMillis(1000)
                    .setWindowSize(10_000)
                    .setDuration(5_000)
                    .build(),
                Unpooled.EMPTY_BUFFER)
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
