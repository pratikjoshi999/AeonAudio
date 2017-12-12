package com.release.aeonaudio.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.release.aeonaudio.R;
import com.release.aeonaudio.adapter.MyDownloadAdapter;
import com.release.aeonaudio.model.ContactModel1;
import com.release.aeonaudio.model.QueueModel;
import com.release.aeonaudio.service.MusicService;
import com.release.aeonaudio.utils.Constants;
import com.release.aeonaudio.utils.DBHelper;
import com.release.aeonaudio.utils.ProgressBarHandler;
import com.release.aeonaudio.utils.Util;

import java.util.ArrayList;

import static android.R.attr.name;
import static com.release.aeonaudio.utils.Util.QUEUE_ARRAY;
import static java.lang.Thread.sleep;

public class MyDownloads extends AppCompatActivity {

    Context context;
    ListView list;
    TextView noDataTextView;
    RelativeLayout nodata;
    SharedPreferences prefs;
    String emailIdStr,pathh,titles,gen,tok,contentid,muviid,post,filename;
    DBHelper dbHelper;
    ArrayList<ContactModel1> download;
    ProgressBarHandler pDialog;
    MyDownloadAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_downloads);

        final Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        list= (ListView)findViewById(R.id.listView);
        nodata= (RelativeLayout) findViewById(R.id.noData);
        noDataTextView= (TextView) findViewById(R.id.noDataTextView);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_icon_close));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setIcon(R.drawable.logo);
        getSupportActionBar().setTitle("");

        dbHelper=new DBHelper(MyDownloads.this);

        registerReceiver(UpadateDownloadList, new IntentFilter("NewVodeoAvailable"));

        prefs = getSharedPreferences(Util.LOGINPREFERENCE, MODE_PRIVATE);
        emailIdStr = prefs.getString("emailPref", null);

        download=dbHelper.getContactt(emailIdStr,1);

        if(download.size()>0) {
            adapter = new MyDownloadAdapter(MyDownloads.this, android.R.layout.simple_dropdown_item_1line, download);
            list.setAdapter(adapter);
        }else {
            nodata.setVisibility(View.VISIBLE);
            noDataTextView.setText(Util.getTextofLanguage(MyDownloads.this,Util.NO_CONTENT,Util.DEFAULT_NO_CONTENT));
        }

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,final int position, long id) {

                pDialog = new ProgressBarHandler(MyDownloads.this);
                pDialog.show();

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        SQLiteDatabase DB = MyDownloads.this.openOrCreateDatabase("DOWNLOADMANAGER_ONDEMAND.db", MODE_PRIVATE, null);
                        Cursor cursor = DB.rawQuery("SELECT LANGUAGE,PATH FROM SUBTITLE_ONDEMAND WHERE UID = '"+download.get(position).getUniqueId()+"'", null);
                        int count = cursor.getCount();


                        Log.v("pratiko","count="+count);

                        pathh=download.get(position).getPath();
                      titles=download.get(position).getMUVIID();
                         gen=download.get(position).getGenere();
                        tok=download.get(position).getToken();
                         contentid=download.get(position).getContentid();
                         muviid=download.get(position).getMuviid();
                         post=download.get(position).getPoster();

                        final String vidduration=download.get(position).getDuration();
                         filename=pathh.substring(pathh.lastIndexOf("/") + 1);

                        Log.v("pratiko","path="+pathh);
                        Log.v("pratiko","titles="+titles);
                        Log.v("pratiko","gen="+gen);
                        Log.v("pratiko","tok="+tok);
                        Log.v("pratiko","contentid="+contentid);
                        Log.v("pratiko","muviid="+muviid);
                        Log.v("pratiko","vidduration="+vidduration);
                        Log.v("pratiko","post="+post);
                        Log.v("pratiko","filename="+filename);


                        try{

                            sleep(1200);

//                            Toast.makeText(MyDownloads.this, "Audio playing", Toast.LENGTH_SHORT).show();




                            runOnUiThread(new Runnable() {


                                @Override
                                public void run() {


                                    Player_State(1);

                                    runOnUiThread(new Runnable() {

                                        @Override
                                        public void run() {


                                            if (pDialog != null && pDialog.isShowing()) {
                                                pDialog.hide();
                                                pDialog = null;
                                            }


                                        }
                                    });


                                }




                            });
                            finish();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                }).start();

            }
        });
    }


    public void visible(){

        if(download.size()>0) {
            adapter = new MyDownloadAdapter(MyDownloads.this, android.R.layout.simple_dropdown_item_1line, download);
            list.setAdapter(adapter);

        }else {

            nodata.setVisibility(View.VISIBLE);
            noDataTextView.setText(Util.getTextofLanguage(MyDownloads.this,Util.NO_CONTENT,Util.DEFAULT_NO_CONTENT));
        }


    }

    private BroadcastReceiver UpadateDownloadList = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Log.v("BIBHU1","Onreceive called");

              /*  Intent intent1 = new Intent(MyDownloads.this,MyDownloads.class);
                startActivity(intent1);
                finish();*/

            download=dbHelper.getContactt(emailIdStr,1);
            //download=dbHelper.getDownloadcontent(emailIdStr);
            if(download.size()>0) {
                adapter = new MyDownloadAdapter(MyDownloads.this, android.R.layout.simple_dropdown_item_1line, download);
                list.setAdapter(adapter);
                nodata.setVisibility(View.GONE);
            }

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(UpadateDownloadList);
    }


    public void Player_State(int funId) {


        Intent playerData = new Intent("PLAYER_DETAIL");
        playerData.putExtra("songName", titles);
        Log.v("pratiko", "album_name==" + titles);
        Log.v("pratiko", "in player_state");
        Log.v("pratiko", "song_url*********=" + pathh);
        Log.v("pratiko", "" + post);


//        Toast.makeText(this, "song url="+song_url+"\n"+"song_imageUrl="+song_imageUrl+"\n"+"song_name="+song_name, Toast.LENGTH_SHORT).show();

        LocalBroadcastManager.getInstance(this).sendBroadcast(playerData);


       /* String DEL_QRY = "DELETE FROM " + Util.USER_TABLE_NAME + "";
        DB.execSQL(DEL_QRY);

        String INS_QRY = "INSERT INTO " + Util.USER_TABLE_NAME + "(ALBUM_ART_PATH,ALBUM_SONG_NAME) VALUES ( '" + song_imageUrl + "','" + song_name + "')";
        DB.execSQL(INS_QRY);*/


        QUEUE_ARRAY.add(new QueueModel(titles,post, pathh, gen));

        Intent j = new Intent(MyDownloads.this, MusicService.class);
        j.putExtra("ALBUM", pathh);
//        j.putExtra("PERMALINK", desired_string);
        j.putExtra("ALBUM_ART", post);
        j.putExtra("Artist", gen);
        j.putExtra("ALBUM_NAME", name);
        j.putExtra("ALBUM_SONG_NAME", titles);
        j.putExtra("STATE", funId);
        j.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);

        startService(j);


//        Log.v("pratikc", "" + mCastSession);

    }
}
