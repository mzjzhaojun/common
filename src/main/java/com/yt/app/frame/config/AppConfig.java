package com.yt.app.frame.config;

import java.util.Arrays;
import java.util.List;


import org.springframework.boot.context.properties.ConfigurationProperties;

/*@Setter
@Getter*/
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

	public String getWorkerId() {
		return workerId;
	}

	public void setWorkerId(String workerId) {
		this.workerId = workerId;
	}

	public String getWorkerKey() {
		return workerKey;
	}

	public void setWorkerKey(String workerKey) {
		this.workerKey = workerKey;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<String> getOrigins() {
		return origins;
	}

	public void setOrigins(List<String> origins) {
		this.origins = origins;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public String getOrigin() {
		return origin;
	}
	
	

}