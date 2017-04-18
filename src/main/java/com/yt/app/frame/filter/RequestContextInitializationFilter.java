package com.yt.app.frame.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import com.yt.app.enums.RequestMethodEnums;
import com.yt.app.modul.BodyReaderHttpServletRequestWrapper;
import com.yt.app.util.CusAccessObjectUtil;
import com.yt.app.util.ReaderUtil;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestContextInitializationFilter extends GenericFilterBean {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		long s = System.currentTimeMillis();
		if (request.getMethod().equals(RequestMethodEnums.POST.getValue()) && request.getRequestURI().indexOf("/file") == -1) {
			ServletRequest requestWrapper = new BodyReaderHttpServletRequestWrapper(request);
			logger.info(String.format("===>>>>>>   ip:%s  url:%s  method:%s  args:%s", CusAccessObjectUtil.getIpAddress(request),
					request.getRequestURI(), request.getMethod(), ReaderUtil.getBodyString(requestWrapper.getReader())));
			chain.doFilter(requestWrapper, resp);
		} else {
			logger.info(String.format("===>>>>>>   ip:%s  url:%s  method:%s ", CusAccessObjectUtil.getIpAddress(request),
					request.getRequestURI(), request.getMethod()));
			chain.doFilter(req, resp);
		}
		long e = System.currentTimeMillis();
		logger.info(String.format("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< url:%s  >>> %d ms", request.getRequestURI(), (e - s)));
	}
}
