package com.yt.app.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.RequestEntity;

import com.yt.app.enums.PageBeanEnum;
import com.yt.app.frame.page.PageBean;

public class RequestUtil {

	@SuppressWarnings("unchecked")
	public static Map<String, Object> requestEntityToParamMap(RequestEntity<Object> requestEntity) {
		Map<String, Object> map = requestEntity.getBody() == null ? new HashMap<String, Object>() : (Map<String, Object>) requestEntity.getBody();
		String pageNo = requestEntity.getHeaders().getFirst(PageBeanEnum.PAGENO.getValue());
		String orderby = requestEntity.getHeaders().getFirst(PageBeanEnum.ORDERBY.getValue());
		Integer start;
		Integer calculatePageSizes;
		if (orderby != null && StringUtil.checkNotEmpty(orderby)) {
			map.put(PageBeanEnum.ORDERBY.getValue(), orderby);
			String dir = requestEntity.getHeaders().getFirst(PageBeanEnum.DIR.getValue());
			if (dir == null || !StringUtil.checkNotEmpty(dir))
				map.put(PageBeanEnum.DIR.getValue(), PageBeanEnum.ASC.getValue());
			else
				map.put(PageBeanEnum.DIR.getValue(), dir);
		}
		if (pageNo == null || !StringUtil.checkNotEmpty(pageNo)) {
			return map;
		}
		String pageSize = requestEntity.getHeaders().getFirst(PageBeanEnum.PAGESIZE.getValue());
		Integer pageNos = Integer.parseInt(pageNo);
		if (pageSize == null || !StringUtil.checkNotEmpty(pageSize)) {
			calculatePageSizes = PageBean.getDefaultPageSize();
			start = PageBean.getStartOfPage(pageNos, calculatePageSizes) - 1;
		} else {
			calculatePageSizes = Integer.parseInt(pageSize);
			start = PageBean.getStartOfPage(pageNos, calculatePageSizes) - 1;
		}
		map.put(PageBeanEnum.PAGENO.getValue(), pageNo);
		map.put(PageBeanEnum.PAGESIZE.getValue(), calculatePageSizes);
		map.put(PageBeanEnum.PAGESTART.getValue(), start);
		map.put(PageBeanEnum.PAGEEND.getValue(), calculatePageSizes);
		return map;
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> requestToParamMap(HttpServletRequest request) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Iterator<?> entries = request.getParameterMap().entrySet().iterator();
		Map.Entry<String, Object> entry;
		String name = "";
		String value = "";
		while (entries.hasNext()) {
			entry = (Map.Entry<String, Object>) entries.next();
			name = (String) entry.getKey();
			Object valueObj = entry.getValue();
			if (null == valueObj) {
				value = "";
			} else if (valueObj instanceof String[]) {
				String[] values = (String[]) valueObj;
				for (int i = 0; i < values.length; i++) {
					value = values[i] + ",";
				}
				value = value.substring(0, value.length() - 1);
			} else {
				value = valueObj.toString();
			}
			returnMap.put(name, value);
		}
		return returnMap;
	}

	public static void requestSetAttribute(HttpServletRequest request, String key, Object v) {
		request.getSession().setAttribute(key, v);
	}

	public static void requestGetAttribute(HttpServletRequest request, String key) {
		request.getSession().getAttribute(key);
	}

}
