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

public class FileUtil {

	private static Log logger = LogFactory.getLog(FileUtil.class);

	public final static String fileSep = File.separator;
	public final static String fileDelim = File.separator;

	// ========================================================================
	// readFileAsString
	// ========================================================================
	public static String readFileAsString(File file) {
		String targetFileStr = "";
		try {
			FileInputStream fisTargetFile = new FileInputStream(file);
			targetFileStr = org.apache.commons.io.IOUtils.toString(fisTargetFile, "UTF-8");
			fisTargetFile.close();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return targetFileStr;
	}

	public static String readFileAsString(String filePath) {
		return readFileAsString(new File(filePath));
	}

	public static String readFileAsString(String fileFolder, String filename) {
		return readFileAsString(new File(fileFolder + File.separator + filename));
	}

	// ========================================================================
	// writeStringToFile
	// ========================================================================
	public static boolean writeStringToFile(File file, String content) {
		try {
			org.apache.commons.io.FileUtils.writeStringToFile(file, content, "UTF-8", false);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			return false;
		}
		return true;
	}

	public static boolean writeStringToFile(String filePath, String content) {
		return writeStringToFile(new File(filePath), content);
	}

	public static boolean writeStringToFile(String fileFolder, String filename, String content) {
		return writeStringToFile(new File(fileFolder + File.separator + filename), content);
	}

	// ========================================================================
	// readFileAsObject
	// ========================================================================
	public static Object readFileAsObject(File file) {
		Object content = "";
		try {
			FileInputStream fin = new FileInputStream(file);
			ObjectInputStream objectinputstream = new ObjectInputStream(fin);
			content = objectinputstream.readObject();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} catch (ClassNotFoundException e) {
			logger.error(e.getMessage(), e);
		}
		return content;
	}

	public static Object readFileAsObject(String filePath) {
		return readFileAsString(new File(filePath));
	}

	public static Object readFileAsObject(String fileFolder, String filename) {
		return readFileAsString(new File(fileFolder + File.separator + filename));
	}

	// ========================================================================
	// writeObjectToFile
	// ========================================================================
	public static boolean writeObjectToFile(File file, Object content) {
		try {
			FileOutputStream fout = new FileOutputStream(file, false);
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(content);
			oos.close();
			fout.close();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			return false;
		}
		return true;
	}

	public static boolean writeObjectToFile(String filePath, String content) {
		return writeStringToFile(new File(filePath), content);
	}

	public static boolean writeObjectToFile(String fileFolder, String filename, String content) {
		return writeStringToFile(new File(fileFolder + File.separator + filename), content);
	}
	
	// ========================================================================
	// printFileAsString
	// ========================================================================

	public static void printFileAsString(String filePath) {
		FileInputStream fs = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		try {
			fs = new FileInputStream(new File(filePath));
			isr = new InputStreamReader(fs, "MS950");
			// isr = new InputStreamReader(fs,"UTF-8");
			br = new BufferedReader(isr);
			while (br.ready()) {
				System.out.println(br.readLine());
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

	// ========================================================================
	// printStringAsFile
	// ========================================================================
	
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
		return writeStringToFile(file, sb.toString());
	}
	
	// ========================================================================
	// getFilePath
	// ========================================================================

	public static File getFilePath(String... args) {
		boolean isValid = true;
		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			isValid = isValid && isValidFilename(arg);
		}
		if (isValid) {
			String filePath = "";
			for (int i = 0; i < args.length; i++) {
				filePath += args[i] + fileSep;
			}
			filePath = filePath.substring(0, filePath.length() - fileSep.length());
			File file = new File(filePath);
			if (file.exists()) {
				return file;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public static boolean isValidFilename(String filename) {
		if (filename == null) {
			return false;
		} else if (filename.contains("\\") || filename.contains("/") || filename.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}