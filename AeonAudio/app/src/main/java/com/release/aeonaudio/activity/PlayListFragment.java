package com.release.aeonaudio.activity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.release.aeonaudio.R;
import com.release.aeonaudio.adapter.PlayListAdapter;
import com.release.aeonaudio.adapter.PlayerDataAdaptor;
import com.release.aeonaudio.model.PlayListModel;
import com.release.aeonaudio.model.PlayListMultiModel;
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

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.facebook.FacebookSdk.getApplicationContext;
import static com.release.aeonaudio.R.drawable.play;
import static com.release.aeonaudio.R.id.list_artistName;
import static com.release.aeonaudio.R.id.playlist_tv;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlayListFragment extends Fragment {
    Communicate comm;
    TextView NewplayList;
    private Context context;
    PlayListAdapter adapter;
    RecyclerView  PlayListRecycleView;
    String PlayListName,PlayListUrl;
    ArrayList<PlayListModel> ItemList;
    ArrayList<PlayListMultiModel> ItemListDetails;
    SharedPreferences prefs;
     String userId,playlist_id,PlayListCurrentId;
    AlertDialog alertDialog;
    public  interface Communicate{
         Void CommunicateName(int j);
    }
    public PlayListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            comm = (Communicate) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement CommunicateListner");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_play_list, container, false);
        NewplayList = (TextView) v.findViewById(R.id.create_new_playlist);
        NewplayList.setText("+ CREATE NEW PLAYLIST");
        Typeface NewplayList_tf = Typeface.createFromAsset(getActivity().getAssets(), getActivity().getString(R.string.regular_fonts));
        NewplayList.setTypeface(NewplayList_tf);

        ItemList =new  ArrayList<>();
        NewplayList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(getContext());
                View promptsView = li.inflate(R.layout.custom_dialog, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setView(promptsView);
                comm.CommunicateName(3);

                Button  saveButton = (Button)promptsView.findViewById(R.id.saveButton);
                Button  cancelButton = (Button)promptsView.findViewById(R.id.cancelButton);
                 TextView dialog_text = (TextView) promptsView.findViewById(R.id.dialog_text);
                Typeface dialog_text_tf = Typeface.createFromAsset(getActivity().getAssets(), getActivity().getString(R.string.regular_fonts));
                dialog_text.setTypeface(dialog_text_tf);

                final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);

              final   AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
                alertDialog.setCancelable(false);
                cancelButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        // alertDialog.cancel;
                        alertDialog.cancel();
                    }
                });
                saveButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        //Do some thing

                        alertDialog.dismiss();
                        PlayListName = String.valueOf(userInput.getText());
                        AsyncAddToPlaylist asyncAddToPlaylist = new AsyncAddToPlaylist();
                        asyncAddToPlaylist.execute();
                    }
                });
                alertDialog.show();
            }
        });


        PlayListRecycleView = (RecyclerView) v.findViewById(R.id.playlist_recycler_view) ;

        PlayListRecycleView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        prefs = getActivity().getSharedPreferences(Util.LOGINPREFERENCE, MODE_PRIVATE);
        userId = prefs.getString("useridPref", null);
    /*    AsynShowPlaylist asynShowPlaylist = new AsynShowPlaylist();
        asynShowPlaylist.execute();*/
        AsynShowAllUserPlaylist asynShowAllUserPlaylist = new AsynShowAllUserPlaylist();
        asynShowAllUserPlaylist.execute();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(OPTION_RESPONSE, new IntentFilter("OPTION_RESPONSE"));


     /*   v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {


                if (event.getAction() == KeyEvent.ACTION_DOWN) {

                    if (keyCode == KeyEvent.KEYCODE_BACK) {




                        Intent startIntent = new Intent(getActivity(), MainActivity.class);
                        startIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        getActivity().startActivity(startIntent);
                        getActivity().finish();


                    }
                }
                return false;
            }
        });*/


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
    private BroadcastReceiver OPTION_RESPONSE = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            String responce = intent.getStringExtra("response");
            if (responce.equals("rename")){
            LayoutInflater li = LayoutInflater.from(getContext());
            View promptsView = li.inflate(R.layout.custom_dialog, null);
                TextView dialog_text = (TextView) promptsView.findViewById(R.id.dialog_text);
                Typeface dialog_text_tf = Typeface.createFromAsset(getActivity().getAssets(), getActivity().getString(R.string.regular_fonts));
                dialog_text.setTypeface(dialog_text_tf);
                dialog_text.setText("EDIT PLAYLIST");


            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            alertDialogBuilder.setView(promptsView);

              Button  saveButton = (Button)promptsView.findViewById(R.id.saveButton);
                Button  cancelButton = (Button)promptsView.findViewById(R.id.cancelButton);
                String PlaylistName = intent.getStringExtra("PlaylistName");
            final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);
                Typeface userInput_tf = Typeface.createFromAsset(getActivity().getAssets(), getActivity().getString(R.string.regular_fonts));
                userInput.setTypeface(userInput_tf);
                userInput.setText(PlaylistName, TextView.BufferType.EDITABLE);


             alertDialog = alertDialogBuilder.create();
                alertDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
                cancelButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                       // alertDialog.cancel;
                        alertDialog.cancel();
                    }
                });
                saveButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        //Do some thing
                        PlayListCurrentId=intent.getStringExtra("playlistid");

                        PlayListName = String.valueOf(userInput.getText());
                        alertDialog.dismiss();
                        AsyncEditPlaylistName asyncEditPlaylistName = new AsyncEditPlaylistName();
                        asyncEditPlaylistName.execute();
                    }
                });

//                alertDialog.getWindow().setBackgroundDrawableResource(R.color.editTextColor);

            alertDialog.show();}
            else if (responce.equals("remove")){


                LayoutInflater li = LayoutInflater.from(getActivity());
                View promptsView = li.inflate(R.layout.custom_download_dialog, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setView(promptsView);

                RelativeLayout userfield_vg = (RelativeLayout) promptsView.findViewById(R.id.userfield_vg);
                userfield_vg.setVisibility(View.GONE);

                Button saveButton = (Button) promptsView.findViewById(R.id.saveButton);
                saveButton.setText("Yes");
                Button cancelButton = (Button) promptsView.findViewById(R.id.cancelButton);
                cancelButton.setText("No");
                TextView dialog_text = (TextView) promptsView.findViewById(R.id.dialog_text);
                Typeface dialog_text_tf = Typeface.createFromAsset(getActivity().getAssets(), getActivity().getString(R.string.regular_fonts));
                dialog_text.setTypeface(dialog_text_tf);
                dialog_text.setText("Do You Want to Remove Playlist ?");
                final TextView userInput = (TextView) promptsView.findViewById(R.id.editTextDialogUserInput);
                Typeface dialoguserInput = Typeface.createFromAsset(getActivity().getAssets(), getActivity().getString(R.string.regular_fonts));
                userInput.setTypeface(dialoguserInput);
                final AlertDialog alertDialogadd = alertDialogBuilder.create();
                alertDialogadd.getWindow().setBackgroundDrawableResource(R.color.transparent);
                alertDialogadd.setCancelable(false);
                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // alertDialog.cancel;
                        alertDialogadd.hide();
                        PlayListCurrentId=intent.getStringExtra("playlistid");
                        AsyncDeletePlaylist asyncDeletePlaylist = new AsyncDeletePlaylist();
                        asyncDeletePlaylist.execute();
                    }
                });
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Do some thing
                        alertDialogadd.hide();


                    }
                });
                alertDialogadd.show();




              /*
                PlayListCurrentId=intent.getStringExtra("playlistid");
                AsyncDeletePlaylist asyncDeletePlaylist = new AsyncDeletePlaylist();
                asyncDeletePlaylist.execute();*/

            }
        }


    };
    @Override
    public void onDetach() {
        super.onDetach();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(OPTION_RESPONSE);

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v("Nihar_broadcast","onPause Called");
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(OPTION_RESPONSE);
    }




    private class AsynShowAllUserPlaylist  extends AsyncTask<String, Void, Void> {
        JSONObject myJson = null;
        int status;
        ProgressBarHandler pDialog;
        String responseStr;
        String sucessMsg;
        boolean responce;
        String playlist_poster,playlist_name,count;
        String title,movie_stream_id,poster = "";


        @Override
        protected void onPreExecute() {
            pDialog = new ProgressBarHandler(getActivity());
            pDialog.show();

        }

        @Override
        protected Void doInBackground(String... strings) {

            String urlRouteList = Util.rootUrl().trim() + Util.AllUserPlaylist.trim();

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("user_id", prefs.getString("useridPref", null));
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());

                } catch (org.apache.http.conn.ConnectTimeoutException e) {
                }
            }catch (Exception e){
            }
            if (responseStr != null) {
                try {
                    myJson = new JSONObject(responseStr);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    JSONObject jsonObject = null;
                    if (myJson != null){
                        try {
                            jsonObject = myJson.getJSONObject("msg");
                        }catch (Exception e){
                            Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                        }

                    }else {
                        Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();

                    }

                    JSONArray jsonArray = jsonObject.optJSONArray("userplaylist");
                    int lengthJsonArr = jsonArray.length();
                    ItemList.clear();
                    for (int i = 0; i < lengthJsonArr; i++) {
                        JSONObject jsonChildNode;
                        try {
                            jsonChildNode = jsonArray.getJSONObject(i);
                            if ((jsonChildNode.has("poster_playlist")) && jsonChildNode.getString("poster_playlist").trim() != null && !jsonChildNode.getString("poster_playlist").trim().isEmpty() && !jsonChildNode.getString("poster_playlist").trim().equals("null") && !jsonChildNode.getString("poster_playlist").trim().matches("")) {
                                playlist_poster =  jsonChildNode.getString("poster_playlist");
                            }
                            if ((jsonChildNode.has("list_name")) && jsonChildNode.getString("list_name").trim() != null && !jsonChildNode.getString("list_name").trim().isEmpty() && !jsonChildNode.getString("list_name").trim().equals("null") && !jsonChildNode.getString("list_name").trim().matches("")) {
                                playlist_name =  jsonChildNode.getString("list_name");
                            } if ((jsonChildNode.has("list_id")) && jsonChildNode.getString("list_id").trim() != null && !jsonChildNode.getString("list_id").trim().isEmpty() && !jsonChildNode.getString("list_id").trim().equals("null") && !jsonChildNode.getString("list_id").trim().matches("")) {
                                playlist_id =  jsonChildNode.getString("list_id");
                            }
                            if ((jsonChildNode.has("total_content")) && jsonChildNode.getString("total_content").trim() != null && !jsonChildNode.getString("total_content").trim().isEmpty() && !jsonChildNode.getString("total_content").trim().equals("null") && !jsonChildNode.getString("total_content").trim().matches("")) {
                                count =  jsonChildNode.getString("total_content");
                            }

                                ItemList.add(new PlayListModel(playlist_name,playlist_poster,playlist_id,count,responce));
                        } catch (Exception e) {
                        }
                    }
                    status = Integer.parseInt(myJson.optString("code"));
                    sucessMsg = myJson.optString("msg");
                } catch (Exception e) {
                    e.printStackTrace();

                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            if(pDialog.isShowing()&& pDialog!=null)
            {
                pDialog.hide();
            }

            adapter = new PlayListAdapter(getActivity(), ItemList, PlayListFragment.this);
            PlayListRecycleView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    private class AsyncEditPlaylistName  extends AsyncTask<String, Void, Void> {
        JSONObject myJson = null;
        int status;
        ProgressBarHandler pDialog;
        String responseStr;
        String sucessMsg;

        @Override
        protected void onPreExecute() {
            alertDialog.dismiss();
            pDialog = new ProgressBarHandler(getActivity());
            pDialog.show();


        }

        @Override
        protected Void doInBackground(String... strings) {

            String urlRouteList = Util.rootUrl().trim() + Util.PlayListNameEdit.trim();

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("user_id", prefs.getString("useridPref", null));
                httppost.addHeader("playlist_id",PlayListCurrentId );
                httppost.addHeader("playlist_name",PlayListName );
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());

                } catch (org.apache.http.conn.ConnectTimeoutException e) {
                }
            }catch (Exception e){
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

            if(pDialog.isShowing()&& pDialog!=null)
            {
                pDialog.hide();
            }
            AsynShowAllUserPlaylist asynShowAllUserPlaylist = new AsynShowAllUserPlaylist();
            asynShowAllUserPlaylist.execute();
            Toast.makeText(getActivity(), ""+sucessMsg, Toast.LENGTH_SHORT).show();
        }
    }

    private class AsyncAddToPlaylist extends AsyncTask<String, Void, Void> {
        JSONObject myJson = null;
        int status;
        ProgressBarHandler pDialog;
        String responseStr;
        String sucessMsg,code;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressBarHandler(getActivity());
            pDialog.show();


        }

        @Override
        protected Void doInBackground(String... strings) {

            String urlRouteList = Util.rootUrl().trim() + Util.AddToPlaylist .trim();

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("user_id", prefs.getString("useridPref", null));
                Log.v("Nihar_ggg",prefs.getString("useridPref", null) + "PlayListName"+PlayListName);
                httppost.addHeader("playlistname",PlayListName.trim());
                httppost.addHeader("is_episode","0" );
                httppost.addHeader("is_content","1" );

                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());
                    Log.v("Nihar_ggg",responseStr);


                } catch (org.apache.http.conn.ConnectTimeoutException e) {
                }
            }catch (Exception e){
            }
            if (responseStr != null) {
                try {
                    myJson = new JSONObject(responseStr);
                    sucessMsg = myJson.optString("msg");
                    code = myJson.optString("code");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            if(pDialog.isShowing()&& pDialog!=null)
            {
                pDialog.hide();
            }
            AsynShowAllUserPlaylist asynShowAllUserPlaylist = new AsynShowAllUserPlaylist();
            asynShowAllUserPlaylist.execute();
            if (code.equals("405")){
                Toast.makeText(getActivity(), "Sorry ,Try Again.", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getActivity(), ""+sucessMsg, Toast.LENGTH_SHORT).show();

            }
        }
    }

    private class AsyncDeletePlaylist extends AsyncTask<String, Void, Void> {
        JSONObject myJson = null;
        int status;
        ProgressBarHandler pDialog;
        String responseStr;
        String sucessMsg;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressBarHandler(getActivity());
            pDialog.show();


        }

        @Override
        protected Void doInBackground(String... strings) {

            String urlRouteList = Util.rootUrl().trim() + Util.DeletePlaylist.trim();

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("user_id", prefs.getString("useridPref", null));
                httppost.addHeader("playlist_id",PlayListCurrentId );
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());

                } catch (org.apache.http.conn.ConnectTimeoutException e) {
                }
            }catch (Exception e){
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

            if(pDialog.isShowing()&& pDialog!=null)
            {
                pDialog.hide();
            }
            AsynShowAllUserPlaylist asynShowAllUserPlaylist = new AsynShowAllUserPlaylist();
            asynShowAllUserPlaylist.execute();
            Toast.makeText(getActivity(), ""+sucessMsg, Toast.LENGTH_SHORT).show();
        }
    }


}
