package com.vo;

import java.io.Serializable;

public class MovieLensMovieVo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String movieId;
	private String title;
	private String genres;

	public String getMovieId() {
		return movieId;
	}

	public void setMovieId(String movieId) {
		this.movieId = movieId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getGenres() {
		return genres;
	}

	public void setGenres(String genres) {
		this.genres = genres;
	}

	@Override
	public String toString() {
		return "MovieLensMovie [movieId=" + movieId + ", title=" + title + ", genres=" + genres + "]";
	}

}
