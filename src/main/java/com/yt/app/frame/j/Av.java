package com.yt.app.frame.j;

import com.yt.app.frame.k.Ay;

import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.ProgressListener;

public class Av implements ProgressListener {
	private HttpSession aa;

	public void a(HttpSession paramHttpSession) {
		this.aa = paramHttpSession;
		Ay localProgress = new Ay();
		paramHttpSession.setAttribute("status", localProgress);
	}

	public void update(long paramLong1, long paramLong2, int paramInt) {
		Ay localProgress = (Ay) this.aa.getAttribute("status");
		localProgress.setBytesRead(paramLong1);
		localProgress.setContentLength(paramLong2);
		localProgress.setItems(paramInt);
	}
}