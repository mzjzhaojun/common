package com.yt.app.frame.j;

import com.yt.app.frame.k.Progress;

import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.ProgressListener;

public class FileUploadProgressListener implements ProgressListener {
	private HttpSession aa;

	public void a(HttpSession paramHttpSession) {
		this.aa = paramHttpSession;
		Progress localProgress = new Progress();
		paramHttpSession.setAttribute("status", localProgress);
	}

	public void update(long paramLong1, long paramLong2, int paramInt) {
		Progress localProgress = (Progress) this.aa.getAttribute("status");
		localProgress.setBytesRead(paramLong1);
		localProgress.setContentLength(paramLong2);
		localProgress.setItems(paramInt);
	}
}