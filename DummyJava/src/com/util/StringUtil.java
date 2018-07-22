package com.util;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.constant.Const;

public class StringUtil {

	private static Log logger = LogFactory.getLog(StringUtil.class);

	// ========================================================================
	// getStringConcate
	// ========================================================================

	public static String getStringConcate(String... args) {
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			if (arg.endsWith(Const.csvSep)) {
				result.append(arg);
			} else {
				result.append(arg + Const.csvSep);
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
			String[] strArr = str.split(Const.csvDelim);
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
				result.append(strArr[i] + Const.csvSep);
			}
		}
		return result.toString();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}