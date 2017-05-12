package com.yt.app.modul;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Tree implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6910894272790828284L;

	private String id;

	@JsonProperty("pId")
	private String pId;

	private String name;

	private boolean open;
	
	private String linkUrl;

	public Tree() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Tree(String id, String pId, String name) {
		super();
		this.id = id;
		this.pId = pId;
		this.name = name;
	}

	public Tree(String id, String pId, String name, boolean open) {
		super();
		this.id = id;
		this.pId = pId;
		this.name = name;
		this.open = open;
	}
	
	public Tree(String id, String pId, String name, boolean open, String linkUrl) {
		super();
		this.id = id;
		this.pId = pId;
		this.name = name;
		this.open = open;
		this.linkUrl = linkUrl;
	}

}
