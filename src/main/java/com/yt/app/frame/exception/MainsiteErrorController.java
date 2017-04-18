package com.yt.app.frame.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yt.app.frame.config.AppConfig;

@Controller
public class MainsiteErrorController {

	@Autowired
	AppConfig appconfig;

	private static final String NOT_FOUND = "/error/404";

	private static final String ERROR = "/error/400";

	private static final String INTERNAL_SERVER_ERROR = "/error/500";

	private static final String BANDWIDTH_LIMIT_EXCEEDED = "/error/509";

	@RequestMapping(value = NOT_FOUND)
	public String handle404Error(RequestEntity<Object> requestEntity) {
		appconfig.setStatus(NOT_FOUND);
		return "static/resource/error/404";
	}

	@RequestMapping(value = ERROR)
	public String handle400Error(RequestEntity<Object> requestEntity) {
		appconfig.setStatus(ERROR);
		return "static/resource/error/400";
	}

	@RequestMapping(value = INTERNAL_SERVER_ERROR)
	public String handle500Error(RequestEntity<Object> requestEntity) {
		appconfig.setStatus(INTERNAL_SERVER_ERROR);
		return "static/resource/error/500";
	}

	@RequestMapping(value = BANDWIDTH_LIMIT_EXCEEDED)
	public String handle509Error(RequestEntity<Object> requestEntity) {
		appconfig.setStatus(BANDWIDTH_LIMIT_EXCEEDED);
		return "static/resource/error/500";
	}

}
