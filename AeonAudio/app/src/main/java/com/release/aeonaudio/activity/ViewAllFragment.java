package com.release.aeonaudio.activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.internal.Utility;
import com.home.apisdk.apiController.GetFeatureContentAsynTask;
import com.home.apisdk.apiModel.FeatureContentInputModel;
import com.home.apisdk.apiModel.FeatureContentOutputModel;
import com.release.aeonaudio.R;
import com.release.aeonaudio.adapter.ViewallDataAdapter;
import com.release.aeonaudio.utils.ProgressBarHandler;
import com.release.aeonaudio.utils.Util;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewAllFragment extends Fragment implements GetFeatureContentAsynTask.GetFeatureContent{

    public RecyclerView myyrecyclerview;
    private ArrayList<FeatureContentOutputModel> featureContentOutputModelArrayList;
    ProgressBarHandler progressHandler;
    ViewallDataAdapter viewallDataAdapter;

    public ViewAllFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v=inflater.inflate(R.layout.fragment_view_all, container, false);
        myyrecyclerview=(RecyclerView)v.findViewById(R.id.recycleview_viewall);

        Bundle argument=getArguments();
        String sectionid=argument.getString("sectionid");
        Log.v("nihar888","section id=="+sectionid);

        FeatureContentInputModel featureContentInputModel = new FeatureContentInputModel();
        featureContentInputModel.setAuthToken(Util.authTokenStr);
        featureContentInputModel.setSection_id(sectionid);
        if (Util.checkNetwork(getActivity()) == true) {
            GetFeatureContentAsynTask getFeatureContentAsynTask = new GetFeatureContentAsynTask(featureContentInputModel,this,getActivity());
            getFeatureContentAsynTask.execute();
        }else{
            Util.showToast(getActivity(),Util.getTextofLanguage(getActivity(),Util.SLOW_INTERNET_CONNECTION,Util.DEFAULT_SLOW_INTERNET_CONNECTION));

        }


        return v;
    }

    @Override
    public void onGetFeatureContentPreExecuteStarted() {
        progressHandler = new ProgressBarHandler(getActivity());
        if (!progressHandler.isShowing()) {
            progressHandler.show();
        }

    }

    @Override
    public void onGetFeatureContentPostExecuteCompleted(ArrayList<FeatureContentOutputModel> featureContentOutputModelArray, int status, String message, int requestCode) {

        if (progressHandler.isShowing()) {
            progressHandler.hide();
        }

        this.featureContentOutputModelArrayList=featureContentOutputModelArray;
        String url=featureContentOutputModelArrayList.get(1).getPoster_url();

        if (getActivity()!=null) {
            int mNoOfColumns = Util.calculateNoOfColumns(getActivity());
            try{
                viewallDataAdapter = new ViewallDataAdapter(getActivity(), featureContentOutputModelArrayList);
                myyrecyclerview.setLayoutManager(new GridLayoutManager(getActivity(),mNoOfColumns));
                myyrecyclerview.setHasFixedSize(true);
                myyrecyclerview.setItemAnimator(new DefaultItemAnimator());
                myyrecyclerview.setAdapter(viewallDataAdapter);

            }catch (Exception e){}

        }



    }
}
