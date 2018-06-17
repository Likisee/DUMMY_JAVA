package com.util;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.vo.MovieLensRatingVo;

public class Test {

	private static Log logger = LogFactory.getLog(Test.class);
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		ArrayList<MovieLensRatingVo> listMovies = MovieLensUtils.parseMovieLensRating("E:\\DataSet\\#Running\\MovieLens Latest Datasets\\ml-latest-small\\movies.csv");
		logger.info(listMovies.get(0));
		
		ArrayList<MovieLensRatingVo> listLinks = MovieLensUtils.parseMovieLensRating("E:\\DataSet\\#Running\\MovieLens Latest Datasets\\ml-latest-small\\links.csv");
		logger.info(listLinks.get(0));
		
		ArrayList<MovieLensRatingVo> listRatings = MovieLensUtils.parseMovieLensRating("E:\\DataSet\\#Running\\MovieLens Latest Datasets\\ml-latest-small\\ratings.csv");
		logger.info(listRatings.get(0));
		
		ArrayList<MovieLensRatingVo> listTags = MovieLensUtils.parseMovieLensRating("E:\\DataSet\\#Running\\MovieLens Latest Datasets\\ml-latest-small\\tags.csv");
		logger.info(listTags.get(0));
	}

}
