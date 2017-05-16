package com.yt.app.frame.d;

import com.yt.app.frame.c.AppConfig;
import com.yt.app.frame.i.Ar;

import javax.servlet.MultipartConfigElement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.web.servlet.DispatcherServlet;

@Configuration
public class Ad
{

  @Autowired
  AppConfig fconfig;

  @Bean
  public Ar d()
  {
    return new Ar(Long.parseLong(this.fconfig.getWorkerId()), Long.parseLong(this.fconfig.getWorkerKey()));
  }

  @Bean
  public StandardPasswordEncoder e() {
    return new StandardPasswordEncoder();
  }

  @Bean
  public Ah f() {
    return new Ah();
  }

  @Bean
  public MultipartConfigElement multipartConfigElement() {
    MultipartConfigFactory localMultipartConfigFactory = new MultipartConfigFactory();
    localMultipartConfigFactory.setMaxFileSize("2048MB");
    localMultipartConfigFactory.setMaxRequestSize("2048MB");
    return localMultipartConfigFactory.createMultipartConfig();
  }

  @Bean
  public DispatcherServlet dispatcherServlet()
  {
    DispatcherServlet localDispatcherServlet = new DispatcherServlet();
    localDispatcherServlet.setDispatchOptionsRequest(true);
    localDispatcherServlet.setThrowExceptionIfNoHandlerFound(true);
    return localDispatcherServlet;
  }
}