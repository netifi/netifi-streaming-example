package com.netifi.acme;

import com.google.protobuf.Empty;
import com.netifi.spring.core.annotation.Group;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class DefaultParserService implements ParserService {
  private static final Logger logger = LoggerFactory.getLogger(DefaultParserService.class);

  @Group("netifi.acme.logs")
  LogServiceClient logService;

  @Override
  public Flux<ParserResponse> parseLogs(Empty message, ByteBuf metadata) {
    return logService
        .streamLogs(message)
        .map(
            logResponse -> {
              String customerId = getCustomerId(logResponse.getMessage());
              String productId = getProductId(logResponse.getMessage());
              return ParserResponse.newBuilder()
                  .setCustomerId(customerId)
                  .setProductId(productId)
                  .setResponse(logResponse)
                  .build();
            });
  }

  @Override
  public Flux<ParserResponse> filterByProduct(FilterRequest message, ByteBuf metadata) {
    return parseLogs(Empty.getDefaultInstance(), Unpooled.EMPTY_BUFFER)
        .filter(parserResponse -> parserResponse.getProductId().equals(message.getProductId()));
  }

  private String getCustomerId(String message) {
    String customerId = null;
    String[] split = message.split(",");
    for (String s : split) {
      if (s.startsWith("customer_id")) {
        customerId = s.split("=")[1];
      }
    }

    return customerId;
  }

  private String getProductId(String message) {
    String customerId = null;
    String[] split = message.split(",");
    for (String s : split) {
      if (s.startsWith("product_id")) {
        customerId = s.split("=")[1];
      }
    }

    return customerId;
  }
}
