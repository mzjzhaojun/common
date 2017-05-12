package com.yt.app.frame.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;

import com.yt.app.enums.PageBeanEnum;
import com.yt.app.util.StringUtil;

public class PageBean<T> implements IPage<T>, Serializable {

	private static final long serialVersionUID = 1L;

	private static int defaultPageSize = 10;

	private List<T> list;

	private List<Map<String, Object>> map;

	private int pageSize;

	private int pageNo;

	private int totalCount = 0;

	public PageBean(List<T> elements) {
		this.list = elements;
	}

	public PageBean(Map<String, Object> param, List<T> elements, int totalCount) {
		this.pageNo = param.get(PageBeanEnum.PAGENO.getValue()) == null ? 0 : Integer.parseInt(param.get(PageBeanEnum.PAGENO.getValue()).toString());
		this.pageSize = param.get(PageBeanEnum.PAGESIZE.getValue()) == null ? 0 : Integer.parseInt(param.get(PageBeanEnum.PAGESIZE.getValue())
				.toString());
		if (elements == null)
			elements = new ArrayList<>();
		this.list = elements;
		this.totalCount = totalCount;
	}

	public PageBean(Map<String, Object> param, int totalCount, List<Map<String, Object>> elements) {
		this.pageNo = param.get(PageBeanEnum.PAGENO.getValue()) == null ? 0 : Integer.parseInt(param.get(PageBeanEnum.PAGENO.getValue()).toString());
		this.pageSize = param.get(PageBeanEnum.PAGESIZE.getValue()) == null ? 0 : Integer.parseInt(param.get(PageBeanEnum.PAGESIZE.getValue())
				.toString());
		if (elements == null)
			elements = new ArrayList<>();
		this.map = elements;
		this.totalCount = totalCount;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static final PageBean EMPTY_PAGE = new PageBean(Collections.emptyList());

	public List<T> getPageList() {
		return list;
	}

	public List<Map<String, Object>> getPageMap() {
		return map;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public int getNextPageNo() {
		return getPageNo() + 1;
	}

	public int getPreviousPageNo() {
		return getPageNo() - 1;
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getPageNo() {
		return pageNo;
	}

	public int getPageCount() {
		if (totalCount == 0)
			return totalCount;
		return (totalCount + pageSize - 1) / pageSize;
	}

	public static int getStartOfPage(int pageNo, int pageSize) {
		int startIndex = (pageNo - 1) * pageSize + 1;
		if (startIndex < 1)
			startIndex = 1;
		return startIndex;
	}

	public static int getDefaultPageSize() {
		return defaultPageSize;
	}

	public HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.set(PageBeanEnum.TOTALCOUNT.getValue(), String.valueOf(getTotalCount()));
		headers.set(PageBeanEnum.PAGECOUNT.getValue(), String.valueOf(getPageCount()));
		return headers;
	}

	public static boolean isPaging(Map<String, Object> param) {
		if (param.get(PageBeanEnum.PAGENO.getValue()) != null && StringUtil.checkNotEmpty(param.get(PageBeanEnum.PAGENO.getValue()).toString()))
			return true;
		else
			return false;
	}
}
