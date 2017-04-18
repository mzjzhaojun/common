package com.yt.app.frame.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoders implements PasswordEncoder {

	@Autowired
	StandardPasswordEncoder standardPasswordEncoder;

	@Override
	public String encode(CharSequence rawPassword) {
		return standardPasswordEncoder.encode(rawPassword);
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return standardPasswordEncoder.matches(rawPassword, encodedPassword);
	}

}
