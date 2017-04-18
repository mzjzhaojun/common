package com.yt.app.modul;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Progress implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long bytesRead;
	private long contentLength;
	private long items;
}