package com.release.aeonaudio.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Muvi on 8/11/2017.
 */

public class PlayListModel implements Serializable {
    String Name;
    String imageUrl;
    String playlist_id;
    String count;
    boolean responce;

    public ArrayList<PlayListMultiModel> getItemListDetails() {
        return itemListDetails;
    }

    public void setItemListDetails(ArrayList<PlayListMultiModel> itemListDetails) {
        this.itemListDetails = itemListDetails;
    }

    ArrayList<PlayListMultiModel> itemListDetails;

    public PlayListModel(String Name, String imageUrl, String playlist_id, ArrayList<PlayListMultiModel> itemListDetails){
        this.Name = Name;
        this.imageUrl=imageUrl;
        this.playlist_id=playlist_id;
        this.itemListDetails = itemListDetails;
        this.responce = responce;
    }

    public boolean isResponce() {
        return responce;
    }

    public void setResponce(boolean responce) {
        this.responce = responce;
    }

    public PlayListModel(String Name, String imageUrl, String playlist_id,String count ,boolean responce){
        this.Name = Name;
        this.imageUrl=imageUrl;
        this.playlist_id=playlist_id;
        this.responce = responce;
        this.count = count;


    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

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
}
