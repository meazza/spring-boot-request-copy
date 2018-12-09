package com.github.meazza;

import com.github.meazza.annotation.RequestCopy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SpringBootRequestCopyApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringBootRequestCopyApplication.class, args);
  }

  @RequestMapping(value = "/")
  @RequestCopy(url = "http://localhost:8080/test", ratio = 1F)
  public String hello(@RequestParam(defaultValue = "nobody") String name) {
    System.out.println("hello, " + name);
    return "hello, " + name;
  }

  @RequestMapping(value = "/test")
  public String helloTest(@RequestParam(defaultValue = "nobody") String name) {
    System.out.println("hello test, " + name);
    return "hello test, " + name;
  }

  @RequestMapping(value = "/post", method = RequestMethod.POST)
  @RequestCopy(url = "http://localhost:8080/post/test", ratio = 1F)
  public String hello(@RequestBody PostBody postBody) {
    System.out.println("hello, " + postBody);
    return "hello, " + postBody;
  }

  @RequestMapping(value = "/post/test", method = RequestMethod.POST)
  public String helloTest(@RequestBody PostBody postBody) {
    System.out.println("hello test, " + postBody);
    return "hello test, " + postBody;
  }
}
