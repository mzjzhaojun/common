package com.yt.app.frame.c;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "app", locations = "classpath:config/app.properties")
public class AppConfig {

	private String workerId;

	private String workerKey;

	private String filePath;

	private String url;

	private String origin;

	private List<String> origins;

	private String Status = "";

	public void setOrigin(String origin) {
		this.origins = Arrays.asList(origin.split(","));
		this.origin = origin;
	}
}