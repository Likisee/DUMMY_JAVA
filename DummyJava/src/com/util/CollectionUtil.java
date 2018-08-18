package com.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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

	public static Integer[] getArrayList2Array(ArrayList<Integer> dataArrayList) {
		Integer[] dataArray = new Integer[dataArrayList.size()];
		dataArrayList.toArray(dataArray);
		return dataArray;
	}
	
	public static Integer[][] getArrayList2Array(ArrayList<ArrayList<Integer>> dataArrayList) {
		Integer[][] dataArray = new Integer[dataArrayList.size()][dataArrayList.get(0).size()];
		for(int i = 0; i < dataArrayList.size(); i++) {
			dataArrayList.get(i).toArray(dataArray[i]);	
		}
		return dataArray;
	}

	public static HashMap<Integer, Integer> getArrayList2HashMap(ArrayList<Integer> dataArrayList) {
		HashMap<Integer, Integer> dataHashMap = new HashMap<Integer, Integer>();
		for (int i = 0; i < dataArrayList.size(); i++) {
			dataHashMap.put(dataArrayList.get(i), i);
		}
		return dataHashMap;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
