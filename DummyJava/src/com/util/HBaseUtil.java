package com.util;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HBaseUtil {

	private static Log logger = LogFactory.getLog(HBaseUtil.class);

	public final static String hbaseSep = "~|";
	public final static String hbaseDelim = "~\\|";

	// ========================================================================
	// getStringConcate
	// ========================================================================

	public static String getStringConcate(String... args) {
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			if (arg.endsWith(hbaseSep)) {
				result.append(arg);
			} else {
				result.append(arg + hbaseSep);
			}
		}
		return result.toString();
	}

	// ========================================================================
	// getStringSplit
	// ========================================================================

	public static ArrayList<String> getStringSplit(String str) {
		ArrayList<String> result = new ArrayList<String>();
		if (str != null) {
			String[] strArr = str.split(hbaseDelim);
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