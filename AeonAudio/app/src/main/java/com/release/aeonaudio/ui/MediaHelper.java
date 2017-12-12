package com.release.aeonaudio.ui;

import android.content.Context;

/**
 * Created by Muvi on 9/1/2017.
 */

public interface MediaHelper {

    void Transporter (String SongUrl, String ImageUrl, String SongName, Context context,String artist);
    void BottomOptionMenu(Context context ,String SongName, String ArtistName,int Position );

}
