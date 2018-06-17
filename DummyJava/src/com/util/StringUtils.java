package com.util;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class StringUtils {

	private static Log logger = LogFactory.getLog(StringUtils.class);

	public final static String commonSep = ",";
	public final static String commonDelim = ",";

	// ========================================================================
	// getStringConcate
	// ========================================================================

	public static String getStringConcate(String... args) {
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			if (arg.endsWith(commonSep)) {
				result.append(arg);
			} else {
				result.append(arg + commonSep);
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
			String[] strArr = str.split(commonDelim);
			for (int i = 0; i < strArr.length; i++) {
				result.add(strArr[i]);
			}
		}
		return result;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}