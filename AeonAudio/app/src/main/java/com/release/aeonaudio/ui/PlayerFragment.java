package com.release.aeonaudio.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.release.aeonaudio.R;

/**
 * Created by Muvi on 9/1/2017.
 */

public  class PlayerFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.playerlist_recycle, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.playerlist_recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new PlayerAdapter(getActivity()));

        return view;
    }
}