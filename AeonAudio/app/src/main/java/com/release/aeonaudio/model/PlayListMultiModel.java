package com.release.aeonaudio.model;

/**
 * Created by Muvi on 8/21/2017.
 */

public class PlayListMultiModel {
    String Name;
    String imageUrl;
    String playlist_id;
    String movie_unique_id;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPlaylist_id() {
        return playlist_id;
    }

    public void setPlaylist_id(String playlist_id) {
        this.playlist_id = playlist_id;
    }

    public String getMovie_unique_id() {
        return movie_unique_id;
    }

    public void setMovie_unique_id(String movie_unique_id) {
        this.movie_unique_id = movie_unique_id;
    }

    public PlayListMultiModel(String Name, String imageUrl, String playlist_id, String movie_unique_id){
        this.Name = Name;
        this.imageUrl=imageUrl;
        this.playlist_id=playlist_id;
        this.movie_unique_id=movie_unique_id;
    }
}
