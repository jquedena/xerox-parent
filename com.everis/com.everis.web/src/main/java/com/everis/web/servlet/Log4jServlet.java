package com.everis.web.servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.everis.core.service.Log4jService;
import com.everis.util.TrazaUtil;
import com.everis.web.listener.WebServletContextListener;

public class Log4jServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(Log4jServlet.class);
    private static final String METHOD_TEST = "test";
    private static final String METHOD_RELOAD = "reloadLog4J";
    private static final String METHOD_DOWNLOAD = "download";
    private static final String METHOD_LIST_FILES = "listFiles";

    private Log4jService log4jService;
    private String log4jServiceName = "log4jService";
    private String app = "core";

    protected void reloadLog4J(String modo) {
        String rootCategory = "INFO,stdout,LOGFILE";
        String fileName = "/pr/" + app.toLowerCase() + "/online/pe/web/log/log_" + app.toLowerCase() + ".log";
        String maxFileSize = "1024kb";
        String maxBackupIndex = "20";

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

                    maxFileSize = log4jService.obtener(Log4jService.MAX_FILE_SIZE);
                    if (maxFileSize == null || maxFileSize.isEmpty()) {
                        maxFileSize = "1024kb";
                    }

                    maxBackupIndex = log4jService.obtener(Log4jService.MAX_BACKUP_INDEX);
                    if (maxBackupIndex == null || maxBackupIndex.isEmpty()) {
                        maxBackupIndex = "20";
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("Error:", e);
            rootCategory = "INFO,stdout,LOGFILE";
            fileName = "/pr/" + app.toLowerCase() + "/online/pe/web/log/log_" + app.toLowerCase() + ".log";
            maxFileSize = "1024kb";
            maxBackupIndex = "20";
        }

        Properties prop = new Properties();
        prop.setProperty("log4j.rootCategory", rootCategory);
        prop.setProperty("log4j.appender.LOGFILE", "org.apache.log4j.RollingFileAppender");
        prop.setProperty("log4j.appender.LOGFILE.file", fileName);
        prop.setProperty("log4j.appender.LOGFILE.MaxFileSize", maxFileSize);
        prop.setProperty("log4j.appender.LOGFILE.MaxBackupIndex", maxBackupIndex);
        prop.setProperty("log4j.appender.LOGFILE.append", "true");
        prop.setProperty("log4j.appender.LOGFILE.layout", "org.apache.log4j.PatternLayout");
        prop.setProperty("log4j.appender.LOGFILE.layout.ConversionPattern", "[%d{yyyy-MM-dd HH:mm:ss}] - [%5p] (%C{1}.%M:%L) - %m%n");
        prop.setProperty("log4j.appender.stdout", "org.apache.log4j.ConsoleAppender");
        prop.setProperty("log4j.appender.stdout.layout", "org.apache.log4j.PatternLayout");
        prop.setProperty("log4j.appender.stdout.layout.ConversionPattern", "[%d{HH:mm:ss}]%p - %C{1}.%M(%L)  %m%n");
        
        if(modo != null && modo.equalsIgnoreCase("debugSQL")) {
            prop.setProperty("log4j.logger.org.hibernate.SQL", "DEBUG");
            prop.setProperty("log4j.logger.org.hibernate.TYPE", "TRACE");
        }

        PropertyConfigurator.configure(prop);
        LOG.error("Logger -> Init");
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        log4jServiceName = config.getInitParameter("log4jService");
        app = config.getInitParameter("app") == null ? "everis" : config.getInitParameter("app");

        reloadLog4J("");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        service(request, response);
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getParameter("method");
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();

        if (METHOD_TEST.equalsIgnoreCase(method)) {
            if (log4jService != null) {
                log4jService.test(out);
            }
        } else if (METHOD_RELOAD.equalsIgnoreCase(method)) {
            reloadLog4J(request.getParameter("modo"));
            out.write("Actualizado");
        } else if (METHOD_DOWNLOAD.equalsIgnoreCase(method)) {
            try {
                String linea;
                File file = new File(request.getParameter("file"));
                FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr);

                while ((linea = br.readLine()) != null) {
                    out.write(linea);
                    out.write("\n");
                }

                br.close();
            } catch (Exception e) {
                out.write(TrazaUtil.mostrarMensajeText(e));
            }
        } else if (METHOD_LIST_FILES.equalsIgnoreCase(method)) {
            out.write("Por implementar");
        }

        out.close();
    }
}