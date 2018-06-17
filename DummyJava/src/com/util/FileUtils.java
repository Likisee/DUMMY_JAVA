package com.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FileUtils {

	private static Log logger = LogFactory.getLog(FileUtils.class);

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
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	public static String writeStringToFile(File file, String content) {
		String absoluteFile = "";
		try {
			org.apache.commons.io.FileUtils.writeStringToFile(file, content, "UTF-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return absoluteFile;
	}

	public static String writeStringToFile(String filePath, String content) {
		return writeStringToFile(new File(filePath), content);
	}

	public static String writeStringToFile(String fileFolder, String filename, String content) {
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (isr != null) {
				try {
					isr.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (fs != null) {
				try {
					fs.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	// ========================================================================
	// printStringAsFile
	// ========================================================================

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