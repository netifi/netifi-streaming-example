package com.netifi.acme;

import com.google.protobuf.Empty;
import com.netifi.spring.core.annotation.Group;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Component
public class DefaultAggregatorService implements AggregatorService {
  @Group("netifi.acme.parser")
  ParserServiceClient parserServiceClient;

  @Override
  public Flux<AggregateTotal> aggregateStream(AggregateStreamRequest message, ByteBuf metadata) {
    final int maxSize = Math.max(10, message.getWindowSize());
    final Duration maxTime = Duration.ofMillis(Math.max(10, message.getWindowInMillis()));

    return parserServiceClient
        .parseLogs(Empty.getDefaultInstance())
        .windowTimeout(maxSize, maxTime)
        .flatMap(
            // Window over a given stream of logs
            window ->
                window
                    // Group the streams by customer id
                    .groupBy(ParserResponse::getCustomerId)
                    .flatMap(
                        customerGroup ->
                            customerGroup
                                // Group the customer streams by product id
                                .groupBy(ParserResponse::getProductId)
                                .flatMap(
                                    productGroup ->
                                        // Aggregate total messages and errors in the window
                                        productGroup
                                            .reduceWith(
                                                () ->
                                                    AggregateTotal.newBuilder()
                                                        .setCustomerId(customerGroup.key())
                                                        .setProductId(productGroup.key()),
                                                (builder, parserResponse) -> {
                                                  builder.setTotal(builder.getTotal() + 1);
                                                  if (parserResponse.getResponse().getLevel()
                                                      == LogLevel.ERROR) {
                                                    builder.setErrors(builder.getErrors() + 1);
                                                  }

                                                  return builder;
                                                })
                                            .delayElement(Duration.ofSeconds(1)))))
        .map(AggregateTotal.Builder::build);
  }
}
