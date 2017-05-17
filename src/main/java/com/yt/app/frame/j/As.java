package com.yt.app.frame.j;

import com.yt.app.frame.k.Av;

import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.ProgressListener;

public class As implements ProgressListener {
	private HttpSession aa;

	public void a(HttpSession paramHttpSession) {
		this.aa = paramHttpSession;
		Av localProgress = new Av();
		paramHttpSession.setAttribute("status", localProgress);
	}

	public void update(long paramLong1, long paramLong2, int paramInt) {
		Av localProgress = (Av) this.aa.getAttribute("status");
		localProgress.setBytesRead(paramLong1);
		localProgress.setContentLength(paramLong2);
		localProgress.setItems(paramInt);
	}
}