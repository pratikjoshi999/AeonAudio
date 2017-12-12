package com.release.aeonaudio.adapter;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.home.apisdk.apiModel.ContentDetailsOutput;
import com.home.apisdk.apiModel.Episode_Details_output;
import com.release.aeonaudio.R;
import com.release.aeonaudio.activity.ListFragment;
import com.release.aeonaudio.activity.MainActivity;
import com.release.aeonaudio.activity.MultiPartFragment;
import com.release.aeonaudio.model.ContactModel1;
import com.release.aeonaudio.model.PlayListMultiModel;
import com.release.aeonaudio.model.QueueModel;
import com.release.aeonaudio.utils.DBHelper;
import com.release.aeonaudio.utils.ProgressBarHandler;
import com.release.aeonaudio.utils.Util;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import static android.content.Context.DOWNLOAD_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static android.media.CamcorderProfile.get;
import static com.release.aeonaudio.R.id.downloadLayout;
import static com.release.aeonaudio.utils.Util.QUEUE_ARRAY;

/**
 * Created by Muvi on 6/23/2017.
 */

public class MultiDataAdaptor extends RecyclerView.Adapter<MultiDataAdaptor.ItemHolder> {

    private Episode_Details_output itemsList;
    private Context mContext;
    Fragment fragment = null;
    Fragment fragment2 = null;
    private ListFragment listFragment;
    private MultiPartFragment multiPartFragment;
    boolean itemClicked = true;
    ArrayList<ContentDetailsOutput> list;
    ArrayList<Episode_Details_output> episode_details_output;
    ArrayList<PlayListMultiModel> ItemListDetails;
    int adaptorPosition = -1;
    int prevPosition =  -1;
    int position;
    ItemHolder myholder;
    String artist_multi_data;
    /***********offline***********/
    ArrayList<ItemHolder> hoder = new ArrayList<>();
    ItemHolder holder2;
    SharedPreferences prefs;
    DownloadManager downloadManager;
    public Handler downloadHandler;
    DBHelper dbHelper;
    ContactModel1 contactId,audio;
    String emailIdStr,audioUrl,audioTitle,fileExtenstion,option_value;
    public long enqueue;
    Boolean nowDownloading;
    int lenghtOfFile;
    int lengthfile;
    ContentDetailsOutput contentDetailsOutputModel;
    Episode_Details_output episode_details_outputModel;
    private static final int REQUEST_STORAGE = 1;

    /***********offline***********/


    public MultiDataAdaptor(Context context, ArrayList<Episode_Details_output> episode_details_output, MultiPartFragment multiPartFragment, String artist_multi,String option_value) {
        this.episode_details_output = episode_details_output;
        this.mContext = context;
        this.multiPartFragment = multiPartFragment;
        this.artist_multi_data=artist_multi;
        this.option_value=option_value;
        ///download/////
        prefs = mContext.getSharedPreferences(Util.LOGINPREFERENCE, MODE_PRIVATE);
        emailIdStr = prefs.getString("emailPref", null);

        for(int k=0;k<episode_details_output.size();k++) {
            downloadManager = (DownloadManager) mContext.getSystemService(DOWNLOAD_SERVICE);
            downloadHandler = new Handler();
            dbHelper = new DBHelper(mContext);
            dbHelper.getWritableDatabase();


            try{
                contactId = dbHelper.getContact(episode_details_output.get(k).getId() + emailIdStr);
                Log.v("pratiko","contactId=="+contactId);
                if (contactId != null) {
                    if (contactId.getUSERNAME().trim().equals(emailIdStr.trim())) {
                        nowDownloading=true;
                        checkDownLoadStatusFromDownloadManager1(contactId,k);
                    }
                }
            }catch (Exception e){

            }

        }
    }

    public MultiDataAdaptor(FragmentActivity activity, ArrayList<PlayListMultiModel> itemListDetails, MultiPartFragment multiPartFragment, String mutiArtist,String option_value) {
        this.ItemListDetails = itemListDetails;
        this.mContext = activity;
        this.multiPartFragment = multiPartFragment;
        this.artist_multi_data=mutiArtist;
        this.option_value= option_value;

    }



    @Override
    public ItemHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listdata_item, null);
        ItemHolder mh = new ItemHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(final ItemHolder holder,   final int i) {
        this.holder2 = holder;

        episode_details_outputModel=episode_details_output.get(i);
        itemsList=episode_details_output.get(i);
        hoder.add(holder);

        final Episode_Details_output multi_song_list_adaptor =  episode_details_output.get(i);

        SQLiteDatabase DB = mContext.openOrCreateDatabase(Util.DATABASE_NAME, MODE_PRIVATE, null);

        String DEL_QRY = "DELETE FROM " + Util.ADAPTOR_TABLE_NAME +"";
        DB.execSQL(DEL_QRY);

        String INS_QRY = "INSERT INTO " + Util.ADAPTOR_TABLE_NAME + "(ALBUM_URL,PERMALINK,ALBUM_ART,ALBUM_NAME,ALBUM_SONG_NAME)" +
                " VALUES ( '" + multi_song_list_adaptor.getVideo_url() + "','" + multi_song_list_adaptor.getPermalink() + "','" + multi_song_list_adaptor.getPoster_url() + "'," +
                "'" + "" + "','" + multi_song_list_adaptor.getName() + "')";
        DB.execSQL(INS_QRY);
       // itemsList = episode_details_output.get(i);

        holder.list_songName.setText(multi_song_list_adaptor.getEpisode_title());
        holder.artist_multi.setText(artist_multi_data);
        Log.v("Nihar_artist","adaptor"+artist_multi_data);
        if(adaptorPosition!=-1 )
        {
            holder.list_songName.setTextColor(mContext.getResources().getColor(R.color.button_background));
            holder.artist_multi.setTextColor(mContext.getResources().getColor(R.color.button_background));
            adaptorPosition = -1;
        }
        else{
            if(prevPosition != -1 && i == prevPosition )
            {
                //change color like
                holder.list_songName.setTextColor(mContext.getResources().getColor(R.color.button_background));
                holder.artist_multi.setTextColor(mContext.getResources().getColor(R.color.button_background));
            }

            else
            {
                //revert back to regular color
                holder.list_songName.setTextColor(Color.WHITE);
                holder.artist_multi.setTextColor(Color.WHITE);
            }
        }
        Util.ArtistName = artist_multi_data;
        Util.SongNameGlobal = multi_song_list_adaptor.getEpisode_title();
        holder.list_option_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent playerData = new Intent("OPTION_MENU");
                playerData.putExtra("sender",option_value);
                playerData.putExtra("option", multi_song_list_adaptor.getEpisode_title());
                playerData.putExtra("artist_opt", artist_multi_data);
                playerData.putExtra("movie_stream_id", multi_song_list_adaptor.getId());
                playerData.putExtra("movie_stream_id_playlist", multi_song_list_adaptor.getMuvi_uniq_id());
                playerData.putExtra("isEpisode", "1");
                playerData.putExtra("MusicAlbumArt",  multi_song_list_adaptor.getPoster_url());
                playerData.putExtra("MusicUrl",  multi_song_list_adaptor.getVideo_url());
                playerData.putExtra("ArtistUrl",  artist_multi_data);
                Log.v("Nihar_id",multi_song_list_adaptor.getId());
                (mContext).sendBroadcast(playerData);
            }
        });
        holder.listsong_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                multiPartFragment.PlaySongsmulti(multi_song_list_adaptor,itemClicked,holder.getAdapterPosition());
                QUEUE_ARRAY.clear();
                QUEUE_ARRAY.add(new QueueModel(multi_song_list_adaptor.getEpisode_title(), multi_song_list_adaptor.getPoster_url(), multi_song_list_adaptor.getVideo_url(),artist_multi_data));
                prevPosition = i;
//               notifyItemRangeChanged(0, episode_details_output.size());
                /*  if (audio.getProgress() == 100) {


                    hoder.get(audio.getPosition()).download.setVisibility(View.GONE);
                    hoder.get(audio.getPosition()).percentage.setVisibility(View.GONE);
                    hoder.get(audio.getPosition()).progressBar.setVisibility(View.GONE);
                }
*/
            }
        });


//        if (currentWord.hasImage()) {
        String link = multi_song_list_adaptor.getPoster_url();
//        String link = "https:\\/\\/d2sal5lpzsf102.cloudfront.net\\/5622\\/public\\/public\\/system\\/posters\\/76283\\/standard\\/actionable_1496478219.jpg";
//        Glide.with(mContext).load(link).asBitmap().centerCrop().into(new BitmapImageViewTarget(holder.list_albumart) {
//            @Override
//            protected void setResource(Bitmap resource) {
//                RoundedBitmapDrawable circularBitmapDrawable =
//                        RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
//                circularBitmapDrawable.setCircular(true);
//                holder.list_albumart.setImageDrawable(circularBitmapDrawable);
//            }
//        });
        Log.v("Nihar_flow",link);
        Picasso.with(mContext)
                .load(link)
                .into(holder.list_albumart);


        if (i == episode_details_output.size()-1){
         holder.listDummy.setVisibility(View.VISIBLE);
           // holder.dividerView.setVisibility(View.VISIBLE);

        }else{
            holder.listDummy.setVisibility(View.GONE);
          //  holder.dividerView.setVisibility(View.VISIBLE);

        }

//        holder.itemImage.setImageResource();


        /******************offline********************/


        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                multiPartFragment.DownloadClickedMulti(true,multi_song_list_adaptor);
                boolean downloading;
                nowDownloading=true;
                itemsList=episode_details_output.get(i);
                position=i;
                new DownloadFileFromURL().execute(itemsList.getVideo_url());
                Log.v("pratiko","url=="+itemsList.getVideo_url());
                Log.v("pratiko","url=="+itemsList.getEpisode_title());
                Log.v("pratiko","musuid=="+itemsList.getMovie_stream_uniq_id());
                Log.v("pratiko","musuid=="+itemsList.getGenre());
                Log.v("pratiko","musuid=="+itemsList.getId());
            }
        });


        holder.percentage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final boolean[] downloading = new boolean[1];
                position=i;
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(mContext, R.style.MyAlertDialogStyle);
                dlgAlert.setTitle("Stop saving this video");
                dlgAlert.setMessage("Your video can not be saved");
                dlgAlert.setPositiveButton("Keep", null);
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton("Keep",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();

                            }
                        });
                dlgAlert.setNegativeButton("Discard", null);
                dlgAlert.setCancelable(false);
                dlgAlert.setNegativeButton("Discard",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                downloading[0] = false;
                                nowDownloading=false;
                                audio = dbHelper.getContact(episode_details_output.get(position).getId() + emailIdStr);

                                if (audio != null) {
                                    downloadManager.remove(audio.getDOWNLOADID());
                                    dbHelper.deleteRecord(audio);
                                }


                                downloadHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        hoder.get(position).progressBar.setProgress((int) 0);
                                        hoder.get(position).percentage.setVisibility(View.GONE);
                                        hoder.get(position).download.setVisibility(View.VISIBLE);
                                    }
                                });

                                Toast.makeText(mContext, "Download Cancelled", Toast.LENGTH_SHORT).show();

                            }
                        });

                dlgAlert.create().show();
            }
        });
        /******************offline********************/



    }

    @Override
    public int getItemCount() {
        return episode_details_output.size();
    }

    ///swipe left and right


//    public void removeItem(int position) {
//        itemsList.remove(position);
//        notifyItemRemoved(position);
//        notifyItemRangeChanged(position, itemsList.size());
//    }




    public class ItemHolder extends RecyclerView.ViewHolder {
        private TextView list_songName,listDummy,percentage;
        private TextView artist_multi;
        private ImageView list_albumart,list_option_menu,download;
        private RelativeLayout listsong_layout,downloadLayout;
        private View dividerView;
        ProgressBar progressBar;

        public ItemHolder(View view) {
            super(view);
            this.listsong_layout = (RelativeLayout) view.findViewById(R.id.listsong_layout);
            this.downloadLayout = (RelativeLayout) view.findViewById(R.id.downloadLayout);
            this.list_songName = (TextView) view.findViewById(R.id.list_songName);
            this.listDummy = (TextView) view.findViewById(R.id.listDummy);
            Typeface list_songName_tf = Typeface.createFromAsset(mContext.getAssets(), mContext.getString(R.string.regular_fonts));
            list_songName.setTypeface(list_songName_tf);
          this.artist_multi = (TextView) view.findViewById(R.id.list_artistName);
            Typeface artist_multi_tf = Typeface.createFromAsset(mContext.getAssets(), mContext.getString(R.string.regular_fonts));
            artist_multi.setTypeface(artist_multi_tf);
            this.list_albumart = (ImageView) view.findViewById(R.id.list_albumart);
            this.dividerView = (View) view.findViewById(R.id.divider);

            this.list_option_menu = (ImageView) view.findViewById(R.id.list_option_menu);

            if ((Util.getTextofLanguage(mContext, Util.HASDOWNLOAD, Util.DEFAULT_HASDOWNLOAD)
                    .trim()).equals("1")) {
                Log.v("Nihar_view","called"+Util.getTextofLanguage(mContext, Util.HAS_FAVORITE, Util.DEFAULT_HAS_FAVORITE).toString());
                downloadLayout.setVisibility(View.VISIBLE);
            }else{
                downloadLayout.setVisibility(View.GONE);
            }

            ////////offline*******************/
            this.download = (ImageView) view.findViewById(R.id.download);
            this.progressBar = (ProgressBar) view.findViewById(R.id.downloadProgress);
            this.percentage = (TextView) view.findViewById(R.id.percentage);



        }
    }
    class DownloadFileFromURL extends AsyncTask<String,String,String> {

        ProgressBarHandler pDialog;
        String responseStr;
        Boolean downloading;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressBarHandler(mContext);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... f_url) {

            try {
                URL url = new URL(f_url[0]);

                URLConnection conection = url.openConnection();
                conection.connect();
                lenghtOfFile = conection.getContentLength();
                lengthfile = lenghtOfFile / 1024 / 1024;
                Log.v("pratiko", "lof=" + lengthfile);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            try {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.hide();
                }

                String lengh = String.valueOf(lengthfile);

                LayoutInflater li = LayoutInflater.from(mContext);
                View promptsView = li.inflate(R.layout.custom_download_dialog, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                alertDialogBuilder.setView(promptsView);

                Button saveButton = (Button) promptsView.findViewById(R.id.saveButton);
                Button cancelButton = (Button) promptsView.findViewById(R.id.cancelButton);
                TextView dialog_text = (TextView) promptsView.findViewById(R.id.dialog_text);
                Typeface dialog_text_tf = Typeface.createFromAsset(mContext.getAssets(), mContext.getString(R.string.regular_fonts));
                dialog_text.setTypeface(dialog_text_tf);
                dialog_text.setText("Want to Download");
                final TextView userInput = (TextView) promptsView.findViewById(R.id.editTextDialogUserInput);
                Typeface dialoguserInput = Typeface.createFromAsset(mContext.getAssets(), mContext.getString(R.string.regular_fonts));
                userInput.setTypeface(dialoguserInput);
                userInput.setText(itemsList.getName() + " " + "("+lengh +"MB)");
                final AlertDialog alertDialogadd = alertDialogBuilder.create();
                alertDialogadd.getWindow().setBackgroundDrawableResource(R.color.transparent);
                alertDialogadd.setCancelable(false);
                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // alertDialog.cancel;
                        alertDialogadd.hide();
                    }
                });
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Do some thing
                        alertDialogadd.hide();
                        downloading = true;

                        int currentApiVersion = android.os.Build.VERSION.SDK_INT;
                        if (currentApiVersion >= Build.VERSION_CODES.M) {
                            requestStoragePermission();
                        } else {
                            downloadFile(true);
                        }

                    }
                });
                alertDialogadd.show();
            }catch (Exception e){
                Toast.makeText(mContext,"Error occured!",Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void requestStoragePermission() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE);
        } else {
            downloadFile(true);
        }
    }
    private void downloadFile(boolean singlefile){


        DownloadManager.Request request;

        request = new DownloadManager.Request(Uri.parse(itemsList.getVideo_url()));

        request.setTitle(itemsList.getEpisode_title());
//        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        fileExtenstion = MimeTypeMap.getFileExtensionFromUrl(itemsList.getVideo_url());

        String timestamp = System.currentTimeMillis() + ".exo";
        request.setDestinationInExternalPublicDir("Android/data/" + mContext.getApplicationContext().getPackageName().trim() + "/WITHOUT_DRM", timestamp);
        enqueue = downloadManager.enqueue(request);

        Log.v("pratiko","postn in dwldf="+position);
        hoder.get(position).download.setVisibility(View.GONE);
        hoder.get(position).percentage.setVisibility(View.VISIBLE);
        hoder.get(position).progressBar.setProgress(0);

        ContactModel1 contactModel1 = new ContactModel1();
        contactModel1.setMUVIID(itemsList.getEpisode_title());
        contactModel1.setDOWNLOADID((int) enqueue);
        contactModel1.setProgress(0);
        contactModel1.setUSERNAME(emailIdStr);

        contactModel1.setUniqueId(itemsList.getId() + emailIdStr);
        Log.v("pratiko","in download msuid="+itemsList.getId()+emailIdStr);

        contactModel1.setDSTATUS(2);

        contactModel1.setPoster(itemsList.getPoster_url().trim());
        contactModel1.setToken(fileExtenstion);
        contactModel1.setPath(Environment.getExternalStorageDirectory() + "/Android/data/" + mContext.getApplicationContext().getPackageName().trim() + "/WITHOUT_DRM/" + timestamp);
        contactModel1.setContentid("6");

        contactModel1.setGenere(itemsList.getName().trim());
        contactModel1.setMuviid(itemsList.getId().trim());
        contactModel1.setPosition(position);
//        contactModel1.setDuration(itemsList.getVideoDuration().trim());
        dbHelper.insertRecord(contactModel1);

        audio = dbHelper.getContact(itemsList.getId() + emailIdStr);
        if (audio != null) {
            if (audio.getUSERNAME().trim().equals(emailIdStr.trim())) {
                checkDownLoadStatusFromDownloadManager1(audio,100);
            }
        }
    }
    public void checkDownLoadStatusFromDownloadManager1(final ContactModel1 model, final int Position ) {



        final Boolean[] downloading = new Boolean[1];

        if (model.getDOWNLOADID() != 0) {
            new Thread(new Runnable() {

                @Override
                public void run() {

                    downloading[0] = true;
                    int bytes_downloaded = 0;
                    int bytes_total = 0;
                    downloadManager = (DownloadManager) mContext.getSystemService(DOWNLOAD_SERVICE);


                    while (downloading[0] && nowDownloading) {

                        DownloadManager.Query q = new DownloadManager.Query();
                        q.setFilterById(model.getDOWNLOADID()); //filter by id which you have receieved when reqesting download from download manager
                        Cursor cursor = downloadManager.query(q);

                        Log.v("pratiko", "poos==" + model.getPosition());

                        if (cursor != null && cursor.getCount() > 0) {
                            if (cursor.moveToFirst()) {
                                int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                                int status = cursor.getInt(columnIndex);
                                if (status == DownloadManager.STATUS_SUCCESSFUL) {

                                    model.setDSTATUS(1);
                                    try {
                                        dbHelper.updateRecord(model);
                                    } catch (Exception e) {

                                    }


                                    Intent intent = new Intent("NewVodeoAvailable");
                                    mContext.sendBroadcast(intent);
                                    downloading[0] = false;

                                } else if (status == DownloadManager.STATUS_FAILED) {
                                    // 1. process for download fail.
                                    model.setDSTATUS(0);


                                } else if ((status == DownloadManager.STATUS_PAUSED) ||
                                        (status == DownloadManager.STATUS_RUNNING)) {
                                    model.setDSTATUS(2);

                                } else if (status == DownloadManager.STATUS_PENDING) {
                                    //Not handling now
                                }
                                int sizeIndex = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
                                int downloadedIndex = cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
                                long size = cursor.getInt(sizeIndex);
                                long downloaded = cursor.getInt(downloadedIndex);
                                double progress = 0.0;
                                if (size != -1) progress = downloaded * 100.0 / size;
                                // At this point you have the progress as a percentage.
                                model.setProgress((int) progress);
                            }
                        }


                        ((MainActivity) mContext).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.v("pratiko", "in checkDownLoadStatusFromDownloadManager1");
                                if (Position == 100) {

                                }

                                hoder.get(model.getPosition()).download.setVisibility(View.GONE);
                                hoder.get(model.getPosition()).percentage.setVisibility(View.VISIBLE);

//                                if(Position==100)
                                hoder.get(model.getPosition()).progressBar.setProgress((int) model.getProgress());
                                hoder.get(model.getPosition()).percentage.setText(model.getProgress() + "%");

                                if (model.getProgress() == 100) {


                                    hoder.get(model.getPosition()).download.setVisibility(View.GONE);
                                    hoder.get(model.getPosition()).percentage.setVisibility(View.GONE);
                                    hoder.get(model.getPosition()).progressBar.setVisibility(View.GONE);

                                    /*if(Position==100) {
                                        Toast.makeText(mContext, "Download completed", Toast.LENGTH_SHORT).show();
                                        hoder.get(model.getPosition()).download.setVisibility(View.GONE);
                                        hoder.get(model.getPosition()).percentage.setVisibility(View.GONE);
                                        hoder.get(model.getPosition()).progressBar.setVisibility(View.GONE);
                                    }
                                    else{
                                        hoder.get(Position).download.setVisibility(View.GONE);
                                        hoder.get(Position).percentage.setVisibility(View.GONE);
                                        hoder.get(Position).progressBar.setVisibility(View.GONE);
                                    }*/
                                }/*else{

                                    hoder.get(model.getPosition()).download.setVisibility(View.VISIBLE);
                                    hoder.get(model.getPosition()).percentage.setVisibility(View.VISIBLE);
                                    hoder.get(model.getPosition()).progressBar.setVisibility(View.VISIBLE);

                                }*/
                            }
                        });


                        cursor.close();
                    }

                }
            }).start();
        }
    }

}
