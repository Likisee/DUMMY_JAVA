package com.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CollectionUtil {

	private static Log logger = LogFactory.getLog(CollectionUtil.class);

	public static void getStringArrayListSortWithoutDuplication(ArrayList<String> dataList) {
		if (dataList != null) {
			Set<String> hs = new HashSet<String>();
			hs.addAll(dataList);
			dataList.clear();
			dataList.addAll(hs);
			Collections.sort(dataList);
		}
	}
	
	public static void getIntegerArrayListSortWithoutDuplication(ArrayList<Integer> dataList) {
		if (dataList != null) {
			Set<Integer> hs = new HashSet<Integer>();
			hs.addAll(dataList);
			dataList.clear();
			dataList.addAll(hs);
			Collections.sort(dataList);
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
