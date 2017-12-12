package com.release.aeonaudio.model;

/**
 * Created by Muvi on 8/23/2017.
 */

public class ListModel {
    private String  PlayListName;
    private String PlayListId;

    public ListModel(String PlayListName,String PlayListId){
        this.PlayListId=PlayListId;
        this.PlayListName = PlayListName;
    }

    public String getPlayListName() {
        return PlayListName;
    }

    public void setPlayListName(String playListName) {
        PlayListName = playListName;
    }

    public String getPlayListId() {
        return PlayListId;
    }

    public void setPlayListId(String playListId) {
        PlayListId = playListId;
    }
}
