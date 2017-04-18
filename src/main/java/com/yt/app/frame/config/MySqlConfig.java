package com.yt.app.frame.config;

import lombok.Getter;
import lombok.Setter;

import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "mysql", locations = "classpath:config/mysql.properties")
public class MySqlConfig {
	
	private String masterdriver;
	private String masterjdbcurl;
	private String masteruser;
	private String masterpassword;
	
	private String slavedriver;
	private String slavejdbcurl;
	private String slaveuser;
	private String slavepassword;
	
	private String version;

	private int maxActive = 20;
	private int maxIdle = 20;
	private int minIdle = 1;
	private int initialSize = 1;
	private int maxWait = 60000;
	private int timeBetweenEvictionRunsMillis = 60000;
	private int minEictableIdleTimeMillis = 300000;
	private boolean testOnBorrow = false;
	private boolean testOnReturn = false;
	private boolean testWhileIdle = true;
	private String validationQuery;
	private boolean poolPreparedStatements = false;
	private int maxOpenPreparedStatements = 20;
}