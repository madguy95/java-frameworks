package com.demo;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class XmlWebApplication implements WebApplicationInitializer {

	public void onStartup(ServletContext servletContext) throws ServletException {

		XmlWebApplicationContext webApplicationContext = new XmlWebApplicationContext();
		webApplicationContext.setConfigLocation("classpath:springConfig.xml");

		// Creating a dispatcher servlet object
		DispatcherServlet dispatcherServlet = new DispatcherServlet(webApplicationContext);

		// Registering Dispatcher Servlet with Servlet
		// Context
		ServletRegistration.Dynamic myCustomDispatcherServlet = servletContext.addServlet("SpringMyBatisServlet",
				dispatcherServlet);

		// Setting load on startup
		myCustomDispatcherServlet.setLoadOnStartup(1);

		// Adding mapping url
		myCustomDispatcherServlet.addMapping("/");
	}
}
