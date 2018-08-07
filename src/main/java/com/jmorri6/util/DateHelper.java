package com.jmorri6.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {
	private static final SimpleDateFormat fmt = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss.SSS");
	
	public static Date fromString(String date) throws ParseException {
		return fmt.parse(date);
	}
	
	public static String toString(Date date) {
		return fmt.format(date);
	}
}
