package com.yt.app.frame.p;

import java.io.BufferedReader;
import java.io.IOException;

public class ReaderUtil {
	public static String getBodyString(BufferedReader br) {
		String inputLine;
		String str = "";
		try {
			while ((inputLine = br.readLine()) != null) {
				str += inputLine;
			}
			br.close();
		} catch (IOException e) {
		}
		return str;
	}
}
