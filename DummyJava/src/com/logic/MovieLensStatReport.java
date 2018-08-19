package com.logic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;

import net.sf.json.JSONArray;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import au.com.bytecode.opencsv.CSVReader;

import com.constant.Const;
import com.util.CollectionUtil;
import com.util.FileUtil;
import com.util.HBaseUtil;
import com.util.StringUtil;
import com.util.TimeUtil;

public class MovieLensStatReport {

	private static Log logger = LogFactory.getLog(MovieLensStatReport.class);

	private static String dataRoot = "E:\\DataSet\\#Running\\";
	private static String dataMovieLendsSmall = dataRoot + "MovieLens Latest Datasets\\ml-latest-small\\";
	private static String dataMovieLendsFull = dataRoot + "MovieLens Latest Datasets\\ml-latest\\";

	private static String dataExport = "Export\\";

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
			logger.error("MovieLensStatReport.exportTopRatingUserId Error: " + e.getMessage());
		} catch (IOException e) {
			logger.error("MovieLensStatReport.exportTopRatingUserId Error: " + e.getMessage());
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

		SimpleDateFormat sdf = TimeUtil.getSimpleDateTime(null);
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
		File fileExport = new File(file.getParent() + File.separator + dataExport + "TopRatingUserId.csv");
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
			logger.error("MovieLensStatReport.exportTopRatingMovieId Error: " + e.getMessage());
		} catch (IOException e) {
			logger.error("MovieLensStatReport.exportTopRatingMovieId Error: " + e.getMessage());
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

		SimpleDateFormat sdf = TimeUtil.getSimpleDateTime(null);
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
		File fileExport = new File(file.getParent() + File.separator + dataExport + "TopRatingMovieId.csv");
		FileUtil.writeStringToFile(fileExport, sbResult.toString());
	}

	private static void exportRatingsByDate(String filePath) {
		HashMap<String, StringBuffer> mapDateRatings = new HashMap<String, StringBuffer>();
		try {
			CSVReader reader = new CSVReader(new FileReader(filePath), ',', '"', 1);
			String[] nextLine;
			Long timestamp;
			SimpleDateFormat sdf = TimeUtil.getSimpleDateTime(null);
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
			logger.error("MovieLensStatReport.exportRatingsByDate Error: " + e.getMessage());
		} catch (IOException e) {
			logger.error("MovieLensStatReport.exportRatingsByDate Error: " + e.getMessage());
		}

		File file = new File(filePath);
		String filename, content;
		for (Map.Entry<String, StringBuffer> entry : mapDateRatings.entrySet()) {
			filename = entry.getKey();
			content = entry.getValue().toString();
			File fileExport = new File(file.getParent() + File.separator + dataExport + "RatingsByDate" + File.separator + filename + ".csv");
			FileUtil.writeStringToFile(fileExport, "userId,movieId,rating,timestamp," + Const.lineBrakeSep + content);
		}
	}
	
	private static ArrayList<Integer> loadUserIdList(String filePath) {
		ArrayList<Integer> userIdList = new ArrayList<Integer>();
		File file = new File(filePath);
		File fileExport = new File(file.getParent() + File.separator + dataExport + userIdListResult);
		if (!fileExport.exists()) {
			try {
				CSVReader reader = new CSVReader(new FileReader(filePath), ',', '"', 1);
				String[] nextLine;
				Integer userId;
				SimpleDateFormat sdf = TimeUtil.getSimpleDateTime(null);
				String date;
				while ((nextLine = reader.readNext()) != null) {
					if (nextLine != null) {

						userId = Integer.valueOf(nextLine[0]);
						userIdList.add(userId);
					}
				}
				CollectionUtil.getIntegerArrayListSortWithoutDuplication(userIdList);
				FileUtil.writeObjectToFile(fileExport, userIdList);
			} catch (FileNotFoundException e) {
				logger.error("MovieLensStatReport.loadUserIdList Error: " + e.getMessage());
			} catch (IOException e) {
				logger.error("MovieLensStatReport.loadUserIdList Error: " + e.getMessage());
			}
		}
		userIdList = (ArrayList<Integer>) FileUtil.readFileAsObject(fileExport);
		return userIdList;
	}

	private static ArrayList<Integer> loadMovieIdList(String filePath) {
		ArrayList<Integer> movieIdList = new ArrayList<Integer>();
		File file = new File(filePath);
		File fileExport = new File(file.getParent() + File.separator + dataExport + movieIdListResult);
		if (!fileExport.exists()) {
			try {
				CSVReader reader = new CSVReader(new FileReader(filePath), ',', '"', 1);
				String[] nextLine;
				Integer movieId;
				SimpleDateFormat sdf = TimeUtil.getSimpleDateTime(null);
				String date;
				while ((nextLine = reader.readNext()) != null) {
					if (nextLine != null) {

						movieId = Integer.valueOf(nextLine[1]);
						movieIdList.add(movieId);
					}
				}
				CollectionUtil.getIntegerArrayListSortWithoutDuplication(movieIdList);
				FileUtil.writeObjectToFile(fileExport, movieIdList);
			} catch (FileNotFoundException e) {
				logger.error("MovieLensStatReport.loadMovieIdList Error: " + e.getMessage());
			} catch (IOException e) {
				logger.error("MovieLensStatReport.loadMovieIdList Error: " + e.getMessage());
			}
		}
		movieIdList = (ArrayList<Integer>) FileUtil.readFileAsObject(fileExport);
		return movieIdList;
	}
	
	// ================================================================================================================================================
	// ================================================================================================================================================
	
	// ========================================================================
	// Data Prepare: mapUserId2MovieId, mapMovieId2UserId
	// ========================================================================
	
	private static HashMap<String, HashSet<String>> loadRatingsMapUserId2MovieId(String filePath, ArrayList<Integer> userIdList, Integer daysPre, Integer daysAfter) {
		HashMap<String, HashSet<String>> mapUserId2MovieId = new HashMap<String, HashSet<String>>();

		// initiate fileList
		File file = new File(filePath);
		File fileRatingsByDate = new File(file.getParent() + File.separator + dataExport + "RatingsByDate");
		File[] fileRatingsByDateArr = fileRatingsByDate.listFiles();

		// initiate ratingTimestampSet (reduce computational cost)
		SimpleDateFormat sdf = TimeUtil.getSimpleDateTime(null);
		long timestamp, timestampMin = Long.MAX_VALUE, timestampMax = Long.MIN_VALUE;
		HashSet<Long> ratingTimestampSet = new HashSet<Long>();
		for (int i = 0; i < fileRatingsByDateArr.length; i++) {
			try {
				timestamp = sdf.parse(fileRatingsByDateArr[i].getName().substring(0, 10)).getTime() / 1000;
				if (timestampMin > timestamp) {
					timestampMin = timestamp;
				}
				if (timestampMax < timestamp) {
					timestampMax = timestamp;
				}
				ratingTimestampSet.add(sdf.parse(fileRatingsByDateArr[i].getName().substring(0, 10)).getTime() / 1000);
			} catch (ParseException e) {
				logger.error("MovieLensStatReport.loadRatingsMapUserId2MovieId Error: " + e.getMessage());
			}
		}

		// initiate userIdSet
		HashSet<Integer> userIdSet = new HashSet<Integer>();
		if (userIdList == null) {
			userIdSet.addAll(loadUserIdList(filePath));
		} else {
			userIdSet.addAll(userIdList);
		}

		// initiate mapUserId2MovieId
		for (Integer userId : userIdSet) {
			mapUserId2MovieId.put(userId + "", new HashSet<String>());
		}

		// parse file
		Integer userId, movieId;
		HashSet<String> movieIdSet = null;
		long timestampStart, timestampEnd;
		if (file.exists()) {
			try {
				CSVReader reader = new CSVReader(new FileReader(file), ',', '"', 1);
				String[] nextLine;
				while ((nextLine = reader.readNext()) != null) {
					if (nextLine != null) {
						try {
							userId = Integer.valueOf(nextLine[0]);
							movieId = Integer.valueOf(nextLine[1]);
							timestamp = Long.valueOf(nextLine[3]);
							timestamp = sdf.parse(sdf.format(timestamp * 1000)).getTime() / 1000;

							movieIdSet = new HashSet<String>();
							if (daysPre == null && daysAfter == null) {
								movieIdSet.add(movieId + "");
							} else {
								if (daysPre != null) {
									timestampStart = timestamp - daysPre;
								} else {
									timestampStart = timestampMin;
								}
								if (daysAfter != null) {
									timestampEnd = timestamp + daysAfter;
								} else {
									timestampEnd = timestampMax;
								}
								for (long i = timestampStart; i <= timestampEnd; i++) {
									if (ratingTimestampSet.contains(i)) {
										movieIdSet.add(HBaseUtil.getStringConcate(movieId + "", i + ""));
									}
								}
							}
							if (mapUserId2MovieId.containsKey(userId + "")) {
								mapUserId2MovieId.get(userId + "").addAll(movieIdSet);
							}
						} catch (ParseException e) {
							logger.error("MovieLensStatReport.loadRatingsMapUserId2MovieId Error: " + e.getMessage());
						}
					}
				}
			} catch (FileNotFoundException e) {
				logger.error("MovieLensStatReport.loadRatingsMapUserId2MovieId Error: " + e.getMessage());
			} catch (IOException e) {
				logger.error("MovieLensStatReport.loadRatingsMapUserId2MovieId Error: " + e.getMessage());
			}
		}
		return mapUserId2MovieId;
	}
	
	private static HashMap<String, HashSet<String>> loadRatingsMapMovieId2UserId(String filePath, ArrayList<Integer> movieIdList, Integer daysPre, Integer daysAfter) {
		HashMap<String, HashSet<String>> mapMovieId2UserId = new HashMap<String, HashSet<String>>();
		return mapMovieId2UserId;
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private static ArrayList<ArrayList<Integer>> getUserIdBasedMatrix(String filePathRatings, ArrayList<Integer> userIdList, Integer daysPre, Integer daysAfter) {
		ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
		StringBuffer rsb = new StringBuffer("");

		HashMap<String, HashSet<String>> mapUserId2MovieId = loadRatingsMapUserId2MovieId(filePathRatings, userIdList, daysPre, daysAfter);
		HashMap<String, HashSet<String>> mapMovieId2UserId = getRatingsMapReverse(mapUserId2MovieId);

		// TODO ENHANCEMENT
		String userIdRow, userIdCol;
		int count, countMax = Integer.MIN_VALUE;
		HashSet<String> movieIdSet;
		for (int i = 0; i < userIdList.size(); i++) {
			userIdRow = userIdList.get(i) + "";
			ArrayList<Integer> userIdRowList = new ArrayList<Integer>();
			for (int j = 0; j < userIdList.size(); j++) {
				userIdCol = userIdList.get(j) + "";

				// logic skill
				count = 0;
				if (i != j) {
					if (mapUserId2MovieId.containsKey(userIdRow)) {
						movieIdSet = mapUserId2MovieId.get(userIdRow);
						for (String movieId : movieIdSet) {
							if (mapMovieId2UserId.containsKey(movieId) && mapMovieId2UserId.get(movieId).contains(userIdCol)) {
								count++;
							}
						}
					}
					if (countMax < count) {
						countMax = count;
					}
				}
				userIdRowList.add(count);
			}
			result.add(userIdRowList);
			rsb.append(JSONArray.fromObject(userIdRowList) + Const.lineBrakeSep);
		}
		
//		for (Map.Entry<String, HashSet<String>> entry : mapMovieId2UserId.entrySet()) {
//			if (entry.getValue().size() > 2) {
//				logger.info("key: " + entry.getKey());
//				logger.info("value: " + JSONArray.fromObject(entry.getValue()));
//			}
//		}

		logger.info("countMax: " + countMax);
		String filename = "userIdBasedMatrix_" + daysPre + "_" + daysAfter + "_" + countMax + ".txt";
		File file = new File(filePathRatings);
		File fileMatrix = new File(file.getParent() + File.separator + dataExport + "Matrix" + File.separator + filename);
		FileUtil.writeStringToFile(fileMatrix, rsb.toString());
		return result;
	}
	
	@Deprecated
	private static HashSet<Integer> searchIds(HashMap<Integer, HashSet<Integer>> collections, HashSet<Integer> keys) {
		HashSet<Integer> result = new HashSet<Integer>();
		for (Integer eachKey : keys) {
			result.addAll(collections.get(eachKey));
		}
		return result;
	}

	@Deprecated
	public static void calUserIdDistance(String filePathRatings,
			Integer userIdStart, Integer userIdEnd, Integer daysPre, Integer daysAfter, int deepLimit) {

		ArrayList<Integer> userIdList = loadUserIdList(filePathRatings);
		ArrayList<Integer> movieIdList = loadMovieIdList(filePathRatings);

		HashMap<String, HashSet<String>> mapUserId2MovieIdTemp = loadRatingsMapUserId2MovieId(filePathRatings, userIdList, null, null);
		HashMap<String, HashSet<String>> mapMovieId2UserIdTemp = loadRatingsMapMovieId2UserId(filePathRatings, movieIdList, null, null);
		HashMap<Integer, HashSet<Integer>> mapUserId2MovieId = new HashMap<Integer, HashSet<Integer>>();
		HashMap<Integer, HashSet<Integer>> mapMovieId2UserId = new HashMap<Integer, HashSet<Integer>>();
		for (Map.Entry<String, HashSet<String>> entry : mapUserId2MovieIdTemp.entrySet()) {
			Integer userId = Integer.valueOf(entry.getKey());
			HashSet<String> movieIdSet = entry.getValue();
			HashSet<Integer> movieIdSetNew = new HashSet<Integer>();
			for (String movieId : movieIdSet) {
				movieIdSetNew.add(Integer.valueOf(HBaseUtil.getStringSplit(movieId).get(0)));
			}
			mapUserId2MovieId.put(userId, movieIdSetNew);
		}
		for (Map.Entry<String, HashSet<String>> entry : mapMovieId2UserIdTemp.entrySet()) {
			Integer movieId = Integer.valueOf(entry.getKey());
			HashSet<String> userIdSet = entry.getValue();
			HashSet<Integer> userIdSetNew = new HashSet<Integer>();
			for (String userId : userIdSet) {
				userIdSetNew.add(Integer.valueOf(HBaseUtil.getStringSplit(userId).get(0)));
			}
			mapUserId2MovieId.put(movieId, userIdSetNew);
		}

		Integer userId, movieId;
		HashSet<Integer> newUserIdSet, newMovieIdSet, currentUserIdSet, currentMovieIdSet;

		StringBuffer sbSpecialRepot = new StringBuffer();

		// userIdList driven
		for (int i = 0; i < userIdList.size(); i++) {
			userId = userIdList.get(i);
			if (userId < userIdStart || userId > userIdEnd) {
				continue;
			}
			
			currentUserIdSet = new HashSet<Integer>();
			currentUserIdSet.add(userId);
			ArrayList<HashSet<Integer>> lstUserIdSet = new ArrayList<HashSet<Integer>>();
			ArrayList<HashSet<Integer>> lstMovieIdSet = new ArrayList<HashSet<Integer>>();

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
				currentUserIdSet = (HashSet<Integer>) lstUserIdSet.get(j).clone();
				newUserIdSet = (HashSet<Integer>) lstUserIdSet.get(j + 1).clone();
				newUserIdSet.removeAll(currentUserIdSet);
				for (Integer eachUserId : newUserIdSet) {
					mapDistResult.put(eachUserId, j + 1);
				}
				if (j == lstUserIdSet.size() - 2) {
					sbSpecialRepot.append("userId:" + userId + "'s max depth is " + (j + 1) + Const.lineBrakeSep);
				}
			}

			// do dump
			StringBuffer sb = new StringBuffer();
			userId = userIdList.get(i);
			for (Map.Entry<Integer, Integer> entry : mapDistResult.entrySet()) {
				sb.append(userId + Const.csvSep + entry.getKey() + Const.csvSep + entry.getValue() + Const.csvSep + Const.lineBrakeSep);
			}
			File file = new File(filePathRatings);
			File fileExport = new File(file.getParent() + File.separator + dataExport + "distanceUserId2MovieId" + File.separator + userId + ".csv");
			FileUtil.writeStringToFile(fileExport, "userId,userId,distance," + Const.lineBrakeSep + sb.toString());
			
		}
		
		// SpecialRepot
		File file = new File(dataMovieLendsSmall + filenameRatings);
		File fileExport = new File(file.getParent() + File.separator + dataExport + "distanceUserId2MovieId" + File.separator + "SpecilaReport.txt");
		FileUtil.writeStringToFile(fileExport, sbSpecialRepot.toString());
	}
	
	@Deprecated
	public static void calMovieIdDistanceSmall(String filePathRatings,
			Integer movieIdStart, Integer movieIdEnd, Integer daysPre, Integer daysAfter, int deepLimit) {// last modified on 2018/07/22
		
	}
	
	public static void main(String[] args) {

//		ArrayList<MovieLensMovieVo> listMovies = MovieLensUtils.parseMovieLensMovie("E:\\DataSet\\#Running\\MovieLens Latest Datasets\\ml-latest-small\\movies.csv");
//		logger.info(listMovies.get(0));
//		listMovies = MovieLensUtils.parseMovieLensMovie("E:\\DataSet\\#Running\\MovieLens Latest Datasets\\ml-latest\\movies.csv");
//		logger.info(listMovies.get(0));
//		ArrayList<MovieLensLinkVo> listLinks = MovieLensUtils.parseMovieLensLink("E:\\DataSet\\#Running\\MovieLens Latest Datasets\\ml-latest-small\\links.csv");
//		logger.info(listLinks.get(0));
//		listLinks = MovieLensUtils.parseMovieLensLink("E:\\DataSet\\#Running\\MovieLens Latest Datasets\\ml-latest\\links.csv");
//		logger.info(listLinks.get(0));
//		List<MovieLensRatingVo> listRatings = MovieLensUtils.parseMovieLensRating("E:\\DataSet\\#Running\\MovieLens Latest Datasets\\ml-latest-small\\ratings.csv");
//		logger.info(listRatings.get(0));
//		listRatings = MovieLensUtils.parseMovieLensRating("E:\\DataSet\\#Running\\MovieLens Latest Datasets\\ml-latest\\ratings.csv");
//		logger.info(listRatings.get(0));
//		ArrayList<MovieLensTagVo> listTags = MovieLensUtils.parseMovieLensTag("E:\\DataSet\\#Running\\MovieLens Latest Datasets\\ml-latest-small\\tags.csv");
//		logger.info(listTags.get(0));
//		listTags = MovieLensUtils.parseMovieLensTag("E:\\DataSet\\#Running\\MovieLens Latest Datasets\\ml-latest\\tags.csv");
//		logger.info(listTags.get(0));

		int deepLimit = 10;
		
		// Small
//		exportTopRatingUserId(dataMovieLendsSmall + filenameRatings);
//		exportTopRatingMovieId(dataMovieLendsSmall + filenameRatings);
//		exportRatingsByDate(dataMovieLendsSmall + filenameRatings);
//		
//		ArrayList<Integer> userIdList = loadUserIdList(dataMovieLendsSmall + filenameRatings);
//		logger.info(userIdList.size());															// 671
//		logger.info(userIdList.get(userIdList.size() - 1));										// 671
//		ArrayList<Integer> movieIdList = loadMovieIdList(dataMovieLendsSmall + filenameRatings);
//		logger.info(movieIdList.size());														// 9066
//		logger.info(movieIdList.get(movieIdList.size() - 1));									// 163949

		// Non-matrix: 先使用暴力法玩玩
//		calUserIdDistance(dataMovieLendsSmall + filenameRatings, 1, 671, null, null, deepLimit);
		
		// Matrix: 先使用暴力法玩玩
		ArrayList<Integer> userIdList = loadUserIdList(dataMovieLendsSmall + filenameRatings);
		ArrayList<Integer> movieIdList = loadMovieIdList(dataMovieLendsSmall + filenameRatings);
		ArrayList<ArrayList<Integer>> result = getUserIdBasedMatrix(dataMovieLendsSmall + filenameRatings, userIdList, null, null);
		result = getUserIdBasedMatrix(dataMovieLendsSmall + filenameRatings, userIdList, 50, 50);
		
		
		

//		ArrayList<ArrayList<Integer>> big = new ArrayList<ArrayList<Integer>>();
//		ArrayList<Integer> data = new ArrayList<Integer>();
//		data.add(1);
//		data.add(2);
//		big.add(data);
//		big.add(data);
//		Integer[][] result = CollectionUtil.getArrayList2Array(big);
//		logger.info(result);
		
		
		// Full
//		exportTopRatingUserId(dataMovieLendsFull + filenameRatings);
//		exportTopRatingMovieId(dataMovieLendsFull + filenameRatings);
//		exportRatingsByDate(dataMovieLendsFull + filenameRatings);
//		
//		ArrayList<Integer> userIdList = loadUserIdList(dataMovieLendsFull + filenameRatings);
//		logger.info(userIdList.size());															// 270896
//		logger.info(userIdList.get(userIdList.size() - 1));										// 270896
//		ArrayList<Integer> movieIdList = loadMovieIdList(dataMovieLendsFull + filenameRatings);
//		logger.info(movieIdList.size());														// 45115
//		logger.info(movieIdList.get(movieIdList.size() - 1));									// 176275

		// Non-matrix: 先使用暴力法玩玩		
//		calUserIdDistance(dataMovieLendsFull + filenameRatings, 1, 100, null, null, deepLimit);
		
	}

}
