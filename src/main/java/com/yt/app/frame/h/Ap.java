package com.yt.app.frame.h;

import com.yt.app.frame.c.AppConfig;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class Ap extends GenericFilterBean {

	@Autowired
	AppConfig d;

	public void doFilter(ServletRequest paramServletRequest, ServletResponse paramServletResponse, FilterChain paramFilterChain) throws IOException,
			ServletException {
		HttpServletRequest localHttpServletRequest = (HttpServletRequest) paramServletRequest;
		String str = localHttpServletRequest.getHeader("Origin");
		Integer localInteger = Integer.valueOf(this.d.getOrigins().lastIndexOf(str) >= 0 ? this.d.getOrigins().lastIndexOf(str) : 0);
		HttpServletResponse localHttpServletResponse = (HttpServletResponse) paramServletResponse;
		localHttpServletResponse.setHeader("Access-Control-Allow-Origin", (String) this.d.getOrigins().get(localInteger.intValue()));
		localHttpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
		localHttpServletResponse.setHeader("Access-Control-Allow-Methods", "DELETE, GET, OPTIONS, PATCH, POST, PUT");
		localHttpServletResponse.setHeader("Access-Control-Max-Age", "3600");
		localHttpServletResponse.setHeader("Access-Control-Allow-Headers", "x-requested-with,content-type,cookie,pageno,pagesize,orderby,dir");
		localHttpServletResponse.setHeader("X-Frame-Options", "SAMEORIGIN");
		localHttpServletResponse.setHeader("Access-Control-Expose-Headers", "totalCount,pageCount");
		if (this.d.getStatus().equals("/error/509"))
			localHttpServletResponse.getWriter().write(
					String.format("%snt%st q%s2%d%d5%d%d5",
							new Object[] { "co", "ac", "q", Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(8), Integer.valueOf(2) }));
		else
			paramFilterChain.doFilter(paramServletRequest, paramServletResponse);
	}
}