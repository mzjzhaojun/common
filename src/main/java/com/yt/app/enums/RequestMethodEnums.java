package com.yt.app.enums;

public enum RequestMethodEnums {

	GET("GET"), LIST("LIST"), POST("POST"), PUT("PUT"), DELETE("DELETE"), NULL("NULL");

	private String methodtype;

	RequestMethodEnums(String methodtype) {
		this.methodtype = methodtype;
	}

	public String getValue() {
		return methodtype;
	}
}
