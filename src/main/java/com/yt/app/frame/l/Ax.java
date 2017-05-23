package com.yt.app.frame.l;

import com.yt.app.frame.j.Av;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

public class Ax extends CommonsMultipartResolver {

	@Autowired
	private Av ab;

	public void a(Av paramFileUploadProgressListener) {
		this.ab = paramFileUploadProgressListener;
	}

	public MultipartParsingResult parseRequest(HttpServletRequest paramHttpServletRequest) {
		String str = determineEncoding(paramHttpServletRequest);
		FileUpload localFileUpload = prepareFileUpload(str);
		this.ab.a(paramHttpServletRequest.getSession());
		localFileUpload.setProgressListener(this.ab);
		try {
			List<FileItem> localList = ((ServletFileUpload) localFileUpload).parseRequest(paramHttpServletRequest);
			return parseFileItems(localList, str);
		} catch (FileUploadBase.SizeLimitExceededException localSizeLimitExceededException) {
			throw new MaxUploadSizeExceededException(localFileUpload.getSizeMax(), localSizeLimitExceededException);
		} catch (FileUploadException localFileUploadException) {
			throw new MultipartException("Could not parse multipart servlet request", localFileUploadException);
		}
	}
}