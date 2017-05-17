package com.yt.app.frame.h;

import java.io.IOException;

import com.yt.app.frame.f.PageBeanEnum;
import com.yt.app.frame.f.RequestMethodEnums;
import com.yt.app.frame.k.Au;
import com.yt.app.frame.p.CusAccessObjectUtil;
import com.yt.app.frame.p.ReaderUtil;

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

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class Ao extends GenericFilterBean {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	public void doFilter(ServletRequest paramServletRequest, ServletResponse paramServletResponse, FilterChain paramFilterChain) throws IOException,
			ServletException {
		HttpServletRequest localHttpServletRequest = (HttpServletRequest) paramServletRequest;
		long l1 = System.currentTimeMillis();
		if ((localHttpServletRequest.getMethod().equals(RequestMethodEnums.POST.getValue()))
				&& (localHttpServletRequest.getRequestURI().indexOf("/file") == -1)) {
			Au localBodyReaderHttpServletRequestWrapper = new Au(
					localHttpServletRequest);
			this.logger.info(String.format("===>>>>>> ip : %s", new Object[] { CusAccessObjectUtil.getIpAddress(localHttpServletRequest) }));
			this.logger.info(String.format("===>>>>>> url : %s", new Object[] { localHttpServletRequest.getRequestURI() }));
			this.logger.info(String.format("===>>>>>> method : %s", new Object[] { localHttpServletRequest.getMethod() }));
			this.logger.info(String.format("===>>>>>> args : %s",
					new Object[] { ReaderUtil.getBodyString(localBodyReaderHttpServletRequestWrapper.getReader()) }));
			this.logger.info(String.format(
					"===>>>>>> pageno : %s",
					new Object[] { localHttpServletRequest.getHeader(PageBeanEnum.PAGENO.getValue()) == null ? "" : localHttpServletRequest
							.getHeader(PageBeanEnum.PAGENO.getValue()) }));
			this.logger.info(String.format(
					"===>>>>>> orderby : %s",
					new Object[] { localHttpServletRequest.getHeader(PageBeanEnum.ORDERBY.getValue()) == null ? "" : localHttpServletRequest
							.getHeader(PageBeanEnum.ORDERBY.getValue()) }));
			this.logger.info(String.format(
					"===>>>>>> dir : %s",
					new Object[] { localHttpServletRequest.getHeader(PageBeanEnum.DIR.getValue()) == null ? "" : localHttpServletRequest
							.getHeader(PageBeanEnum.DIR.getValue()) }));
			paramFilterChain.doFilter(localBodyReaderHttpServletRequestWrapper, paramServletResponse);
		} else {
			this.logger.info(String.format("===>>>>>>  ip:%s  url:%s  method:%s ",
					new Object[] { CusAccessObjectUtil.getIpAddress(localHttpServletRequest), localHttpServletRequest.getRequestURI(),
							localHttpServletRequest.getMethod() }));
			paramFilterChain.doFilter(paramServletRequest, paramServletResponse);
		}
		long l2 = System.currentTimeMillis();
		this.logger.info(String.format("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< url:%s  >>> %d ms", new Object[] { localHttpServletRequest.getRequestURI(),
				Long.valueOf(l2 - l1) }));
	}
}