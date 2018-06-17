package com.vo;

import java.io.Serializable;

public class MovieLensLinkVo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String movieId;
	private String imdbId;
	private String tmdbId;

	public String getMovieId() {
		return movieId;
	}

	public void setMovieId(String movieId) {
		this.movieId = movieId;
	}

	public String getImdbId() {
		return imdbId;
	}

	public void setImdbId(String imdbId) {
		this.imdbId = imdbId;
	}

	public String getTmdbId() {
		return tmdbId;
	}

	public void setTmdbId(String tmdbId) {
		this.tmdbId = tmdbId;
	}

	@Override
	public String toString() {
		return "MovieLensLink [movieId=" + movieId + ", imdbId=" + imdbId + ", tmdbId=" + tmdbId + "]";
	}

}
