package com.yt.app.util;

import org.apache.commons.lang.StringUtils;


/**
 * emoji表情替换工具类
 * 
 * @date 2016年9月22日
 * @Description
 */
public class EmojiUtil {

	public static String repalceEmoji(String source) {
		if (StringUtils.isBlank(source)) {
			return source;
		}
		 return source.replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff\\ue000-\\uefff\\s]", "");  
	}
}
