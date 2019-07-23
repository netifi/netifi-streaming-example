package com.netifi.acme;

import com.netifi.spring.core.annotation.Group;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

@Component("localErrorService")
public class LocalErrorService implements ErrorService {
  @Group("netifi.acme.aggregator")
  AggregatorServiceClient client;
  
  @Override
  public Mono<ErrorResponse> countErrors(ErrorsRequest message, ByteBuf metadata) {
    System.out.println(">>>>>>>>>>>>>>>> counting errors");
    return client
             .aggregateStream(
               AggregateStreamRequest.newBuilder()
                 .setWindowSize(message.getWindowSize())
                 .setWindowInMillis(message.getWindowInMillis())
                 .build())
             .take(Duration.ofMillis(message.getDuration()))
             .reduce(
               new AtomicInteger(),
               (o, aggregateTotal) -> {
                 o.getAndAdd(aggregateTotal.getErrors());
                 return o;
               })
             .map(l -> ErrorResponse.newBuilder().setTotal(l.get()).build());
  }
}
