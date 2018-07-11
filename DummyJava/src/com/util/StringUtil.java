package com.util;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class StringUtil {

	private static Log logger = LogFactory.getLog(StringUtil.class);

	public final static String csvSep = ",";
	public final static String csvDelim = ",";
	
	public final static String lineBrakeSep = "\r\n";
	public final static String lineBrakeDelim = "\r?\n";

	// ========================================================================
	// getStringConcate
	// ========================================================================

	public static String getStringConcate(String... args) {
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			if (arg.endsWith(csvSep)) {
				result.append(arg);
			} else {
				result.append(arg + csvSep);
			}
		}
		return result.toString().substring(0, result.toString().length() - 1);
	}

	// ========================================================================
	// getStringSplit
	// ========================================================================

	public static ArrayList<String> getStringSplit(String str) {
		ArrayList<String> result = new ArrayList<String>();
		if (str != null) {
			String[] strArr = str.split(csvDelim);
			for (int i = 0; i < strArr.length; i++) {
				result.add(strArr[i]);
			}
		}
		return result;
	}
	
	// ========================================================================
	// getStringArray2Csv
	// ========================================================================

	public static String getStringArray2Csv(String [] strArr) {
		StringBuffer result = new StringBuffer();
		if (strArr != null) {
			for (int i = 0; i < strArr.length; i++) {
				result.append(strArr[i] + ",");
			}
		}
		return result.toString();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}