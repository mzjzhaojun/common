package com.yt.app.frame.g;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yt.app.frame.c.AppConfig;

@Controller
public class Am {
	@Autowired
	AppConfig d;

	@RequestMapping({ "/error/401" })
	public String a(RequestEntity<Object> paramRequestEntity) {
		return "static/project/login/html/401";
	}

	@RequestMapping({ "/error/404" })
	public String b(RequestEntity<Object> paramRequestEntity) {
		return "static/resource/error/400";
	}

	@RequestMapping({ "/error/500" })
	public String c(RequestEntity<Object> paramRequestEntity) {
		return "static/resource/error/500";
	}

}
