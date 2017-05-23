package com.yt.app.frame.k;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Ay implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long bytesRead;
	private long contentLength;
	private long items;
}
