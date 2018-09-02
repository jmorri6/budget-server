package com.jmorri6.util;

import java.io.IOException;
import java.util.Enumeration;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@Startup
public class LogConfig {
	
	private static final int MB = 1024 * 1024;
	
	public LogConfig() {
		Enumeration<String> loggers = LogManager.getLogManager().getLoggerNames();

		FileHandler handler = null;
		try {
			handler = new FileHandler("app.%g.log",
					MB * 10, 5, true);
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
			return;
		}

		handler.setFormatter(new SimpleFormatter());

		while (loggers.hasMoreElements()) {
			String logger = loggers.nextElement();
			if (logger.startsWith("com.jmorri6")) {
				Logger log = LogManager.getLogManager().getLogger(logger);
				log.setLevel(Level.INFO);
				log.addHandler(handler);
			}
		}
	}
	
	public static String format(String msg, String...parameters) {
		
		if (parameters.length == 0) {
			return msg;
		}
		
		String formatted = msg;
		for (int i = 0; i < parameters.length; i++) {
			formatted = formatted.replaceFirst("\\{\\}", parameters[i]);
		}
		return formatted;
	}
	
	public static String format(String msg, Throwable ex) {
		String formatted = msg;

		StackTraceElement[] st = ex.getStackTrace();
		
		if (st != null && st.length > 0) {
			formatted.concat(": \n");
			for (StackTraceElement e : st) {
				formatted.concat(e.toString()).concat("\n");
			}
		}
		
		return formatted;
	}
}
