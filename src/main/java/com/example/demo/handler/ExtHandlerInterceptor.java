package com.example.demo.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.Random;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

public class ExtHandlerInterceptor implements HandlerInterceptor {

  private RestTemplate restTemplate = new RestTemplate();

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    HandlerMethod handlerMethod = (HandlerMethod) handler;
    Method method = handlerMethod.getMethod();
    RequestCopy requestCopy = method.getAnnotation(RequestCopy.class);
    if (requestCopy != null) {
      String url = requestCopy.url();
      float ratio = requestCopy.ratio();
      if (new Random().nextFloat() <= ratio) {
        switch (request.getMethod()) {
          case "GET": {
            new Thread(() -> {
              String result = restTemplate.getForObject(
                  url + "?" + Optional.ofNullable(request.getQueryString()).orElse(""),
                  String.class);
              System.out.println(result);
            }).run();
            break;
          }
          case "POST": {
            switch (request.getHeader("Content-Type")) {
              case "application/json": {
                ExtRequestWrapper requestWrapper = new ExtRequestWrapper(request);
                JSONObject jsonObject = JSON.parseObject(requestWrapper.getBody());
                if (jsonObject != null) {
                  new Thread(() -> {
                    String result = restTemplate.postForObject(
                        url + "?" + Optional.ofNullable(request.getQueryString()).orElse(""),
                        jsonObject, String.class);
                    System.out.println(result);
                  }).run();
                }
                break;
              }
            }
            break;
          }
        }
      }
    }
    return true;
  }
}
