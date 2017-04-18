package com.yt.app.modul.lambda;

import java.util.Date;

@FunctionalInterface
public interface DateOperator {
	/**
	 * 适用于普通对象
	 * @param t
	 * return 当返回false时终止遍历
	 */
	public abstract boolean operator(Date t);
}
