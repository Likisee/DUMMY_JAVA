package com.logic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import net.sf.json.JSONArray;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import au.com.bytecode.opencsv.CSVReader;

import com.constant.Const;
import com.util.CollectionUtil;
import com.util.DataSourceUtil;
import com.util.DateTimeUtil;
import com.util.FileUtil;
import com.util.HBaseUtil;
import com.util.MathUtil;
import com.util.StringUtil;
import com.util.DataSet.KaggleTheMoviesDatasetUtil;
import com.util.DataSet.MovieLensUtil;

public class MovieLensStatReport {

	private static Log logger = LogFactory.getLog(MovieLensStatReport.class);

		private static String filenameLinks = "links.csv";
	private static String filenameMovies = "movies.csv";
	private static String filenameRatings = "ratings.csv";
	private static String filenameTags = "tags.csv";
	
	private static String userIdListResult = "userIdList.result";
	private static String movieIdListResult = "movieIdList.result";
	
	// ================================================================================================================================================
	// ================================================================================================================================================

	// ========================================================================
	// Data Pre-process: One-time effort
	// ========================================================================
	
	private static void exportTopRatingUserId(String filePath) {
		HashMap<String, Integer> mapUserIdCnt = new HashMap<String, Integer>();
		HashMap<String, Long> mapUserMinTimestamp = new HashMap<String, Long>();
		HashMap<String, Long> mapUserMaxTimestamp = new HashMap<String, Long>();
		try {
			CSVReader reader = new CSVReader(new FileReader(filePath), ',', '"', 1);
			String[] nextLine;
			String userId;
			Long timestamp;
			while ((nextLine = reader.readNext()) != null) {
				if (nextLine != null) {
					userId = nextLine[0];
					if (mapUserIdCnt.containsKey(userId)) {
						mapUserIdCnt.put(userId, mapUserIdCnt.get(userId) + 1);
					} else {
						mapUserIdCnt.put(userId, 1);
					}

					timestamp = Long.valueOf(nextLine[3]);
					if (!mapUserMinTimestamp.containsKey(userId) || !mapUserMaxTimestamp.containsKey(userId)) {
						mapUserMinTimestamp.put(userId, Long.MAX_VALUE);
						mapUserMaxTimestamp.put(userId, Long.MIN_VALUE);
					}
					if (mapUserMinTimestamp.get(userId) > timestamp) {
						mapUserMinTimestamp.put(userId, timestamp);
					}
					if (mapUserMaxTimestamp.get(userId) < timestamp) {
						mapUserMaxTimestamp.put(userId, timestamp);
					}
				}
			}
		} catch (FileNotFoundException e) {
			logger.error("MovieLensStatReport.exportTopRatingUserId Error: " + e.getMessage(), e);
		} catch (IOException e) {
			logger.error("MovieLensStatReport.exportTopRatingUserId Error: " + e.getMessage(), e);
		}

		String userId;
		int count;
		TreeMap<Integer, ArrayList<String>> mapUserIdCntRank = new TreeMap<Integer, ArrayList<String>>(Collections.reverseOrder());
		ArrayList<String> userIdList;
		for (Map.Entry<String, Integer> entry : mapUserIdCnt.entrySet()) {
			userId = entry.getKey();
			count = entry.getValue();
			if (mapUserIdCntRank.containsKey(count)) {
				userIdList = mapUserIdCntRank.get(count);
				userIdList.add(userId);
				mapUserIdCntRank.put(count, userIdList);
			} else {
				userIdList = new ArrayList<String>();
				userIdList.add(userId);
				mapUserIdCntRank.put(count, userIdList);
			}
		}

		SimpleDateFormat sdf = DateTimeUtil.getSimpleDateTime(null);
		StringBuffer sbResult = new StringBuffer();
		for (Map.Entry<Integer, ArrayList<String>> entry : mapUserIdCntRank.entrySet()) {
			count = entry.getKey();
			userIdList = entry.getValue();
			for (int i = 0; i < userIdList.size(); i++) {
				userId = userIdList.get(i);
				sbResult.append(userId + Const.csvSep + count + Const.csvSep + mapUserMinTimestamp.get(userId) + Const.csvSep + sdf.format(mapUserMinTimestamp.get(userId) * 1000) + Const.csvSep + mapUserMaxTimestamp.get(userId) + Const.csvSep + sdf.format(mapUserMaxTimestamp.get(userId) * 1000) + Const.csvSep + Const.lineBrakeSep);
			}
		}

		File file = new File(filePath);
		File fileExport = new File(file.getParent() + File.separator + DataSourceUtil.dataExport + "TopRatingUserId.csv");
		FileUtil.writeStringToFile(fileExport, sbResult.toString());
	}

	private static void exportTopRatingMovieId(String filePath) {
		HashMap<String, Integer> mapMovieIdCnt = new HashMap<String, Integer>();
		HashMap<String, Long> mapMovieMinTimestamp = new HashMap<String, Long>();
		HashMap<String, Long> mapMovieMaxTimestamp = new HashMap<String, Long>();
		try {
			CSVReader reader = new CSVReader(new FileReader(filePath), ',', '"', 1);
			String[] nextLine;
			String movieId;
			Long timestamp;
			while ((nextLine = reader.readNext()) != null) {
				if (nextLine != null) {
					movieId = nextLine[1];
					if (mapMovieIdCnt.containsKey(movieId)) {
						mapMovieIdCnt.put(movieId, mapMovieIdCnt.get(movieId) + 1);
					} else {
						mapMovieIdCnt.put(movieId, 1);
					}

					timestamp = Long.valueOf(nextLine[3]);
					if (!mapMovieMinTimestamp.containsKey(movieId) || !mapMovieMaxTimestamp.containsKey(movieId)) {
						mapMovieMinTimestamp.put(movieId, Long.MAX_VALUE);
						mapMovieMaxTimestamp.put(movieId, Long.MIN_VALUE);
					}
					if (mapMovieMinTimestamp.get(movieId) > timestamp) {
						mapMovieMinTimestamp.put(movieId, timestamp);
					}
					if (mapMovieMaxTimestamp.get(movieId) < timestamp) {
						mapMovieMaxTimestamp.put(movieId, timestamp);
					}
				}
			}
		} catch (FileNotFoundException e) {
			logger.error("MovieLensStatReport.exportTopRatingMovieId Error: " + e.getMessage(), e);
		} catch (IOException e) {
			logger.error("MovieLensStatReport.exportTopRatingMovieId Error: " + e.getMessage(), e);
		}

		String movieId;
		int count;
		TreeMap<Integer, ArrayList<String>> mapMovieIdCntRank = new TreeMap<Integer, ArrayList<String>>(Collections.reverseOrder());
		ArrayList<String> movieIdList;
		for (Map.Entry<String, Integer> entry : mapMovieIdCnt.entrySet()) {
			movieId = entry.getKey();
			count = entry.getValue();
			if (mapMovieIdCntRank.containsKey(count)) {
				movieIdList = mapMovieIdCntRank.get(count);
				movieIdList.add(movieId);
				mapMovieIdCntRank.put(count, movieIdList);
			} else {
				movieIdList = new ArrayList<String>();
				movieIdList.add(movieId);
				mapMovieIdCntRank.put(count, movieIdList);
			}
		}

		SimpleDateFormat sdf = DateTimeUtil.getSimpleDateTime(null);
		StringBuffer sbResult = new StringBuffer();
		for (Map.Entry<Integer, ArrayList<String>> entry : mapMovieIdCntRank.entrySet()) {
			count = entry.getKey();
			movieIdList = entry.getValue();
			for (int i = 0; i < movieIdList.size(); i++) {
				movieId = movieIdList.get(i);
				sbResult.append(movieId + Const.csvSep + count + Const.csvSep + mapMovieMinTimestamp.get(movieId) + Const.csvSep + sdf.format(mapMovieMinTimestamp.get(movieId) * 1000) + Const.csvSep + mapMovieMaxTimestamp.get(movieId) + Const.csvSep + sdf.format(mapMovieMaxTimestamp.get(movieId) * 1000) + Const.csvSep + Const.lineBrakeSep);
			}
		}

		File file = new File(filePath);
		File fileExport = new File(file.getParent() + File.separator + DataSourceUtil.dataExport + "TopRatingMovieId.csv");
		FileUtil.writeStringToFile(fileExport, sbResult.toString());
	}

	private static void exportRatingsByDate(String filePath) {
		HashMap<String, StringBuffer> mapDateRatings = new HashMap<String, StringBuffer>();
		try {
			CSVReader reader = new CSVReader(new FileReader(filePath), ',', '"', 1);
			String[] nextLine;
			Long timestamp;
			SimpleDateFormat sdf = DateTimeUtil.getSimpleDateTime(null);
			String date;
			while ((nextLine = reader.readNext()) != null) {
				if (nextLine != null) {

					timestamp = Long.valueOf(nextLine[3]);
					date = sdf.format(timestamp * 1000);

					if (mapDateRatings.containsKey(date)) {
						mapDateRatings.get(date).append(StringUtil.getStringArray2Csv(nextLine) + Const.lineBrakeSep);
					} else {
						StringBuffer sb = new StringBuffer();
						sb.append(StringUtil.getStringArray2Csv(nextLine) + Const.lineBrakeSep);
						mapDateRatings.put(date, sb);
					}
				}
			}
		} catch (FileNotFoundException e) {
			logger.error("MovieLensStatReport.exportRatingsByDate Error: " + e.getMessage(), e);
		} catch (IOException e) {
			logger.error("MovieLensStatReport.exportRatingsByDate Error: " + e.getMessage(), e);
		}

		File file = new File(filePath);
		String filename, content;
		for (Map.Entry<String, StringBuffer> entry : mapDateRatings.entrySet()) {
			filename = entry.getKey();
			content = entry.getValue().toString();
			File fileExport = new File(file.getParent() + File.separator + DataSourceUtil.dataExport + "RatingsByDate" + File.separator + filename + ".csv");
			FileUtil.writeStringToFile(fileExport, "userId,movieId,rating,timestamp," + Const.lineBrakeSep + content);
		}
	}
	
	private static ArrayList<String> loadUserIdList(String filePath) {
		ArrayList<String> userIdList = new ArrayList<String>();
		File file = new File(filePath);
		File fileExport = new File(file.getParent() + File.separator + DataSourceUtil.dataExport + userIdListResult);
		if (!fileExport.exists()) {
			ArrayList<Integer> userIdListTemp = new ArrayList<Integer>();
			try {
				CSVReader reader = new CSVReader(new FileReader(filePath), ',', '"', 1);
				String[] nextLine;
				while ((nextLine = reader.readNext()) != null) {
					if (nextLine != null) {
						userIdListTemp.add(Integer.valueOf(nextLine[0])); // userId
					}
				}
				CollectionUtil.getIntegerArrayListSortWithoutDuplication(userIdListTemp); // make order
				
				for (Integer userId : userIdListTemp) {
					userIdList.add(userId + "");
				}
				FileUtil.writeObjectToFile(fileExport, userIdList);
			} catch (FileNotFoundException e) {
				logger.error("MovieLensStatReport.loadUserIdList Error: " + e.getMessage(), e);
			} catch (IOException e) {
				logger.error("MovieLensStatReport.loadUserIdList Error: " + e.getMessage(), e);
			}
		}
		userIdList = (ArrayList<String>) FileUtil.readFileAsObject(fileExport);
		return userIdList;
	}

	private static ArrayList<String> loadMovieIdList(String filePath) {
		ArrayList<String> movieIdList = new ArrayList<String>();
		File file = new File(filePath);
		File fileExport = new File(file.getParent() + File.separator + DataSourceUtil.dataExport + movieIdListResult);
		if (!fileExport.exists()) {
			ArrayList<Integer> movieIdListTemp = new ArrayList<Integer>();
			try {
				CSVReader reader = new CSVReader(new FileReader(filePath), ',', '"', 1);
				String[] nextLine;
				while ((nextLine = reader.readNext()) != null) {
					if (nextLine != null) {
						movieIdListTemp.add(Integer.valueOf(nextLine[1])); // movieId
					}
				}
				CollectionUtil.getStringArrayListSortWithoutDuplication(movieIdList); // make order
				
				for (Integer movieId : movieIdListTemp) {
					movieIdList.add(movieId + "");
				}
				FileUtil.writeObjectToFile(fileExport, movieIdList);
			} catch (FileNotFoundException e) {
				logger.error("MovieLensStatReport.loadMovieIdList Error: " + e.getMessage(), e);
			} catch (IOException e) {
				logger.error("MovieLensStatReport.loadMovieIdList Error: " + e.getMessage(), e);
			}
		}
		movieIdList = (ArrayList<String>) FileUtil.readFileAsObject(fileExport);
		return movieIdList;
	}
	
	// ================================================================================================================================================
	// ================================================================================================================================================
	
	// ========================================================================
	// Data Prepare: mapUserId2MovieId, mapMovieId2UserId
	// ========================================================================
	
	private static HashMap<String, HashSet<String>> loadRatingsMapUserId2MovieId(String filePath, ArrayList<String> userIdList, Integer daysPre, Integer daysAfter) {
		HashMap<String, HashSet<String>> mapUserId2MovieId = new HashMap<String, HashSet<String>>();

		// initiate fileList
		File file = new File(filePath);
		File fileRatingsByDate = new File(file.getParent() + File.separator + DataSourceUtil.dataExport + "RatingsByDate");
		File[] fileRatingsByDateArr = fileRatingsByDate.listFiles();

		// initiate timestampMin, timestampMax for the null handling
		// initiate ratingTimestampSet (reduce computational cost)
		SimpleDateFormat sdf = DateTimeUtil.getSimpleDateTime(null);
		long timestamp;
		long timestampMin = Long.MAX_VALUE, timestampMax = Long.MIN_VALUE;
		HashSet<Long> ratingTimestampSet = new HashSet<Long>();
		for (int i = 0; i < fileRatingsByDateArr.length; i++) {
			try {
				timestamp = sdf.parse(fileRatingsByDateArr[i].getName().substring(0, 10)).getTime() / 1000; // normalize sec/min/hr to sdf's "date"
				if (timestampMin > timestamp) {
					timestampMin = timestamp;
				}
				if (timestampMax < timestamp) {
					timestampMax = timestamp;
				}
				ratingTimestampSet.add(timestamp);
			} catch (ParseException e) {
				logger.error("MovieLensStatReport.loadRatingsMapUserId2MovieId Error: " + e.getMessage(), e);
			}
		}

		// initiate userIdSet
		HashSet<String> userIdSet = new HashSet<String>();
		if (userIdList == null) {
			userIdSet.addAll(loadUserIdList(filePath));
		} else {
			userIdSet.addAll(userIdList);
		}

		// initiate mapUserId2MovieId
		for (String userId : userIdSet) {
			mapUserId2MovieId.put(userId, new HashSet<String>());
		}

		// parse file
		String userId, movieId;
		HashSet<String> movieIdSet = null;
		long timestampStart, timestampEnd;
		if (file.exists()) {
			try {
				CSVReader reader = new CSVReader(new FileReader(file), ',', '"', 1);
				String[] nextLine;
				while ((nextLine = reader.readNext()) != null) {
					if (nextLine != null) {
						try {
							userId = nextLine[0];
							movieId = nextLine[1];
							timestamp = Long.valueOf(nextLine[3]);
							timestamp = sdf.parse(sdf.format(timestamp * 1000)).getTime() / 1000; // normalize sec/min/hr to sdf's "date"

							movieIdSet = new HashSet<String>();
							if (daysPre == null && daysAfter == null) {
								movieIdSet.add(movieId);
							} else {
								if (daysPre != null) {
									timestampStart = timestamp - daysPre * 60 * 60 * 24;
//									if (timestampStart < timestampMin) {
//										timestampStart = timestampMin;
//									}
								} else {
									timestampStart = timestampMin;
								}
								if (daysAfter != null) {
									timestampEnd = timestamp + daysAfter * 60 * 60 * 24;
//									if (timestampEnd > timestampMax) {
//										timestampEnd = timestampMax;
//									}
								} else {
									timestampEnd = timestampMax;
								}
								for (long i = timestampStart; i <= timestampEnd; i = i + 60 * 60 * 24) {
//									if (ratingTimestampSet.contains(i)) {
										movieIdSet.add(HBaseUtil.getStringConcate(movieId, i + ""));
//									}
								}
							}
							if (mapUserId2MovieId.containsKey(userId)) {
								mapUserId2MovieId.get(userId).addAll(movieIdSet);
							}
						} catch (ParseException e) {
							logger.error("MovieLensStatReport.loadRatingsMapUserId2MovieId Error: " + e.getMessage(), e);
						}
					}
				}
			} catch (FileNotFoundException e) {
				logger.error("MovieLensStatReport.loadRatingsMapUserId2MovieId Error: " + e.getMessage(), e);
			} catch (IOException e) {
				logger.error("MovieLensStatReport.loadRatingsMapUserId2MovieId Error: " + e.getMessage(), e);
			}
		}
		return mapUserId2MovieId;
	}
	
	private static HashMap<String, HashSet<String>> getRatingsMapReverse(HashMap<String, HashSet<String>> reatingMap) {
		HashMap<String, HashSet<String>> result = new HashMap<String, HashSet<String>>();
		for (Map.Entry<String, HashSet<String>> entry : reatingMap.entrySet()) {
			String key = entry.getKey();
			HashSet<String> valueSet = entry.getValue();
			for (String keyNew : valueSet) {
				if (!result.containsKey(keyNew)) {
					result.put(keyNew, new HashSet<String>());
				}
				result.get(keyNew).add(key);
			}
		}
		return result;
	}
	
	private static ArrayList<ArrayList<Integer>> getUserIdBasedMatrix(String filePathRatings, ArrayList<String> userIdList, Integer daysPre, Integer daysAfter) {
		ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
		StringBuffer rsb = new StringBuffer("");

		// get mapMovieId2UserId
		HashMap<String, HashSet<String>> mapUserId2MovieId = loadRatingsMapUserId2MovieId(filePathRatings, userIdList, daysPre, daysAfter);
		HashMap<String, HashSet<String>> mapMovieId2UserId = getRatingsMapReverse(mapUserId2MovieId);
		
		// get mapMovieId2UserIdPairs
		String movieId = null;
		HashSet<String> pairs = null;
		HashMap<String, HashSet<String>> mapMovieId2UserIdPairs = new HashMap<String, HashSet<String>>();
		for (Map.Entry<String, HashSet<String>> entry : mapMovieId2UserId.entrySet()) {
			movieId = HBaseUtil.getStringSplit(entry.getKey()).get(0);
			if (!mapMovieId2UserIdPairs.containsKey(movieId)) {
				mapMovieId2UserIdPairs.put(movieId, new HashSet<String>());
			}
			pairs = mapMovieId2UserIdPairs.get(movieId);
			for (String userId1 : entry.getValue()) {
				for (String userId2 : entry.getValue()) {
					if (!userId1.equals(userId2)) {
						pairs.add(HBaseUtil.getStringConcate(userId1, userId2));
					}
				}
			}
			mapMovieId2UserIdPairs.put(movieId, pairs);
		}
		
		// get mapUserIdPairMovieIds
		HashMap<String, HashSet<String>> mapUserIdPair2MovieId = new HashMap<String, HashSet<String>>();
		for(Map.Entry<String, HashSet<String>> entry : mapMovieId2UserIdPairs.entrySet()) {
			movieId = entry.getKey();
			for(String userIdPair : entry.getValue()) {
				if (!mapUserIdPair2MovieId.containsKey(userIdPair)) {
					mapUserIdPair2MovieId.put(userIdPair, new HashSet<String>());
				}
				mapUserIdPair2MovieId.get(userIdPair).add(movieId);
			}
		}
		
		// get Result
		String userIdRow, userIdCol, userIdPair;
		int count, countMax = Integer.MIN_VALUE;
		for (int i = 0; i < userIdList.size(); i++) {
			userIdRow = userIdList.get(i);
			ArrayList<Integer> userIdRowList = new ArrayList<Integer>();
			for (int j = 0; j < userIdList.size(); j++) {
				userIdCol = userIdList.get(j);
				userIdPair = HBaseUtil.getStringConcate(userIdRow, userIdCol);
				count = 0;
				if (mapUserIdPair2MovieId.containsKey(userIdPair)) {
					count = mapUserIdPair2MovieId.get(userIdPair).size();
					if (countMax < count) {
						countMax = count;
					}
				}
				userIdRowList.add(count);
			}
			result.add(userIdRowList);
			rsb.append(JSONArray.fromObject(userIdRowList) + Const.lineBrakeSep);
		}
		
		// get dump
		logger.info("countMax: " + countMax);
//		String filename = "userIdBasedMatrix_" + daysPre + "_" + daysAfter + "_" + countMax + ".txt"; // <BAD_FILE_NAMING>
		String filename = "userIdBasedMatrix_" + daysPre + "_" + daysAfter + ".txt";
		File file = new File(filePathRatings);
		File fileMatrix = new File(file.getParent() + File.separator + DataSourceUtil.dataExport + "UserIdBasedMatrix" + File.separator + filename);
		FileUtil.writeStringToFile(fileMatrix, rsb.toString());
		return result;
	}
	
	private static void digestUserIdBasedMatrix(String folderPathUserIdBasedMatrix) {
		StringBuffer rsbMean = new StringBuffer("");
		StringBuffer rsbMin = new StringBuffer("");
		StringBuffer rsbMedian = new StringBuffer("");
		StringBuffer rsbMax = new StringBuffer("");
		StringBuffer rsbStdDev = new StringBuffer("");
		
		// <BAD_FILE_NAMING>
//		File folder = new File(folderPathUserIdBasedMatrix);
//		String newFilename;
//		File newFile;
//		File[] fileArr = folder.listFiles();
//		for (int i = 0; i < fileArr.length; i++) {
//			if(fileArr[i].isFile()) {
//				newFilename = fileArr[i].getName().replace(".txt", "");
//				newFilename = newFilename.substring(0, newFilename.lastIndexOf("_")) + ".txt";
//				newFile = new File(folderPathUserIdBasedMatrix + File.separator + newFilename);
//				fileArr[i].renameTo(newFile);
//			}
//		}
		String filename;
		File file;
		for (int daysPre = 0; daysPre <= 100; daysPre++) {
			for (int daysAfter = 0; daysAfter <= 100; daysAfter++) {
				filename = "userIdBasedMatrix_" + daysPre + "_" + daysAfter + ".txt";
				logger.info(filename);
				file = new File(folderPathUserIdBasedMatrix + File.separator + filename);
				if(file.exists()) {
					double value;
					ArrayList<Double> dataList = new ArrayList<Double>();
					ArrayList<Double> noZeroDataList = new ArrayList<Double>();
					String[] contentArr = FileUtil.readFileAsString(file).split(Const.lineBrakeDelim);
					for (int i = 0; i < contentArr.length; i++) {
						JSONArray jData = JSONArray.fromObject(contentArr[i]);
						for (int j = 0; j < jData.size(); j++) {
							value = jData.getDouble(j);
							dataList.add(value);
							if (value != 0) {
								noZeroDataList.add(value);
							}
						}
					}
					rsbMean.append(MathUtil.getMean(dataList) + "\t");
					rsbMin.append(MathUtil.getMin(dataList)+ "\t");
					rsbMedian.append(MathUtil.getMedian(dataList)+ "\t");
					rsbMax.append(MathUtil.getMax(dataList)+ "\t");
					rsbStdDev.append(MathUtil.getStdDev(noZeroDataList) + "\t");
				}
			}
			rsbMean.append(Const.lineBrakeSep);
			rsbMin.append(Const.lineBrakeSep);
			rsbMedian.append(Const.lineBrakeSep);
			rsbMax.append(Const.lineBrakeSep);
			rsbStdDev.append(Const.lineBrakeSep);
		}

		// get dump
		filename = "DigestedUserIdBasedMatrix_" + "Mean" + ".txt";
		file = new File(folderPathUserIdBasedMatrix);
		File fileResult = new File(file.getParent() + File.separator + "UserIdBasedMatrix" + File.separator + filename);
		FileUtil.writeStringToFile(fileResult, rsbMean.toString());
		
		filename = "DigestedUserIdBasedMatrix_" + "Min" + ".txt";
		file = new File(folderPathUserIdBasedMatrix);
		fileResult = new File(file.getParent() + File.separator + "UserIdBasedMatrix" + File.separator + filename);
		FileUtil.writeStringToFile(fileResult, rsbMin.toString());
		
		filename = "DigestedUserIdBasedMatrix_" + "Median" + ".txt";
		file = new File(folderPathUserIdBasedMatrix);
		fileResult = new File(file.getParent() + File.separator + "UserIdBasedMatrix" + File.separator + filename);
		FileUtil.writeStringToFile(fileResult, rsbMedian.toString());
		
		filename = "DigestedUserIdBasedMatrix_" + "Max" + ".txt";
		file = new File(folderPathUserIdBasedMatrix);
		fileResult = new File(file.getParent() + File.separator + "UserIdBasedMatrix" + File.separator + filename);
		FileUtil.writeStringToFile(fileResult, rsbMax.toString());
		
		filename = "DigestedUserIdBasedMatrix_" + "StdDev" + ".txt";
		file = new File(folderPathUserIdBasedMatrix);
		fileResult = new File(file.getParent() + File.separator + "UserIdBasedMatrix" + File.separator + filename);
		FileUtil.writeStringToFile(fileResult, rsbStdDev.toString());
	}
	
	private static TreeMap<Integer, HashSet<String>> getRankUserPairs(String folderPathUserIdBasedMatrix, int daysPre, int daysAfter) {
		TreeMap<Integer, HashSet<String>> rankUserPairs = new TreeMap<Integer, HashSet<String>>(Collections.reverseOrder());
		
		String filename;
		File file;
		filename = "userIdBasedMatrix_" + daysPre + "_" + daysAfter + ".txt";
		file = new File(folderPathUserIdBasedMatrix + File.separator + filename);
		if(!file.exists()) {
			// generate
		}
		if(file.exists()) {
			double value;
			ArrayList<Double> dataList = new ArrayList<Double>();
			ArrayList<Double> noZeroDataList = new ArrayList<Double>();
			String[] contentArr = FileUtil.readFileAsString(file).split(Const.lineBrakeDelim);
			for (int i = 0; i < contentArr.length; i++) {
				JSONArray jData = JSONArray.fromObject(contentArr[i]);
				for (int j = 0; j < jData.size(); j++) {
					value = jData.getDouble(j);
					dataList.add(value);
					if (value != 0) {
						noZeroDataList.add(value);
					}
					// THE DIFF
					if (value != 0) {
						if(i < j) {
							if(!rankUserPairs.containsKey((int) value)) {
								rankUserPairs.put((int) value, new HashSet<String>());
							}
							rankUserPairs.get((int) value).add(HBaseUtil.getStringConcate((i + 1) + "", (j + 1) + ""));
						}
					}
				}
			}
		}
		return rankUserPairs;
	}
	
	private static void getUserPairHistory(String filePathRatings, int count, String userPair) {
		// initiate fileList
		File file = new File(filePathRatings);

		// userPair
		String userId1 = HBaseUtil.getStringSplit(userPair).get(0);
		String userId2 = HBaseUtil.getStringSplit(userPair).get(1);

		// initiate timestampMap, ratingMap
		HashMap<String, Long> timestampMap = new HashMap<String, Long>();
		HashMap<String, String> ratingMap = new HashMap<String, String>();

		// parse file
		String userId, movieId, rating;
		long timestamp;
		SimpleDateFormat sdf = DateTimeUtil.getSimpleDateTime(null);
		StringBuffer content = new StringBuffer("");
		int cntLarger = 0, cntSmaller = 0;
		if (file.exists()) {
			try {
				CSVReader reader = new CSVReader(new FileReader(file), ',', '"', 1);
				String[] nextLine;
				while ((nextLine = reader.readNext()) != null) {
					if (nextLine != null) {
						try {
							userId = nextLine[0];
							movieId = nextLine[1];
							rating = nextLine[2];
							if (userId1.equals(userId) || userId2.equals(userId)) {
								timestamp = Long.valueOf(nextLine[3]);
//								timestamp = sdf.parse(sdf.format(timestamp * 1000)).getTime() / 1000; // normalize sec/min/hr to sdf's "date"
								if (timestampMap.containsKey(movieId)) {
									if (userId1.equals(userId)) {
										content.append(movieId + "," + timestamp + "," + timestampMap.get(movieId) + "," + Double.valueOf(timestamp - timestampMap.get(movieId)) / 60 / 60 / 24 + "," 
									+ rating + "," + ratingMap.get(movieId) + "," + Const.lineBrakeSep);

										if (timestamp > timestampMap.get(movieId)) {
											cntLarger++;
										} else {
											cntSmaller++;
										}
									} else {
										content.append(movieId + "," + timestampMap.get(movieId) + "," + timestamp + "," + Double.valueOf(timestampMap.get(movieId) - timestamp) / 60 / 60 / 24 + ","
									+ ratingMap.get(movieId) + "," + rating + "," + Const.lineBrakeSep);

										if (timestampMap.get(movieId) > timestamp) {
											cntLarger++;
										} else {
											cntSmaller++;
										}
									}
								} else {
									timestampMap.put(movieId, timestamp);
									ratingMap.put(movieId, rating);
								}
							}
						} catch (Exception e) {
							logger.error("MovieLensStatReport.getUserPairHistory Error: " + e.getMessage(), e);
						}
					}
				}
			} catch (Exception e) {
				logger.error("MovieLensStatReport.getUserPairHistory Error: " + e.getMessage(), e);
			}
			
			if(cntLarger > 0 && cntSmaller > 0) {
				String filename = count + "~" + userId1 + "~" + userId2 + "~" + cntLarger + "~" + cntSmaller + ".txt";
				File fileExport = new File(file.getParent() + File.separator + DataSourceUtil.dataExport + "UserPairHistory" + File.separator + filename + ".csv");
				FileUtil.writeStringToFile(fileExport, "movieId,timestamp_userId1,timestamp_userId2,timestamp_diff,userId1_rating,userId2_rating," + Const.lineBrakeSep + content);				
			} else {
				logger.info(count + "~" + userId1 + "~" + userId2 + "~" + cntLarger + "~" + cntSmaller + ".txt");;
			}
		}
	}
	
	private static void getUserPairHistory(String filePathRatings, ArrayList<String> userIdList, HashMap<String, Long> mapMovieId2ReleaseLong) {
		// initiate fileList
		File file = new File(filePathRatings);

		// initiate mapRatingTimestampOffset, mapMovieIdUserIdSet
		HashMap<String, Long> mapRatingTimestampOffset = new HashMap<String, Long>(); // HashMap<String movieId~|userId, Long ratingTimestampOffset>
		HashMap<String, TreeSet<Integer>> mapMovieIdUserIdSet = new HashMap<String, TreeSet<Integer>>();

		// parse file
		String userId, movieId, rating;
		long timestamp, releaseTimestamp, offset;
		if (file.exists()) {
			try {
				CSVReader reader = new CSVReader(new FileReader(file), ',', '"', 1);
				String[] nextLine;
				while ((nextLine = reader.readNext()) != null) {
					if (nextLine != null) {
						try {
							userId = nextLine[0];
							movieId = nextLine[1];
							rating = nextLine[2];
							timestamp = Long.valueOf(nextLine[3]);
							if(mapMovieId2ReleaseLong.containsKey(movieId)) {
								releaseTimestamp = mapMovieId2ReleaseLong.get(movieId);
								offset = timestamp - releaseTimestamp; 
								
								mapRatingTimestampOffset.put(HBaseUtil.getStringConcate(movieId, userId), offset);
								if(mapMovieIdUserIdSet.containsKey(movieId)) {
									mapMovieIdUserIdSet.get(movieId).add(Integer.valueOf(userId));
								} else {
									TreeSet<Integer> userIdSet = new TreeSet<Integer>();
									userIdSet.add(Integer.valueOf(userId));
									mapMovieIdUserIdSet.put(movieId, userIdSet);
								}
							}
						} catch (Exception e) {
							logger.error("MovieLensStatReport.getUserPairHistory Error: " + e.getMessage(), e);
						}
					}
				}
			} catch (Exception e) {
				logger.error("MovieLensStatReport.getUserPairHistory Error: " + e.getMessage(), e);
			}
		}
		
		// initiate mapCoRatingCount, mapCoRatinOffsetgCount
		HashMap<String, Integer> mapCoRatingCount = new HashMap<String, Integer>(); // HashMap<String userId1~|userId2, Integer count>
		HashMap<String, HashMap<Integer, Integer>> mapCoRatingOffsetCount = new HashMap<String, HashMap<Integer, Integer>>(); // HashMap<String userId1~|userId2, HashMap<Integer offset, Integer count>>
		HashMap<String, HashMap<Integer, Integer>> mapCoRatingAbsOffsetCount = new HashMap<String, HashMap<Integer, Integer>>(); // HashMap<String userId1~|userId2, HashMap<Integer ABS(offset), Integer count>>
		
		// mapRatingTimestampOffset + mapMovieIdUserIdSet = mapCoRatingCount, mapCoRatinOffsetCount
		String userIdPair;
		int daysWithin;
		long daysOffset1, daysOffset2;
		HashMap<Integer, Integer> data = null;
		for(Map.Entry<String, TreeSet<Integer>> entry : mapMovieIdUserIdSet.entrySet()) {
			movieId = entry.getKey();
			for(Integer userId1 : entry.getValue()) {
				for(Integer userId2 : entry.getValue()) {
					if(userId1 < userId2) {
						
						// mapCoRatingCount
						userIdPair = HBaseUtil.getStringConcate(userId1 + "", userId2 + "");
						if(mapCoRatingCount.containsKey(userIdPair)) {
							mapCoRatingCount.put(userIdPair, mapCoRatingCount.get(userIdPair) + 1);
						} else {
							mapCoRatingCount.put(userIdPair, 1);
						}
						
						// mapCoRatingOffsetCount, mapCoRatingAbsOffsetCount
						daysOffset1 = mapRatingTimestampOffset.get(HBaseUtil.getStringConcate(movieId, userId1 + ""));
						daysOffset2 = mapRatingTimestampOffset.get(HBaseUtil.getStringConcate(movieId, userId2 + ""));
						daysWithin = MathUtil.getDaysWithin((daysOffset2 - daysOffset1) / 60 / 60 / 24);
						if(mapCoRatingOffsetCount.containsKey(userIdPair)) {
							data = mapCoRatingOffsetCount.get(userIdPair);
						} else {
							data = new HashMap<Integer, Integer>();
						}
						if(data.containsKey(daysWithin)) {
							data.put(daysWithin, data.get(daysWithin) + 1);
						} else {
							data.put(daysWithin, 1);
						}
						mapCoRatingOffsetCount.put(userIdPair, data);
						
						daysWithin = Math.abs(daysWithin);
						if(mapCoRatingAbsOffsetCount.containsKey(userIdPair)) {
							data = mapCoRatingAbsOffsetCount.get(userIdPair);
						} else {
							data = new HashMap<Integer, Integer>();
						}
						if(data.containsKey(daysWithin)) {
							data.put(daysWithin, data.get(daysWithin) + 1);
						} else {
							data.put(daysWithin, 1);
						}
						mapCoRatingAbsOffsetCount.put(userIdPair, data);
					}
				}
			}
		}
		
		// TODO get quartile
		// get median
		ArrayList<Double> countList = new ArrayList<Double>();
		for(Map.Entry<String, Integer> entry :mapCoRatingCount.entrySet()) {
			countList.add((double) entry.getValue());
		}
		Double countMedian = MathUtil.getMedian(countList);

		StringBuffer content = new StringBuffer("");
		for(countMedian = 10.0; countMedian <= 600; countMedian = countMedian + 10) {

			// dump report
			int [] countArr = new int[201];
			for(int i = 0; i < userIdList.size(); i++) {
				for(int j = i + 1; j < userIdList.size(); j++) {
					String userId1 = userIdList.get(i);
					String userId2 = userIdList.get(j);
					userIdPair = HBaseUtil.getStringConcate(userId1 + "", userId2 + "");
//					logger.info(userIdPair + " is processing.");
					if(mapCoRatingCount.containsKey(userIdPair)
							&& mapCoRatingCount.get(userIdPair) > countMedian) {
						
						int totalCount = mapCoRatingCount.get(userIdPair);
						
						countArr[0] += totalCount;
						
						data = mapCoRatingAbsOffsetCount.get(userIdPair);
						for(Map.Entry<Integer, Integer> entry : data.entrySet()) {
							for(int days = 1; days <= 200; days++) { // 0 is impossible
								if(entry.getKey() <= days) {
									countArr[days] += entry.getValue();
								}
							}
						}
					}
				}	
			}
			content.append(countMedian + ",");
			content.append(countArr[0] + ",");
			for(int i = 1; i < countArr.length; i++) {
				content.append(Double.valueOf(countArr[i]) / countArr[0] + ",");
			}
			content.append(Const.lineBrakeSep);
		}

		// save report
		StringBuffer header = new StringBuffer("");
		header.append("PairTotalCountUpperThan" + ",");
		header.append("SumTotalCount" + ",");
		for(int days = 1; days <= 200; days++) {
			header.append(days + ",");
		}
		header.append(Const.lineBrakeSep);
		String filename = "getUserPairHistory_20190421.txt";
		File fileExport = new File(file.getParent() + File.separator + DataSourceUtil.dataExport + "UserPairHistory" + File.separator + filename + ".csv");
		FileUtil.writeStringToFile(fileExport, header.toString() + content.toString());
	}
	
	// 爆力法
	private static HashSet<String> searchIds(HashMap<String, HashSet<String>> collections, HashSet<String> keys) {
		HashSet<String> result = new HashSet<String>();
		for (String eachKey : keys) {
			if (collections.containsKey(eachKey)) {
				result.addAll(collections.get(eachKey));
			}
		}
		return result;
	}

	// 爆力法
	private static void calUserIdDistance(String filePathRatings,
			ArrayList<String> userIdList, Integer daysPre, Integer daysAfter, int deepLimit) {

		if (userIdList == null) {
			userIdList = loadUserIdList(filePathRatings);
		}

		HashMap<String, HashSet<String>> mapUserId2MovieId = loadRatingsMapUserId2MovieId(filePathRatings, userIdList, daysPre, daysAfter);
		HashMap<String, HashSet<String>> mapMovieId2UserId = getRatingsMapReverse(mapUserId2MovieId);

		String userId, movieId;
		HashSet<String> newUserIdSet, newMovieIdSet, currentUserIdSet, currentMovieIdSet;

		StringBuffer sbSpecialReport = new StringBuffer();

		// userIdList driven
		for (int i = 0; i < userIdList.size(); i++) {
			userId = userIdList.get(i);
			
			currentUserIdSet = new HashSet<String>();
			currentUserIdSet.add(userId);
			ArrayList<HashSet<String>> lstUserIdSet = new ArrayList<HashSet<String>>();
			ArrayList<HashSet<String>> lstMovieIdSet = new ArrayList<HashSet<String>>();

			for (int j = 0; j < deepLimit; j++) {
				newMovieIdSet = searchIds(mapUserId2MovieId, currentUserIdSet);
				lstUserIdSet.add(currentUserIdSet);
				lstMovieIdSet.add(newMovieIdSet);
				logger.info("currentUserIdList size: " + currentUserIdSet.size());
				logger.info("newMovieIdList size: " + newMovieIdSet.size());

				newUserIdSet = searchIds(mapMovieId2UserId, newMovieIdSet);
				if (currentUserIdSet.size() == newUserIdSet.size()) {
					break;
				} else {
					currentUserIdSet = newUserIdSet;
				}
			}

			// do report
			TreeMap<Integer, Integer> mapDistResult = new TreeMap<Integer, Integer>();
			for (int j = 0; j < lstUserIdSet.size() - 1; j++) {
				currentUserIdSet = (HashSet<String>) lstUserIdSet.get(j).clone();
				newUserIdSet = (HashSet<String>) lstUserIdSet.get(j + 1).clone();
				newUserIdSet.removeAll(currentUserIdSet);
				for (String eachUserId : newUserIdSet) {
					mapDistResult.put(Integer.valueOf(eachUserId), j + 1);
				}
				if (j == lstUserIdSet.size() - 2) {
					sbSpecialReport.append("userId:" + userId + "'s max depth is " + (j + 1) + Const.lineBrakeSep);
				}
			}

			// do dump
			StringBuffer sb = new StringBuffer();
			userId = userIdList.get(i);
			for (Map.Entry<Integer, Integer> entry : mapDistResult.entrySet()) {
				sb.append(userId + Const.csvSep + entry.getKey() + Const.csvSep + entry.getValue() + Const.csvSep + Const.lineBrakeSep);
			}
			File file = new File(filePathRatings);
			File fileExport = new File(file.getParent() + File.separator + DataSourceUtil.dataExport + "distanceUserId2MovieId" + File.separator + userId + ".csv");
			FileUtil.writeStringToFile(fileExport, "userId,userId,distance," + Const.lineBrakeSep + sb.toString());
			
		}
		
		// SpecialRepot
		File file = new File(DataSourceUtil.dataMovieLensSmall + filenameRatings);
		File fileExport = new File(file.getParent() + File.separator + DataSourceUtil.dataExport + "distanceUserId2MovieId" + File.separator + "SpecilaReport.txt");
		FileUtil.writeStringToFile(fileExport, sbSpecialReport.toString());
	}
	
	public static void main(String[] args) {

//		ArrayList<MovieLensMovieVo> listMovies = MovieLensUtils.parseMovieLensMovie("D:\\DataSet\\#Running\\MovieLens Latest Datasets\\ml-latest-small\\movies.csv");
//		logger.info(listMovies.get(0));
//		listMovies = MovieLensUtils.parseMovieLensMovie("D:\\DataSet\\#Running\\MovieLens Latest Datasets\\ml-latest\\movies.csv");
//		logger.info(listMovies.get(0));
//		ArrayList<MovieLensLinkVo> listLinks = MovieLensUtils.parseMovieLensLink("D:\\DataSet\\#Running\\MovieLens Latest Datasets\\ml-latest-small\\links.csv");
//		logger.info(listLinks.get(0));
//		listLinks = MovieLensUtils.parseMovieLensLink("D:\\DataSet\\#Running\\MovieLens Latest Datasets\\ml-latest\\links.csv");
//		logger.info(listLinks.get(0));
//		List<MovieLensRatingVo> listRatings = MovieLensUtils.parseMovieLensRating("D:\\DataSet\\#Running\\MovieLens Latest Datasets\\ml-latest-small\\ratings.csv");
//		logger.info(listRatings.get(0));
//		listRatings = MovieLensUtils.parseMovieLensRating("D:\\DataSet\\#Running\\MovieLens Latest Datasets\\ml-latest\\ratings.csv");
//		logger.info(listRatings.get(0));
//		ArrayList<MovieLensTagVo> listTags = MovieLensUtils.parseMovieLensTag("D:\\DataSet\\#Running\\MovieLens Latest Datasets\\ml-latest-small\\tags.csv");
//		logger.info(listTags.get(0));
//		listTags = MovieLensUtils.parseMovieLensTag("D:\\DataSet\\#Running\\MovieLens Latest Datasets\\ml-latest\\tags.csv");
//		logger.info(listTags.get(0));

		int deepLimit = 10;
		
		/* Small */
//		exportTopRatingUserId(dataMovieLensSmall + filenameRatings);
//		exportTopRatingMovieId(dataMovieLensSmall + filenameRatings);
//		exportRatingsByDate(dataMovieLensSmall + filenameRatings);
//		
//		ArrayList<Integer> userIdList = loadUserIdList(dataMovieLensSmall + filenameRatings);
//		logger.info(userIdList.size());															// 671
//		logger.info(userIdList.get(userIdList.size() - 1));										// 671
//		ArrayList<Integer> movieIdList = loadMovieIdList(dataMovieLensSmall + filenameRatings);
//		logger.info(movieIdList.size());														// 9066
//		logger.info(movieIdList.get(movieIdList.size() - 1));									// 163949

		// Non-matrix: 先使用暴力法玩玩
//		calUserIdDistance(dataMovieLensSmall + filenameRatings, 1, 671, null, null, deepLimit);	
		
		ArrayList<String> userIdList = loadUserIdList(DataSourceUtil.dataMovieLensSmall + filenameRatings);
		ArrayList<String> movieIdList = loadMovieIdList(DataSourceUtil.dataMovieLensSmall + filenameRatings);
		
		// 2018/12/18 00:02 ~ 2018/12/22 04:39
//		ArrayList<ArrayList<Integer>> result = getUserIdBasedMatrix(DataSourceUtil.dataMovieLensSmall + filenameRatings, userIdList, 50, 50);
//		ArrayList<ArrayList<Integer>> result = getUserIdBasedMatrix(DataSourceUtil.dataMovieLensSmall + filenameRatings, userIdList, 10, 10);
//		ArrayList<ArrayList<Integer>> result = getUserIdBasedMatrix(DataSourceUtil.dataMovieLensSmall + filenameRatings, userIdList, 0, 0);
//		for(int i = 0; i <= 100 ; i++) {
//			for(int j = 0; j <= 100 ; j++) {
//				result = getUserIdBasedMatrix(DataSourceUtil.dataMovieLensSmall + filenameRatings, userIdList, i, j);
//			}	
//		}
		
		// 2019/01/27
//		digestUserIdBasedMatrix(dataMovieLensSmall + ResourceUtil.dataExport + "UserIdBasedMatrix");

		// 2019/04/07
//		TreeMap<Integer, HashSet<String>> rankUserPairs = getRankUserPairs(DataSourceUtil.dataMovieLensSmall + DataSourceUtil.dataExport + "UserIdBasedMatrix", 10, 10);
//		for (Map.Entry<Integer, HashSet<String>> entry : rankUserPairs.entrySet()) {
//			int count = entry.getKey();
//			if (count > 49) {
//				for (String userPair : entry.getValue()) {
//					getUserPairHistory(DataSourceUtil.dataMovieLensSmall + filenameRatings, count, userPair);
//				}
//			}
//		}
		
		// 2019/04/20
		HashMap<String, String> mapMovieId2ImdbId =MovieLensUtil.getMapMovieId2ImdbId();
		HashMap<String, Date> mapImdbId2ReleaseDate = KaggleTheMoviesDatasetUtil.getMapImdbId2ReleaseDate();
		HashMap<String, Long> mapMovieId2ReleaseLong = new HashMap<String ,Long>();
		for(Map.Entry<String ,String> entry : mapMovieId2ImdbId.entrySet()) {
			String imdbId = "tt" + entry.getValue();
			if(mapImdbId2ReleaseDate.containsKey(imdbId)) {
				mapMovieId2ReleaseLong.put(entry.getKey(), mapImdbId2ReleaseDate.get(imdbId).getTime() / 1000); // 可拿來限制MovieId
			}
		}
		getUserPairHistory(DataSourceUtil.dataMovieLensSmall + filenameRatings, userIdList, mapMovieId2ReleaseLong);

		/* Full */
//		exportTopRatingUserId(DataSourceUtil.dataMovieLensFull + filenameRatings);
//		exportTopRatingMovieId(DataSourceUtil.dataMovieLensFull + filenameRatings);
//		exportRatingsByDate(DataSourceUtil.dataMovieLensFull + filenameRatings);
//		
//		ArrayList<Integer> userIdList = loadUserIdList(DataSourceUtil.dataMovieLensFull + filenameRatings);
//		logger.info(userIdList.size());															// 270896
//		logger.info(userIdList.get(userIdList.size() - 1));										// 270896
//		ArrayList<Integer> movieIdList = loadMovieIdList(DataSourceUtil.dataMovieLensFull + filenameRatings);
//		logger.info(movieIdList.size());														// 45115
//		logger.info(movieIdList.get(movieIdList.size() - 1));									// 176275

		// Non-matrix: 先使用暴力法玩玩		
//		calUserIdDistance(DataSourceUtil.dataMovieLensFull + filenameRatings, 1, 100, null, null, deepLimit);
		
	}

}
