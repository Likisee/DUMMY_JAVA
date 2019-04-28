package com.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DateTimeUtil {

	private static Log logger = LogFactory.getLog(DateTimeUtil.class);

	public final static String strDate = "yyyy-MM-dd";
	public final static String strDateTime = "yyyy-MM-dd hh:mm:ss";

	public final static String strDateUI = "yyyy/MM/dd";

	public static SimpleDateFormat getSimpleDateTime(String format) {
		SimpleDateFormat sdf = null;
		if (format == null || format.isEmpty()) {
			sdf = new SimpleDateFormat(strDate);
		} else {
			sdf = new SimpleDateFormat(format);
		}
		return sdf;
	}

	public static Date parseByDefaultSimpleDateTime(String date) {
		SimpleDateFormat sdf = getSimpleDateTime(null);
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			logger.error("parseByDefaultSimpleDateTime error: " + e.getMessage(), e);
			return null;
		}
	}

	@Deprecated
	public static Date addDays(String date, int days) {
		SimpleDateFormat sdf = getSimpleDateTime(null);
		try {
			return addDays(sdf.parse(date), days);
		} catch (ParseException e) {
			logger.error("addDays error: " + e.getMessage(), e);
			return null;
		}
	}

	public static Date addDays(Date date, int days) {
		long timestamp = date.getTime();
		timestamp = timestamp + Long.valueOf(1000) * 60 * 60 * 24 * days;
		return new Date(timestamp);
	}
	
	public static Date getIntDateToDate(int intDate) {
		Date date = new Date(intDate * Long.valueOf(1000) * 60 * 60 * 24);
		return date;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}