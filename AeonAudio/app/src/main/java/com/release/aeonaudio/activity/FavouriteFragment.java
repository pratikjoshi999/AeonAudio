package com.release.aeonaudio.activity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.home.apisdk.apiModel.ContentDetailsOutput;
import com.release.aeonaudio.R;
import com.release.aeonaudio.adapter.ListDataAdaptor;
import com.release.aeonaudio.model.GridItem;
import com.release.aeonaudio.service.MusicService;
import com.release.aeonaudio.utils.Constants;
import com.release.aeonaudio.utils.ProgressBarHandler;
import com.release.aeonaudio.utils.Util;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static android.R.attr.name;
import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavouriteFragment extends Fragment {
    private Paint p = new Paint();
    ListDataAdaptor adapter;
    private RecyclerView my_recycler_view;
    boolean s = true;
    String movieName;
    String movieImageStr;
    String moviePermalinkStr;
    String videoTypeIdStr;
    int corePoolSize = 60;
    int maximumPoolSize = 80;
    int keepAliveTime = 10;

    SharedPreferences prefs;
    String movieUniqueId, isEpisodeStr, sucessMsg;
    ArrayList<GridItem> itemData = new ArrayList<GridItem>();
    ArrayList<String> stream_uniq_id = new ArrayList<>();
    ProgressBarHandler videoPDialog;


    int position;

    RelativeLayout noContent;
    private String types_id_video;

    public FavouriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_list, container, false);

        videoPDialog=new ProgressBarHandler(getActivity());


        noContent = (RelativeLayout) v.findViewById(R.id.noContent);

        prefs = getActivity().getSharedPreferences(Util.LOGINPREFERENCE, MODE_PRIVATE);
        my_recycler_view = (RecyclerView) v.findViewById(R.id.card_recycler_view);
        try {

            AsynViewFavourite asynViewFavourite = new AsynViewFavourite();
            asynViewFavourite.execute();

        }catch(Exception e){

        }



        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.v("nihar_back","onKey");

                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    Log.v("nihar_back","actiondown");
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        Log.v("nihar_back","actionback");

                        Intent startIntent = new Intent(getActivity(), MainActivity.class);
                        startIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        getActivity().startActivity(startIntent);
                        getActivity().finish();


                    }
                }
                return false;
            }
        });
        return v;
    }




    private void initSwipe() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                position = viewHolder.getAdapterPosition();
                types_id_video = itemData.get(viewHolder.getAdapterPosition()).getVideoTypeId();

                if (direction == ItemTouchHelper.LEFT) {
                    adapter.removeItem(position);
                    AsynFavoriteDelete asynFavoriteDelete = new AsynFavoriteDelete();
                    asynFavoriteDelete.execute();

                    adapter.notifyDataSetChanged();

                } else {
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if (dX > 0) {
                        p.setColor(Color.parseColor("#388E3C"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_favorite_border_white_24dp);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    } else {
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete_white);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(my_recycler_view);

    }
    ////////////ViewFavourite API
    private class AsynViewFavourite extends AsyncTask<Void, Void, Void> {

        String responseStr;
        int status, item_count;
        String movieGenreStr = "";
        String movieTrailer = "";

        int isAPV = 0;
        int isPPV = 0;
        int isConverted = 0;
        private String artist_name;

        @Override
        protected Void doInBackground(Void... voids) {


            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(Util.rootUrl().trim() + Util.ViewFavorite.trim());
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("user_id", prefs.getString("useridPref", null));

           /*     httppost.addHeader("limit", String.valueOf(limit));
                httppost.addHeader("offset", String.valueOf(offset));
                httppost.addHeader("orderby", "lastupload");*/

                // Execute HTTP Post Request
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());
                    Log.v("pratikf", "fav responseStr=" + responseStr);

                } catch (org.apache.http.conn.ConnectTimeoutException e) {
                } catch (IOException e) {

                    e.printStackTrace();
                }

                JSONObject myJson = null;
                if (responseStr != null) {
                    myJson = new JSONObject(responseStr);
                    status = Integer.parseInt(myJson.optString("status"));
                    item_count = Integer.parseInt(myJson.optString("item_count"));


                   /* String items = myJson.optString("item_count");
                    itemsInServer = Integer.parseInt(items);*/
                }

                if (status > 0) {
                    if (status == 200) {

                        JSONArray jsonMainNode = myJson.getJSONArray("movieList");
                        itemData.clear();
                        int lengthJsonArr = jsonMainNode.length();
                        for (int i = 0; i < lengthJsonArr; i++) {
                            JSONObject jsonChildNode;
                            try {
                                jsonChildNode = jsonMainNode.getJSONObject(i);
                                Log.v("nihar_c", "enter");

                                movieUniqueId = jsonChildNode.getString("movie_uniq_id");
                                stream_uniq_id.add(jsonChildNode.getString("stream_uniq_id"));
                                movieName = jsonChildNode.getString("title");
                                Log.v("pratikf", "" + movieUniqueId);
//                               if ((jsonChildNode.has("genre")) && jsonChildNode.getString("genre").trim() != null && !jsonChildNode.getString("genre").trim().isEmpty() && !jsonChildNode.getString("genre").trim().equals("null") && !jsonChildNode.getString("genre").trim().matches("")) {
//                                    movieGenreStr = jsonChildNode.getString("genre");
//
//                                }
                                if ((jsonChildNode.has("parent_content_title")) && jsonChildNode.getString("parent_content_title").trim() != null && !jsonChildNode.getString("parent_content_title").trim().isEmpty() && !jsonChildNode.getString("parent_content_title").trim().equals("null") && !jsonChildNode.getString("parent_content_title").trim().matches("")) {

                                    Log.v("mainActivity", movieName);
                                }
                                if ((jsonChildNode.has("poster")) && jsonChildNode.getString("poster").trim() != null && !jsonChildNode.getString("poster").trim().isEmpty() && !jsonChildNode.getString("poster").trim().equals("null") && !jsonChildNode.getString("poster").trim().matches("")) {
                                    movieImageStr = jsonChildNode.getString("poster");
                                    //movieImageStr = movieImageStr.replace("episode", "original");
                                    Log.v("mainActivity", movieImageStr);

                                }
                                if ((jsonChildNode.has("permalink")) && jsonChildNode.getString("permalink").trim() != null && !jsonChildNode.getString("permalink").trim().isEmpty() && !jsonChildNode.getString("permalink").trim().equals("null") && !jsonChildNode.getString("permalink").trim().matches("")) {
                                    moviePermalinkStr = jsonChildNode.getString("permalink");
//                                    moviePermalinkStr = "chit-kwint-she-the";
                                    Log.v("pratikf", "Api permalink===" + moviePermalinkStr);

                                }
                                if ((jsonChildNode.has("content_types_id")) && jsonChildNode.getString("content_types_id").trim() != null && !jsonChildNode.getString("content_types_id").trim().isEmpty() && !jsonChildNode.getString("content_types_id").trim().equals("null") && !jsonChildNode.getString("content_types_id").trim().matches("")) {
                                    videoTypeIdStr = jsonChildNode.getString("content_types_id");

                                }

                                if ((jsonChildNode.has("is_episode")) && jsonChildNode.getString("is_episode").trim() != null && !jsonChildNode.getString("is_episode").trim().isEmpty() && !jsonChildNode.getString("is_episode").trim().equals("null") && !jsonChildNode.getString("is_episode").trim().matches("")) {
                                    isEpisodeStr = jsonChildNode.getString("is_episode");

                                }
                                //videoTypeIdStr = "1";
// if (mainJson.has("cast_detail")) {
                                JSONObject jsonObject = jsonChildNode.optJSONObject("casts");
                                JSONArray castArray = jsonObject.getJSONArray("artist");
                                Log.v("SUBHA", "cast_detail" + castArray);


                                StringBuilder sb = new StringBuilder();
                                if (castArray.length() > 0) {
                                    for (int j = 0; j < castArray.length(); j++) {
                                        JSONObject jsonChildNodes = castArray.getJSONObject(j);
                                        if (jsonChildNodes.has("celeb_name")) {
                                            sb.append(jsonChildNodes.getString("celeb_name").toString());
                                            if (j != castArray.length() - 1) {
                                                sb.append(",");
                                            }

                                        }

                                    }
                                    artist_name= sb.toString();
                                    Log.v("SUBHA", "SB" + sb.toString());

                                } else {

                                }
                                Log.v("nihar_c", movieImageStr + "/" + movieName + "/" + videoTypeIdStr + "/" + moviePermalinkStr + "/" + isEpisodeStr + "/" + isConverted + "/" + isPPV + "/" + isAPV);

                                itemData.add(new GridItem(movieImageStr, movieName, "", videoTypeIdStr, "", "", moviePermalinkStr, isEpisodeStr, movieUniqueId, "", 0, 0, 0,artist_name));


                            } catch (Exception e) {
                                Log.v("nihar_c", "catch_enter" + e.toString());


                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    } else {
                        responseStr = "0";

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            if (videoPDialog != null && videoPDialog.isShowing()) {
                videoPDialog.hide();
            }


            Log.v("pratikt","size in fav "+itemData.size());

            if(itemData.size()!=0) {
                if (item_count == 0) {
                    noContent.setVisibility(View.VISIBLE);
                } else {


//                my_recycler_view.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));

                    adapter = new ListDataAdaptor(getActivity(), itemData);
                    my_recycler_view.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                    my_recycler_view.setAdapter(adapter);
                    initSwipe();
                }
            }else{
                Log.v("pratikt","else part"+itemData.size());
//                Log.v("pratikt","else  pb"+videoPDialog.isShowing());


                my_recycler_view.setVisibility(View.GONE);
                noContent.setVisibility(View.VISIBLE);
            }

        }

        @Override
        protected void onPreExecute() {

            try{
                if (videoPDialog != null && videoPDialog.isShowing()) {
                    videoPDialog.hide();
                }else {
                    if (videoPDialog != null){
                        videoPDialog.show();
                    }
                }
            }catch (Exception e){
              /*  videoPDialog = new ProgressBarHandler(getActivity());
                videoPDialog.show();*/
            }
           /* if (videoPDialog != null ) {
                videoPDialog.show();
            }*/
//            videoPDialog = new ProgressBarHandler(getActivity());

        /*    if (videoPDialog != null && videoPDialog.isShowing()) {
                videoPDialog.hide();
            }else{
                videoPDialog.show();
            }*/
//            Log.v("modi","view fav pre"+videoPDialog.isShowing());
        }
    }
    private class AsynFavoriteDelete extends AsyncTask<String, Void, Void> {

        String contName;
        JSONObject myJson = null;
        int status;

        String contMessage;
        String responseStr;

        @Override
        protected Void doInBackground(String... strings) {
            String urlRouteList = Util.rootUrl().trim() + Util.DeleteFavList.trim();

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("movie_uniq_id", itemData.get(position).getMovieUniqueId());
                if (types_id_video.equals("5")) {
                    httppost.addHeader("content_type", "0");
                } else {
                    httppost.addHeader("content_type", "0");
                }
                httppost.addHeader("user_id", prefs.getString("useridPref", null));
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());
                        Log.v("Nihar_fev",""+responseStr);
                        Log.v("Nihar_fev",""+itemData.get(position).getMovieUniqueId());
                        Log.v("Nihar_fev",""+types_id_video);


                } catch (org.apache.http.conn.ConnectTimeoutException e) {

                }
            } catch (IOException e) {

                e.printStackTrace();
            }
            if (responseStr != null) {
                try {
                    myJson = new JSONObject(responseStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                status = Integer.parseInt(myJson.optString("code"));
                sucessMsg = myJson.optString("msg");
//                statusmsg = myJson.optString("status");


            }

            return null;
        }


            @Override
            protected void onPostExecute(Void aVoid) {

            if ( videoPDialog != null && videoPDialog.isShowing()) {
                videoPDialog.hide();
//                Log.v("modi","del fav post"+videoPDialog.isShowing());
            }



            Toast.makeText(getActivity(), "" + sucessMsg, Toast.LENGTH_SHORT).show();
            Log.v("pratikt","size itemData"+itemData.size());
          /*  if(itemData.size()==1){
                itemData.clear();
            }*/

            AsynViewFavourite asynViewFavourite = new AsynViewFavourite();
            asynViewFavourite.execute();

        }

        @Override
        protected void onPreExecute() {
//            pDialog = new ProgressBarHandler(getActivity());

            if ((videoPDialog!=null && videoPDialog.isShowing()) ) {
                videoPDialog.hide();
//                Log.v("modi","del fav post"+pDialog.isShowing());
            }else{
                videoPDialog.show();
            }

//            Log.v("modi","del fav pre"+videoPDialog.isShowing());

        }
    }

}
