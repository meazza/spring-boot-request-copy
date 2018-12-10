### Introduction

This project is help to implement copying Spring mvc controller request to the other address by a given ratio.
The Traditional method to achieve this is using tools like gor or tcpcopy.
If these tools could not be used in your Spring Boot project for some reason, please read the following context and it can help you.

### How to use it

First, add Maven dependencies in your project
```xml
<dependency>
  <groupId>com.github.meazza</groupId>
  <artifactId>spring-boot-request-copy</artifactId>
  <version>1.0.4</version>
</dependency>
```

and then, import configuration classes in main class.
```java
@SpringBootApplication
@RestController
@Import({WebAppConfigurer.class, RequestWrapFilter.class})
public class AnnotationTestApplication { 
 
  public static void main(String[] args) { 
    SpringApplication.run(AnnotationTestApplication.class, args); 
  }
}
```

Now you can add @RequestCopy annotation on the method that you want the requests received by it to be copied.
```java
// This request is copied to url: http://localhost:8080/test by the ratio of 5%
@RequestMapping(value = "/")
@RequestCopy(url = "http://localhost:8080/test", ratio = 0.05F)
  public String hello(@RequestParam(defaultValue = "nobody") String name) {
  System.out.println("hello, " + name);
  return "hello, " + name;
}
``` 

### What can be supported

All GET requests and POST requests whose headers include "Content-Type:application/json"

### Testing the effects

After cloning this whole project, you could test the effects directly by executing command:
```text
mvn spring-boot:run
```

* Send a GET request: http://localhost:8080?name=meazza, and the log shows that this request is copied and send to http://localhost:8080/test:
```text
2018-12-10 09:46:51.768  INFO 10196 --- [nio-8080-exec-7] c.g.m.handler.RequestHandlerInterceptor  : Send copied GET request to url: http://localhost:8080/test?name=meazza, and receive response: hello test, meazza
```
* Send a POST(json) request: http://localhost:8080/post, and the log shows that this request is copied and send to http://localhost:8080/post/test
```text
2018-12-10 09:48:15.747  INFO 10196 --- [nio-8080-exec-3] c.g.m.handler.RequestHandlerInterceptor  : Send copied POST request to url: http://localhost:8080/post/test, body: {"name":"meazza","id":1}, and receive response: hello test, PostBody(id=1, name=meazza)

```