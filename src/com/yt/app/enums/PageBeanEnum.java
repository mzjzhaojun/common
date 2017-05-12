package com.yt.app.enums;

public enum PageBeanEnum {

	PAGENO("pageNo"), PAGESIZE("pageSize"), TOTALCOUNT("totalCount"),PAGESTART("pageStart"),PAGEEND("pageEnd"), PAGECOUNT("pageCount"), ORDERBY("orderby"), DIR("dir"), ASC("asc"), DESC("desc");

	private String name;

	PageBeanEnum(String name) {
		this.name = name;
	}

	public String getValue() {
		return name;
	}
}
