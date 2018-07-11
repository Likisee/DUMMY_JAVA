package com.util;

import java.text.SimpleDateFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TimeUtil {

	private static Log logger = LogFactory.getLog(TimeUtil.class);

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

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}