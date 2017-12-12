package com.release.aeonaudio.model;


public class GridItem {
	private String title;
	private String videoType;
	private String imageId;
	private String permalink;
	private String Artist;
	private int isConverted;
	private int isPPV;
	private int isAPV;


	public String getArtist() {
		return Artist;
	}

	public void setArtist(String artist) {
		Artist = artist;
	}

	public int getIsAPV() {
		return isAPV;
	}

	public void setIsAPV(int isAPV) {
		this.isAPV = isAPV;
	}

	public int getIsPPV() {
		return isPPV;
	}

	public void setIsPPV(int isPPV) {
		this.isPPV = isPPV;
	}

	public int getIsConverted() {
		return isConverted;
	}

	public void setIsConverted(int isConverted) {
		this.isConverted = isConverted;
	}


	public String getMovieUniqueId() {
		return movieUniqueId;
	}

	public void setMovieUniqueId(String movieUniqueId) {
		this.movieUniqueId = movieUniqueId;
	}

	public String getMovieStreamUniqueId() {
		return movieStreamUniqueId;
	}

	public void setMovieStreamUniqueId(String movieStreamUniqueId) {
		this.movieStreamUniqueId = movieStreamUniqueId;
	}

	private String movieUniqueId;
	private String movieStreamUniqueId;

	public String getIsEpisode() {
		return isEpisode;
	}

	public void setIsEpisode(String isEpisode) {
		this.isEpisode = isEpisode;
	}

	private String isEpisode;

	public String getPermalink() {
		return permalink;
	}

	public void setPermalink(String permalink) {
		this.permalink = permalink;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	private String videoUrl;

	public String getVideoTypeId() {
		return videoTypeId;
	}

	public void setVideoTypeId(String videoTypeId) {
		this.videoTypeId = videoTypeId;
	}

	public String getMovieGenre() {
		return movieGenre;
	}

	public void setMovieGenre(String movieGenre) {
		this.movieGenre = movieGenre;
	}

	private String videoTypeId;
	private String movieGenre;
	public String getVideoType() {

		return videoType;
	}

	public void setVideoType(String videoType) {
		this.videoType = videoType;
	}



//	public GridItem(int imageId, String title,String rating) {
//		super();
//		this.imageId = imageId;
//		this.title = title;
//		this.rating = rating;
//	}

public GridItem(String imageId, String title, String videoType, String videoTypeId, String movieGenre, String videoUrl, String permalink, String isEpisode, String movieUniqueId, String movieStreamUniqueId, int isConverted, int isPPV, int isAPV, String artist_name) {
	super();
	this.imageId = imageId;
	this.title = title;
	this.videoType = videoType;
	this.videoTypeId = videoTypeId;
	this.movieGenre = movieGenre;
	this.videoUrl = videoUrl;
	this.permalink = permalink;
	this.isEpisode = isEpisode;
	this.movieUniqueId = movieUniqueId;
	this.movieStreamUniqueId = movieStreamUniqueId;
	this.isConverted = isConverted;
	this.isPPV = isPPV;
	this.isAPV = isAPV;
	this.Artist =  artist_name;


}
	public String getImage() {
		return imageId;
	}

	public void setImage(String imageId) {

		this.imageId = imageId;
	}
	/*public String getImage() {
		return imageId;
	}

	public void setImage(String imageId) {
		this.imageId = imageId;
	}*/

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
