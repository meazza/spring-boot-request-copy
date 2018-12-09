package com.github.meazza.handler;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@ConditionalOnProperty(name = "request-copy", havingValue = "on")
public class WebAppConfigurer extends WebMvcConfigurerAdapter {

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new ExtHandlerInterceptor()).addPathPatterns("/**");
    super.addInterceptors(registry);
  }
}
