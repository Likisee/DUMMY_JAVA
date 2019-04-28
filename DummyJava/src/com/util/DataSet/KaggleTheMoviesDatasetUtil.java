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

public class KaggleTheMoviesDatasetUtil {

	private static Log logger = LogFactory.getLog(KaggleTheMoviesDatasetUtil.class);

	private static String filenameCredits = "credits.csv";
	private static String filenameKeywords = "keywords.csv";
	private static String filenameLinks = "links.csv";
	private static String filenameLinksSmall = "links_small.csv";
	private static String filenameMoviesMetadata = "movies_metadata.csv";
	private static String filenameRatings = "ratings.csv";
	private static String filenameRatingsSmall = "ratings_small.csv";
	
	// ================================================================================================================================================
	// ================================================================================================================================================

	// ========================================================================
	// MoviesMetadata
	// ========================================================================
	
	public static ArrayList<KaggleTheMoviesDataSetMoviesMetadataVo> parseKaggleTheMoviesDatasetMoviesMetadata(String filePath) {
		return parseKaggleTheMoviesDatasetMoviesMetadata(new File(filePath));
	}
	
	public static ArrayList<KaggleTheMoviesDataSetMoviesMetadataVo> parseKaggleTheMoviesDatasetMoviesMetadata(File file) {
		ArrayList<KaggleTheMoviesDataSetMoviesMetadataVo> result = new ArrayList<KaggleTheMoviesDataSetMoviesMetadataVo>();
		try {
			CSVReader reader = new CSVReader(new FileReader(file), ',', '"', 1);

			// Set column mapping strategy
			CsvToBean csv = new CsvToBean();
			ColumnPositionMappingStrategy strategy = new ColumnPositionMappingStrategy();
			strategy.setType(KaggleTheMoviesDataSetMoviesMetadataVo.class);
			String[] columns = new String[] { "adult", "belongs_to_collection", "budget", "genres", "homepage", "id", "imdb_id", "original_language", "original_title", "overview", "popularity", "poster_path", "production_companies", "production_countries", "release_date", "revenue", "runtime", "spoken_languages", "status", "tagline", "title", "video", "vote_average", "vote_count" };
			strategy.setColumnMapping(columns);

			List list = csv.parse(strategy, reader);
			for (Object object : list) {
				KaggleTheMoviesDataSetMoviesMetadataVo data = (KaggleTheMoviesDataSetMoviesMetadataVo) object;
				result.add(data);
			}

		} catch (FileNotFoundException e) {
			logger.error("KaggleTheMoviesDatasetUtil.parseKaggleTheMoviesDatasetMoviesMetadata Error: " + e.getMessage(), e);
		}
		return result;
	}
	
	public static HashMap<String, Date> getMapImdbId2ReleaseDate() {
		HashMap<String, Date> result = new HashMap<String, Date>();
		String imdbId = null; 
		Date releaseDate = null;
		SimpleDateFormat sdf = DateTimeUtil.getSimpleDateTime(null);
		ArrayList<KaggleTheMoviesDataSetMoviesMetadataVo> dataArr = parseKaggleTheMoviesDatasetMoviesMetadata(DataSourceUtil.dataKggleTheMoviesDataset + filenameMoviesMetadata);
		for(KaggleTheMoviesDataSetMoviesMetadataVo oneData : dataArr) {
			imdbId = oneData.getImdb_id(); 
			releaseDate = null;
			try {
				releaseDate = sdf.parse(oneData.getRelease_date());
			} catch (Exception e) {
//				logger.error(e.getMessage(), e);
//				logger.error("Unparseable date: " + oneData.getRelease_date());
			}
			if(imdbId != null && releaseDate != null) {
				result.put(imdbId, releaseDate);
			}
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
			CSVReader reader = new CSVReader(new FileReader(DataSourceUtil.dataKggleTheMoviesDataset + filenameMoviesMetadata), ',', '"', 1);

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
//			strategy.setType(KaggleTheMoviesDataSetMoviesMetadataVo.class);
//			String[] columns = new String[] { "adult", "belongs_to_collection", "budget", "genres", "homepage", "id", "imdb_id", "original_language", "original_title", "overview", "popularity", "poster_path", "production_companies", "production_countries", "release_date", "revenue", "runtime", "spoken_languages", "status", "tagline", "title", "video", "vote_average", "vote_count"};
//			strategy.setColumnMapping(columns);
//			
//			List list = csv.parse(strategy, reader);
//			for (Object object : list) {
//				KaggleTheMoviesDataSetMoviesMetadataVo data = (KaggleTheMoviesDataSetMoviesMetadataVo) object;
//				System.out.println(data);
//			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

}
