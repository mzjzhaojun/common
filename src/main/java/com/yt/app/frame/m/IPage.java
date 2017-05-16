package com.yt.app.frame.m;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;

public interface IPage<T> extends Serializable {

	List<T> getPageList();

	List<Map<String, Object>> getPageMap();

	HttpHeaders getHeaders();

	int getTotalCount();

	int getNextPageNo();

	int getPreviousPageNo();

	int getPageSize();

	int getPageNo();

	int getPageCount();

}
