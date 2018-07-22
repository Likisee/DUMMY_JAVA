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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
	public static boolean writeStringToFile(File file, String content) {
		try {
			org.apache.commons.io.FileUtils.writeStringToFile(file, content, "UTF-8", false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			// TODO Auto-generated catch block
			e.printStackTrace();
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