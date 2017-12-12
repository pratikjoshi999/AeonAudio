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
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
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
import static com.androidquery.util.AQUtility.getContext;
import static com.release.aeonaudio.R.id.downloadLayout;
import static com.release.aeonaudio.utils.Util.QUEUE_ARRAY;

/**
 * Created by Muvi on 6/23/2017.
 */

public class SingleDataAdaptor extends RecyclerView.Adapter<SingleDataAdaptor.ItemHolder> {

    private ContentDetailsOutput itemsList;
    private Context mContext;
    Fragment fragment = null;
    Fragment fragment2 = null;
    private ListFragment listFragment;
    private MultiPartFragment multiPartFragment;
    boolean itemClicked = true;
    ArrayList<ContentDetailsOutput> list;
    View sheetView;
    BottomSheetDialog mBottomSheetDialog;
    /***********offline***********/
    SharedPreferences prefs;
    ItemHolder holder2;
    DownloadManager downloadManager;
    public Handler downloadHandler;
    DBHelper dbHelper;
    ContactModel1 contactId,audio;
    String emailIdStr,audioUrl,audioTitle,fileExtenstion;
    public long enqueue;
    public boolean downloading;
    int lenghtOfFile;
    int lengthfile;
    ContentDetailsOutput contentDetailsOutputModel;
    Episode_Details_output episode_details_outputModel;
    private static final int REQUEST_STORAGE = 1;
    String userIdStr;
    /***********offline***********/


    public SingleDataAdaptor(Context context, ContentDetailsOutput itemsList, MultiPartFragment multiPartFragment) {
        this.itemsList = itemsList;
        this.mContext = context;
        this.multiPartFragment = multiPartFragment;

        prefs = mContext.getSharedPreferences(Util.LOGINPREFERENCE, MODE_PRIVATE);
        emailIdStr = prefs.getString("emailPref", null);
        /******************offline********************/
        downloadManager = (DownloadManager) mContext.getSystemService(DOWNLOAD_SERVICE);
        downloadHandler=new Handler();
        dbHelper = new DBHelper(mContext);
        dbHelper.getWritableDatabase();

        contactId = dbHelper.getContact(itemsList.getMovie_stream_id() + emailIdStr);
        if (contactId != null) {
            if (contactId.getUSERNAME().trim().equals(emailIdStr.trim())) {
                checkDownLoadStatusFromDownloadManager1(contactId);
            }
        }
        /******************offline********************/
    }





    @Override
    public ItemHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listdata_item, null);
        ItemHolder mh = new ItemHolder(v);

        return mh;
    }

    @Override
    public void onBindViewHolder(final ItemHolder holder,  int i) {
        Log.v("StreamNihar",""+itemsList.getMovieStreamUniqId());
        this.holder2 = holder;
        try {
            SQLiteDatabase DB = mContext.openOrCreateDatabase(Util.DATABASE_NAME, MODE_PRIVATE, null);

            String DEL_QRY = "DELETE FROM " + Util.ADAPTOR_TABLE_NAME +"";
            DB.execSQL(DEL_QRY);

            String INS_QRY = "INSERT INTO " + Util.ADAPTOR_TABLE_NAME + "(ALBUM_URL,PERMALINK,ALBUM_ART,ALBUM_NAME,ALBUM_SONG_NAME)" +
                    " VALUES ( '" + itemsList.getMovieUrl() + "','" + itemsList.getPermalink() + "','" + itemsList.getPoster() + "'," +
                    "'" + "" + "','" + itemsList.getName() + "')";
            DB.execSQL(INS_QRY);


            holder.list_songName.setText(itemsList.getName());
            holder.list_artistName.setText(itemsList.getArtist());
            holder.listsong_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    QUEUE_ARRAY.clear();
                    QUEUE_ARRAY.add(new QueueModel(itemsList.getName(), itemsList.getPoster(), itemsList.getMovieUrl(),itemsList.getArtist()));

                    holder.list_songName.setTextColor(mContext.getResources().getColor(R.color.button_background));
                    holder.list_artistName.setTextColor(mContext.getResources().getColor(R.color.button_background));
                    multiPartFragment.PlaySongs(itemsList,itemClicked);

                }
            });
            String link = itemsList.getPoster();
            holder.list_option_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent playerData = new Intent("OPTION_MENU");
                    playerData.putExtra("sender","SinglepartContent");
                    playerData.putExtra("option", itemsList.getName());
                    playerData.putExtra("movie_stream_id", itemsList.getMovie_stream_id());
                    playerData.putExtra("isEpisode", "0");
                    playerData.putExtra("MusicAlbumArt",  itemsList.getPoster());
                    playerData.putExtra("MusicUrl",  itemsList.getMovieUrl());
                    playerData.putExtra("ArtistUrl",  itemsList.getArtist());
                    playerData.putExtra("artist_opt", itemsList.getArtist());
                    Log.v("nihar_exception",""+itemsList.getArtist());

                    (mContext).sendBroadcast(playerData);
                }
            });


            holder.download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    multiPartFragment.DownloadClicked(true,itemsList);

//                    showPopup(view);
                    new DownloadFileFromURL().execute(itemsList.getMovieUrl());

                }
            });
            ////
            holder.percentage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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
                                    downloading=false;
                                    audio = dbHelper.getContact(itemsList.getMovie_stream_id() + emailIdStr);

                                    if (audio != null) {
                                        downloadManager.remove(audio.getDOWNLOADID());
                                        dbHelper.deleteRecord(audio);
                                    }


//                                    downloadHandler=new Handler();
                                    downloadHandler.post(new Runnable() {
                                        @Override
                                        public void run() {

                                            Log.v("tago","download cancelling...");
                                            holder2.progressBar.setProgress((int) 0);
                                            holder2.percentage.setVisibility(View.GONE);
                                            holder2.download.setVisibility(View.VISIBLE);

                                        }
                                    });

                                    Toast.makeText(mContext, "Download Cancelled", Toast.LENGTH_SHORT).show();

                                }
                            });

                    dlgAlert.create().show();
                }
            });
            /////
            Picasso.with(mContext)
                    .load(link)
                    .into(holder.list_albumart);
        }catch (Exception e ){
            Log.v("nihar_exception",""+e.toString());

        }

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    ///swipe left and right


//    public void removeItem(int position) {
//        itemsList.remove(position);
//        notifyItemRemoved(position);
//        notifyItemRangeChanged(position, itemsList.size());
//    }




    public class ItemHolder extends RecyclerView.ViewHolder {
        private TextView list_songName,percentage;
        private TextView list_artistName;
        private ImageView list_albumart,list_option_menu,download;
        private RelativeLayout listsong_layout,downloadLayout;
        private ProgressBar progressBar;


        public ItemHolder(View view) {
            super(view);
            this.listsong_layout = (RelativeLayout) view.findViewById(R.id.listsong_layout);
            this.downloadLayout = (RelativeLayout) view.findViewById(R.id.downloadLayout);
            this.list_songName = (TextView) view.findViewById(R.id.list_songName);
            Typeface list_songName_tf = Typeface.createFromAsset(mContext.getAssets(), mContext.getString(R.string.regular_fonts));
            list_songName.setTypeface(list_songName_tf);

            this.list_artistName = (TextView) view.findViewById(R.id.list_artistName);
            Typeface list_artistName_tf = Typeface.createFromAsset(mContext.getAssets(), mContext.getString(R.string.light_fonts));
            list_artistName.setTypeface(list_artistName_tf);

            this.list_albumart = (ImageView) view.findViewById(R.id.list_albumart);
          this.list_option_menu = (ImageView) view.findViewById(R.id.list_option_menu);
            if ((Util.getTextofLanguage(mContext, Util.HASDOWNLOAD, Util.DEFAULT_HASDOWNLOAD)
                    .trim()).equals("1")) {
                downloadLayout.setVisibility(View.VISIBLE);
            }else{
                downloadLayout.setVisibility(View.GONE);
            }

            this.download = (ImageView) view.findViewById(R.id.download);
            this.progressBar = (ProgressBar) view.findViewById(R.id.downloadProgress);
            this.percentage = (TextView) view.findViewById(R.id.percentage);

//            mSlider = (RelativeLayout) view.findViewById(R.id.mSlider);


//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//
//                    fragment2 = new ListFragment();
//                    if (fragment == null){
//                        FragmentManager fragmentManager = ((AppCompatActivity)mContext).getSupportFragmentManager();
//                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                        fragmentTransaction.replace(R.id.home_content,fragment2).addToBackStack("MuliFragment");
//                        fragmentTransaction.commit();
//
//                        // set the toolbar title
////                        getSupportActionBar().setTitle(title);
//                    }
//
//                }
//            });
        }
    }


    class DownloadFileFromURL extends AsyncTask<String,String,String> {

        ProgressBarHandler pDialog;
        String responseStr;

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
//            try {
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
                            downloadFile();
                        }

                    }
                });
                alertDialogadd.show();


            /*}catch (Exception e){
                Log.v("Nihar",e.toString());
                Toast.makeText(mContext,"Error occured!",Toast.LENGTH_SHORT).show();
            }*/

        }
    }
    private void requestStoragePermission() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE);
        } else {
            downloadFile();
        }
    }
    private void downloadFile(){


        DownloadManager.Request request;

        request = new DownloadManager.Request(Uri.parse(itemsList.getMovieUrl()));

        request.setTitle(itemsList.getName());
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
//        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        fileExtenstion = MimeTypeMap.getFileExtensionFromUrl(itemsList.getMovieUrl());

        String timestamp = System.currentTimeMillis() + ".exo";
        request.setDestinationInExternalPublicDir("Android/data/" + mContext.getApplicationContext().getPackageName().trim() + "/WITHOUT_DRM", timestamp);
        enqueue = downloadManager.enqueue(request);

        holder2.download.setVisibility(View.GONE);
        holder2.percentage.setVisibility(View.VISIBLE);
        holder2.progressBar.setProgress(0);

        ContactModel1 contactModel1 = new ContactModel1();
        contactModel1.setMUVIID(itemsList.getName());
        contactModel1.setDOWNLOADID((int) enqueue);
        contactModel1.setProgress(0);
        contactModel1.setUSERNAME(emailIdStr);

        contactModel1.setUniqueId(itemsList.getMovie_stream_id() + emailIdStr);
        Log.v("pratiko","in download suid="+itemsList.getMovie_stream_id()+emailIdStr);

        contactModel1.setDSTATUS(2);

        contactModel1.setPoster(itemsList.getPoster().trim());
        contactModel1.setToken(fileExtenstion);
        contactModel1.setPath(Environment.getExternalStorageDirectory() + "/Android/data/" + mContext.getApplicationContext().getPackageName().trim() + "/WITHOUT_DRM/" + timestamp);
        contactModel1.setContentid(String.valueOf(itemsList.getContentTypesId()));
        contactModel1.setGenere(itemsList.getArtist().trim());
        contactModel1.setMuviid(itemsList.getMovie_stream_id().trim());
//        contactModel1.setDuration(itemsList.getVideoDuration().trim());
        dbHelper.insertRecord(contactModel1);

        audio = dbHelper.getContact(itemsList.getMovie_stream_id() + emailIdStr);
        if (audio != null) {
            if (audio.getUSERNAME().trim().equals(emailIdStr.trim())) {
                checkDownLoadStatusFromDownloadManager1(audio);
            }
        }
    }
    public void checkDownLoadStatusFromDownloadManager1(final ContactModel1 model) {


        Log.v("pratiko","dwld model=="+model.getDOWNLOADID());
        if (model.getDOWNLOADID() != 0) {
            new Thread(new Runnable() {

                @Override
                public void run() {

                    downloading = true;
                    int bytes_downloaded = 0;
                    int bytes_total = 0;
                    downloadManager = (DownloadManager) mContext.getSystemService(DOWNLOAD_SERVICE);
                    Log.v("pratiko","dwld model== run...");
                    while (downloading) {

                        Log.v("pratiko","while downloading...");
                        DownloadManager.Query q = new DownloadManager.Query();
                        q.setFilterById(model.getDOWNLOADID()); //filter by id which you have receieved when reqesting download from download manager
                        Cursor cursor = downloadManager.query(q);



                        if (cursor != null && cursor.getCount() > 0) {
                            if (cursor.moveToFirst()) {
                                int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                                int status = cursor.getInt(columnIndex);
                                Log.v("tago","status====="+status);
                                if (status == DownloadManager.STATUS_SUCCESSFUL) {

                                    model.setDSTATUS(1);
                                    dbHelper.updateRecord(model);

                                    Intent intent = new Intent("NewVodeoAvailable");
                                    mContext.sendBroadcast(intent);
                                    downloading = false;

                                } else if (status == DownloadManager.STATUS_FAILED) {
                                    // 1. process for download fail.
                                    model.setDSTATUS(0);
                                    Log.v("tago","Status failed");
                                    holder2.progressBar.setProgress(0);
                                    holder2.percentage.setVisibility(View.GONE);
                                    holder2.download.setVisibility(View.VISIBLE);

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

                        ((AppCompatActivity)mContext).runOnUiThread(new Runnable() {
                            //
                            @Override
                            public void run() {

                                if (holder2!=null) {
                                    holder2.download.setVisibility(View.GONE);
                                    holder2.percentage.setVisibility(View.VISIBLE);

                                    holder2.progressBar.setProgress((int) model.getProgress());
                                    holder2.percentage.setText(model.getProgress() + "%");

                                    if (model.getProgress() == 100) {

                                        holder2.download.setVisibility(View.GONE);
                                        holder2.percentage.setVisibility(View.GONE);
                                        holder2.progressBar.setVisibility(View.GONE);

                                    }
                                }
                            }
                        });


                        cursor.close();
                    }
                }
            }).start();
        }
    }


    }

