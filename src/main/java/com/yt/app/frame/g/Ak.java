package com.yt.app.frame.g;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

@Configuration
public class Ak {
	public static Logger logger = LoggerFactory.getLogger(Ak.class);

	@Bean
	public EmbeddedServletContainerCustomizer containerCustomizer() {

		EmbeddedServletContainerCustomizer container = new EmbeddedServletContainerCustomizer() {

			@Override
			public void customize(ConfigurableEmbeddedServletContainer container) {
				container.addErrorPages(new ErrorPage(HttpStatus.UNAUTHORIZED, "/static/project/login/html/index.html"));
				container.addErrorPages(new ErrorPage(HttpStatus.FORBIDDEN, "/static/project/login/html/index.html"));
				container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/static/resource/error/404.html"));
				container.addErrorPages(new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/static/resource/error/500.html"));
			}
		};
		return container;
	}

}