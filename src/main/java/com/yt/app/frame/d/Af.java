package com.yt.app.frame.d;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.CookieHttpSessionStrategy;
import org.springframework.session.web.http.HttpSessionStrategy;

@Configuration
public class Af {
	@Bean
	public HttpSessionStrategy httpSessionStrategy() {
//		return new Az(new HeaderContentNegotiationStrategy());
		return new CookieHttpSessionStrategy();
	}

}
