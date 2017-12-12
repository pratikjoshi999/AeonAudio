package com.release.aeonaudio.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.release.aeonaudio.R;
import com.release.aeonaudio.adapter.DataBaseHandler;
import com.release.aeonaudio.utils.Util;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Timer;
import java.util.TimerTask;

import android.content.pm.Signature;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import static com.release.aeonaudio.R.id.skip;

/**
 * Created by Muvi on 6/12/2017.
 */

public class SplashScreenActivity extends AppCompatActivity {
        Button signin_bt,signup_bt;
    Intent signinIntent,signupIntent;
    SharedPreferences prefs;
    SharedPreferences ControlPref;
    Timer durationTimer;
    String useridPref;
    boolean isRunning = false;
    LinearLayout AuthLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spalash_screen);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.release.aeonaudio",  // replace with your unique package name
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.v("SANJAY:", Base64.encodeToString(md.digest(), Base64.DEFAULT));

            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        ImageView imageResize = (ImageView) findViewById(R.id.splash_screen);
        TextView skipButton = (TextView) findViewById(skip);
        skipButton.setVisibility(View.GONE);
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = prefs.edit();

                editor.putString("display_namePref", "Guest");
                editor.putString("useridPref", "0101D");


                editor.commit();
                finish();
                Intent SkipIntent = new Intent(SplashScreenActivity.this,MainActivity.class);
                SkipIntent.putExtra("Signing","Demo");
                SkipIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(SkipIntent);
            }
        });
        //get the current device height  and width
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        Log.v("nihar_gg",dpHeight+"////////"+dpWidth);

        imageResize.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.splash_screen, dpWidth, dpHeight));




       /* try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.release.recycleview",  // replace with your unique package name
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.v("SUBHA:", Base64.encodeToString(md.digest(), Base64.DEFAULT));

            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }*/
        AsynIsRegistrationEnabled asynIsRegistrationEnabled = new AsynIsRegistrationEnabled();
        asynIsRegistrationEnabled.execute();
        AuthLayout = (LinearLayout) findViewById(R.id.auth_layout) ;
        DataBaseHandler dataBase = new DataBaseHandler(this);
        dataBase.getWritableDatabase();
        prefs = getSharedPreferences(Util.LOGINPREFERENCE, MODE_PRIVATE);

        if (prefs.contains("useridPref") && !prefs.getString("useridPref", "0101D").equals("0101D")){

            if (prefs != null){
                useridPref  = prefs.getString("useridPref", null);
               AuthLayout.setVisibility(View.GONE);
                skipButton.setVisibility(View.GONE);
                    StartTimer();

                // a potentially  time consuming task

            }
            else{
                useridPref = "";
            }

        }



        signin_bt=(Button) findViewById(R.id.signin_bt);
        signup_bt=(Button) findViewById(R.id.signup_bt);
        Typeface signin_bt_tf = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.regular_fonts));
        signin_bt.setTypeface(signin_bt_tf);
        Typeface signup_bt_tf = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.regular_fonts));
        signup_bt.setTypeface(signup_bt_tf);

        signin_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 signinIntent = new Intent(SplashScreenActivity.this,Login.class);
                signinIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(signinIntent);

            }
        });

        signup_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signupIntent = new Intent(SplashScreenActivity.this,Register.class);
                signupIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(signupIntent);
            }
        });


    }

    public void StartTimer() {
        if (isRunning) {
            durationTimer.cancel();
            isRunning = false;
        }


        durationTimer = new Timer();
        durationTimer.schedule(new TimerTask() {
            @Override
            public void run() {

                durationTimer.cancel();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent  upIntent = new Intent(SplashScreenActivity.this,MainActivity.class);
                        upIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(upIntent);
                        finish();
                    }
                });
                isRunning = false;

            }
        }, 2000, 1000);
        isRunning = true;
    }
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         float reqWidth, float reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }
    public static int calculateInSampleSize(
            BitmapFactory.Options options, float reqWidth, float reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    private class AsynIsRegistrationEnabled extends AsyncTask<Void, Void, Void> {
        String responseStr;
        int statusCode;
        private int isLogin = 0;
        private String IsMyLibrary = "0";
        private String IsOneStepReg = "0";
        private String isPlayList = "0";
        private String isQueue = "0";
        private String isRestrictDevice = "0";
        private String isStreamingRestriction = "0";
        private String hasFavorite = "0";
        private String hasChromecast = "0";
        private String hasDownload = "0";

        @Override
        protected Void doInBackground(Void... params) {

            try {
                HttpClient httpclient=new DefaultHttpClient();
                HttpPost httppost = new HttpPost(Util.rootUrl()+Util.isRegistrationEnabledurl.trim());
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                // Execute HTTP Post Request
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());


                } catch (org.apache.http.conn.ConnectTimeoutException e){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }

                    });

                }catch (IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                        }

                    });
                    e.printStackTrace();
                }


                JSONObject myJson =null;
                if(responseStr!=null){
                    myJson = new JSONObject(responseStr);
                    statusCode = Integer.parseInt(myJson.optString("code"));

                }

                if (statusCode > 0) {
                    if (statusCode == 200) {
                        if ((myJson.has("chromecast")) && myJson.getString("chromecast").trim() != null && !myJson.getString("chromecast").trim().isEmpty() && !myJson.getString("chromecast").trim().equals("null") && !myJson.getString("chromecast").trim().matches("")) {
                            hasChromecast = myJson.getString("chromecast");
                            Log.v("NiharCast","has Chromecast = "+hasChromecast+"  "+myJson.getInt("chromecast"));

                        }
                        if ((myJson.has("is_login")) && myJson.getString("is_login").trim() != null && !myJson.getString("is_login").trim().isEmpty() && !myJson.getString("is_login").trim().equals("null") && !myJson.getString("is_login").trim().matches("")) {
                            isLogin = Integer.parseInt(myJson.getString("is_login"));

                            IsOneStepReg = myJson.optString("signup_step");
                            isRestrictDevice = myJson.optString("isRestrictDevice");
                            isStreamingRestriction = myJson.optString("is_streaming_restriction");
                            //hasFavorite = myJson.optString("has_favourite");
                            //Adder Later By Bibhu
                            //This code is used for the 'My Library Feature'

                            if(isLogin == 1)

                            {
                                if((myJson.optString("isMylibrary")).trim().equals("1"))
                                {
                                    IsMyLibrary = "1";
                                }
                                if((myJson.optString("isPlayList")).trim().equals("1"))
                                {
                                    isPlayList = "1";
                                }
                                if((myJson.optString("isQueue")).trim().equals("1"))
                                {
                                    isQueue = "1";
                                }
                                if((myJson.optString("is_offline")).trim().equals("1"))
                                {
                                    hasDownload = "1";
                                }
                            }
                            else
                            {
                                IsMyLibrary = "0";
                                hasDownload = "0";
                            }

                        } else {
                            isLogin = 0;
                        }
                        if ((myJson.has("has_favourite")) && myJson.getString("has_favourite").trim() != null && !myJson.getString("has_favourite").trim().isEmpty() && !myJson.getString("has_favourite").trim().equals("null") && !myJson.getString("has_favourite").trim().matches("")) {
                            hasFavorite = myJson.getString("has_favourite");
                            Log.v("SUBHA","has favorite = "+hasFavorite);

                        }







                    }else{
                        isLogin = 0;
                    }
                }
                else {
                    responseStr = "0";
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            isLogin = 0;


                        }
                    });
                }
            } catch (JSONException e1) {
                try {
                }
                catch(IllegalArgumentException ex)
                {
                    responseStr = "0";
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            isLogin = 0;


                        }
                    });
                    e1.printStackTrace();
                }
            }

            catch (Exception e)
            {
                try {

                }
                catch(IllegalArgumentException ex)
                {
                    responseStr = "0";
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            isLogin = 0;


                        }
                    });
                    e.printStackTrace();
                }

            }
            return null;

        }

        protected void onPostExecute(Void result) {


            try {

            }catch (IllegalArgumentException e){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        isLogin = 0;



                    }
                });
            }

            if(responseStr == null) {
                isLogin = 0;


            }
            if ((responseStr.trim().equalsIgnoreCase("0"))){
                isLogin = 0;

            }

//            Util.setLanguageSharedPrefernce(SplashScreenActivity.this, Util.IS_STREAMING_RESTRICTION, isStreamingRestriction);
            Util.setLanguageSharedPrefernce(SplashScreenActivity.this,Util.IS_MYLIBRARY,IsMyLibrary);
            Util.setLanguageSharedPrefernce(SplashScreenActivity.this, Util.IS_RESTRICT_DEVICE, isRestrictDevice);
            Util.setLanguageSharedPrefernce(SplashScreenActivity.this, Util.IS_ONE_STEP_REGISTRATION, IsOneStepReg);
            Util.setLanguageSharedPrefernce(SplashScreenActivity.this, Util.HAS_FAVORITE, hasFavorite);
            Util.setLanguageSharedPrefernce(SplashScreenActivity.this, Util.CHROMECAST, hasChromecast);
            Util.setLanguageSharedPrefernce(SplashScreenActivity.this, Util.HASDOWNLOAD, hasDownload);
            Util.setLanguageSharedPrefernce(SplashScreenActivity.this, Util.ISPLAYLIST, isPlayList);
            Util.setLanguageSharedPrefernce(SplashScreenActivity.this, Util.ISQUEUE, isQueue);
            SharedPreferences.Editor isLoginPrefEditor = prefs.edit();
            isLoginPrefEditor.putInt(Util.IS_LOGIN_PREF_KEY, isLogin);

            isLoginPrefEditor.commit();
      /*      AsynGetLanguageList asynGetLanguageList = new AsynGetLanguageList();
            asynGetLanguageList.executeOnExecutor(threadPoolExecutor);*/


            /*AsynGetGenreList asynGetGenreList = new AsynGetGenreList();
            asynGetGenreList.executeOnExecutor(threadPoolExecutor);*/


        }

        @Override
        protected void onPreExecute() {

        }


    }
}
