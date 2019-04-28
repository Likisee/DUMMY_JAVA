package com.util.DataSet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import au.com.bytecode.opencsv.bean.CsvToBean;

import com.util.DataSourceUtil;
import com.util.DateTimeUtil;
import com.vo.KaggleTheMoviesDataSetMoviesMetadataVo;
import com.vo.MovieLensLinkVo;
import com.vo.MovieLensMovieVo;
import com.vo.MovieLensRatingVo;
import com.vo.MovieLensTagVo;

public class MovieLensUtil {

	private static Log logger = LogFactory.getLog(MovieLensUtil.class);

	private static String filenameLinks = "links.csv";
	private static String filenameMovies = "movies.csv";
	private static String filenameRatings = "ratings.csv";
	private static String filenameTags = "tags.csv";
	
	// ================================================================================================================================================
	// ================================================================================================================================================

	// ========================================================================
	// Movie
	// ========================================================================
	
	public static ArrayList<MovieLensMovieVo> parseMovieLensMovie(String filePath) {
		return parseMovieLensMovie(new File(filePath));
	}
	
	public static ArrayList<MovieLensMovieVo> parseMovieLensMovie(File file) {
		ArrayList<MovieLensMovieVo> result = new ArrayList<MovieLensMovieVo>();
		try {
			CSVReader reader = new CSVReader(new FileReader(file), ',', '"', 1);

			// Set column mapping strategy
			CsvToBean csv = new CsvToBean();
			ColumnPositionMappingStrategy strategy = new ColumnPositionMappingStrategy();
			strategy.setType(MovieLensMovieVo.class);
			String[] columns = new String[] { "movieId", "title", "genres" };
			strategy.setColumnMapping(columns);

			List list = csv.parse(strategy, reader);
			for (Object object : list) {
				MovieLensMovieVo movie = (MovieLensMovieVo) object;
				result.add(movie);
			}

		} catch (FileNotFoundException e) {
			logger.error("MovieLensUtils.parseMovieLensMovie Error: " + e.getMessage(), e);
		}
		return result;
	}
	
	// ========================================================================
	// Link
	// ========================================================================

	public static ArrayList<MovieLensLinkVo> parseMovieLensLink(String filePath) {
		return parseMovieLensLink(new File(filePath));
	}

	public static ArrayList<MovieLensLinkVo> parseMovieLensLink(File file) {
		ArrayList<MovieLensLinkVo> result = new ArrayList<MovieLensLinkVo>();
		try {
			CSVReader reader = new CSVReader(new FileReader(file), ',', '"', 1);

			// Set column mapping strategy
			CsvToBean csv = new CsvToBean();
			ColumnPositionMappingStrategy strategy = new ColumnPositionMappingStrategy();
			strategy.setType(MovieLensLinkVo.class);
			String[] columns = new String[] { "movieId", "imdbId", "tmdbId" };
			strategy.setColumnMapping(columns);

			List list = csv.parse(strategy, reader);
			for (Object object : list) {
				MovieLensLinkVo link = (MovieLensLinkVo) object;
				result.add(link);
			}

		} catch (FileNotFoundException e) {
			logger.error("MovieLensUtils.parseMovieLensLink Error: " + e.getMessage(), e);
		}
		return result;
	}
	
	public static HashMap<String, String> getMapMovieId2ImdbId() {
		HashMap<String, String> result = new HashMap<String, String>();
		String movieId = null, imdbId = null; 
		ArrayList<MovieLensLinkVo> dataArr = parseMovieLensLink(DataSourceUtil.dataMovieLensFull + filenameLinks);
		for(MovieLensLinkVo oneData : dataArr) {
			movieId = oneData.getMovieId();
			imdbId = oneData.getImdbId(); 
			if(movieId != null && imdbId != null) {
				result.put(movieId, imdbId);
			}
		}
		return result;
	}
	
	// ========================================================================
	// Rating (maybe a large-size file)
	// ========================================================================

	public static ArrayList<MovieLensRatingVo> parseMovieLensRating(String filePath) {
		return parseMovieLensRating(new File(filePath));
	}

	public static ArrayList<MovieLensRatingVo> parseMovieLensRating(File file) {
		ArrayList<MovieLensRatingVo> result = new ArrayList<MovieLensRatingVo>();
		try {
			CSVReader reader = new CSVReader(new FileReader(file), ',', '"', 1);

			// Set column mapping strategy
			CsvToBean csv = new CsvToBean();
			ColumnPositionMappingStrategy strategy = new ColumnPositionMappingStrategy();
			strategy.setType(MovieLensRatingVo.class);
			String[] columns = new String[] { "userId", "movieId", "rating", "timestamp" };
			strategy.setColumnMapping(columns);

			List list = csv.parse(strategy, reader);
			for (Object object : list) {
				MovieLensRatingVo rating = (MovieLensRatingVo) object;
				result.add(rating);
			}

		} catch (FileNotFoundException e) {
			logger.error("MovieLensUtils.parseMovieLensRating Error: " + e.getMessage(), e);
		}
		return result;
	}
	
	public static ArrayList<MovieLensRatingVo> parseMovieLensRating(String directoryPath, String date, int daysPres, int daysAfter) {
		ArrayList<MovieLensRatingVo> result = new ArrayList<MovieLensRatingVo>();
		ArrayList<MovieLensRatingVo> resultTemp = null;
		int now = (int) DateTimeUtil.parseByDefaultSimpleDateTime(date).getTime() / 1000 / 60 / 60 / 24;
		int nowPres = now - daysPres;
		int nowAfter = now + daysAfter;
		SimpleDateFormat sdf = DateTimeUtil.getSimpleDateTime(null);
		File file = null;
		for (int i = nowPres; i <= nowAfter; i++) {
			file = new File(directoryPath + sdf.format(DateTimeUtil.getIntDateToDate(i)));
			if(file.exists()) {
				resultTemp = parseMovieLensRating(file);
				result.addAll(resultTemp);
			}
		}
		return result;
	}

	// ========================================================================
	// Tag
	// ========================================================================
	
	public static ArrayList<MovieLensTagVo> parseMovieLensTag(String filePath) {
		return parseMovieLensTag(new File(filePath));
	}
	
	public static ArrayList<MovieLensTagVo> parseMovieLensTag(File file) {
		ArrayList<MovieLensTagVo> result = new ArrayList<MovieLensTagVo>();
		try {
			CSVReader reader = new CSVReader(new FileReader(file), ',', '"', 1);

			// Set column mapping strategy
			CsvToBean csv = new CsvToBean();
			ColumnPositionMappingStrategy strategy = new ColumnPositionMappingStrategy();
			strategy.setType(MovieLensTagVo.class);
			String[] columns = new String[] { "userId", "movieId", "tag", "timestamp" };
			strategy.setColumnMapping(columns);

			List list = csv.parse(strategy, reader);
			for (Object object : list) {
				MovieLensTagVo tag = (MovieLensTagVo) object;
				result.add(tag);
			}

		} catch (FileNotFoundException e) {
			logger.error("MovieLensUtils.parseMovieLensTag Error: " + e.getMessage(), e);
		}
		return result;
	}
	
	// ================================================================================================================================================
	// ================================================================================================================================================
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try {

			// Parse / Read / write CSV files : OpenCSV tutorial
			// https://howtodoinjava.com/apache-commons/parse-read-write-csv-files-opencsv-tutorial/

			// Build reader instance
			// Read data.csv
			// Default seperator is comma
			// Default quote character is double quote
			// Start reading from line number 2 (line numbers start from zero)
			CSVReader reader = new CSVReader(new FileReader(DataSourceUtil.dataMovieLensFull + filenameRatings), ',', '"', 1);

			// Method 1 (faster for large-size file)
			// Read CSV line by line and use the string array as you want
//			String[] nextLine;
//			while ((nextLine = reader.readNext()) != null) {
//				if (nextLine != null) {
//					System.out.println(Arrays.toString(nextLine));
//				}
//			}

			// Method 2
			// Read all rows at once
//			List<String[]> allRows = reader.readAll();
//			for (String[] row : allRows) {
//				System.out.println(Arrays.toString(row));
//			}

			// Method 3
//			CsvToBean csv = new CsvToBean();
//
//			// Set column mapping strategy
//			ColumnPositionMappingStrategy strategy = new ColumnPositionMappingStrategy();
//			strategy.setType(MovieLensRatingVo.class);
//			String[] columns = new String[] { "userId", "movieId", "rating", "timestamp"};
//			strategy.setColumnMapping(columns);
//			
//			List list = csv.parse(strategy, reader);
//			for (Object object : list) {
//				MovieLensRatingVo link = (MovieLensRatingVo) object;
//				System.out.println(link);
//			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

}
