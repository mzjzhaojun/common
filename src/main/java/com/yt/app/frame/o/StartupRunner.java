package com.yt.app.frame.o;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(value = 10000)
public abstract interface StartupRunner extends CommandLineRunner {

	@Override
	void run(String... args) throws Exception;
}
