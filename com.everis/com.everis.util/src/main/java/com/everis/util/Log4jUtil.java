package com.everis.util;

import org.apache.log4j.FileAppender;
import org.apache.log4j.HTMLLayout;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class Log4jUtil {

	private static void createLogger(String category, String fileName,  String fileThreshold, Layout layout, Logger log) {
		FileAppender fileAppener = new FileAppender();
		fileAppener.setAppend(true);
		fileAppener.setFile(fileName);
		fileAppener.setThreshold(Level.toLevel(fileThreshold));
		fileAppener.setLayout(layout);
		fileAppener.activateOptions();
		log.addAppender(fileAppener);	
	}
	
	public static Logger createLoggerHTML(String category, String fileName,  String fileThreshold) {
		Logger log = Logger.getLogger(category);
		
		HTMLLayout htmlLayout = new HTMLLayout();
		htmlLayout.setTitle("Errores:");
		htmlLayout.setLocationInfo(true);
		
		createLogger(category, fileName, fileThreshold, htmlLayout, log);
		return log;
	}
	
	public static Logger createLoggerText(String category, String fileName,  String fileThreshold) {
		Logger log = Logger.getLogger(category);
		
		PatternLayout layout = new PatternLayout();
		layout.setConversionPattern("[%d{yyyy-MM-dd HH:mm:ss}] - [%5p] (%C{1}.%M:%L) - %m%n");
		
		createLogger(category, fileName, fileThreshold, layout, log);
		return log;
	}
}
