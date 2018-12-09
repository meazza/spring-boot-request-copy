package com.github.meazza.handler;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
@WebFilter(filterName = "requestWrapFilter" ,urlPatterns = "/*")
public class RequestWrapFilter implements Filter {

  @Override
  public void init(FilterConfig arg0) {

  }

  @Override
  public void destroy() {

  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response,
                       FilterChain chain) throws IOException, ServletException {
    HttpServletRequest httpServletRequest = (HttpServletRequest) request;
    ServletRequest requestWrapper = new RequestBodyWrapper(httpServletRequest);
    chain.doFilter(requestWrapper, response);
  }
}
