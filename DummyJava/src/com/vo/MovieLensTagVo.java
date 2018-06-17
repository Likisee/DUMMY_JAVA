package com.vo;

import java.io.Serializable;
import java.util.Date;

public class MovieLensTagVo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String userId;
	private String movieId;
	private String tag;
	private String timestamp;
	private Date date;

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

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
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
		return "MovieLensTag [userId=" + userId + ", movieId=" + movieId + ", tag=" + tag + ", timestamp=" + timestamp  + ", date=" + date + "]";
	}

}
