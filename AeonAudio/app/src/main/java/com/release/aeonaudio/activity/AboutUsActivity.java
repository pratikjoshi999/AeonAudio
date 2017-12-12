package com.release.aeonaudio.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import com.release.aeonaudio.R;
import com.release.aeonaudio.utils.ProgressBarHandler;
import com.release.aeonaudio.utils.Util;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import static com.release.aeonaudio.activity.SplashScreenActivity.decodeSampledBitmapFromResource;

public class AboutUsActivity extends AppCompatActivity {

    String about;
    // TextView textView;
    Context context;
    ProgressBarHandler pDialog;
    WebView webView;
        Toolbar mActionBarToolbar ;
    AsyncAboutUS asyncAboutUS;
    String fperma;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);


        ImageView imageResize = (ImageView) findViewById(R.id.background_contactus);
        //get the current device height  and width
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        Log.v("nihar_gg",dpHeight+"////////"+dpWidth);

        imageResize.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.splash_screen, dpWidth, dpHeight));
        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(mActionBarToolbar);
        mActionBarToolbar.setTitle(" ");
        mActionBarToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_icon_close));
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        webView = (WebView) findViewById(R.id.aboutUsWebView);
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
        pDialog = new ProgressBarHandler(this);
        Intent data=getIntent();
         fperma=data.getStringExtra("fpermalink");
        if (Util.checkNetwork(AboutUsActivity.this) == true) {
            asyncAboutUS = new AsyncAboutUS();
            asyncAboutUS.execute();
        }else{
            Util.showToast(context,Util.getTextofLanguage(AboutUsActivity.this,Util.SLOW_INTERNET_CONNECTION,Util.DEFAULT_SLOW_INTERNET_CONNECTION));

        }
    }

    public class AsyncAboutUS extends AsyncTask<Void, Void, Void> {
        String responseStr;

  //      private ProgressBarHandler progressBarHandler = null;

        @Override
        protected Void doInBackground(Void... params) {
            String urlRouteList = Util.rootUrl().trim() + Util.AboutUs.trim();
            try{
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("permalink",fperma.trim());
//                httppost.addHeader("message","Test Message  Test Message");
            //    httppost.addHeader("lang_code",Util.getTextofLanguage(context,Util.SELECTED_LANGUAGE_CODE,Util.DEFAULT_SELECTED_LANGUAGE_CODE));
                HttpResponse response = httpclient.execute(httppost);
                responseStr = EntityUtils.toString(response.getEntity());

            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();

            }
            JSONObject myJson = null;
            if (responseStr != null) {
                try {
                    myJson = new JSONObject(responseStr);
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
            try {
                JSONObject jsonMainNode = myJson.getJSONObject("page_details");
                about = jsonMainNode.optString("content").trim();

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            try {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.hide();
                    pDialog = null;
                }
            } catch (IllegalArgumentException ex) {


            }

            String text = "<html><head>"
                    + "<style type=\"text/css\" >body{color:#ddd; background-color: #00000000;}"
                    + "</style></head>"
                    + "<body style >"
                    + about
                    + "</body></html>";


            webView.loadData(text, "text/html", "utf-8");
           // webView.loadData("aboutus", "text/html", "utf-8");
            webView.getSettings().setJavaScriptEnabled(true);
            Log.v("pratik","abotttou=="+text);
        }

        @Override
        protected void onPreExecute() {
            pDialog.show();
        }


    }

    @Override
    public void onPause() {
        super.onPause();
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.hide();
            pDialog = null;
        }
    }
}
