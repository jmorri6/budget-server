package com.jmorri6.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateHelper {
	private static final SimpleDateFormat fmt = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss.SSS");
	
	public static Date fromString(String date) throws ParseException {
		return fmt.parse(date);
	}
	
	public static String toString(Date date) {
		return fmt.format(date);
	}
	
	public static Integer getNbrOfMonths(Date startDate, Date endDate) {
		Calendar startCalendar = new GregorianCalendar();
		startCalendar.setTime(startDate);
		Calendar endCalendar = new GregorianCalendar();
		endCalendar.setTime(endDate);

		int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
		int diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH) + 1;
		
		return diffMonth;
	}
}
