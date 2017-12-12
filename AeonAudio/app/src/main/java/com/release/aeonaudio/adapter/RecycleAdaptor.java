package com.release.aeonaudio.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.home.apisdk.apiController.GetFeatureContentAsynTask;
import com.home.apisdk.apiModel.FeatureContentInputModel;
import com.home.apisdk.apiModel.FeatureContentOutputModel;
import com.home.apisdk.apiModel.HomePageSectionModel;
import com.release.aeonaudio.R;

import com.release.aeonaudio.activity.MainActivity;
import com.release.aeonaudio.slider.CarouselPagerAdapter;
import com.release.aeonaudio.slider.MyCustomPagerAdapter;
import com.release.aeonaudio.slider.ZoomOutPageTransformer;
import com.release.aeonaudio.utils.ProgressBarHandler;
import com.release.aeonaudio.utils.Util;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static com.release.aeonaudio.R.drawable.tab_selector;

/**
 * Created by Muvi on 6/5/2017.
 */

public class RecycleAdaptor extends RecyclerView.Adapter<RecycleAdaptor.MyHolder> implements GetFeatureContentAsynTask.GetFeatureContent {
    private ArrayList<HomePageSectionModel> HomePageSectionModel;
    private Context mContext;
    Fragment fragment = null;
    ProgressBarHandler progressHandler;
    int myrequestCode;
    int i = 0;
    Timer timer;
    MyHolder holder ;
    ArrayList<TextView> indicator_text=new ArrayList<>();
    private ArrayList<MyHolder> myHolderArrayList = new ArrayList<MyHolder>();

    ArrayList<FeatureCoverFlow> featureCoverFlow = new ArrayList<>();

    private ArrayList<FeatureContentOutputModel> featureContentOutputModelArrayList;

    public RecycleAdaptor(Context context, ArrayList<HomePageSectionModel> HomePageSectionModel) {
        this.HomePageSectionModel = HomePageSectionModel;
        this.mContext = context;
        myHolderArrayList.clear();

    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, null);
        MyHolder mh = new MyHolder(v);
         progressHandler = new ProgressBarHandler(mContext);
        return mh;
    }

    @Override
    public void onBindViewHolder( final MyHolder itemRowHolder, final int  i) {
        holder = itemRowHolder;



        if (i == 0){
            itemRowHolder.indicator_layout.setVisibility(View.VISIBLE);
            itemRowHolder.view_pager_vg.setVisibility(View.VISIBLE);
            Util.viewPager = itemRowHolder.viewPager;

        }     else{
            itemRowHolder.indicator_layout.setVisibility(View.GONE);
            itemRowHolder.view_pager_vg.setVisibility(View.GONE);
        }
        Log.v("nihar_space",""+HomePageSectionModel.size());
        if (i==HomePageSectionModel.size()-1){
            StartTimer();
            itemRowHolder.space_view.setVisibility(View.VISIBLE);
        }


        final String sectionName = HomePageSectionModel.get(i).getTitle();
        HomePageSectionModel FeatureContentOutputModel = HomePageSectionModel.get(i);
        final String section_data_id = HomePageSectionModel.get(i).getSection_id();
        itemRowHolder.itemTitle.setText(sectionName);
        Log.v("pratikid","section id=="+section_data_id);
        myHolderArrayList.add(itemRowHolder);

        FeatureContentInputModel featureContentInputModel = new FeatureContentInputModel();
        featureContentInputModel.setAuthToken(Util.authTokenStr);
//        featureContentInputModel.setAuthToken("00d7ae780e9aff77f85378dd470e9317");
        featureContentInputModel.setSection_id(section_data_id);
        featureContentInputModel.setRequest_code(i);
        if (Util.checkNetwork(mContext) == true) {
            GetFeatureContentAsynTask getFeatureContentAsynTask = new GetFeatureContentAsynTask(featureContentInputModel,this,mContext);
            getFeatureContentAsynTask.execute();
        }else{
            Util.showToast(mContext,Util.getTextofLanguage(mContext,Util.SLOW_INTERNET_CONNECTION,Util.DEFAULT_SLOW_INTERNET_CONNECTION));

        }




      /*  SectionListDataAdapter itemListDataAdapter = new SectionListDataAdapter(mContext, featureContentOutputModelArrayList);
        itemRowHolder.recycler_view_list.setHasFixedSize(true);
        itemRowHolder.recycler_view_list.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        itemRowHolder.recycler_view_list.setAdapter(itemListDataAdapter);*/


        itemRowHolder.btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   Toast.makeText(v.getContext(), "click event on more, "+sectionName , Toast.LENGTH_SHORT).show();

                fragment = new com.release.aeonaudio.activity.ViewAllFragment();
                Bundle arguments = new Bundle();


                arguments.putString("sectionid",section_data_id);
                fragment.setArguments(arguments);
                if (fragment != null) {

                    FragmentManager fragmentManager =((AppCompatActivity)mContext).getSupportFragmentManager();;
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.main_content, fragment,"ViewAll");
                    fragmentTransaction.addToBackStack("ViewAll");
                    fragmentTransaction.commit();

                }

            }
        });


        itemRowHolder.iimg_recycle_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                    myHolderArrayList.get(myrequestCode).recycler_view_list.getLayoutManager().scrollToPosition(linearLayoutManager.findLastVisibleItemPosition() + 1);
                itemRowHolder.linearLayoutManager.smoothScrollToPosition(itemRowHolder.recycler_view_list,null,itemRowHolder.linearLayoutManager.findLastVisibleItemPosition()+3);

            }
        });


    }

    @Override
    public int getItemCount() {
        return (null != HomePageSectionModel ? HomePageSectionModel.size() : 0);
    }

    @Override
   public void onGetFeatureContentPreExecuteStarted() {
      /*  if (!progressHandler.isShowing()) {
            progressHandler.show();
        }*/

    }

    @Override
    public void onGetFeatureContentPostExecuteCompleted(ArrayList<FeatureContentOutputModel> featureContentOutputModelArray, int status, String message, int requestCode) {
        this.featureContentOutputModelArrayList=featureContentOutputModelArray;
        this.myrequestCode=requestCode;
        if (progressHandler.isShowing()) {
            progressHandler.hide();
        }
        SectionListDataAdapter itemListDataAdapter = new SectionListDataAdapter(mContext, featureContentOutputModelArrayList);
        myHolderArrayList.get(requestCode).recycler_view_list.setHasFixedSize(true);
        myHolderArrayList.get(requestCode).linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        myHolderArrayList.get(requestCode).recycler_view_list.setLayoutManager(myHolderArrayList.get(requestCode).linearLayoutManager);
        myHolderArrayList.get(requestCode).recycler_view_list.setAdapter(itemListDataAdapter);


    }

    public void StopTimer() { timer.cancel();
    }


    public class MyHolder extends RecyclerView.ViewHolder{
        protected TextView itemTitle;
        public RecyclerView recycler_view_list;
        LinearLayoutManager linearLayoutManager;
        LinearLayout indicator_layout;
        protected TextView btnMore,space_view;
        protected RelativeLayout view_pager_vg;
        protected ImageView iimg_recycle_img;
        MyCustomPagerAdapter myCustomPagerAdapter;
        ViewPager viewPager;
        FeatureCoverFlow coverFlow;
        CarouselPagerAdapter madapter;
        int page;


        public MyHolder(View view) {
            super(view);
            this.view_pager_vg = (RelativeLayout) view.findViewById(R.id.view_pager_vg);
            this.itemTitle = (TextView) view.findViewById(R.id.itemTitle);
            this.indicator_layout = (LinearLayout) view.findViewById(R.id.indicator_layout);
            this.space_view = (TextView) view.findViewById(R.id.space_view);
            this.iimg_recycle_img = (ImageView) view.findViewById(R.id.iimg_recycle_img);
            Typeface itemTitle_tf = Typeface.createFromAsset(mContext.getAssets(),mContext.getResources().getString(R.string.regular_fonts));
            itemTitle.setTypeface(itemTitle_tf);

            this.recycler_view_list = (RecyclerView) view.findViewById(R.id.recycler_view_list);
            this.btnMore= (TextView) view.findViewById(R.id.btnMore);
            Typeface btnMore_tf = Typeface.createFromAsset(mContext.getAssets(),mContext.getResources().getString(R.string.regular_fonts));
            btnMore.setTypeface( btnMore_tf);

            viewPager = (ViewPager) view.findViewById(R.id.viewPager);
            viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
            coverFlow=(FeatureCoverFlow)view.findViewById(R.id.coverFlow);

           String[] banner= new String[]{"https://sampledesign.muvi.com/mobileaudio/newimage.png","http://motivational-quotes-for-athletes.com/wp-content/uploads/2013/11/play-music.jpg",
                   "https://t3.ftcdn.net/jpg/01/24/05/60/240_F_124056004_pbfPOjDj8UfU3nV4YofiWxiXPpvnLCM0.jpg","https://s-media-cache-ak0.pinimg.com/originals/99/d6/04/99d604c95210d831da61e049a1f7fc79.jpg"};
            madapter=new CarouselPagerAdapter(mContext, banner);
            coverFlow.setAdapter(madapter);
            coverFlow.scrollToPosition(1);
            featureCoverFlow.add(coverFlow);


            Log.v("Nihar_library", "viewPager ====== ");

            coverFlow.setOnScrollPositionListener(new FeatureCoverFlow.OnScrollPositionListener() {
                @Override
                public void onScrolledToPosition(int position) {
                    //TODO CoverFlow stopped to position
                       Log.v("Nihar_library", "viewPager ====== called" + position + "");

                    if (madapter.getCount() != 0) {
                        indicator_layout.removeAllViewsInLayout();
                        for (int i = 0; i < madapter.getCount(); i++) {
                            ImageView imageView = new ImageView(mContext);
                            imageView.setMinimumHeight(25);
                            imageView.setMinimumWidth(25);
                            imageView.setPadding(10, 10, 10, 10);// left,top,right,bottom
                            if (i == position) {
                                imageView.setImageResource(R.drawable.selected_dot);
                            } else {
                                imageView.setImageResource(R.drawable.default_dot);
                            }

                            indicator_layout.addView(imageView, i);
                        }

                    }
                }

                @Override
                public void onScrolling() {
                    //TODO CoverFlow began scrolling


                }
            });


        }
    }
    public void StartTimer() {

  timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.v("NiharTimer",""+i);

                ((MainActivity)mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        featureCoverFlow.get(0).scrollToPosition(i);
//                        featureCoverFlow.get(1).scrollToPosition(3);
//                        featureCoverFlow.get(2).scrollToPosition(3);
                    }
                });

//
                if (i == holder.madapter.getCount()){
                    i = 0;
                }
                i++;

            }
        }, 5000, 5000);
    }
}
