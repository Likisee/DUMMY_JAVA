package com.logic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import au.com.bytecode.opencsv.CSVReader;

import com.util.FileUtil;
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
				sbResult.append(userId + "," + count + "," + mapUserMinTimestamp.get(userId) + "," + sdf.format(mapUserMinTimestamp.get(userId) * 1000) + "," + mapUserMaxTimestamp.get(userId) + "," + sdf.format(mapUserMaxTimestamp.get(userId) * 1000) + "," + "\r\n");
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
				sbResult.append(movieId + "," + count + "," + mapMovieMinTimestamp.get(movieId) + "," + sdf.format(mapMovieMinTimestamp.get(movieId) * 1000) + "," + mapMovieMaxTimestamp.get(movieId) + "," + sdf.format(mapMovieMaxTimestamp.get(movieId) * 1000) + "," + "\r\n");
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
						mapDateRatings.get(date).append(StringUtil.getStringArray2Csv(nextLine) + StringUtil.lineBrakeSep);
					} else {
						StringBuffer sb = new StringBuffer();
						sb.append(StringUtil.getStringArray2Csv(nextLine) + StringUtil.lineBrakeSep);
						mapDateRatings.put(date, sb);
					}
				}
			}
		} catch (FileNotFoundException e) {
			logger.error("MovieLensStatReport.exportTopRatingMovieId Error: " + e.getMessage());
		} catch (IOException e) {
			logger.error("MovieLensStatReport.exportTopRatingMovieId Error: " + e.getMessage());
		}

		File file = new File(filePath);
		String filename, content;
		for (Map.Entry<String, StringBuffer> entry : mapDateRatings.entrySet()) {
			filename = entry.getKey();
			content = entry.getValue().toString();
			File fileExport = new File(file.getParent() + File.separator + dataExport + "RatingsByDate" + File.separator + filename + ".csv");
			FileUtil.writeStringToFile(fileExport, content);
		}

	}

	public static void main(String[] args) {

//		 ArrayList<MovieLensMovieVo> listMovies = MovieLensUtils.parseMovieLensMovie("E:\\DataSet\\#Running\\MovieLens Latest Datasets\\ml-latest-small\\movies.csv");
//		 logger.info(listMovies.get(0));
//		 listMovies = MovieLensUtils.parseMovieLensMovie("E:\\DataSet\\#Running\\MovieLens Latest Datasets\\ml-latest\\movies.csv");
//		 logger.info(listMovies.get(0));

//		 ArrayList<MovieLensLinkVo> listLinks = MovieLensUtils.parseMovieLensLink("E:\\DataSet\\#Running\\MovieLens Latest Datasets\\ml-latest-small\\links.csv");
//		 logger.info(listLinks.get(0));
//		 listLinks = MovieLensUtils.parseMovieLensLink("E:\\DataSet\\#Running\\MovieLens Latest Datasets\\ml-latest\\links.csv");
//		 logger.info(listLinks.get(0));

//		 List<MovieLensRatingVo> listRatings = MovieLensUtils.parseMovieLensRating("E:\\DataSet\\#Running\\MovieLens Latest Datasets\\ml-latest-small\\ratings.csv");
//		 logger.info(listRatings.get(0));
//		 listRatings = MovieLensUtils.parseMovieLensRating("E:\\DataSet\\#Running\\MovieLens Latest Datasets\\ml-latest\\ratings.csv");
//		 logger.info(listRatings.get(0));

//		 ArrayList<MovieLensTagVo> listTags = MovieLensUtils.parseMovieLensTag("E:\\DataSet\\#Running\\MovieLens Latest Datasets\\ml-latest-small\\tags.csv");
//		 logger.info(listTags.get(0));
//		 listTags = MovieLensUtils.parseMovieLensTag("E:\\DataSet\\#Running\\MovieLens Latest Datasets\\ml-latest\\tags.csv");
//		 logger.info(listTags.get(0));

		exportTopRatingUserId(dataMovieLendsFull + filenameRatings);	// last 2018/07/11
		exportTopRatingMovieId(dataMovieLendsFull + filenameRatings);	// last 2018/07/11
//		exportRatingsByDate(dataMovieLendsFull + filenameRatings);		// last 2018/07/11
	}

}
