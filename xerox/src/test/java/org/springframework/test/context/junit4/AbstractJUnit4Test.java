package org.springframework.test.context.junit4;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.naming.NamingException;

import oracle.jdbc.pool.OracleConnectionPoolDataSource;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.BeforeClass;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;

import com.everis.web.listener.WebServletContextListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import flexjson.JSONSerializer;

public class AbstractJUnit4Test implements ApplicationContextAware {

    protected Logger LOGGER = null;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		WebServletContextListener.setApplicationContext(applicationContext);
	}
    
    @BeforeClass
    public static void beforeSetup() throws Exception {
        try {
            SimpleNamingContextBuilder builder = new SimpleNamingContextBuilder();

            OracleConnectionPoolDataSource ds = new OracleConnectionPoolDataSource();
            ds.setURL("jdbc:oracle:thin:@127.0.0.1:1521:XE");
            ds.setUser("APP_FATCA");
            ds.setPassword("APP_FATCA");

            builder.bind("jdbc/APP_FATCA", ds);
            builder.activate();
        } catch (NamingException ex) {
            ex.printStackTrace();
        }
    }
    
    @PostConstruct
    public void setup() {

        Properties prop = new Properties();
        prop.setProperty("log4j.rootCategory", "INFO,LOGFILE,stdout");
        prop.setProperty("log4j.appender.LOGFILE", "org.apache.log4j.RollingFileAppender");
        prop.setProperty("log4j.appender.LOGFILE.file", "/pr/fatca/online/pe/web/log/log_fatca-test.log");
        prop.setProperty("log4j.appender.LOGFILE.MaxFileSize", "1024kb");
        prop.setProperty("log4j.appender.LOGFILE.MaxBackupIndex", "10");
        prop.setProperty("log4j.appender.LOGFILE.append", "true");
        prop.setProperty("log4j.appender.LOGFILE.layout", "org.apache.log4j.PatternLayout");
        prop.setProperty("log4j.appender.LOGFILE.layout.ConversionPattern", "[%d{yyyy-MM-dd HH:mm:ss:SSS}] - [%5p] (%C{1}.%M:%L) - %m%n");
        prop.setProperty("log4j.appender.stdout", "org.apache.log4j.ConsoleAppender");
        prop.setProperty("log4j.appender.stdout.layout", "org.apache.log4j.PatternLayout");
        prop.setProperty("log4j.appender.stdout.layout.ConversionPattern", "[%d{yyyy-MM-dd HH:mm:ss:SSS}] - [%5p] (%C{1}.%M:%L) - %m%n");
        prop.setProperty("log4j.logger.org.hibernate.SQL", "DEBUG");
        // prop.setProperty("log4j.logger.org.hibernate.type", "TRACE");
        // prop.setProperty("log4j.logger.org.hibernate", "INFO,LOGFILE");
        // prop.setProperty("log4j.appender.stdout.layout.ConversionPattern", "[%d{HH:mm:ss}]%p - %C{1}.%M(%L)  %m%n");
        
        PropertyConfigurator.configure(prop);
        LOGGER = Logger.getLogger(AbstractJUnit4Test.class);
        LOGGER.error("Logger -> Init");
    }

    public void prettyPrinter(Object o) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(o);
            LOGGER.info("\n" + json);
        } catch (Exception e) {
            LOGGER.error("", e);
        }
    }

    public void printer(Object o, String[] exclude, String message) {
        try {
            LOGGER.info(message + " := " + (new JSONSerializer().exclude(exclude).deepSerialize(o)));
        } catch (Exception e) {
            LOGGER.error("", e);
        }
    }

    public void printer(Object o, String[] exclude) {
        try {
            LOGGER.info(new JSONSerializer().exclude(exclude).deepSerialize(o));
        } catch (Exception e) {
            LOGGER.error("", e);
        }
    }

    public void printer(Object o) {
        printer(o, new String[]{});
    }
    
    public void printer(Collection<?> o, String[] exclude) {
        if(o != null) {
            JSONSerializer serializer = new JSONSerializer().exclude(exclude);
            StringBuilder sb = new StringBuilder();
            try {
                for (Object iO : o) {
                    sb.append("\n\t" + serializer.deepSerialize(iO));
                }
                LOGGER.info(sb.toString());
            } catch (Exception e) {
                LOGGER.error("", e);
            }
        }
    }
    
    public byte[] obtenerBytes(String file) {
        byte[] result = null;
        try {
            File ruleFile = new File(file);
            InputStream input = new FileInputStream(ruleFile);
            ByteArrayOutputStream output = new ByteArrayOutputStream(); 
            IOUtils.copy(input, output);    
            
            result = output.toByteArray();
        } catch (Exception e) {
            LOGGER.error("No se pudo obtener la regla");
        }
        
        return result;
    }
}
