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

	@RequestMapping({ "/error/404" })
	public String a(RequestEntity<Object> paramRequestEntity) {
		this.d.setStatus("/error/404");
		return "static/resource/error/404";
	}

	@RequestMapping({ "/error/400" })
	public String b(RequestEntity<Object> paramRequestEntity) {
		this.d.setStatus("/error/400");
		return "static/resource/error/400";
	}

	@RequestMapping({ "/error/500" })
	public String c(RequestEntity<Object> paramRequestEntity) {
		this.d.setStatus("/error/500");
		return "static/resource/error/500";
	}

	@RequestMapping({ "/error/509" })
	public String d(RequestEntity<Object> paramRequestEntity) {
		this.d.setStatus("/error/509");
		return "static/resource/error/500";
	}

}
