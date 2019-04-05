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
	
	public static Double[][] getDerivative(Double[][] dataMatrix, int dimY, int dimX, int order) {
		Double[][] result = null;
		if (dataMatrix != null && dimY > 0 && dimX > 0) {
			result = dataMatrix;
			int itr = order;
			while (itr > 0) {
				itr--;
				Double[][] resultTemp = new Double[dimY][dimX];
//				for (int i = 0; i < dimY; i++) {
//					for (int j = 0; j < dimX; j++) {
//						resultTemp[i][j] = Double.valueOf("0");
//					}
//				}
				Double d1, d2, d3;
				for (int i = 0; i < dimY - (order - itr); i++) {
					for (int j = 0; j < dimX - (order - itr); j++) {
						d1 = result[i][j + 1] - result[i][j];
						d2 = result[i + 1][j + 1] - result[i][j];
						d3 = result[i + 1][j] - result[i][j];
						resultTemp[i][j] = getMax(d1, d2, d3);
					}
				}
				result = resultTemp;
			}
		}
		return result;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
Double[][] data = FileUtil.readFileAsMaxtrix("D:\\DataSet\\#Running\\MovieLens Latest Datasets\\ml-latest-small\\Export\\UserIdBasedMatrix\\DigestedUserIdBasedMatrix_Max.txt");
data = getDerivative(data, 101, 101, 1);
FileUtil.writeMaxtrixAsFile("D:\\DataSet\\#Running\\MovieLens Latest Datasets\\ml-latest-small\\Export\\UserIdBasedMatrix\\DigestedUserIdBasedMatrix_Max_d1.txt", data, true);

data = FileUtil.readFileAsMaxtrix("D:\\DataSet\\#Running\\MovieLens Latest Datasets\\ml-latest-small\\Export\\UserIdBasedMatrix\\DigestedUserIdBasedMatrix_Mean.txt");
data = getDerivative(data, 101, 101, 1);
FileUtil.writeMaxtrixAsFile("D:\\DataSet\\#Running\\MovieLens Latest Datasets\\ml-latest-small\\Export\\UserIdBasedMatrix\\DigestedUserIdBasedMatrix_Mean_d1.txt", data, true);

data = FileUtil.readFileAsMaxtrix("D:\\DataSet\\#Running\\MovieLens Latest Datasets\\ml-latest-small\\Export\\UserIdBasedMatrix\\DigestedUserIdBasedMatrix_StdDev.txt");
data = getDerivative(data, 101, 101, 1);
FileUtil.writeMaxtrixAsFile("D:\\DataSet\\#Running\\MovieLens Latest Datasets\\ml-latest-small\\Export\\UserIdBasedMatrix\\DigestedUserIdBasedMatrix_StdDev_d1.txt", data, true);
	}

}
