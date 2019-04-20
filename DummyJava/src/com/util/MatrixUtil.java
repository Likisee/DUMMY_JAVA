package com.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.constant.Const;

public class MatrixUtil {

	private static Log logger = LogFactory.getLog(MatrixUtil.class);

	public final static String fileSep = File.separator;
	public final static String fileDelim = File.separator;

	// ========================================================================
	// readFileAsMaxtrix
	// ========================================================================
	
	public static Double[][] readFileAsMaxtrix(String filePath) {
		return readFileAsMaxtrix(new File(filePath));
	}
	
	public static Double[][] readFileAsMaxtrix(File file) {
		// determine dimY, dimX
		Double[][] result = null;
		if (file.exists()) {
			ArrayList<Integer> DimYDimX = getDimYDimX(file);
			int DimY = DimYDimX.get(0);
			int DimX = DimYDimX.get(1);
			if(DimY > 0 && DimY > 0) {
				result = new Double[DimY][DimX];
				String oneLine;
				String[] oneLineArr;
				int lineCount = 0; // dimY
				int lineWidth = 0; // dimX
				{
					FileInputStream fs = null;
					InputStreamReader isr = null;
					BufferedReader br = null;
					try {
						fs = new FileInputStream(file);
						isr = new InputStreamReader(fs, "MS950");
						// isr = new InputStreamReader(fs,"UTF-8");
						br = new BufferedReader(isr);
						while (br.ready()) {
							oneLine = br.readLine();
							oneLineArr = oneLine.split("\t"); 
							lineWidth = oneLineArr.length;
							for(int i = 0; i < lineWidth; i++) {
								result[lineCount][i] = Double.valueOf(oneLineArr[i]);
							}
							lineCount++;
						}
					} catch (FileNotFoundException e) {
						logger.error(e.getMessage(), e);
					} catch (IOException e) {
						logger.error(e.getMessage(), e);
					} finally {
						if (br != null) {
							try {
								br.close();
							} catch (IOException e) {
								logger.error(e.getMessage(), e);
							}
						}
						if (isr != null) {
							try {
								isr.close();
							} catch (IOException e) {
								logger.error(e.getMessage(), e);
							}
						}
						if (fs != null) {
							try {
								fs.close();
							} catch (IOException e) {
								logger.error(e.getMessage(), e);
							}
						}
					}
				}
			}
		}
		return result;
	}
	
	private static ArrayList<Integer> getDimYDimX(File file) {
		FileInputStream fs = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		String oneLine;
		int lineCount = 0; // dimY
		int lineWidth = 0; // dimX
		try {
			fs = new FileInputStream(file);
			isr = new InputStreamReader(fs, "MS950");
			// isr = new InputStreamReader(fs,"UTF-8");
			br = new BufferedReader(isr);
			while (br.ready()) {
				oneLine = br.readLine();
				lineCount++;
				if (lineWidth == 0) {
					lineWidth = oneLine.split("\t").length;
				}
			}
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
			if (isr != null) {
				try {
					isr.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
			if (fs != null) {
				try {
					fs.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
		ArrayList<Integer> result = new ArrayList<Integer>();
		if (lineCount != 0 && lineWidth != 0) {
			result.add(lineCount);
			result.add(lineWidth);
		} else {
			result.add(0);
			result.add(0);
		}
		return result;
	}
	
	// ========================================================================
	// writeMaxtrixAsFile
	// ========================================================================

	public static boolean writeMaxtrixAsFile(String filePath, Double[][] matrix, boolean overWrite) {
		return writeMaxtrixAsFile(new File(filePath), matrix, overWrite);
	}

	public static boolean writeMaxtrixAsFile(File file, Double[][] matrix, boolean overWrite) {
		if (file.exists() && overWrite == false) {
			return false;
		}
		if (file.exists() && overWrite == true) {
			file.delete();
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				sb.append(matrix[i][j] + "\t");
			}
			sb.append(Const.lineBrakeSep);
		}
		return FileUtil.writeStringToFile(file, sb.toString());
	}
	
	// ========================================================================
	// getMatrixDerivative
	// ========================================================================
	
	public static Double[][] getMatrixDerivative(Double[][] dataMatrix, int dimY, int dimX, int order) {
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
						resultTemp[i][j] = MathUtil.getMax(d1, d2, d3);
					}
				}
				result = resultTemp;
			}
		}
		return result;
	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Double[][] data = MatrixUtil.readFileAsMaxtrix("D:\\DataSet\\#Running\\MovieLens Latest Datasets\\ml-latest-small\\Export\\UserIdBasedMatrix\\DigestedUserIdBasedMatrix_Max.txt");
		data = MatrixUtil.getMatrixDerivative(data, 101, 101, 1);
		MatrixUtil.writeMaxtrixAsFile("D:\\DataSet\\#Running\\MovieLens Latest Datasets\\ml-latest-small\\Export\\UserIdBasedMatrix\\DigestedUserIdBasedMatrix_Max_d1.txt", data, true);

		data = MatrixUtil.readFileAsMaxtrix("D:\\DataSet\\#Running\\MovieLens Latest Datasets\\ml-latest-small\\Export\\UserIdBasedMatrix\\DigestedUserIdBasedMatrix_Mean.txt");
		data = MatrixUtil.getMatrixDerivative(data, 101, 101, 1);
		MatrixUtil.writeMaxtrixAsFile("D:\\DataSet\\#Running\\MovieLens Latest Datasets\\ml-latest-small\\Export\\UserIdBasedMatrix\\DigestedUserIdBasedMatrix_Mean_d1.txt", data, true);

		data = MatrixUtil.readFileAsMaxtrix("D:\\DataSet\\#Running\\MovieLens Latest Datasets\\ml-latest-small\\Export\\UserIdBasedMatrix\\DigestedUserIdBasedMatrix_StdDev.txt");
		data = MatrixUtil.getMatrixDerivative(data, 101, 101, 1);
		MatrixUtil.writeMaxtrixAsFile("D:\\DataSet\\#Running\\MovieLens Latest Datasets\\ml-latest-small\\Export\\UserIdBasedMatrix\\DigestedUserIdBasedMatrix_StdDev_d1.txt", data, true);
	}

}