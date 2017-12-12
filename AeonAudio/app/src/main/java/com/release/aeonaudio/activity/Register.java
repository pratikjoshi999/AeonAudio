package com.release.aeonaudio.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.home.apisdk.apiController.RegistrationAsynTask;
import com.home.apisdk.apiModel.Registration_input;
import com.home.apisdk.apiModel.Registration_output;
import com.release.aeonaudio.R;
import com.release.aeonaudio.utils.ProgressBarHandler;
import com.release.aeonaudio.utils.Util;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.release.aeonaudio.R.id.alreadyHaveALoginButton;
import static com.release.aeonaudio.R.id.forgotPasswordTextView;

/**
 * Created by Muvi on 6/27/2017.
 */

public class Register extends AppCompatActivity implements RegistrationAsynTask.RegistrationDetails{
    /*********fb****/

    String fbUserId = "";
    String fbEmail = "";
    String fbName = "";
    private LinearLayout btnLogin;
    private ProgressBarHandler progressDialog;
    private CallbackManager callbackManager;
    AsynCheckFbUserDetails asynCheckFbUserDetails;
    AsynFbRegDetails asynFbRegDetails;
    String UniversalIsSubscribed = "";
    LoginButton loginWithFacebookButton;
    /*********fb****/

    int corePoolSize = 60;
    int maximumPoolSize = 80;
    int keepAliveTime = 10;
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
    Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);

    Toolbar mActionBarToolbar;
    EditText editNameStr,editEmailStr,editPasswordStr,editConfirmPasswordStr;
    Button registerButton ;
    TextView loginTextView;
    String name,email,password,conformPassword;
    SharedPreferences sharedpreferences;
    ProgressBarHandler pDialog;
    TextView terms_tv,forgotPasswordTextView;
    String emailPref;
    String display_namePref;
    String studio_idPref;
    String isSubscribedPref;
    LinearLayout email_vg,password_vg,conformpassword_vg,name_vg;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_register);
        getWindow().setBackgroundDrawableResource(R.drawable.saplashscreen2);

        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //  ImageView imageResize = (ImageView) findViewById(R.id.background_register);
        //get the current device height  and width
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
       // imageResize.setImageBitmap(decodeSampledBitmapFromResourcee(getResources(), R.drawable.splash_screen, dpWidth, dpHeight));

        email_vg = (LinearLayout) findViewById(R.id.email_vg);
        password_vg = (LinearLayout) findViewById(R.id.password_vg);
        conformpassword_vg = (LinearLayout) findViewById(R.id.conformpassword_vg);
        name_vg = (LinearLayout) findViewById(R.id.name_vg);

        sharedpreferences = getSharedPreferences(Util.LOGINPREFERENCE, Context.MODE_PRIVATE);
        pDialog = new ProgressBarHandler(this);
        //ui review changes
//        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(mActionBarToolbar);
//        mActionBarToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_icon_close));
//        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
        editNameStr = (EditText) findViewById(R.id.editNameStr);

        editEmailStr = (EditText) findViewById(R.id.editEmailStr);
        editPasswordStr = (EditText) findViewById(R.id.editPasswordStr);
        editConfirmPasswordStr = (EditText) findViewById(R.id.editConfirmPasswordStr);
        registerButton = (Button) findViewById(R.id.registerButton);
        loginTextView = (TextView) findViewById(alreadyHaveALoginButton);
        terms_tv= (TextView) findViewById(R.id.terms_tv);
        forgotPasswordTextView= (TextView) findViewById(R.id.forgotPasswordTextView);

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
        editPasswordStr.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    password_vg.setBackground(getResources().getDrawable(R.drawable.shape_edit_text_active));
                }
                else {
                    password_vg.setBackground(getResources().getDrawable(R.drawable.shape_editttext));

                }
            }
        });
        editConfirmPasswordStr.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    conformpassword_vg.setBackground(getResources().getDrawable(R.drawable.shape_edit_text_active));
                }
                else {
                    conformpassword_vg.setBackground(getResources().getDrawable(R.drawable.shape_editttext));

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






        Typeface editNameTypeface = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.light_fonts));
        editNameStr.setTypeface(editNameTypeface);
        Typeface editEmailStrTypeface = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.light_fonts));
        editEmailStr.setTypeface(editEmailStrTypeface);
        Typeface editPasswordTypeface = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.light_fonts));
        editPasswordStr.setTypeface(editPasswordTypeface);
        Typeface editConfirmPasswordTypeface = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.light_fonts));
        editConfirmPasswordStr.setTypeface(editConfirmPasswordTypeface);
        Typeface registerButtonTypeface = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.light_fonts));
        registerButton.setTypeface(registerButtonTypeface);
        Typeface loginTextViewTypeface = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.light_fonts));
        loginTextView.setTypeface(loginTextViewTypeface);
        Typeface terms_tvwTypeface = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.light_fonts));
        terms_tv.setTypeface(terms_tvwTypeface);
        Typeface forgotPasswordTextView_tvwTypeface = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.light_fonts));
        forgotPasswordTextView.setTypeface(forgotPasswordTextView_tvwTypeface);
        loginWithFacebookButton = (LoginButton) findViewById(R.id.loginWithFacebookButton);
        loginWithFacebookButton.setVisibility(View.GONE);


        editNameStr.setHint(Util.getTextofLanguage(Register.this, Util.NAME_HINT, Util.DEFAULT_NAME_HINT));
        editEmailStr.setHint(Util.getTextofLanguage(Register.this, Util.TEXT_EMIAL, Util.DEFAULT_TEXT_EMIAL));
        editPasswordStr.setHint(Util.getTextofLanguage(Register.this, Util.TEXT_PASSWORD, Util.DEFAULT_TEXT_PASSWORD));
        editConfirmPasswordStr.setHint(Util.getTextofLanguage(Register.this,Util.CONFIRM_PASSWORD,Util.DEFAULT_CONFIRM_PASSWORD));
        registerButton.setText(Util.getTextofLanguage(Register.this, Util.BTN_REGISTER, Util.DEFAULT_BTN_REGISTER));
//        loginTextView.setText(Util.getTextofLanguage(Register.this, Util.LOGIN, Util.DEFAULT_LOGIN));

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerButtonClicked();
            }
        });
        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent detailsIntent = new Intent(Register.this, Login.class);
                detailsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(detailsIntent);
                finish();
            }
        });
        //add underline below text
//        loginTextView.setPaintFlags(loginTextView.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        TextView fbLoginTextView= (TextView) findViewById(R.id.fbLoginTextView);
        Typeface loginWithFbButtonTypeface = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.regular_fonts));
        fbLoginTextView.setTypeface(loginWithFbButtonTypeface);


        callbackManager= CallbackManager.Factory.create();


        loginWithFacebookButton.setReadPermissions("public_profile", "email","user_friends");

        btnLogin= (LinearLayout) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginWithFacebookButton.performClick();

                loginWithFacebookButton.setPressed(true);

                loginWithFacebookButton.invalidate();

                loginWithFacebookButton.registerCallback(callbackManager, mCallBack);

                loginWithFacebookButton.setPressed(false);

                loginWithFacebookButton.invalidate();

            }
        });
        terms_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://audiostreaming.muvi.com/page/terms-privacy-policy"));
                startActivity(intent);
            }
        });

    }



    public void registerButtonClicked() {

        name = editNameStr.getText().toString().trim();
        email = editEmailStr.getText().toString();
        password = editPasswordStr.getText().toString();
        conformPassword = editConfirmPasswordStr.getText().toString();


        boolean isNetwork = Util.checkNetwork(Register.this);
        if (isNetwork == true) {
            if (!name.matches("") && (!email.matches("")) && (!password.matches("")) && !name.equals("")) {
                boolean isValidEmail = Util.isValidMail(email);
                if (isValidEmail) {
                    if (password.equals(conformPassword)) {
                        Registration_input registration_input = new Registration_input();
                        registration_input.setAuthToken(Util.authTokenStr);
                        registration_input.setName(name);
                        registration_input.setEmail(email);
                        registration_input.setPassword(password);
                        Log.v("Niihhar",registration_input.getPassword());
                        if (Util.checkNetwork(Register.this) == true) {
                            RegistrationAsynTask registrationAsynTask= new RegistrationAsynTask(registration_input,this,Register.this);
                            registrationAsynTask.execute();
                        }else{
                            Util.showToast(Register.this,Util.getTextofLanguage(Register.this,Util.SLOW_INTERNET_CONNECTION,Util.DEFAULT_SLOW_INTERNET_CONNECTION));

                        }

                    } else {
                        Util.showToast(Register.this,Util.getTextofLanguage(Register.this,Util.PASSWORDS_DO_NOT_MATCH,Util.DEFAULT_PASSWORDS_DO_NOT_MATCH));
                    }
                } else {
                    Util.showToast(Register.this,Util.getTextofLanguage(Register.this,Util.OOPS_INVALID_EMAIL,Util.DEFAULT_OOPS_INVALID_EMAIL));
                }
            } else {
                Util.showToast(Register.this,Util.getTextofLanguage(Register.this,Util.ENTER_REGISTER_FIELDS_DATA,Util.DEFAULT_ENTER_REGISTER_FIELDS_DATA));
            }
        } else {
            Util.showToast(Register.this,Util.getTextofLanguage(Register.this,Util.NO_INTERNET_CONNECTION,Util.DEFAULT_NO_INTERNET_CONNECTION));
        }


    }

    @Override
    public void onRegistrationDetailsPreExecuteStarted() {
        if (!pDialog.isShowing()){
            pDialog.show();
        }
    }

    @Override
    public void onRegistrationDetailsPostExecuteCompleted(Registration_output registration_output, int status, String message) {
        if (pDialog.isShowing()){
            pDialog.hide();
        }
     /*   SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString("display_namePref", registration_output.getDisplay_name());
        editor.putString("studio_idPref", registration_output.getStudio_id());
        editor.putString("isSubscribedPref", registration_output.getIsSubscribed());
        editor.putString("useridPref", registration_output.getId());
        editor.putString("emailPref", registration_output.getEmail());
        editor.commit();*/
      Log.v("Niihhar_register",registration_output.getEmail()+registration_output.getMsg()+"\\\\"+status);
        Toast.makeText(this, registration_output.getMsg(), Toast.LENGTH_SHORT).show();
        if (status==200){
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("display_namePref", registration_output.getDisplay_name());
            editor.putString("studio_idPref", registration_output.getStudio_id());
            editor.putString("isSubscribedPref", registration_output.getIsSubscribed());
            editor.putString("useridPref", registration_output.getId());
            editor.putString("emailPref", registration_output.getEmail());
            editor.putString("login_history_idPref", registration_output.getLoginhistory_id());

            editor.commit();
            Intent in = new Intent(Register.this, MainActivity.class);
            in.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(in);
            finish();
        }else{
            Toast.makeText(Register.this,registration_output.getMsg(),Toast.LENGTH_LONG).show();
        }
    }

    /*  ************facebook*******--------------/
            *
            *
            */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private FacebookCallback<LoginResult> mCallBack = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {

            //progressDialog.dismiss();

            // App code
            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {


                            JSONObject json = response.getJSONObject();
                            try {
                                if(json != null){



                                    String fName = "";
                                    if ((json.has("id")) && json.getString("id").trim() != null && !json.getString("id").trim().isEmpty() && !json.getString("id").trim().equals("null") && !json.getString("id").trim().matches("")) {
                                        fbUserId = json.getString("id");
                                    }
                                    if ((json.has("first_name")) && json.getString("first_name").trim() != null && !json.getString("first_name").trim().isEmpty() && !json.getString("first_name").trim().equals("null") && !json.getString("first_name").trim().matches("")) {
                                        if ((json.has("last_name")) && json.getString("last_name").trim() != null && !json.getString("last_name").trim().isEmpty() && !json.getString("last_name").trim().equals("null") && !json.getString("last_name").trim().matches("")) {
                                            fbName = json.getString("first_name") + " " + json.getString("last_name");
                                        }
                                        fName =  json.getString("first_name");
                                    } else {

                                        if ((json.has("last_name")) && json.getString("last_name").trim() != null && !json.getString("last_name").trim().isEmpty() && !json.getString("last_name").trim().equals("null") && !json.getString("last_name").trim().matches("")) {
                                            fbName = json.getString("last_name");
                                            fName =  json.getString("last_name");
                                        }else{
                                            if ((json.has("name")) && json.getString("name").trim() != null && !json.getString("name").trim().isEmpty() && !json.getString("name").trim().equals("null") && !json.getString("name").trim().matches("")) {
                                                fbName = json.getString("name");
                                                fName = json.getString("name").replace(" ","").trim();
                                            }
                                        }

                                    }

                                  /*  if (fbName!=null && fbName.matches("")){
                                        fbName = fbUserId;
                                    }*/
                                    if ((json.has("email")) && json.getString("email").trim() != null && !json.getString("email").trim().isEmpty() && !json.getString("email").trim().equals("null") && !json.getString("email").trim().matches("")) {
                                        fbEmail = json.getString("email");
                                    } else {
                                        if(fbName!=null && !fbName.matches("")){
                                            fbEmail = fName+ "@facebook.com";
                                        }else {
                                            fbName = fbUserId;
                                            fbEmail = fbUserId + "@facebook.com";
                                        }

                                    }
                                    registerButton.setVisibility(View.GONE);
                                    loginWithFacebookButton.setVisibility(View.GONE);
                                    btnLogin.setVisibility(View.GONE);
                                    if (Util.checkNetwork(Register.this) == true) {
                                        AsynCheckFbUserDetails asynCheckFbUserDetails = new AsynCheckFbUserDetails();
                                        asynCheckFbUserDetails.executeOnExecutor(threadPoolExecutor);
                                    }else{
                                        Util.showToast(Register.this,Util.getTextofLanguage(Register.this,Util.SLOW_INTERNET_CONNECTION,Util.DEFAULT_SLOW_INTERNET_CONNECTION));

                                    }


//
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }




//
                        }

                    });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender, birthday");
            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {
            registerButton.setVisibility(View.VISIBLE);
            loginWithFacebookButton.setVisibility(View.GONE);
            btnLogin.setVisibility(View.VISIBLE);
            Util.showToast(Register.this, Util.getTextofLanguage(Register.this,Util.DETAILS_NOT_FOUND_ALERT,Util.DEFAULT_DETAILS_NOT_FOUND_ALERT));

            //Toast.makeText(LoginActivity.this, Util.getTextofLanguage(LoginActivity.this, Util.DETAILS_NOT_FOUND_ALERT, Util.DEFAULT_DETAILS_NOT_FOUND_ALERT), Toast.LENGTH_LONG).show();
            //progressDialog.dismiss();
        }

        @Override
        public void onError(FacebookException e) {
            registerButton.setVisibility(View.VISIBLE);
            loginWithFacebookButton.setVisibility(View.GONE);
            btnLogin.setVisibility(View.VISIBLE);
            Util.showToast(Register.this, Util.getTextofLanguage(Register.this,Util.DETAILS_NOT_FOUND_ALERT,Util.DEFAULT_DETAILS_NOT_FOUND_ALERT));

            //  Toast.makeText(LoginActivity.this, Util.getTextofLanguage(LoginActivity.this, Util.DETAILS_NOT_FOUND_ALERT, Util.DEFAULT_DETAILS_NOT_FOUND_ALERT), Toast.LENGTH_LONG).show();

            //progressDialog.dismiss();
        }
    };
    private class AsynCheckFbUserDetails extends AsyncTask<Void, Void, Void> {
        ProgressBarHandler pDialog;

        int status;
        String responseStr;
        int isNewUserStr = 1;
        JSONObject myJson = null;

        @Override
        protected Void doInBackground(Void... params) {

            String urlRouteList = Util.rootUrl().trim() + Util.fbUserExistsUrl.trim();
            try {
                HttpClient httpclient=new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("fb_userid",fbUserId.trim());

                Log.v("SUBHA","fb userid"+fbUserId );

                // Execute HTTP Post Request
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());

                } catch (org.apache.http.conn.ConnectTimeoutException e){

                    status = 0;

                } catch (IOException e) {
                    status = 0;
                    e.printStackTrace();
                }
                if(responseStr!=null){
                    myJson = new JSONObject(responseStr);
                    status = Integer.parseInt(myJson.optString("code"));
                    isNewUserStr = Integer.parseInt(myJson.optString("is_newuser"));
                }

            }
            catch (Exception e) {
                status = 0;

            }

            return null;
        }


        protected void onPostExecute(Void result) {


            if(responseStr == null){
                status = 0;

            }
            if (status == 0) {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.hide();
                    pDialog = null;
                }
                android.app.AlertDialog.Builder dlgAlert = new android.app.AlertDialog.Builder(Register.this, R.style.MyAlertDialogStyle);
                dlgAlert.setMessage(Util.getTextofLanguage(Register.this, Util.DETAILS_NOT_FOUND_ALERT, Util.DEFAULT_DETAILS_NOT_FOUND_ALERT));
                dlgAlert.setTitle(Util.getTextofLanguage(Register.this, Util.SORRY, Util.DEFAULT_SORRY));
                dlgAlert.setMessage(Util.getTextofLanguage(Register.this,Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK));
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(Util.getTextofLanguage(Register.this,Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                dlgAlert.create().show();
            }

            if (status == 200){
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.hide();
                    pDialog = null;
                }
                if (Util.checkNetwork(Register.this) == true) {
                    asynFbRegDetails = new AsynFbRegDetails();
                    asynFbRegDetails.executeOnExecutor(threadPoolExecutor);
                }else{
                    Util.showToast(Register.this,Util.getTextofLanguage(Register.this,Util.SLOW_INTERNET_CONNECTION,Util.DEFAULT_SLOW_INTERNET_CONNECTION));

                }



            }else{
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.hide();
                    pDialog = null;
                }
                android.app.AlertDialog.Builder dlgAlert = new android.app.AlertDialog.Builder(Register.this, R.style.MyAlertDialogStyle);
                dlgAlert.setMessage(Util.getTextofLanguage(Register.this, Util.DETAILS_NOT_FOUND_ALERT, Util.DEFAULT_DETAILS_NOT_FOUND_ALERT));
                dlgAlert.setTitle(Util.getTextofLanguage(Register.this, Util.SORRY, Util.DEFAULT_SORRY));
                dlgAlert.setMessage(Util.getTextofLanguage(Register.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK));
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(Util.getTextofLanguage(Register.this,Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                dlgAlert.create().show();
            }



        }

        @Override
        protected void onPreExecute() {
            AccessToken accessToken = AccessToken.getCurrentAccessToken();
            if(accessToken != null){
                LoginManager.getInstance().logOut();
            }
            pDialog = new ProgressBarHandler(Register.this);
            pDialog.show();

        }
    }
    private class AsynFbRegDetails extends AsyncTask<Void, Void, Void> {
        // ProgressDialog pDialog;
        ProgressBarHandler pDialog;
        int statusCode;
        String loggedInIdStr;
        String responseStr;
        String isSubscribedStr;
        String loginHistoryIdStr;

        JSONObject myJson = null;

        @Override
        protected Void doInBackground(Void... params) {

            String urlRouteList = Util.rootUrl().trim() + Util.fbRegUrl.trim();
            try {
                HttpClient httpclient=new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("name", fbName.trim());
                httppost.addHeader("email", fbEmail.trim());
                httppost.addHeader("password","");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("fb_userid", fbUserId.trim());
               // httppost.addHeader("device_type", "1");
              //  httppost.addHeader("device_id", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));

                httppost.addHeader("lang_code",Util.getTextofLanguage(Register.this, Util.SELECTED_LANGUAGE_CODE, Util.DEFAULT_SELECTED_LANGUAGE_CODE));

                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());


                } catch (org.apache.http.conn.ConnectTimeoutException e){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            statusCode = 0;
                            Util.showToast(Register.this, Util.getTextofLanguage(Register.this,Util.SLOW_INTERNET_CONNECTION,Util.DEFAULT_SLOW_INTERNET_CONNECTION));

                            //  Toast.makeText(LoginActivity.this, Util.getTextofLanguage(LoginActivity.this,Util.SLOW_INTERNET_CONNECTION,Util.DEFAULT_SLOW_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();

                        }

                    });

                } catch (IOException e) {
                    statusCode = 0;

                    e.printStackTrace();
                }
                if(responseStr!=null){
                    myJson = new JSONObject(responseStr);
                    statusCode = Integer.parseInt(myJson.optString("code"));
                    loggedInIdStr = myJson.optString("id");
                    isSubscribedStr = myJson.optString("isSubscribed");
                    UniversalIsSubscribed = isSubscribedStr;
                    if (myJson.has("login_history_id")) {
                        loginHistoryIdStr = myJson.optString("login_history_id");
                    }


                }

            }
            catch (Exception e) {
                statusCode = 0;

            }

            return null;
        }


        protected void onPostExecute(Void result) {

            if(responseStr == null){
                try {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.hide();
                        pDialog = null;
                    }
                } catch (IllegalArgumentException ex) {
                    statusCode = 0;

                }
                android.app.AlertDialog.Builder dlgAlert = new android.app.AlertDialog.Builder(Register.this, R.style.MyAlertDialogStyle);
                dlgAlert.setMessage(Util.getTextofLanguage(Register.this, Util.DETAILS_NOT_FOUND_ALERT, Util.DEFAULT_DETAILS_NOT_FOUND_ALERT));
                dlgAlert.setTitle(Util.getTextofLanguage(Register.this, Util.SORRY, Util.DEFAULT_SORRY));
                dlgAlert.setMessage(Util.getTextofLanguage(Register.this,Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK));
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(Util.getTextofLanguage(Register.this,Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                dlgAlert.create().show();
            }

            if (statusCode > 0) {

                if (statusCode == 200) {
                    String displayNameStr = myJson.optString("display_name");
                    String emailFromApiStr = myJson.optString("email");
                    String profileImageStr = myJson.optString("profile_image");
                    SharedPreferences.Editor editor = sharedpreferences.edit();

                    editor.putString("useridPref", loggedInIdStr);
                    editor.putString("emailPref", emailFromApiStr);
                    editor.putString("display_namePref", displayNameStr);
                    editor.putString("isSubscribedPref", isSubscribedStr);
                    editor.putString("login_history_idPref", loginHistoryIdStr);

                    editor.commit();

                    Intent in = new Intent(Register.this, MainActivity.class);
                    in.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(in);
                    finish();
                } else {
                    android.app.AlertDialog.Builder dlgAlert = new android.app.AlertDialog.Builder(Register.this, R.style.MyAlertDialogStyle);
                    dlgAlert.setMessage(Util.getTextofLanguage(Register.this, Util.EMAIL_PASSWORD_INVALID, Util.DEFAULT_EMAIL_PASSWORD_INVALID));
                    dlgAlert.setTitle(Util.getTextofLanguage(Register.this, Util.SORRY, Util.DEFAULT_SORRY));
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(Register.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                    dlgAlert.setCancelable(false);
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(Register.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    dlgAlert.create().show();
                }
            }else{
                android.app.AlertDialog.Builder dlgAlert = new android.app.AlertDialog.Builder(Register.this, R.style.MyAlertDialogStyle);
                dlgAlert.setMessage(Util.getTextofLanguage(Register.this, Util.NO_DETAILS_AVAILABLE, Util.DEFAULT_NO_DETAILS_AVAILABLE));                dlgAlert.setTitle(Util.getTextofLanguage(Register.this, Util.SORRY, Util.DEFAULT_SORRY));
                dlgAlert.setPositiveButton(Util.getTextofLanguage(Register.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(Util.getTextofLanguage(Register.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                dlgAlert.create().show();
            }

        }

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressBarHandler(Register.this);
            pDialog.show();

        }

    }
    public  Bitmap decodeSampledBitmapFromResourcee(Resources res, int resId,
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
    public  int calculateInSampleSize(
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
}
