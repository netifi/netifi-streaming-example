package com.netifi.acme;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class DefaultParserServiceTest {
  @Test
  public void test() {
    //String matcher = "(?<=is \\()(.*?)(?=\\s*\\))"
    /*
    /begin.*?end/s
(?s)begin.*?end
     */
    //String matcher = "(?s)customer_id=.*?,p";
    String matcher = "(?<=customer_id=)(.*)(?=,p)";
    Pattern compile = Pattern.compile(matcher);
    Matcher matcher1 = compile.matcher("customer_id=cust1,product_id=product-b,some-data3");
    String group = matcher1.group(1);
    System.out.println(group);
  }
}
