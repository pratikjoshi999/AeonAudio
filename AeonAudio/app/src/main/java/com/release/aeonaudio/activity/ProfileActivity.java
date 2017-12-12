package com.release.aeonaudio.activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.release.aeonaudio.R;
import com.release.aeonaudio.utils.AndroidMultiPartEntity;
import com.release.aeonaudio.utils.ProgressBarHandler;
import com.release.aeonaudio.utils.Util;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static android.app.Activity.RESULT_OK;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static com.release.aeonaudio.utils.Util.authTokenStr;

public class ProfileActivity extends Fragment {
    private static final int SELECT_PICTURE = 0;
    private static final int IMAGE_CAPTURE = 1;
    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 2;
    Bitmap bm;
    String SelectedPath = "";
    public static final String IMAGE_DIRECTORY_NAME = "Profile_Image";
    long totalSize = 0;

    SharedPreferences loginPref;
    Uri photoURI;

    ImageView bannerImageView, editprofile;
    ImageView profile_icon;
    EditText editOldPassword, editNewPassword, editProfileNameEditText;
    LinearLayout edit_newpassword_layout, edit_oldpassword_layout;
    EditText emailAddressEditText;
    Button changePassword, update_profile, manage_devices;
    SharedPreferences sharedpreferences;
    String Name, Password;
    boolean password_visibility = false;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;
    String User_Id = "";
    String Email_Id = "";
    TextView name_of_user;
        String[] requests = {"uses-permission android:name=\"android.permission.CAMERA\"","uses-permission android:name=\"android.permission.WRITE_EXTERNAL_STORAGE\""};
    File profile_image_file;
    // load asynctask
    int corePoolSize = 60;
    int maximumPoolSize = 80;
    int keepAliveTime = 10;
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
    Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);
    LinearLayout name_vg;
    // Added for country and language spinner
    Spinner country_spinner, language_spinner;
    ArrayAdapter<String> Language_arrayAdapter, Country_arrayAdapter;

    String Selected_Language, Selected_Country = "0", Selected_Language_Id, Selected_Country_Id;
    SharedPreferences countryPref;
    List<String> Country_List, Country_Code_List, Language_List, Language_Code_List;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.activity_profile, container, false);
        loginPref = getActivity().getSharedPreferences(Util.LOGINPREFERENCE, 0);
        bannerImageView = (ImageView) v.findViewById(R.id.bannerImageView);
        profile_icon = (ImageView) v.findViewById(R.id.profile_icon);
        editNewPassword = (EditText) v.findViewById(R.id.editNewPassword);
        edit_newpassword_layout = (LinearLayout) v.findViewById(R.id.edit_newpassword_layout);
        edit_oldpassword_layout = (LinearLayout) v.findViewById(R.id.edit_oldpassword_layout);
        editOldPassword = (EditText) v.findViewById(R.id.editOldPassword);
        editProfileNameEditText = (EditText) v.findViewById(R.id.editProfileNameEditText);
        emailAddressEditText = (EditText) v.findViewById(R.id.emailAddressEditText);
        changePassword = (Button) v.findViewById(R.id.changePasswordButton);
        update_profile = (Button) v.findViewById(R.id.update_profile);
        editOldPassword.setVisibility(View.GONE);
        editNewPassword.setVisibility(View.GONE);
        edit_newpassword_layout.setVisibility(View.GONE);
        edit_oldpassword_layout.setVisibility(View.GONE);
        name_of_user = (TextView) v.findViewById(R.id.name_of_user);
        editprofile = (ImageView) v.findViewById(R.id.editprofile);

        name_vg = (LinearLayout) v.findViewById(R.id.name_vg);


        editProfileNameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    name_vg.setBackground(getResources().getDrawable(R.drawable.shape_edit_text_active));
                }
                else {
                    name_vg.setBackground(getResources().getDrawable(R.drawable.shape_editttext));

                }
            }
        });
        editNewPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    edit_newpassword_layout.setBackground(getResources().getDrawable(R.drawable.shape_edit_text_active));
                }
                else {
                    edit_newpassword_layout.setBackground(getResources().getDrawable(R.drawable.shape_editttext));

                }
            }
        });
        editOldPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    edit_oldpassword_layout.setBackground(getResources().getDrawable(R.drawable.shape_edit_text_active));
                }
                else {
                    edit_oldpassword_layout.setBackground(getResources().getDrawable(R.drawable.shape_editttext));

                }
            }
        });




        requestPermissions(getActivity(),requests,0);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);



        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence[] items = {"Take Photo", "Choose from Library",
                        "Cancel"};

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Add Photo!");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (items[item].equals("Take Photo")) {


                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            photoURI = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                            SelectedPath = photoURI.getPath();
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                            // start the image capture Intent
                            startActivityForResult(intent, REQUEST_CAMERA);




                        } else if (items[item].equals("Choose from Library")) {
                            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image/*");
                            startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
                        } else if (items[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });

        Typeface editProfileNameEditTextTypeface = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.light_fonts));
        editProfileNameEditText.setTypeface(editProfileNameEditTextTypeface);


        Typeface editOldPasswordTypeface = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.light_fonts));
        editOldPassword.setTypeface(editOldPasswordTypeface);


        Typeface editNewPasswordTypeface = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.light_fonts));
        editNewPassword.setTypeface(editNewPasswordTypeface);
        Typeface name_of_userTypeface = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.light_fonts));
        name_of_user.setTypeface(name_of_userTypeface);


        Typeface changePasswordTypeface = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.regular_fonts));
        changePassword.setTypeface(changePasswordTypeface);

        Typeface update_profileTypeface = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.regular_fonts));
        update_profile.setTypeface(update_profileTypeface);

        editProfileNameEditText.setHint(Util.getTextofLanguage(getActivity(), Util.NAME_HINT, Util.DEFAULT_NAME_HINT));
        editOldPassword.setHint(Util.getTextofLanguage(getActivity(), Util.OLD_PASSWORD, Util.DEFAULT_OLD_PASSWORD));
        editNewPassword.setHint(Util.getTextofLanguage(getActivity(), Util.NEW_PASSWORD, Util.DEFAULT_NEW_PASSWORD));
        changePassword.setText(Util.getTextofLanguage(getActivity(), Util.CHANGE_PASSWORD, Util.DEFAULT_CHANGE_PASSWORD));
        update_profile.setText(Util.getTextofLanguage(getActivity(), Util.UPDATE_PROFILE, Util.DEFAULT_UPDATE_PROFILE));


//        Toolbar mActionBarToolbar= (Toolbar) v.findViewById(R.id.toolbar);
//        mActionBarToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_icon_close));
//        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });


       /* User_Id = getIntent().getStringExtra("LOGID");
        Email_Id = getIntent().getStringExtra("EMAIL");
*/
        if (loginPref != null) {
            User_Id = loginPref.getString("useridPref", null);
            Email_Id = loginPref.getString("emailPref", null);
        }

   /*     if(!Util.getTextofLanguage(ProfileActivity.this,Util.IS_RESTRICT_DEVICE,Util.DEFAULT_IS_RESTRICT_DEVICE).trim().equals("1"))
        {
            manage_devices.setVisibility(View.GONE);
        }

        manage_devices.setText(Util.getTextofLanguage(ProfileActivity.this,Util.MANAGE_DEVICE,Util.DEFAULT_MANAGE_DEVICE));
        manage_devices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isNetwork = Util.checkNetwork(ProfileActivity.this);
                if(isNetwork)
                {
                    Intent intent = new Intent(ProfileActivity.this,ManageDevices.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(ProfileActivity.this,Util.getTextofLanguage(ProfileActivity.this,Util.NO_INTERNET_CONNECTION,Util.DEFAULT_NO_INTERNET_CONNECTION),Toast.LENGTH_LONG).show();
                }

            }
        });

        Typeface manage_typeface = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.regular_fonts));
        manage_devices.setTypeface(manage_typeface);*/

        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        if (Util.checkNetwork(getActivity()) == true) {
            AsynLoadProfileDetails asynLoadProfileDetails = new AsynLoadProfileDetails();
            asynLoadProfileDetails.executeOnExecutor(threadPoolExecutor);
        } else {
            Util.showToast(getActivity(), Util.getTextofLanguage(getActivity(), Util.SLOW_INTERNET_CONNECTION, Util.DEFAULT_SLOW_INTERNET_CONNECTION));

        }


        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (changePassword.isClickable() && editOldPassword.isShown() && editNewPassword.isShown() && edit_newpassword_layout.isShown() && edit_oldpassword_layout.isShown()) {


                    if (editOldPassword.getText().toString().trim() != null && !(editOldPassword.getText().toString().trim().equalsIgnoreCase(""))) {
                        if (Util.isConfirmPassword(editOldPassword.getText().toString(), editNewPassword.getText().toString()) == false) {
                            Util.showToast(getActivity(), Util.getTextofLanguage(getActivity(), Util.PASSWORDS_DO_NOT_MATCH, Util.DEFAULT_PASSWORDS_DO_NOT_MATCH));

                            //   Toast.makeText(ProfileActivity.this, Util.getTextofLanguage(ProfileActivity.this, Util.PASSWORDS_DO_NOT_MATCH, Util.DEFAULT_PASSWORDS_DO_NOT_MATCH), Toast.LENGTH_LONG).show();

                            editOldPassword.setText("");
                            editNewPassword.setText("");

                            return;

                        } else {
                            boolean isNetwork = Util.checkNetwork(getActivity());
                            if (isNetwork) {
                                UpdateProfile();
                                editOldPassword.setText("");
                                editNewPassword.setText("");
                                editOldPassword.setVisibility(View.GONE);
                                editNewPassword.setVisibility(View.GONE);
                                edit_newpassword_layout.setVisibility(View.GONE);
                                edit_oldpassword_layout.setVisibility(View.GONE);
                            }


                        }
                    }
                    if (editOldPassword.getText().toString().trim().equalsIgnoreCase("") || (editNewPassword.getText().toString().trim().equalsIgnoreCase(""))) {
                        editOldPassword.setText("");
                        editNewPassword.setText("");
                        editOldPassword.setVisibility(View.GONE);
                        editNewPassword.setVisibility(View.GONE);
                        edit_newpassword_layout.setVisibility(View.GONE);
                        edit_oldpassword_layout.setVisibility(View.GONE);
                        editProfileNameEditText.requestFocus();
                    }
                    /*editOldPassword.setText("");
                    editNewPassword.setText("");*/
                } else {
                   /* editOldPassword.setText("");
                    editNewPassword.setText("");*/
                 /*   editOldPassword.setVisibility(View.GONE);
                    editNewPassword.setVisibility(View.GONE);*/
                    editOldPassword.setVisibility(View.VISIBLE);
                    editNewPassword.setVisibility(View.VISIBLE);
                    edit_newpassword_layout.setVisibility(View.VISIBLE);
                    edit_oldpassword_layout.setVisibility(View.VISIBLE);
                    editOldPassword.requestFocus();

                }


               /* if (editOldPassword.getText().toString().trim() != null && !(editOldPassword.getText().toString().trim().equalsIgnoreCase(""))) {
                    if (Util.isConfirmPassword(editOldPassword.getText().toString(), editNewPassword.getText().toString()) == false) {
                        Toast.makeText(ProfileActivity.this, Util.getTextofLanguage(ProfileActivity.this, Util.PASSWORDS_DO_NOT_MATCH, Util.DEFAULT_PASSWORDS_DO_NOT_MATCH), Toast.LENGTH_LONG).show();

                        return;
                    }
                }*/
            }
        });

        update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editProfileNameEditText.getText().toString().matches("")) {
                    ShowDialog(Util.getTextofLanguage(getActivity(), Util.FAILURE, Util.DEFAULT_FAILURE), Util.getTextofLanguage(getActivity(), Util.NAME_HINT, Util.DEFAULT_NAME_HINT));

                } else if (!editOldPassword.getText().toString().matches(editNewPassword.getText().toString().trim())) {
                    ShowDialog(Util.getTextofLanguage(getActivity(), Util.FAILURE, Util.DEFAULT_FAILURE), Util.getTextofLanguage(getActivity(), Util.PASSWORDS_DO_NOT_MATCH, Util.DEFAULT_PASSWORDS_DO_NOT_MATCH));

                } else {
                    boolean isNetwork = Util.checkNetwork(getActivity());
                    if (isNetwork) {
                        UpdateProfile();
                    }
                }
              /*  boolean isNetwork = Util.checkNetwork(ProfileActivity.this);

                if (isNetwork) {

                    Name = editProfileNameEditText.getText().toString().trim();
                    Password = editNewPassword.getText().toString().trim();

                    if (editOldPassword.getVisibility() == View.VISIBLE) {
                        if (!editProfileNameEditText.getText().toString().trim().equals("")
                                && !emailAddressEditText.getText().toString().trim().equals("")
                                && !editNewPassword.getText().toString().trim().equals("")
                                && !editOldPassword.getText().toString().trim().equals("")) {
                            if (editNewPassword.equals(editOldPassword)) {
                                password_visibility = true;
                                UpdateProfile();
                            } else {
                                ShowDialog(Util.getTextofLanguage(ProfileActivity.this,Util.FAILURE,Util.DEFAULT_FAILURE), Util.getTextofLanguage(ProfileActivity.this,Util.PASSWORDS_DO_NOT_MATCH,Util.DEFAULT_PASSWORDS_DO_NOT_MATCH));

                            }
                        } else {
                            ShowDialog(Util.getTextofLanguage(ProfileActivity.this,Util.FAILURE,Util.DEFAULT_FAILURE), Util.getTextofLanguage(ProfileActivity.this,Util.NO_RECORD,Util.DEFAULT_NO_RECORD));


                        }
                    } else {
                        if (!editProfileNameEditText.getText().toString().trim().equals("") && !emailAddressEditText.getText().toString().trim().equals("")) {
                            password_visibility = false;
                            UpdateProfile();
                        } else {
                            ShowDialog(Util.getTextofLanguage(ProfileActivity.this,Util.FAILURE,Util.DEFAULT_FAILURE), Util.getTextofLanguage(ProfileActivity.this,Util.NO_RECORD,Util.DEFAULT_NO_RECORD));

                        }
                    }

                } else {

                    ShowDialog(Util.getTextofLanguage(ProfileActivity.this,Util.FAILURE,Util.DEFAULT_FAILURE), Util.getTextofLanguage(ProfileActivity.this,Util.NO_INTERNET_CONNECTION,Util.DEFAULT_NO_INTERNET_CONNECTION));
                }*/
            }
        });
        return v;
    }

    public String getPath(Uri uri, Activity activity) {
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = activity
                .managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public void ShowDialog(String Title, String msg) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogStyle);
        dlgAlert.setMessage(msg);
        dlgAlert.setTitle(Title);
        dlgAlert.setPositiveButton(Util.getTextofLanguage(getActivity(), Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
        dlgAlert.setCancelable(false);
        dlgAlert.setPositiveButton(Util.getTextofLanguage(getActivity(), Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        dlgAlert.create().show();
    }

    public void UpdateProfile() {
        if (Util.checkNetwork(getActivity()) == true) {
            AsynUpdateProfile asyncLoadVideos = new AsynUpdateProfile();
            asyncLoadVideos.executeOnExecutor(threadPoolExecutor);
        } else {
            Util.showToast(getActivity(), Util.getTextofLanguage(getActivity(), Util.SLOW_INTERNET_CONNECTION, Util.DEFAULT_SLOW_INTERNET_CONNECTION));

        }

    }

    private class AsynUpdateProfile extends AsyncTask<Void, Void, Void> {
        ProgressBarHandler pDialog;

        int statusCode;
        String loggedInIdStr;
        String confirmPasswordStr = editNewPassword.getText().toString().trim();
        String nameStr = editProfileNameEditText.getText().toString().trim();

        String responseStr;
        JSONObject myJson = null;


       int serverResponseCode=0;






        @Override
        protected Void doInBackground(Void... params) {
            String selectedFilePath = SelectedPath;
            if (loginPref != null) {
                loggedInIdStr = loginPref.getString("PREFS_LOGGEDIN_ID_KEY", null);
            }
            if (!selectedFilePath.equals("")) {
                String urlRouteList = Util.rootUrl().trim() + Util.updateProfileUrl.trim();
//            String urlRouteList = "http://192.168.17.136/test/index.php";

                int serverResponseCode = 0;

                final HttpURLConnection connection;
                DataOutputStream dataOutputStream;
                String lineEnd = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";


                int bytesRead, bytesAvailable, bufferSize;
                byte[] buffer;
                int maxBufferSize = 5 * 1024 * 1024;

//            String selectedFilePath = photoURI.getPath();
                File selectedFile = new File(selectedFilePath);


                String[] parts = selectedFilePath.split("/");
                final String fileName = parts[parts.length - 1];

                try {
                    FileInputStream fileInputStream = new FileInputStream(selectedFile);
                    URL url = new URL(urlRouteList);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);//Allow Inputs
                    connection.setDoOutput(true);//Allow Outputs
                    connection.setUseCaches(false);//Don't use a cached Copy
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Connection", "Keep-Alive");
                    connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                    connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                    connection.setRequestProperty("file", selectedFilePath);
                    connection.setRequestProperty("authToken", Util.authTokenStr.trim());
                    connection.setRequestProperty("user_id", User_Id.trim());
                    connection.setRequestProperty("name", nameStr.trim());

                    //Create JSONObject here
//                    JSONObject jsonParam = new JSONObject();
//                    jsonParam.put("user_id", User_Id.trim());
//                    jsonParam.put("authToken", Util.authTokenStr.trim());
//                    jsonParam.put("name",nameStr.trim());


                    //creating new dataoutputstream
                    dataOutputStream = new DataOutputStream(connection.getOutputStream());


                    //writing bytes to data outputstream
//                    dataOutputStream.writeBytes(jsonParam.toString());
                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                    dataOutputStream.writeBytes("Content-Disposition: form-data ; name=\"file\";filename=\""
                            + selectedFilePath + "\"" + lineEnd);

                    dataOutputStream.writeBytes(lineEnd);

                    //returns no. of bytes present in fileInputStream
                    bytesAvailable = fileInputStream.available();
                    //selecting the buffer size as minimum of available bytes or 1 MB
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    //setting the buffer as byte array of size of bufferSize
                    buffer = new byte[bufferSize];

                    //reads bytes from FileInputStream(from 0th index of buffer to buffersize)
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    //loop repeats till bytesRead = -1, i.e., no bytes are left to read
                    while (bytesRead > 0) {
                        //write the bytes read from inputstream
                        dataOutputStream.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    }

                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                    serverResponseCode = connection.getResponseCode();
                    String serverResponseMessage = connection.getResponseMessage();

                    Log.v("BIBHU3", "Server Response is: " + serverResponseMessage + ": " + serverResponseCode);

                    //response code of 200 indicates the server status OK
                    if (serverResponseCode == 200) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                try {
                                    InputStream ins = connection.getInputStream();
                                    InputStreamReader isr = new InputStreamReader(ins);
                                    BufferedReader in = new BufferedReader(isr);

                                    String inputLine;

                                    while ((inputLine = in.readLine()) != null) {
                                        System.out.println(inputLine);
                                        responseStr = inputLine;
                                    }

                                    if (responseStr != null) {
                                        myJson = new JSONObject(responseStr);
                                        statusCode = Integer.parseInt(myJson.optString("code"));

                                        Log.v("BIBHU3", "statusCode 1==" + statusCode);

                                    }

                                    Log.v("BIBHU3", "Server Response is: " + responseStr);


                                } catch (Exception e) {
                                    Log.v("BIBHU3", "Exception is: " + e.toString());
                                }
                            }
                        });
                    }

                    //closing the input and output streams
                    fileInputStream.close();
                    dataOutputStream.flush();
                    dataOutputStream.close();





/*
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(new AndroidMultiPartEntity.ProgressListener() {
                        @Override
                        public void transferred(long num) {

                        }
                    });

                    Log.v("BIBHU3","path============="+photoURI.getPath());

                    entity.addPart("profile_image", new FileBody(new File(compressImage(photoURI.getPath()))));

                  *//*  entity.addPart("name", new StringBody(nameStr.trim()));
                    entity.addPart("authToken", new StringBody(authTokenStr.trim()));
                    entity.addPart("user_id", new StringBody(User_Id.trim()));*//*


                    totalSize = entity.getContentLength();
                    httppost.setEntity(entity);
                    httppost.addHeader("user_id", User_Id.trim());
                    httppost.addHeader("authToken", Util.authTokenStr.trim());
                    httppost.addHeader("name", nameStr.trim());*/


                    // Execute HTTP Post Request
         /*           try {
                        HttpResponse response = httpclient.execute(httppost);

                        responseStr = EntityUtils.toString(response.getEntity());
//                        Log.v("BIBHU3", "RES" + responseStr);

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(),responseStr,Toast.LENGTH_LONG).show();

                            }
                        });




                    } catch (org.apache.http.conn.ConnectTimeoutException e) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                statusCode = 0;
                                editOldPassword.setText("");
                                editNewPassword.setText("");
                                Util.showToast(getActivity(), Util.getTextofLanguage(getActivity(), Util.SLOW_INTERNET_CONNECTION, Util.DEFAULT_SLOW_INTERNET_CONNECTION));

                            }

                        });

                    } catch (IOException e) {
                        statusCode = 0;
                        Log.v("BIBHU3", "IOException 1" + e.toString());

                        e.printStackTrace();
                    }*/


                } catch (Exception e) {
                    statusCode = 0;
                    Log.v("BIBHU3", "Exception 1" + e.toString());

                }
            }else {

                String urlRouteList = Util.rootUrl().trim()+Util.updateProfileUrl.trim();
                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(urlRouteList);
                    httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                    httppost.addHeader("user_id", User_Id.trim());
                    httppost.addHeader("authToken", Util.authTokenStr.trim());
                    httppost.addHeader("name", nameStr.trim());
                    if (!confirmPasswordStr.trim().equalsIgnoreCase("") && !confirmPasswordStr.isEmpty() && !confirmPasswordStr.equalsIgnoreCase("null") && !confirmPasswordStr.equalsIgnoreCase(null) && !confirmPasswordStr.equals(null) && !confirmPasswordStr.matches("")){
                        httppost.addHeader("password", confirmPasswordStr.trim());
                    }
                    httppost.addHeader("lang_code",Util.getTextofLanguage(getActivity(),Util.SELECTED_LANGUAGE_CODE,Util.DEFAULT_SELECTED_LANGUAGE_CODE));

                    httppost.addHeader("custom_country", Selected_Country_Id);
                    httppost.addHeader("custom_languages",Selected_Language_Id);

                    // Execute HTTP Post Request
                    try {
                        HttpResponse response = httpclient.execute(httppost);
                        responseStr = EntityUtils.toString(response.getEntity());

                    } catch (org.apache.http.conn.ConnectTimeoutException e){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                statusCode = 0;
                                editOldPassword.setText("");
                                editNewPassword.setText("");
                                Util.showToast(getActivity(),Util.getTextofLanguage(getActivity(),Util.SLOW_INTERNET_CONNECTION,Util.DEFAULT_SLOW_INTERNET_CONNECTION));

                                // Toast.makeText(ProfileActivity.this, Util.getTextofLanguage(ProfileActivity.this, Util.SLOW_INTERNET_CONNECTION, Util.DEFAULT_SLOW_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();

                            }

                        });

                    } catch (IOException e) {
                        statusCode = 0;

                        e.printStackTrace();
                    }
                    if(responseStr!=null) {
                        myJson = new JSONObject(responseStr);
                        statusCode = Integer.parseInt(myJson.optString("code"));

                    }

                }
                catch (Exception e) {
                    statusCode = 0;

                }




            }

                return null;
            }



        protected void onPostExecute(Void result) {

            if (responseStr == null) {
                try {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.hide();
                        pDialog = null;
                    }
                } catch (IllegalArgumentException ex) {
                    statusCode = 0;

                }
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogStyle);
                dlgAlert.setMessage(Util.getTextofLanguage(getActivity(), Util.UPDATE_PROFILE_ALERT, Util.DEFAULT_UPDATE_PROFILE_ALERT));
                dlgAlert.setTitle(Util.getTextofLanguage(getActivity(), Util.SORRY, Util.DEFAULT_SORRY));
                dlgAlert.setPositiveButton(Util.getTextofLanguage(getActivity(), Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(Util.getTextofLanguage(getActivity(), Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                editOldPassword.setText("");
                                editNewPassword.setText("");
                            }
                        });
                dlgAlert.create().show();
            }

            if (statusCode > 0) {

                if (statusCode == 200) {
                    try {
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.hide();
                            pDialog = null;
                        }
                    } catch (IllegalArgumentException ex) {
                        SharedPreferences.Editor editor = loginPref.edit();
                        if (myJson.has("name")) {
                            String displayNameStr = myJson.optString("name");
                            editor.putString("display_namePref", displayNameStr);
                        }
                        if (!confirmPasswordStr.trim().equalsIgnoreCase("") && !confirmPasswordStr.isEmpty() && !confirmPasswordStr.equalsIgnoreCase("null") && !confirmPasswordStr.equalsIgnoreCase(null) && !confirmPasswordStr.equals(null) && !confirmPasswordStr.matches("")) {
                            editor.putString("PREFS_LOGGEDIN_PASSWORD_KEY", confirmPasswordStr.trim());

                        }
                        editor.commit();
                    }
                    SharedPreferences.Editor editor = loginPref.edit();
                    if (myJson.has("name")) {
                        String displayNameStr = myJson.optString("name");
                        editor.putString("display_namePref", displayNameStr);
                    }
                    if (!confirmPasswordStr.trim().equalsIgnoreCase("") && !confirmPasswordStr.isEmpty() && !confirmPasswordStr.equalsIgnoreCase("null") && !confirmPasswordStr.equalsIgnoreCase(null) && !confirmPasswordStr.equals(null) && !confirmPasswordStr.matches("")) {
                        editor.putString("PREFS_LOGGEDIN_PASSWORD_KEY", confirmPasswordStr.trim());

                    }

                    editor.commit();
                    name_of_user.setText(editProfileNameEditText.getText().toString().trim());

                    Util.showToast(getActivity(), Util.getTextofLanguage(getActivity(), Util.PROFILE_UPDATED, Util.DEFAULT_PROFILE_UPDATED));

                    //   Toast.makeText(ProfileActivity.this, Util.getTextofLanguage(ProfileActivity.this, Util.PROFILE_UPDATED, Util.DEFAULT_PROFILE_UPDATED), Toast.LENGTH_SHORT).show();
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                    if (name_of_user != null) {
                        name_of_user.clearFocus();
                        name_of_user.setCursorVisible(false);
                    }
                    if (editOldPassword != null) {
                        editOldPassword.clearFocus();

                    }
                    if (editNewPassword != null) {
                        editNewPassword.clearFocus();
                    }
                   /* if (fullNameEditText != null) fullNameEditText.clearFocus();
                    if (passwordEditText != null) passwordEditText.clearFocus();
                    if (confirmPasswordEditText != null) confirmPasswordEditText.clearFocus();*/

                } else {

                    try {
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.hide();
                            pDialog = null;
                        }
                    } catch (IllegalArgumentException ex) {
                        statusCode = 0;

                    }
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogStyle);
                    dlgAlert.setMessage(Util.getTextofLanguage(getActivity(), Util.UPDATE_PROFILE_ALERT, Util.DEFAULT_UPDATE_PROFILE_ALERT));
                    dlgAlert.setTitle(Util.getTextofLanguage(getActivity(), Util.SORRY, Util.DEFAULT_SORRY));
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(getActivity(), Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                    dlgAlert.setCancelable(false);
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(getActivity(), Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    editOldPassword.setText("");
                                    editNewPassword.setText("");


                                }
                            });
                    dlgAlert.create().show();
                }
            } else {
                try {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.hide();
                        pDialog = null;
                    }
                } catch (IllegalArgumentException ex) {

                }
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogStyle);
                dlgAlert.setMessage(Util.getTextofLanguage(getActivity(), Util.UPDATE_PROFILE_ALERT, Util.DEFAULT_UPDATE_PROFILE_ALERT));
                dlgAlert.setTitle(Util.getTextofLanguage(getActivity(), Util.SORRY, Util.DEFAULT_SORRY));
                dlgAlert.setPositiveButton(Util.getTextofLanguage(getActivity(), Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(Util.getTextofLanguage(getActivity(), Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                editOldPassword.setText("");
                                editNewPassword.setText("");
                            }
                        });
                dlgAlert.create().show();
            }

        }

        @Override
        protected void onPreExecute() {
            Log.v("SUBHA","onPreExecute");

            pDialog = new ProgressBarHandler(getActivity());
            pDialog.show();
        }


    }
    //Getting Profile Details from The Api

    private class AsynLoadProfileDetails extends AsyncTask<Void, Void, Void> {
        ProgressBarHandler pDialog;
        String responseStr;
        int statusCode;
        String name;
        String profileEmail;
        String profileImage;
        String langStr = "";
        String countryStr = "";

        @Override
        protected Void doInBackground(Void... params) {

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(Util.rootUrl().trim() + Util.loadProfileUrl.trim());
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", authTokenStr.trim());
                httppost.addHeader("user_id", User_Id);
                httppost.addHeader("email", Email_Id);
                httppost.addHeader("lang_code", Util.getTextofLanguage(getActivity(), Util.SELECTED_LANGUAGE_CODE, Util.DEFAULT_SELECTED_LANGUAGE_CODE));

                // Execute HTTP Post Request
                try {

                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());
                    Log.v("SUBHA","loadRES"+responseStr);


                } catch (org.apache.http.conn.ConnectTimeoutException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            responseStr = "0";
                            Util.showToast(getActivity(), Util.getTextofLanguage(getActivity(), Util.SLOW_INTERNET_CONNECTION, Util.DEFAULT_SLOW_INTERNET_CONNECTION));

                            // Toast.makeText(ProfileActivity.this, Util.getTextofLanguage(ProfileActivity.this, Util.SLOW_INTERNET_CONNECTION, Util.DEFAULT_SLOW_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();

                        }

                    });

                } catch (IOException e) {
                    responseStr = "0";
                    e.printStackTrace();
                }

                Log.v("BIBHU", "responseStr =" + responseStr);

                JSONObject myJson = null;
                if (responseStr != null) {
                    myJson = new JSONObject(responseStr);
                    statusCode = Integer.parseInt(myJson.optString("code"));

                }
                if (statusCode > 0) {
                    if (statusCode == 200) {

                        if ((myJson.has("display_name")) && myJson.optString("display_name").trim() != null && !myJson.optString("display_name").trim().isEmpty() && !myJson.optString("display_name").trim().equals("null") && !myJson.optString("display_name").trim().matches("")) {
                            name = myJson.getString("display_name");
                        } else {
                            name = "";

                        }

                        if ((myJson.has("email")) && myJson.optString("email").trim() != null && !myJson.optString("email").trim().isEmpty() && !myJson.optString("email").trim().equals("null") && !myJson.optString("email").trim().matches("")) {
                            profileEmail = myJson.optString("email");

                        } else {
                            profileEmail = "";

                        }
                        if ((myJson.has("profile_image")) && myJson.optString("profile_image").trim() != null && !myJson.optString("profile_image").trim().isEmpty() && !myJson.optString("profile_image").trim().equals("null") && !myJson.optString("profile_image").trim().matches("")) {
                            profileImage = myJson.optString("profile_image");


                        } else {
                            profileImage = Util.getTextofLanguage(getActivity(), Util.NO_DATA, Util.DEFAULT_NO_DATA);

                        }

                        /*JSONArray languageJson = null;
                        if ((myJson.has("custom_languages"))) {

                            languageJson = myJson.getJSONArray("custom_languages");

                            if (languageJson.length() > 0) {
                                Selected_Language_Id = languageJson.optString(0);
                                Log.v("BIBHU","Selected_Language_Id st jon parsing ="+Selected_Language_Id);
                            } else {
                                langStr = "";
                            }
                        } else {
                            langStr = "";
                        }




                        if ((myJson.has("custom_country")) && myJson.optString("custom_country").trim() != null && !myJson.optString("custom_country").trim().isEmpty() && !myJson.optString("custom_country").trim().equals("null") && !myJson.optString("custom_country").trim().matches("")) {
                            //countryPosition = Integer.parseInt(myJson.optString("custom_country"));
                            countryStr = myJson.getString("custom_country");
                            Selected_Country_Id = countryStr;

                        } else {
                            countryStr = "";

                        }*/


                    } else {
                        name = "";
                        profileEmail = "";
                        profileImage = Util.NO_DATA;

                    }
                } else {
                    responseStr = "0";
                }
            } catch (JSONException e1) {

                responseStr = "0";
                e1.printStackTrace();
            } catch (Exception e) {
                responseStr = "0";
                e.printStackTrace();

            }

            return null;

        }

        protected void onPostExecute(Void result) {

            try {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.hide();
                    pDialog = null;
                }
            } catch (IllegalArgumentException ex) {
                responseStr = "0";
            }
            if (responseStr == null) {
                responseStr = "0";
            }

            if ((responseStr.trim().equalsIgnoreCase("0"))) {

                editProfileNameEditText.setText("");
                emailAddressEditText.setText("");
                name_of_user.setText("");
                bannerImageView.setAlpha(0.8f);

                // imagebg.setBackgroundColor(Color.parseColor("#969393"));
                bannerImageView.setImageResource(R.drawable.no_image);
            } else {


               /* if(Selected_Country_Id.equals("0"))
                {
                    country_spinner.setSelection(224);
                    Selected_Country_Id = Country_Code_List.get(224);
                    Log.v("BIBHU","country not  matched ="+Selected_Country+"=="+Selected_Country_Id);
                }
                else
                {
                    for(int i=0;i<Country_Code_List.size();i++)
                    {
                        if(Selected_Country_Id.trim().equals(Country_Code_List.get(i)))
                        {
                            country_spinner.setSelection(i);
                            Selected_Country_Id = Country_Code_List.get(i);

                            Log.v("BIBHU","country  matched ="+Selected_Country_Id+"=="+Selected_Country_Id);
                        }
                    }
                }
                Country_arrayAdapter.notifyDataSetChanged();



                    for(int i=0;i<Language_Code_List.size();i++)
                    {
                        if(Selected_Language_Id.trim().equals(Language_Code_List.get(i)))
                        {
                            language_spinner.setSelection(i);
                            Selected_Language_Id = Language_Code_List.get(i);

                            Log.v("BIBHU","Selected_Language_Id ="+Selected_Language_Id);
                        }
                    }
                    Language_arrayAdapter.notifyDataSetChanged();


*/


                editProfileNameEditText.setText(name);
                name_of_user.setText(name);
                emailAddressEditText.setText(profileEmail);
                if (profileImage.matches(Util.NO_DATA)) {

                    profile_icon.setImageResource(R.drawable.profile_default_icon);
                } else {
                    Picasso.with(getActivity())
                            .load(profileImage)
                            .placeholder(R.drawable.logo).error(R.drawable.logo).noFade().resize(200, 200).into(profile_icon, new Callback() {

                        @Override
                        public void onSuccess() {

//                            Bitmap bitmapFromPalette = ((BitmapDrawable) bannerImageView.getDrawable()).getBitmap();
//                            Palette palette = Palette.generate(bitmapFromPalette);
                        }

                        @Override
                        public void onError() {
                            // reset your views to default colors, etc.
                            profile_icon.setAlpha(0.8f);
                            profile_icon.setImageResource(R.drawable.no_image);
                        }

                    });
                    if (profileImage != null && profileImage.length() > 0) {
                        int pos = profileImage.lastIndexOf("/");
                        String x = profileImage.substring(pos + 1, profileImage.length());

                        if (x.equalsIgnoreCase("no-user.png")) {
                            profile_icon.setImageResource(R.drawable.no_image);

                            //imagebg.setBackgroundColor(Color.parseColor("#969393"));

                        } else {
                            Picasso.with(getActivity())
                                    .load(profileImage)
                                    .placeholder(R.drawable.logo).error(R.drawable.logo).noFade().resize(200, 200).into(profile_icon, new Callback() {

                                @Override
                                public void onSuccess() {


                                }

                                @Override
                                public void onError() {
                                    profile_icon.setImageResource(R.drawable.no_image);
                                    profile_icon.setAlpha(0.8f);
                                    //imagebg.setBackgroundColor(Color.parseColor("#969393"));
                                }

                            });
                        }
                    }
                }

            }
        }

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressBarHandler(getActivity());
            pDialog.show();
        }


    }

    //    @Override
//    public void onBackPressed()
//    {
//        finish();
//        overridePendingTransition(0, 0);
//        super.onBackPressed();
//    }
    public void removeFocusFromViews() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences.Editor editor = loginPref.edit();
        editor.putString("display_namePref", editProfileNameEditText.getText().toString().trim());
        removeFocusFromViews();

    }

    @Override
    public void onPause() {
        super.onPause();

        SharedPreferences.Editor editor = loginPref.edit();
        editor.putString("display_namePref", editProfileNameEditText.getText().toString().trim());
        removeFocusFromViews();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            ///for camera
            if (requestCode == REQUEST_CAMERA) {
                 profile_image_file = new File(photoURI.getPath());

                try {
                    profile_icon.setImageURI(photoURI);
                } catch (Exception e) {
                    Log.v("Exception", "" + e.toString());

                    return;
                }
            }
            //for gallery files
            else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();
                SelectedPath = getRealPathFromURI(selectedImageUri);


                String tempPath = getPath(selectedImageUri, getActivity());
                BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
                bm = BitmapFactory.decodeFile(tempPath);

                Log.v("temporaryPath", tempPath);
                profile_icon.setImageBitmap(bm);
            }
        }
    }
    void requestPermissions (Activity activity, String[]permissions, int requestCode) {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_CONTACTS)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }
    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }
    public static String encodeImage(byte[] imageByteArray) {
        return Base64.encodeToString(imageByteArray,0);
    }

//=============================

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("BIBHU2", "Oops! Failed create " + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = ""+System.currentTimeMillis();
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator+ "IMG_" + timeStamp + ".jpg");

        } else {
            return null;
        }

        return mediaFile;
    }



    //=====================================================

    private String compressImage(String imageUri) {

        String filePath = imageUri;
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;             }
            else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight,Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Log.v("App1","filename="+filename);
        return filename;

    }


    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }
    private String getRealPathFromURI(Uri contentURI) {
        Uri contentUri = contentURI;
        Cursor cursor = getActivity().getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }
    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height/ (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;      }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }


}
