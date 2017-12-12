package com.release.aeonaudio.model;

/**
 * Created by Muvi on 8/24/2017.
 */

public class QueueModel {

    private String SongName,AlbumArt,SongUrl,Artist_Name;

    public String getArtist_Name() {
        return Artist_Name;
    }

    public void setArtist_Name(String artist_Name) {
        Artist_Name = artist_Name;
    }

    public QueueModel(String SongName, String AlbumArt, String SongUrl, String Artist_Name){
    this.SongName =SongName ;
    this.AlbumArt =AlbumArt;
    this.SongUrl=SongUrl;
        this.Artist_Name=Artist_Name;


    }
    public String getSongName() {
        return SongName;
    }

    public void setSongName(String songName) {
        SongName = songName;
    }

    public String getAlbumArt() {
        return AlbumArt;
    }

    public void setAlbumArt(String albumArt) {
        AlbumArt = albumArt;
    }

    public String getSongUrl() {
        return SongUrl;
    }

    public void setSongUrl(String songUrl) {
        SongUrl = songUrl;
    }
}
