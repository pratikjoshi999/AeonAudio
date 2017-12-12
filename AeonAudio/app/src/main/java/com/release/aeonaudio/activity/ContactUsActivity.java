package com.release.aeonaudio.activity;


import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import static com.release.aeonaudio.R.id.password_vg;
import static com.release.aeonaudio.activity.SplashScreenActivity.decodeSampledBitmapFromResource;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactUsActivity extends AppCompatActivity {
    Context context = this;
    ProgressBarHandler pDialog;

    String regEmailStr, regNameStr,regMessageStr;
    EditText editEmailStr, editNameStr,editMessageStr;
    TextView contactFormTitle;
    Button submit;
    String sucessMsg,statusmsg;
    String contEmail;
    AsynContactUs asynContactUs;
    Toolbar mActionBarToolbar;
    boolean validate = true;
    LinearLayout email_vg,msg_vg,name_vg;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_contact_us);
        getWindow().setBackgroundDrawableResource(R.drawable.saplashscreen2);
        email_vg = (LinearLayout) findViewById(R.id.email_vg);
        msg_vg = (LinearLayout) findViewById(password_vg);
        name_vg = (LinearLayout) findViewById(R.id.name_vg);



        TextView categoryTitle = (TextView) findViewById(R.id.categoryTitle);
        Typeface castDescriptionTypeface = Typeface.createFromAsset(context.getAssets(),context.getResources().getString(R.string.regular_fonts));
        categoryTitle.setTypeface(castDescriptionTypeface);
        categoryTitle.setText("Contact Us");

        contactFormTitle = (TextView) findViewById(R.id.contactFormTitle);
        Typeface contactFormTitleTypeface = Typeface.createFromAsset(context.getAssets(),context.getResources().getString(R.string.light_fonts));
        contactFormTitle.setTypeface(contactFormTitleTypeface);
        contactFormTitle.setText("Fill the below form");

        editEmailStr=(EditText) findViewById(R.id.contact_email) ;
        Typeface  editEmailStrTypeface = Typeface.createFromAsset(context.getAssets(),context.getResources().getString(R.string.light_fonts));
        editEmailStr.setTypeface(editEmailStrTypeface);
        editEmailStr.setHint("Your Email");

        editNameStr=(EditText) findViewById(R.id.contact_name) ;
        Typeface  editNameStrTypeface = Typeface.createFromAsset(context.getAssets(),context.getResources().getString(R.string.light_fonts));
        editNameStr.setTypeface(editNameStrTypeface);
        editNameStr.setHint("Your Name");

        editMessageStr=(EditText) findViewById(R.id.contact_msg) ;
        Typeface  editMessageStrTypeface = Typeface.createFromAsset(context.getAssets(),context.getResources().getString(R.string.light_fonts));
        editMessageStr.setTypeface(editMessageStrTypeface);
        editMessageStr.setHint("Your Message");

        submit = (Button) findViewById(R.id.submit_cont);
        Typeface  submitTypeface = Typeface.createFromAsset(context.getAssets(),context.getResources().getString(R.string.regular_fonts));
        submit.setTypeface(submitTypeface);
        submit.setHint("Submit");

        editEmailStr.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    email_vg.setBackground(getResources().getDrawable(R.drawable.shape_edit_text_active));
                }
                else {
                    email_vg.setBackground(getResources().getDrawable(R.drawable.shape_editttext));

                }
            }
        });
        editNameStr.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity(), "Submitted successfully", Toast.LENGTH_SHORT).show();

                SubmmitClicked();

            }
        });

        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(mActionBarToolbar);
          mActionBarToolbar.setTitle("");
        mActionBarToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_icon_close));
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    public void SubmmitClicked(){
        regEmailStr = editEmailStr.getText().toString().trim();
        regNameStr = editNameStr.getText().toString().trim();
        regMessageStr = editMessageStr.getText().toString().trim();
        if (!regNameStr.equals("")){
            if (Util.checkNetwork(ContactUsActivity.this) == true) {
                asynContactUs = new AsynContactUs();
                asynContactUs.execute();
            } else {
                Util.showToast(context, Util.getTextofLanguage(ContactUsActivity.this, Util.SLOW_INTERNET_CONNECTION, Util.DEFAULT_SLOW_INTERNET_CONNECTION));

            }
        }else{
            Toast.makeText(context, "Please Enter Your Name", Toast.LENGTH_SHORT).show();
        }


    }

    private class AsynContactUs extends AsyncTask<String, Void, Void> {
        String contName;
        JSONObject myJson = null;
        int status;

        String contMessage;
        String responseStr;

        @Override
        protected Void doInBackground(String... params) {
            String urlRouteList = Util.rootUrl().trim() + Util.ContactUs.trim();

            try{
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("name", String.valueOf(regNameStr));
                httppost.addHeader("email", String.valueOf(regEmailStr));
                regMessageStr = regMessageStr.replaceAll("(\r\n|\n\r|\r|\n|<br />)", " ");
                httppost.addHeader("message", String.valueOf(regMessageStr));
//                httppost.addHeader("message","Test Message  Test Message");
                httppost.addHeader("lang_code",Util.getTextofLanguage(context,Util.SELECTED_LANGUAGE_CODE,Util.DEFAULT_SELECTED_LANGUAGE_CODE));
                HttpResponse response = httpclient.execute(httppost);
                responseStr = EntityUtils.toString(response.getEntity());
            } catch (ClientProtocolException e) {
                e.printStackTrace();
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
                statusmsg = myJson.optString("status");
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(context,sucessMsg,Toast.LENGTH_LONG).show();

            try {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.hide();
                    pDialog = null;
                }
            } catch (IllegalArgumentException ex) {
                status = 0;

            }

            editMessageStr.setText("");
            editNameStr.setText("");
            editEmailStr.setText("");
            editMessageStr.setError(null);
            editNameStr.setError(null);
            editEmailStr.setError(null);
        }

        @Override
        protected void onPreExecute() {

            pDialog = new ProgressBarHandler(context);
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
