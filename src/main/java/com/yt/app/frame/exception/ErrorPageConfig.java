package com.yt.app.frame.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

@Configuration
public class ErrorPageConfig {
	public static Logger logger = LoggerFactory.getLogger(ErrorPageConfig.class);

	@Bean
	public EmbeddedServletContainerCustomizer containerCustomizer() {

		EmbeddedServletContainerCustomizer container = new EmbeddedServletContainerCustomizer() {

			@Override
			public void customize(ConfigurableEmbeddedServletContainer container) {
				container.addErrorPages(new ErrorPage(HttpStatus.BAD_REQUEST, "/error/400"));
				container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/error/404"));
				container.addErrorPages(new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error/500"));
				container.addErrorPages(new ErrorPage(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED, "/error/509"));
			}
		};
		return container;
	}

}