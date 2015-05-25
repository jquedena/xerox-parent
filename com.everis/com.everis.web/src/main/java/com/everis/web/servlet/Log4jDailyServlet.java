package com.everis.web.servlet;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.everis.core.service.Log4jService;
import com.everis.web.listener.WebServletContextListener;

public class Log4jDailyServlet extends Log4jServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(Log4jDailyServlet.class);

    private Log4jService log4jService;
    private String log4jServiceName = "log4jService";
    private String app = "core";

    @Override
    protected void reloadLog4J(String modo) {
        String rootCategory = "INFO,stdout,LOGFILE";
        String fileName = "/pr/" + app.toLowerCase() + "/online/pe/web/log/log_" + app.toLowerCase() + ".log";

        try {
            if (log4jServiceName != null) {
                log4jService = WebServletContextListener.getBean(log4jServiceName);
                if (log4jService != null) {
                    rootCategory = log4jService.obtener(Log4jService.ROOT_CATEGORY);
                    if (rootCategory == null || rootCategory.isEmpty()) {
                        rootCategory = "INFO,stdout,LOGFILE";
                    }

                    fileName = log4jService.obtener(Log4jService.FILE);
                    if (fileName == null || fileName.isEmpty()) {
                        fileName = "/pr/" + app.toLowerCase() + "/online/pe/web/log/log_" + app.toLowerCase() + ".log";
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("Error:", e);
            rootCategory = "INFO,stdout,LOGFILE";
            fileName = "/pr/" + app.toLowerCase() + "/online/pe/web/log/log_" + app.toLowerCase() + ".log";
        }

        Properties prop = new Properties();
        prop.setProperty("log4j.rootCategory", rootCategory);
        prop.setProperty("log4j.appender.LOGFILE", "org.apache.log4j.DailyRollingFileAppender");
        prop.setProperty("log4j.appender.LOGFILE.file", fileName);
        prop.setProperty("log4j.appender.LOGFILE.append", "true");
        prop.setProperty("log4j.appender.LOGFILE.layout", "org.apache.log4j.PatternLayout");
        prop.setProperty("log4j.appender.LOGFILE.layout.ConversionPattern", "[%d{yyyy-MM-dd HH:mm:ss}] - [%5p] (%C{1}.%M:%L) - %m%n");
        prop.setProperty("log4j.appender.stdout", "org.apache.log4j.ConsoleAppender");
        prop.setProperty("log4j.appender.stdout.layout", "org.apache.log4j.PatternLayout");
        prop.setProperty("log4j.appender.stdout.layout.ConversionPattern", "[%d{HH:mm:ss}]%p - %C{1}.%M(%L)  %m%n");
        
        if (log4jServiceName != null && log4jService != null) {
            log4jService.afterSetProperties(prop);
        }
        
        if(modo != null && modo.equalsIgnoreCase("debugSQL")) {
        	prop.setProperty("log4j.rootCategory", "TRACE,stdout,LOGFILE");
        	prop.setProperty("log4j.logger.org.hibernate.SQL", "DEBUG");
            prop.setProperty("log4j.logger.org.hibernate.TYPE", "TRACE");
        }

        PropertyConfigurator.configure(prop);
        LOG.error("Logger -> Init");
    }

}