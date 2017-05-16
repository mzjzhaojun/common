package com.yt.app.frame.d;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

public class Ah implements PasswordEncoder {

	@Autowired
	StandardPasswordEncoder h;

	public String encode(CharSequence paramCharSequence) {
		return this.h.encode(paramCharSequence);
	}

	public boolean matches(CharSequence paramCharSequence, String paramString) {
		return this.h.matches(paramCharSequence, paramString);
	}
}