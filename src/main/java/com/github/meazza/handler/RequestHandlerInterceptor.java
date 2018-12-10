package com.github.meazza.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.meazza.annotation.RequestCopy;
import java.lang.reflect.Method;
import java.util.Random;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
public class RequestHandlerInterceptor implements HandlerInterceptor {

  private static final Logger logger = LoggerFactory.getLogger(RequestHandlerInterceptor.class);
  private RestTemplate restTemplate = new RestTemplate();

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    if (!(handler instanceof HandlerMethod)) {
      return true;
    }
    HandlerMethod handlerMethod = (HandlerMethod) handler;
    Method method = handlerMethod.getMethod();
    RequestCopy requestCopy = method.getAnnotation(RequestCopy.class);
    if (requestCopy != null) {
      String url = requestCopy.url() + request.getRequestURI();
      float ratio = requestCopy.ratio();
      if (new Random().nextFloat() <= ratio) {
        switch (request.getMethod()) {
          case "GET": {
            new Thread(() -> {
              String fullUrl = url + (request.getQueryString() == null ? "" : "?" + request.getQueryString());
              String result = restTemplate.getForObject(fullUrl, String.class);
              logger.info("Send copied GET request to url: {}, and receive response: {}", fullUrl, result);
            }).start();
            break;
          }
          case "POST": {
            switch (request.getHeader("Content-Type")) {
              case "application/json": {
                RequestBodyWrapper requestWrapper = new RequestBodyWrapper(request);
                JSONObject jsonObject = JSON.parseObject(requestWrapper.getBody());
                if (jsonObject != null) {
                  new Thread(() -> {
                    String fullUrl = url + (request.getQueryString() == null ? "" : "?" + request.getQueryString());
                    String result = restTemplate.postForObject(fullUrl, jsonObject, String.class);
                    logger.info("Send copied POST request to url: {}, body: {}, and receive response: {}", fullUrl,
                        jsonObject, result);
                  }).start();
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

  @Override
  public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o,
      ModelAndView modelAndView) throws Exception {

  }

  @Override
  public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o,
      Exception e) throws Exception {

  }
}
