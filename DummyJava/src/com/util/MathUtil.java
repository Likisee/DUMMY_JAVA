package com.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MathUtil {

	private static Log logger = LogFactory.getLog(MathUtil.class);

	public static Double getMean(ArrayList<Double> dataList) {
		Double result = null;
		if (dataList != null) {
			BigDecimal sum = new BigDecimal(0);
			int count = 0;
			for (Double value : dataList) {
				if (value != null) {
					sum = sum.add(new BigDecimal(value.doubleValue()));
					count++;
				}
			}
			result = sum.divide(new BigDecimal(count), 10, BigDecimal.ROUND_HALF_UP).doubleValue();
//			double sum = 0;
//			int count = 0;
//			for (Double value : dataList) {
//				if (value != null) {
//					sum += value;
//					count++;
//				}
//			}
//			result = sum / count;
		}
		return result;
	}

	public static Double getMin(ArrayList<Double> dataList) {
		Double result = null;
		if (dataList != null) {
			result = Double.MAX_VALUE;
			for (Double oneValue : dataList) {
				if (oneValue < result) {
					result = oneValue;
				}
			}
		}
		return result;
	}

	public static Double getMedian(ArrayList<Double> dataList) {
		Double result = null;
		if (dataList != null) {
			Collections.sort(dataList);
			if (dataList.size() % 2 == 0) {
				result = (dataList.get((dataList.size() - 2) / 2) + dataList.get((dataList.size() - 2) / 2 + 1)) / 2.0;
			} else {
				result = dataList.get((dataList.size() - 1) / 2);
			}
		}
		return result;
	}

	public static Double getMax(ArrayList<Double> dataList) {
		Double result = null;
		if (dataList != null) {
			result = Double.MIN_VALUE;
			for (Double oneValue : dataList) {
				if (oneValue > result) {
					result = oneValue;
				}
			}
		}
		return result;
	}
	
	public static Double getMax(Double... values) {
		Double result = null;
		if (values != null) {
			result = Double.MIN_VALUE;
			for (Double oneValue : values) {
				if (oneValue > result) {
					result = oneValue;
				}
			}
		}
		return result;
	}
	
	public static Double getStdDev(ArrayList<Double> dataList) {
		Double result = null;
		if (dataList != null) {
			double mean = getMean(dataList);
			double standardDeviation = 0;
			int count = 0;
			for (Double value : dataList) {
				if (value != null) {
					standardDeviation += Math.pow(value - mean, 2);
					count++;
				}
			}
			result = Math.sqrt(standardDeviation / count);
		}
		return result;
	}
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
