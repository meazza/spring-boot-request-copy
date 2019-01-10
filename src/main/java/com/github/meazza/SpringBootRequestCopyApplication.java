package com.github.meazza;

import com.github.meazza.annotation.RequestCopy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
  public String hello(@RequestParam(defaultValue = "nobody") String name,
      @RequestHeader(name = "id", required = false, defaultValue = "123") String id) {
    System.out.println("hello " + id + ", " + name);
    return "hello " + id + ", " + name;
  }

  @RequestMapping(value = "/test")
  public String helloTest(@RequestParam(defaultValue = "nobody") String name,
      @RequestHeader(name = "id", required = false, defaultValue = "123") String id) {
    System.out.println("hello test " + id + ", " + name);
    return "hello test " + id + ", " + name;
  }

  @RequestMapping(value = "/post", method = RequestMethod.POST)
  @RequestCopy(url = "http://localhost:8080/test", ratio = 1F)
  public String hello(@RequestBody PostBody postBody,
      @RequestHeader(name = "id", required = false, defaultValue = "123") String id) {
    System.out.println("hello " + id + ", " + postBody);
    return "hello " + id + ", " + postBody;
  }

  @RequestMapping(value = "/test/post", method = RequestMethod.POST)
  public String helloTest(@RequestBody PostBody postBody,
      @RequestHeader(name = "id", required = false, defaultValue = "123") String id) {
    System.out.println("hello test " + id + ", " + postBody);
    return "hello test" + id + ", " + postBody;
  }
}
