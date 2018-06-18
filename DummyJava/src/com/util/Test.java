package com.util;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.vo.MovieLensRatingVo;

public class Test {

	private static Log logger = LogFactory.getLog(Test.class);
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
//		ArrayList<MovieLensMovieVo> listMovies = MovieLensUtils.parseMovieLensMovie("E:\\DataSet\\#Running\\MovieLens Latest Datasets\\ml-latest-small\\movies.csv");
//		logger.info(listMovies.get(0));
//		listMovies = MovieLensUtils.parseMovieLensMovie("E:\\DataSet\\#Running\\MovieLens Latest Datasets\\ml-latest\\movies.csv");
//		logger.info(listMovies.get(0));
		
//		ArrayList<MovieLensLinkVo> listLinks = MovieLensUtils.parseMovieLensLink("E:\\DataSet\\#Running\\MovieLens Latest Datasets\\ml-latest-small\\links.csv");
//		logger.info(listLinks.get(0));
//		listLinks = MovieLensUtils.parseMovieLensLink("E:\\DataSet\\#Running\\MovieLens Latest Datasets\\ml-latest\\links.csv");
//		logger.info(listLinks.get(0));
		
		List<MovieLensRatingVo> listRatings = MovieLensUtils.parseMovieLensRating("E:\\DataSet\\#Running\\MovieLens Latest Datasets\\ml-latest-small\\ratings.csv");
		logger.info(listRatings.get(0));
		listRatings = MovieLensUtils.parseMovieLensRating("E:\\DataSet\\#Running\\MovieLens Latest Datasets\\ml-latest\\ratings.csv");
		logger.info(listRatings.get(0));
		
//		ArrayList<MovieLensTagVo> listTags = MovieLensUtils.parseMovieLensTag("E:\\DataSet\\#Running\\MovieLens Latest Datasets\\ml-latest-small\\tags.csv");
//		logger.info(listTags.get(0));
//		listTags = MovieLensUtils.parseMovieLensTag("E:\\DataSet\\#Running\\MovieLens Latest Datasets\\ml-latest\\tags.csv");
//		logger.info(listTags.get(0));
	}

}
