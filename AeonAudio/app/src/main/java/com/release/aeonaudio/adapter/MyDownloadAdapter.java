package com.release.aeonaudio.adapter;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.release.aeonaudio.R;
import com.release.aeonaudio.activity.MyDownloads;
import com.release.aeonaudio.model.ContactModel1;
import com.release.aeonaudio.utils.DBHelper;
import com.release.aeonaudio.utils.Util;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * Created by Muvi on 1/16/2017.
 */
public class MyDownloadAdapter extends BaseAdapter {
    MyDownloads activity;
    ArrayList<ContactModel1> downloadModel;
    List<String[]> allElements;
    //CSVReader readers = null;
    String[] nextLine;
    SharedPreferences pref;
    String emailIdStr = "";
    ContactModel1 audio;
    //MydownloadModel mydownloadModel;
    DBHelper dbHelper;
    public boolean downloading;
    DownloadManager downloadManager;
    //Downloadlistdb downloadlistdb;
    public MyDownloadAdapter(MyDownloads activity, int simple_dropdown_item_1line, ArrayList<ContactModel1> downloadModel) {
        this.activity = activity;
        this.downloadModel = downloadModel;
        dbHelper = new DBHelper(activity);
        downloadManager = (DownloadManager) activity.getSystemService(DOWNLOAD_SERVICE);
        pref = activity.getSharedPreferences(Util.LOGIN_PREF, 0);
        if (pref != null) {
            emailIdStr = pref.getString("PREFS_LOGIN_EMAIL_ID_KEY", null);


        } else {
            emailIdStr = "";


        }



    }

    @Override
    public int getCount() {
        return downloadModel.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v;
        LayoutInflater inflater = activity.getLayoutInflater();
        v = inflater.inflate(R.layout.custom_offlist, null);
        TextView title = (TextView) v.findViewById(R.id.textView);
        TextView realise_date = (TextView) v.findViewById(R.id.textView2);
        TextView genre = (TextView) v.findViewById(R.id.textView3);
//        TextView duration = (TextView) v.findViewById(R.id.textView4);
        ImageView image = (ImageView) v.findViewById(R.id.imageView);
        ImageView image1 = (ImageView) v.findViewById(R.id.imageView1);
//
        Picasso.with(activity)
                .load(downloadModel.get(position).getPoster())
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .into(image);
        title.setText(downloadModel.get(position).getMUVIID());
        realise_date.setText("");
        realise_date.setVisibility(View.GONE);
        genre.setText(downloadModel.get(position).getGenere());

        if(downloadModel.get(position).getGenere().trim().equals(""))
            genre.setVisibility(View.GONE);

        /*String dd = downloadModel.get(position).getDuration();
        Log.v("SUBHA", dd);
        duration.setText(dd);*/

        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                showPopup(v);

                /////////////////////////popup/////////////////////////
                /*PopupMenu popup = new PopupMenu(activity, v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.menu_search, popup.getMenu());
                popup.show();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()){

                            case R.id.action_search: {

                                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(activity, R.style.MyAlertDialogStyle);
                                dlgAlert.setTitle("");
                                dlgAlert.setMessage("Want to Delete");

                                dlgAlert.setPositiveButton(Util.DEFAULT_DELETE_BTN,null);
                                dlgAlert.setCancelable(false);
                                dlgAlert.setPositiveButton(Util.DEFAULT_DELETE_BTN,
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                                DownloadManager  downloadManager = (DownloadManager) activity.getSystemService(DOWNLOAD_SERVICE);
                                                downloadManager.remove(downloadModel.get(position).getDOWNLOADID());

                                                String path1 = downloadModel.get(position).getPath().trim();
                                                File file = new File(path1);
                                                if (file != null && file.exists()) {

                                                    file.delete();

                                                }


                                                audio = dbHelper.getContact(downloadModel.get(position).getUniqueId().trim());

                                                downloadModel.remove(position);
                                                notifyDataSetChanged();
                                                activity.visible();

                                                if (audio != null) {


                                                    dbHelper.deleteRecord(audio);

                                                }

                                            }
                                        });
                                dlgAlert.setNegativeButton(Util.getTextofLanguage(activity,Util.CANCEL_BUTTON,Util.DEFAULT_CANCEL_BUTTON),null);
                                dlgAlert.setCancelable(false);
                                dlgAlert.setNegativeButton(Util.getTextofLanguage(activity,Util.CANCEL_BUTTON,Util.DEFAULT_CANCEL_BUTTON),
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });

                                dlgAlert.create().show();


                            }

                        }

                        return false;
                    }
                });*/
                /////////////////////////popup/////////////////////////


                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(activity, R.style.MyAlertDialogStyle);
                dlgAlert.setTitle("");
                dlgAlert.setMessage("Want to Delete");

                dlgAlert.setPositiveButton(Util.DEFAULT_DELETE_BTN,null);
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(Util.DEFAULT_DELETE_BTN,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                DownloadManager  downloadManager = (DownloadManager) activity.getSystemService(DOWNLOAD_SERVICE);
                                downloadManager.remove(downloadModel.get(position).getDOWNLOADID());

                                String path1 = downloadModel.get(position).getPath().trim();
                                File file = new File(path1);
                                if (file != null && file.exists()) {

                                    file.delete();

                                }


                                audio = dbHelper.getContact(downloadModel.get(position).getUniqueId().trim());

                                downloadModel.remove(position);
                                notifyDataSetChanged();
                                Util.DownloadChange = true;
                                activity.visible();

                                if (audio != null) {

                                    dbHelper.deleteRecord(audio);

                                }

                            }
                        });
                dlgAlert.setNegativeButton(Util.getTextofLanguage(activity,Util.CANCEL_BUTTON,Util.DEFAULT_CANCEL_BUTTON),null);
                dlgAlert.setCancelable(false);
                dlgAlert.setNegativeButton(Util.getTextofLanguage(activity,Util.CANCEL_BUTTON,Util.DEFAULT_CANCEL_BUTTON),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                dlgAlert.create().show();


            }


        });

        return v;
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(activity, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_search, popup.getMenu());
        popup.show();
    }


}
