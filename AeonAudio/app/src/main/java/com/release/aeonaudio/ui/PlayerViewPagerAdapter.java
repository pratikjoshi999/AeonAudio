package com.release.aeonaudio.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Muvi on 9/1/2017.
 */

 public class PlayerViewPagerAdapter extends FragmentPagerAdapter {

    public PlayerViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return new PlayerFragment();
    }

    @Override
    public int getCount() {
        return 1;
    }
}