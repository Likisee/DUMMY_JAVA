package com.vo;

import java.io.Serializable;
import java.util.Date;

public class MovieLensRatingVo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String userId;
	private String movieId;
	private String rating;
	private String timestamp;
	private Date date;

	public MovieLensRatingVo() {
		super();
	}
	
	public MovieLensRatingVo(String userId, String movieId, String rating, String timestamp) {
		super();
		this.userId = userId;
		this.movieId = movieId;
		this.rating = rating;
		this.timestamp = timestamp;
		this.date = new Date(Long.valueOf(timestamp) * 1000);
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getMovieId() {
		return movieId;
	}

	public void setMovieId(String movieId) {
		this.movieId = movieId;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
		this.date = new Date(Long.valueOf(timestamp) * 1000);
	}

	@Override
	public String toString() {
		return "MovieLensRating [userId=" + userId + ", movieId=" + movieId + ", rating=" + rating + ", timestamp=" + timestamp  + ", date=" + date + "]";
	}

}
