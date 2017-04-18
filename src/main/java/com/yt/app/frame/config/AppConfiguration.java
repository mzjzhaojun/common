package com.yt.app.frame.config;

import javax.servlet.MultipartConfigElement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.web.servlet.DispatcherServlet;

import com.yt.app.frame.id.IdWorker;

@Configuration
public class AppConfiguration {

	@Autowired
	AppConfig appConfig;

	@Bean
	public IdWorker IdWorker() {
		return new IdWorker(Long.parseLong(appConfig.getWorkerId()));
	}

	@Bean
	public StandardPasswordEncoder standardPasswordEncoder() {
		return new StandardPasswordEncoder();
	}

	@Bean
	public PasswordEncoders passwordEncoders() {
		return new PasswordEncoders();
	}

	@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		factory.setMaxFileSize("2048MB");
		factory.setMaxRequestSize("2048MB");
		return factory.createMultipartConfig();
	}

	// @Bean(name = "multipartResolver")
	// public MultipartResolver multipartResolver() {
	// // YtMultipartResolver resolver = new YtMultipartResolver();
	// CommonsMultipartResolver resolver = new CommonsMultipartResolver();
	// resolver.setDefaultEncoding("UTF-8");
	// resolver.setResolveLazily(true);
	// resolver.setMaxInMemorySize(-1);
	// resolver.setMaxUploadSize(1024 * 1024 * 1024);
	// return resolver;
	// }

	// @Bean
	// public ServletRegistrationBean dispatcherRegistration(DispatcherServlet
	// dispatcherServlet) {
	// ServletRegistrationBean registration = new
	// ServletRegistrationBean(dispatcherServlet);
	// registration.getUrlMappings().clear();
	// registration.addUrlMappings("*.do");
	// return registration;
	// }

	@Bean
	public DispatcherServlet dispatcherServlet() {
		DispatcherServlet servlet = new DispatcherServlet();
		servlet.setDispatchOptionsRequest(true);
		servlet.setThrowExceptionIfNoHandlerFound(true);
		return servlet;
	}

}