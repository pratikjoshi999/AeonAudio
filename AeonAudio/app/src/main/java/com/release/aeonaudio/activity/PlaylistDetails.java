package com.release.aeonaudio.activity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.home.apisdk.apiController.GetValidateUserAsynTask;
import com.home.apisdk.apiModel.Episode_Details_output;
import com.home.apisdk.apiModel.ValidateUserInput;
import com.home.apisdk.apiModel.ValidateUserOutput;
import com.release.aeonaudio.R;
import com.release.aeonaudio.adapter.MultiDataAdaptor;
import com.release.aeonaudio.adapter.PlayListDetailsAdaptor;
import com.release.aeonaudio.model.ListModel;
import com.release.aeonaudio.model.QueueModel;
import com.release.aeonaudio.service.MusicService;
import com.release.aeonaudio.utils.Constants;
import com.release.aeonaudio.utils.ProgressBarHandler;
import com.release.aeonaudio.utils.Util;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.R.attr.name;
import static android.content.Context.MODE_PRIVATE;
import static com.release.aeonaudio.R.id.dialog_text;
import static com.release.aeonaudio.R.layout.item;
import static com.release.aeonaudio.utils.Util.QUEUE_ARRAY;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlaylistDetails extends Fragment implements GetValidateUserAsynTask.GetValidateUser {

    Bundle arguments;
     String desired_string,ImageUrl,PlayListName,content_types_id;
    Episode_Details_output episode_details_output_model;
    ProgressBarHandler progressDialog;
    SharedPreferences prefs;
    private String movie_stream_id_playlist;
     String userId,PlayListId;
    private String playlist_id;
    Button play_all;
    View  low_row;
    RecyclerView my_recycler_view;
    ArrayList<ListModel> customDatas = new ArrayList<>();
    ArrayList<Episode_Details_output> ItemListDetails = new ArrayList<>();
    private String MutiArtist,MovieStramId_del_cont;
    private PlayListDetailsAdaptor multiAdapter;
    TextView play_list_name,playlist_song_count;
    private String song_url,song_imageUrl,genere,artist_name,album_name,song_name;

    String movieId;
    ProgressBarHandler pDialog;

    public PlaylistDetails() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_playlist_details, container, false);


        progressDialog = new ProgressBarHandler(getActivity());


        prefs = getActivity().getSharedPreferences(Util.LOGINPREFERENCE, MODE_PRIVATE);
        arguments = getArguments();
        desired_string = arguments.getString("PERMALINK");
        content_types_id = arguments.getString("CONTENT_TYPE");
        ImageUrl = arguments.getString("ImageUrl");
        PlayListName = arguments.getString("PlayListName");

        userId = prefs.getString("useridPref", null);
        PlayListId = arguments.getString("PlayListId");
        my_recycler_view = (RecyclerView) v.findViewById(R.id.playlistDetails_rv);
        play_list_name = (TextView) v.findViewById(R.id.play_list_name);
        Typeface play_list_name_tf = Typeface.createFromAsset(getActivity().getAssets(), getActivity().getString(R.string.regular_fonts));
        play_list_name.setTypeface(play_list_name_tf);
        playlist_song_count = (TextView) v.findViewById(R.id.playlist_song_count);
        low_row = (View) v.findViewById(R.id.low_row);
        Typeface playlist_song_count_tf = Typeface.createFromAsset(getActivity().getAssets(), getActivity().getString(R.string.regular_fonts));
        playlist_song_count.setTypeface(playlist_song_count_tf);
        play_all = (Button) v.findViewById(R.id.play_all);

        play_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ItemListDetails.size() != 0) {
                    album_name = ItemListDetails.get(0).getName();
                    song_url = ItemListDetails.get(0).getVideo_url();
                    song_imageUrl = ItemListDetails.get(0).getPoster_url();
                    genere = ItemListDetails.get(0).getGenre();
                    song_name = ItemListDetails.get(0).getEpisode_title();
                    artist_name = MutiArtist;
                   /* ValidateUserInput validateUserInput = new ValidateUserInput();
                    validateUserInput.setAuthToken(Util.authTokenStr);
                    validateUserInput.setUserId(userId);
                    validateUserInput.setMuviUniqueId(movieuniqueid);
                    validateUserInput.setSeasonId("0");
                    validateUserInput.setEpisodeStreamUniqueId("0");

                    GetValidateUserAsynTask getValidateUserAsynTask = new GetValidateUserAsynTask(validateUserInput, MultiPartFragment.this, getActivity());
                    getValidateUserAsynTask.execute();*/



                    Player_State(1);
                    QUEUE_ARRAY.clear();
                    for (int i = 0; i < ItemListDetails.size(); i++) {
                        QUEUE_ARRAY.add(new QueueModel(ItemListDetails.get(i).getName(), ItemListDetails.get(i).getPoster_url(), ItemListDetails.get(i).getVideo_url(), MutiArtist));
                    }
                }
            }
            });


        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(OPTION_RESPONSE, new IntentFilter("OPTION_RESPONSE"));


        AsynAudioUserPlayListDetail asynAudioUserPlayListDetail = new AsynAudioUserPlayListDetail();
        asynAudioUserPlayListDetail.execute();

        return v ;
    }


    private BroadcastReceiver OPTION_RESPONSE = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            movie_stream_id_playlist = intent.getStringExtra("movie_stream_id_playlist");

            if (intent.getStringExtra("response").equals("remove_Playlist_content")) {
                if (Util.checkNetwork(getActivity())) {
                    Log.v("nihar_position", "Called");
                    AsyncDeletePlaylistContent asyncDeletePlaylistContent = new AsyncDeletePlaylistContent();
                    asyncDeletePlaylistContent.execute();

                } else {
                    Util.showToast(getActivity(), Util.getTextofLanguage(getActivity(), Util.SLOW_INTERNET_CONNECTION, Util.DEFAULT_SLOW_INTERNET_CONNECTION));
                }

            }
        }
        };
    public void PlaySongsmulti(Episode_Details_output item, boolean isClicked, int adapterPosition) {
        if (isClicked) {

            episode_details_output_model = item;
            album_name = item.getName();
            song_url = item.getVideo_url();
            song_imageUrl = item.getPoster_url();
            genere = item.getGenre();
            song_name = item.getEpisode_title();
            artist_name = MutiArtist;


            ValidateUserInput validateUserInput = new ValidateUserInput();
            validateUserInput.setAuthToken(Util.authTokenStr);
            validateUserInput.setUserId(userId);
            validateUserInput.setMuviUniqueId(item.getMuvi_uniq_id());
            Log.v("namo","muiid pl="+item.getMuvi_uniq_id());
            validateUserInput.setSeasonId("0");
            validateUserInput.setEpisodeStreamUniqueId("0");
            GetValidateUserAsynTask getValidateUserAsynTask = new GetValidateUserAsynTask(validateUserInput, this, getActivity());
            getValidateUserAsynTask.execute();



 /*           Intent CONTENT_OUTPUT = new Intent("CONTENT_OUTPUT2");
            CONTENT_OUTPUT.putExtra("Content_multipart", episode_details_output);
            CONTENT_OUTPUT.putExtra("position_item", adapterPosition);
            CONTENT_OUTPUT.putExtra("artist", artist_name);
            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(CONTENT_OUTPUT);*/
/*            if (userId.equals("0101D")) {

            } else {
                ValidateUserInput validateUserInput = new ValidateUserInput();
                validateUserInput.setAuthToken(Util.authTokenStr);
                validateUserInput.setUserId(userId);
                validateUserInput.setMuviUniqueId(item.getMuvi_uniq_id());
                validateUserInput.setSeasonId("0");
                validateUserInput.setEpisodeStreamUniqueId("0");
                GetValidateUserAsynTask getValidateUserAsynTask = new GetValidateUserAsynTask(validateUserInput, this, getActivity());
                getValidateUserAsynTask.execute();
            }*/

        }
    }
    public void Player_State(int funId) {

        Intent playerData = new Intent("PLAYER_DETAIL");
        playerData.putExtra("songName", album_name);
        Log.v("nihar3", "" + album_name);
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(playerData);

        try {
            Intent j = new Intent(getContext(), MusicService.class);
            j.putExtra("ALBUM", song_url);
            j.putExtra("PERMALINK", desired_string);

            j.putExtra("ALBUM_ART", song_imageUrl);
            j.putExtra("ALBUM_NAME", name);
            j.putExtra("Artist", artist_name);
            j.putExtra("ALBUM_SONG_NAME", song_name);
            j.putExtra("STATE", funId);
            j.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
            getContext().startService(j);
        } catch (Exception e) {

        }


    }

    @Override
    public void onGetValidateUserPreExecuteStarted() {
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    @Override
    public void onGetValidateUserPostExecuteCompleted(ValidateUserOutput validateUserOutput, int status, String message) {
        if (progressDialog.isShowing() && progressDialog!=null){
            progressDialog.hide();
        }
        Log.v("namo","status pl="+status);
        if (status == 427) {

            Toast.makeText(getActivity(), "Sorry , This content is  not available in your country . ", Toast.LENGTH_SHORT).show();
        } else if (status == 425 || status == 426) {

            Intent intent = new Intent(getActivity(), SubscriptionActivity.class);
            intent.putExtra("movieId", movieId);
            Bundle arguments = getArguments();
            intent.putExtra("permalink", arguments.getString("PERMALINK"));
            intent.putExtra("content_types_id", arguments.getString("CONTENT_TYPE"));
            startActivity(intent);
        }else if (status == 429){
            Player_State(1);
        }else if (status == 430){
            if (validateUserOutput.getIsMemberSubscribed().equals("0")) {
                Intent intent = new Intent(getActivity(), SubscriptionActivity.class);
                intent.putExtra("movieId", movieId);
                Bundle arguments = getArguments();
                intent.putExtra("permalink", arguments.getString("PERMALINK"));
                intent.putExtra("content_types_id", arguments.getString("CONTENT_TYPE"));
                startActivity(intent);
            }else{
                Player_State(1);
            }
        }

    }

    private class AsynAudioUserPlayListDetail extends AsyncTask<String, Void, Void> {
        JSONObject myJson = null;
        String responseStr;
        String sucessMsg;
        String playlist_name;
        int statusCode = 0;

        private String playlist_poster;
        int count;
        private String title;

        @Override
        protected void onPreExecute() {
            if (progressDialog.isShowing() && progressDialog!=null){
                progressDialog.hide();
            }else{
                progressDialog.show();
            }


        }

        @Override
        protected Void doInBackground(String... strings) {

            String urlRouteList = Util.rootUrl().trim() + Util.AudioUserPlayListDetail.trim();

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("user_id", prefs.getString("useridPref", null));
                httppost.addHeader("list_id", PlayListId);
                Log.v("Nihar", "Playlist details user id" + PlayListId);
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());
                    Log.v("Nihar_flow", "Playlist details user id" + responseStr);

                } catch (org.apache.http.conn.ConnectTimeoutException e) {
                }
            } catch (Exception e) {
            }
            if (responseStr != null) {
                try {
                    myJson = new JSONObject(responseStr);
                    if (myJson.has("code")){
                        statusCode = Integer.parseInt(myJson.optString("code"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (statusCode == 200) {
                        JSONObject jsonObject = myJson.getJSONObject("data");


                        customDatas.clear();
                        try {

                            if ((jsonObject.has("poster_playlist")) && jsonObject.getString("poster_playlist").trim() != null && !jsonObject.getString("poster_playlist").trim().isEmpty() && !jsonObject.getString("poster_playlist").trim().equals("null") && !jsonObject.getString("poster_playlist").trim().matches("")) {
                                playlist_poster = jsonObject.getString("poster_playlist");
                            }
                            if ((jsonObject.has("list_name")) && jsonObject.getString("list_name").trim() != null && !jsonObject.getString("list_name").trim().isEmpty() && !jsonObject.getString("list_name").trim().equals("null") && !jsonObject.getString("list_name").trim().matches("")) {
                                playlist_name = jsonObject.getString("list_name");
                            }
                            if ((jsonObject.has("list_id")) && jsonObject.getString("list_id").trim() != null && !jsonObject.getString("list_id").trim().isEmpty() && !jsonObject.getString("list_id").trim().equals("null") && !jsonObject.getString("list_id").trim().matches("")) {
                                playlist_id = jsonObject.getString("list_id");
                            }
                            if ((jsonObject.has("counts")) && jsonObject.getString("counts").trim() != null && !jsonObject.getString("counts").trim().isEmpty() && !jsonObject.getString("counts").trim().equals("null") && !jsonObject.getString("counts").trim().matches("")) {
                                count = Integer.parseInt(jsonObject.getString("counts"));
                            }


                            JSONArray jsonListArray = jsonObject.optJSONArray("lists");
                            ItemListDetails = new ArrayList<>();
                            int jsonListArrayLength = jsonListArray.length();
                            if (ItemListDetails != null && ItemListDetails.size() > 0) {
                                ItemListDetails.clear();
                            }
                            Log.v("NiharMishra1", "jsonListArrayLength    " + jsonListArrayLength);
                            Log.v("NiharMishra1", "jsonListArray    " + jsonListArray.length());

                            for (int j = 0; j < jsonListArrayLength; j++) {
                                episode_details_output_model = new Episode_Details_output();
                                JSONObject jsonChildNode;
                                try {
                                    jsonChildNode = jsonListArray.optJSONObject(j);

                                    if ((jsonChildNode.has("title")) && jsonChildNode.getString("title").trim() != null && !jsonChildNode.getString("title").trim().isEmpty() && !jsonChildNode.getString("title").trim().equals("null") && !jsonChildNode.getString("title").trim().matches("")) {
                                        episode_details_output_model.setEpisode_title(jsonChildNode.getString("title"));
                                        episode_details_output_model.setName(jsonChildNode.getString("title"));
                                    }
                                    if ((jsonChildNode.has("cast")) && jsonChildNode.getString("cast").trim() != null && !jsonChildNode.getString("cast").trim().isEmpty() && !jsonChildNode.getString("cast").trim().equals("null") && !jsonChildNode.getString("cast").trim().matches("")) {
                                        MutiArtist = jsonChildNode.getString("cast");
                                    }
                                    if ((jsonChildNode.has("url")) && jsonChildNode.getString("url").trim() != null && !jsonChildNode.getString("url").trim().isEmpty() && !jsonChildNode.getString("url").trim().equals("null") && !jsonChildNode.getString("url").trim().matches("")) {
                                        episode_details_output_model.setVideo_url(jsonChildNode.getString("url"));
                                    }
                                    if ((jsonChildNode.has("audio_poster")) && jsonChildNode.getString("audio_poster").trim() != null && !jsonChildNode.getString("audio_poster").trim().isEmpty() && !jsonChildNode.getString("audio_poster").trim().equals("null") && !jsonChildNode.getString("audio_poster").trim().matches("")) {
                                        episode_details_output_model.setPoster_url(jsonChildNode.getString("audio_poster"));
                                    }
                                    if ((jsonChildNode.has("movie_id")) && jsonChildNode.getString("movie_id").trim() != null && !jsonChildNode.getString("movie_id").trim().isEmpty() && !jsonChildNode.getString("movie_id").trim().equals("null") && !jsonChildNode.getString("movie_id").trim().matches("")) {
                                        episode_details_output_model.setId(jsonChildNode.getString("movie_id"));
                                    }
                                    if ((jsonChildNode.has("movie_stream_id")) && jsonChildNode.getString("movie_stream_id").trim() != null && !jsonChildNode.getString("movie_stream_id").trim().isEmpty() && !jsonChildNode.getString("movie_stream_id").trim().equals("null") && !jsonChildNode.getString("movie_stream_id").trim().matches("")) {
                                        MovieStramId_del_cont = jsonChildNode.getString("movie_stream_id");
                                        episode_details_output_model.setMuvi_uniq_id(jsonChildNode.getString("movie_stream_id"));
                                    }

                                    ItemListDetails.add(episode_details_output_model);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Log.v("Nihar_flow", "Exception=-=====" + e.toString());

                                }
                            }

                        } catch (Exception e) {
                            Log.v("NiharMishra1", "Exception=-=====" + e.toString());

                        }
                    }else{
                        if (ItemListDetails != null && ItemListDetails.size() > 0) {
                            ItemListDetails.clear();
                        }
                    }
                } catch (JSONException e) {
                    Log.v("NiharMishra1", "JSONException=-=====" + e.toString());

                    e.printStackTrace();

                }
                sucessMsg = myJson.optString("msg");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
//            List_wait_progressBar.setVisibility(View.GONE);
            if (progressDialog.isShowing() && progressDialog!=null){
                progressDialog.hide();
            }
            if (ItemListDetails.size() == 1 || ItemListDetails.size() == 0) {
                playlist_song_count.setText(ItemListDetails.size() + " " + "Track");
            } else {
                playlist_song_count.setText(ItemListDetails.size() + " " + "Tracks");
            }
            play_all.setVisibility(View.VISIBLE);
            low_row.setVisibility(View.VISIBLE);
            play_list_name.setText(PlayListName);



            multiAdapter = new PlayListDetailsAdaptor(getActivity(), ItemListDetails, PlaylistDetails.this, MutiArtist, "PlayListData");
            my_recycler_view.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            my_recycler_view.setAdapter(multiAdapter);
            multiAdapter.notifyDataSetChanged();

        }
    }

    private class AsyncDeletePlaylistContent extends AsyncTask<String, Void, Void> {
        JSONObject myJson = null;
        int status;
        String responseStr;
        String sucessMsg;

        @Override
        protected void onPreExecute() {
            if (progressDialog.isShowing() && progressDialog!=null){
                progressDialog.hide();
            }else{
                progressDialog.show();
            }



        }

        @Override
        protected Void doInBackground(String... strings) {

            String urlRouteList = Util.rootUrl().trim() + Util.DeleteContent.trim();

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("user_id", prefs.getString("useridPref", null));
                httppost.addHeader("playlist_id", PlayListId);
                httppost.addHeader("content_id", movie_stream_id_playlist);
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());
                    Log.v("Nihar_flow2", responseStr + "     " + playlist_id + "movie_stream_id   " + prefs.getString("useridPref", null) + " " + MovieStramId_del_cont);
                } catch (org.apache.http.conn.ConnectTimeoutException e) {
                }
            } catch (Exception e) {
            }
            if (responseStr != null) {
                try {
                    myJson = new JSONObject(responseStr);
                    sucessMsg = myJson.optString("msg");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
//            Toast.makeText(getActivity(), ""+sucessMsg, Toast.LENGTH_SHORT).show();

            if (progressDialog.isShowing() && progressDialog != null) {
                progressDialog.hide();
            }
            AsynAudioUserPlayListDetail asynAudioUserPlayListDetail = new AsynAudioUserPlayListDetail();
            asynAudioUserPlayListDetail.execute();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(OPTION_RESPONSE);

    }
}
