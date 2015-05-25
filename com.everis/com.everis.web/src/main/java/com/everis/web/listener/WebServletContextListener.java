package com.everis.web.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class WebServletContextListener implements ServletContextListener {

    private static ApplicationContext springContext;

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        springContext = null;
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        springContext = WebApplicationContextUtils.getWebApplicationContext(servletContextEvent.getServletContext());
    }

    public static ApplicationContext getApplicationContext() {
        return springContext;
    }

    public static void setApplicationContext(ApplicationContext applicationContext) {
        springContext = applicationContext;
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String beanName) {
        return (T) springContext.getBean(beanName);
    }
}