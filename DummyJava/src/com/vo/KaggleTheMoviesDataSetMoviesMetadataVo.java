package com.vo;

import java.io.Serializable;

public class KaggleTheMoviesDataSetMoviesMetadataVo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String adult;
	private String belongs_to_collection;
	private String budget;
	private String genres;
	private String homepage;
	private String id;
	private String imdb_id;
	private String original_language;
	private String original_title;
	private String overview;
	private String popularity;
	private String poster_path;
	private String production_companies;
	private String production_countries;
	private String release_date;
	private String revenue;
	private String runtime;
	private String spoken_languages;
	private String status;
	private String tagline;
	private String title;
	private String video;
	private String vote_average;
	private String vote_count;
	
	public String getAdult() {
		return adult;
	}
	public void setAdult(String adult) {
		this.adult = adult;
	}
	public String getBelongs_to_collection() {
		return belongs_to_collection;
	}
	public void setBelongs_to_collection(String belongs_to_collection) {
		this.belongs_to_collection = belongs_to_collection;
	}
	public String getBudget() {
		return budget;
	}
	public void setBudget(String budget) {
		this.budget = budget;
	}
	public String getGenres() {
		return genres;
	}
	public void setGenres(String genres) {
		this.genres = genres;
	}
	public String getHomepage() {
		return homepage;
	}
	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getImdb_id() {
		return imdb_id;
	}
	public void setImdb_id(String imdb_id) {
		this.imdb_id = imdb_id;
	}
	public String getOriginal_language() {
		return original_language;
	}
	public void setOriginal_language(String original_language) {
		this.original_language = original_language;
	}
	public String getOriginal_title() {
		return original_title;
	}
	public void setOriginal_title(String original_title) {
		this.original_title = original_title;
	}
	public String getOverview() {
		return overview;
	}
	public void setOverview(String overview) {
		this.overview = overview;
	}
	public String getPopularity() {
		return popularity;
	}
	public void setPopularity(String popularity) {
		this.popularity = popularity;
	}
	public String getPoster_path() {
		return poster_path;
	}
	public void setPoster_path(String poster_path) {
		this.poster_path = poster_path;
	}
	public String getProduction_companies() {
		return production_companies;
	}
	public void setProduction_companies(String production_companies) {
		this.production_companies = production_companies;
	}
	public String getProduction_countries() {
		return production_countries;
	}
	public void setProduction_countries(String production_countries) {
		this.production_countries = production_countries;
	}
	public String getRelease_date() {
		return release_date;
	}
	public void setRelease_date(String release_date) {
		this.release_date = release_date;
	}
	public String getRevenue() {
		return revenue;
	}
	public void setRevenue(String revenue) {
		this.revenue = revenue;
	}
	public String getRuntime() {
		return runtime;
	}
	public void setRuntime(String runtime) {
		this.runtime = runtime;
	}
	public String getSpoken_languages() {
		return spoken_languages;
	}
	public void setSpoken_languages(String spoken_languages) {
		this.spoken_languages = spoken_languages;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTagline() {
		return tagline;
	}
	public void setTagline(String tagline) {
		this.tagline = tagline;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getVideo() {
		return video;
	}
	public void setVideo(String video) {
		this.video = video;
	}
	public String getVote_average() {
		return vote_average;
	}
	public void setVote_average(String vote_average) {
		this.vote_average = vote_average;
	}
	public String getVote_count() {
		return vote_count;
	}
	public void setVote_count(String vote_count) {
		this.vote_count = vote_count;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	@Override
	public String toString() {
		return "KaggleTheMoviesDataSetMoviesMetadataVo [adult=" + adult + ", belongs_to_collection=" + belongs_to_collection + ", budget=" + budget + ", genres=" + genres + ", homepage=" + homepage + ", id=" + id + ", imdb_id=" + imdb_id + ", original_language=" + original_language + ", original_title=" + original_title + ", overview=" + overview + ", popularity=" + popularity + ", poster_path=" + poster_path + ", production_companies=" + production_companies + ", production_countries=" + production_countries + ", release_date=" + release_date + ", revenue=" + revenue + ", runtime=" + runtime + ", spoken_languages=" + spoken_languages + ", status=" + status + ", tagline=" + tagline + ", title=" + title + ", video=" + video + ", vote_average=" + vote_average + ", vote_count=" + vote_count + "]";
	}

}
