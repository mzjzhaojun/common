package com.yt.app.frame.init;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.stereotype.Component;

@Component
public abstract interface SystemInitListener extends ServletContextListener {

	public void contextInitialized(ServletContextEvent servletContextEvent);

	public void contextDestroyed(ServletContextEvent servletContextEvent);

}
