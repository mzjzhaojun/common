package com.yt.app.frame.d;

import java.util.Arrays;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.session.Session;
import org.springframework.session.web.http.CookieHttpSessionStrategy;
import org.springframework.session.web.http.HeaderHttpSessionStrategy;
import org.springframework.session.web.http.HttpSessionStrategy;
import org.springframework.web.accept.ContentNegotiationStrategy;

public class Az implements HttpSessionStrategy {

	private HttpSessionStrategy browser;
	private HttpSessionStrategy api;
	private RequestMatcher browserMatcher;

	@Autowired
	public Az(ContentNegotiationStrategy contentNegotiationStrategy) {
		this(new CookieHttpSessionStrategy(), new HeaderHttpSessionStrategy());
		MediaTypeRequestMatcher matcher = new MediaTypeRequestMatcher(contentNegotiationStrategy, Arrays.asList(MediaType.TEXT_HTML));
		matcher.setIgnoredMediaTypes(Collections.singleton(MediaType.ALL));
		RequestHeaderRequestMatcher javascript = new RequestHeaderRequestMatcher("X-Requested-With");
		this.browserMatcher = new OrRequestMatcher(Arrays.asList(matcher, javascript));
	}

	public Az(HttpSessionStrategy browser, HttpSessionStrategy api) {
		this.browser = browser;
		this.api = api;
	}

	@Override
	public String getRequestedSessionId(HttpServletRequest request) {
		return getStrategy(request).getRequestedSessionId(request);
	}

	@Override
	public void onNewSession(Session session, HttpServletRequest request, HttpServletResponse response) {
		getStrategy(request).onNewSession(session, request, response);
	}

	@Override
	public void onInvalidateSession(HttpServletRequest request, HttpServletResponse response) {
		getStrategy(request).onInvalidateSession(request, response);
	}

	private HttpSessionStrategy getStrategy(HttpServletRequest request) {
		return this.browserMatcher.matches(request) ? this.browser : this.api;
	}
}
