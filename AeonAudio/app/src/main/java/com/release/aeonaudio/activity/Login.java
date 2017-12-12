package com.release.aeonaudio.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.home.apisdk.apiController.LoginAsynTask;
import com.home.apisdk.apiModel.Login_input;
import com.home.apisdk.apiModel.Login_output;
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

import static com.release.aeonaudio.R.id.loginTextView;
import static com.release.aeonaudio.activity.SplashScreenActivity.decodeSampledBitmapFromResource;

/**
 * Created by Muvi on 6/27/2017.
 */


public class Login extends AppCompatActivity implements LoginAsynTask.LoinDetails{
    EditText editEmailStr,editPasswordStr;
    String email,password;
    SharedPreferences sharedpreferences;
    Login_input login_input;
    ProgressBarHandler pDialog;
    TextView forgotPassword,signUpTextView;

    LinearLayout userfield_vg,password_vg;
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
    Button loginButton ;
    LoginButton loginWithFacebookButton;
    /*********fb****/

    int corePoolSize = 60;
    int maximumPoolSize = 80;
    int keepAliveTime = 10;
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
    Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        getWindow().setBackgroundDrawableResource(R.drawable.saplashscreen2);

        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        sharedpreferences = getSharedPreferences(Util.LOGINPREFERENCE, Context.MODE_PRIVATE);
        pDialog = new ProgressBarHandler(this);
        userfield_vg = (LinearLayout) findViewById(R.id.userfield_vg);
        password_vg = (LinearLayout) findViewById(R.id.password_vg);

        ///background image


        editEmailStr = (EditText)findViewById(R.id.editEmailStr);
        email = editEmailStr.getText().toString().trim();
        editPasswordStr = (EditText)findViewById(R.id.editPasswordStr);
        password = editPasswordStr.getText().toString().trim();
        loginButton = (Button) findViewById(R.id.loginButton);
        forgotPassword=(TextView)findViewById(R.id.forgotPasswordTextView);
        signUpTextView=(TextView)findViewById(R.id.signUpTextView);
        loginButton = (Button) findViewById(R.id.loginButton);
        /*Typeface loginButtonTypeface = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.regular_fonts));
        loginButton.setTypeface(loginButtonTypeface);*/

        loginButton.setText(Util.getTextofLanguage(Login.this, Util.LOGIN, Util.DEFAULT_LOGIN));
        loginWithFacebookButton = (LoginButton) findViewById(R.id.loginWithFacebookButton);
        loginWithFacebookButton.setVisibility(View.GONE);

        loginButton.setText(Util.getTextofLanguage(Login.this, Util.LOGIN, Util.DEFAULT_LOGIN));
        ///////UI review changes ///////////
      /*  mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        mActionBarToolbar.setTitle("");
        mActionBarToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_icon_close));
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });*/
        ////type face
        Typeface editPasswordStrTypeface = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.light_fonts));
        editPasswordStr.setTypeface(editPasswordStrTypeface);
        editEmailStr.setTypeface(editPasswordStrTypeface);
        editPasswordStr.setHint(Util.getTextofLanguage(Login.this, Util.TEXT_PASSWORD, Util.DEFAULT_TEXT_PASSWORD));
        editEmailStr.setHint(Util.getTextofLanguage(Login.this, Util.TEXT_EMIAL, Util.DEFAULT_TEXT_EMIAL));

        editEmailStr.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    userfield_vg.setBackground(getResources().getDrawable(R.drawable.shape_edit_text_active));
                }
                else {
                    userfield_vg.setBackground(getResources().getDrawable(R.drawable.shape_editttext));

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

        //login button clicked
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButtonClicked();
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent detailsIntent = new Intent(Login.this, ForgotPassword.class);
                detailsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                startActivity(detailsIntent);
                finish();
            }
        });
        //add underline below text
//        forgotPassword.setPaintFlags(forgotPassword.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        Typeface signUpTextViewViewTypeface = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.light_fonts));
        signUpTextView.setTypeface(signUpTextViewViewTypeface);
        Typeface forgotPasswordViewTypeface = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.light_fonts));
        forgotPassword.setTypeface(forgotPasswordViewTypeface);
        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent detailsIntent = new Intent(Login.this, Register.class);
                detailsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(detailsIntent);
                finish();
            }
        });
        //add underline below text
//        signUpTextView.setPaintFlags(signUpTextView.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

        TextView fbLoginTextView= (TextView) findViewById(R.id.fbLoginTextView);
        Typeface loginWithFbButtonTypeface = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.regular_fonts));
        fbLoginTextView.setTypeface(loginWithFbButtonTypeface);


        callbackManager= CallbackManager.Factory.create();


        loginWithFacebookButton.setReadPermissions("public_profile", "email","user_friends");

        btnLogin= (LinearLayout) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("SUBHA","btnLogin");

               loginWithFacebookButton.performClick();

                loginWithFacebookButton.setPressed(true);

                loginWithFacebookButton.invalidate();

                loginWithFacebookButton.registerCallback(callbackManager, mCallBack);

                loginWithFacebookButton.setPressed(false);

                loginWithFacebookButton.invalidate();

            }
        });

    }

    @Override
    public void onLoginPreExecuteStarted() {
        if (!pDialog.isShowing()){
            pDialog.show();
        }

    }

    @Override
    public void onLoginPostExecuteCompleted(Login_output login_output, int status, String message) {

        if (pDialog.isShowing()){
            pDialog.hide();
        }

        Log.v("Niihar",login_output.getEmail()+login_output.getMsg()+status+email+password+status);
        Toast.makeText(this, login_output.getMsg(), Toast.LENGTH_SHORT).show();
        if (status==200){
            SharedPreferences.Editor editor = sharedpreferences.edit();

            editor.putString("display_namePref", login_output.getDisplay_name());
            editor.putString("studio_idPref", login_output.getStudio_id());
            editor.putString("login_history_idPref", login_output.getLogin_history_id());
            editor.putString("isSubscribedPref", login_output.getIsSubscribed());
            editor.putString("useridPref", login_output.getId());
            editor.putString("emailPref", login_output.getEmail());
            Log.v("NiiharIDD",login_output.getId());

            editor.commit();
            Intent in = new Intent(Login.this, MainActivity.class);
            in.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(in);
            finish();
        }else{
            Toast.makeText(Login.this,login_output.getMsg(),Toast.LENGTH_LONG).show();

        }


    }

    public void loginButtonClicked() {

        email = editEmailStr.getText().toString().trim();
        password = editPasswordStr.getText().toString().trim();

        boolean isNetwork = Util.checkNetwork(Login.this);
        if (isNetwork) {
            if ((!email.equals("")) && (!password.equals(""))) {
                boolean isValidEmail = Util.isValidMail(email);
                if (isValidEmail == true) {

                    login_input = new Login_input();
                    login_input.setAuthToken(Util.authTokenStr);
                    login_input.setEmail(email);
                    login_input.setPassword(password);
                    if (Util.checkNetwork(Login.this) == true) {
                        LoginAsynTask loginAsynTask = new LoginAsynTask(login_input, this , Login.this);
                        loginAsynTask.execute();
                    }else{
                        Util.showToast(Login.this,Util.getTextofLanguage(Login.this,Util.SLOW_INTERNET_CONNECTION,Util.DEFAULT_SLOW_INTERNET_CONNECTION));

                    }

                } else {
                    Util.showToast(Login.this, Util.getTextofLanguage(Login.this, Util.OOPS_INVALID_EMAIL, Util.DEFAULT_OOPS_INVALID_EMAIL));
                }
            } else {
                Util.showToast(Login.this, Util.getTextofLanguage(Login.this, Util.ENTER_REGISTER_FIELDS_DATA, Util.DEFAULT_ENTER_REGISTER_FIELDS_DATA));
            }
        } else {
            Util.showToast(Login.this, Util.getTextofLanguage(Login.this, Util.NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION));

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

    private String FbEmail;
    private FacebookCallback<LoginResult> mCallBack = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            Log.v("SUBHA","onSuccess");

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
                                    loginButton.setVisibility(View.GONE);
                                    loginWithFacebookButton.setVisibility(View.GONE);
                                    btnLogin.setVisibility(View.GONE);
                                    if (Util.checkNetwork(Login.this) == true) {
                                        Log.v("SUBHA","GFG");
                                        AsynCheckFbUserDetails asynCheckFbUserDetails = new AsynCheckFbUserDetails();
                                        asynCheckFbUserDetails.executeOnExecutor(threadPoolExecutor);
                                    }else{
                                        Util.showToast(Login.this,Util.getTextofLanguage(Login.this,Util.SLOW_INTERNET_CONNECTION,Util.DEFAULT_SLOW_INTERNET_CONNECTION));

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
            Log.v("SUBHA","onCancel");

            loginButton.setVisibility(View.VISIBLE);
            loginWithFacebookButton.setVisibility(View.GONE);
            btnLogin.setVisibility(View.VISIBLE);
            Util.showToast(Login.this, Util.getTextofLanguage(Login.this,Util.DETAILS_NOT_FOUND_ALERT,Util.DEFAULT_DETAILS_NOT_FOUND_ALERT));

            //Toast.makeText(LoginActivity.this, Util.getTextofLanguage(LoginActivity.this, Util.DETAILS_NOT_FOUND_ALERT, Util.DEFAULT_DETAILS_NOT_FOUND_ALERT), Toast.LENGTH_LONG).show();
            //progressDialog.dismiss();
        }

        @Override
        public void onError(FacebookException e) {
            Log.v("SUBHA","onError");

            loginButton.setVisibility(View.VISIBLE);
            loginWithFacebookButton.setVisibility(View.GONE);
            btnLogin.setVisibility(View.VISIBLE);
            Util.showToast(Login.this, Util.getTextofLanguage(Login.this,Util.DETAILS_NOT_FOUND_ALERT,Util.DEFAULT_DETAILS_NOT_FOUND_ALERT));

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
                android.app.AlertDialog.Builder dlgAlert = new android.app.AlertDialog.Builder(Login.this, R.style.MyAlertDialogStyle);
                dlgAlert.setMessage(Util.getTextofLanguage(Login.this, Util.DETAILS_NOT_FOUND_ALERT, Util.DEFAULT_DETAILS_NOT_FOUND_ALERT));
                dlgAlert.setTitle(Util.getTextofLanguage(Login.this, Util.SORRY, Util.DEFAULT_SORRY));
                dlgAlert.setMessage(Util.getTextofLanguage(Login.this,Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK));
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(Util.getTextofLanguage(Login.this,Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK),
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
                if (Util.checkNetwork(Login.this) == true) {
                    asynFbRegDetails = new AsynFbRegDetails();
                    asynFbRegDetails.executeOnExecutor(threadPoolExecutor);
                }else{
                    Util.showToast(Login.this,Util.getTextofLanguage(Login.this,Util.SLOW_INTERNET_CONNECTION,Util.DEFAULT_SLOW_INTERNET_CONNECTION));

                }



            }else{
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.hide();
                    pDialog = null;
                }
                android.app.AlertDialog.Builder dlgAlert = new android.app.AlertDialog.Builder(Login.this, R.style.MyAlertDialogStyle);
                dlgAlert.setMessage(Util.getTextofLanguage(Login.this, Util.DETAILS_NOT_FOUND_ALERT, Util.DEFAULT_DETAILS_NOT_FOUND_ALERT));
                dlgAlert.setTitle(Util.getTextofLanguage(Login.this, Util.SORRY, Util.DEFAULT_SORRY));
                dlgAlert.setMessage(Util.getTextofLanguage(Login.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK));
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(Util.getTextofLanguage(Login.this,Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK),
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
            pDialog = new ProgressBarHandler(Login.this);
            pDialog.show();

        }
    }
    private class   AsynFbRegDetails extends AsyncTask<Void, Void, Void> {
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
              //  httppost.addHeader("device_type", "1");
              //  httppost.addHeader("device_id", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));

                httppost.addHeader("lang_code",Util.getTextofLanguage(Login.this, Util.SELECTED_LANGUAGE_CODE, Util.DEFAULT_SELECTED_LANGUAGE_CODE));

                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());


                } catch (org.apache.http.conn.ConnectTimeoutException e){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            statusCode = 0;
                            Util.showToast(Login.this, Util.getTextofLanguage(Login.this,Util.SLOW_INTERNET_CONNECTION,Util.DEFAULT_SLOW_INTERNET_CONNECTION));

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
                android.app.AlertDialog.Builder dlgAlert = new android.app.AlertDialog.Builder(Login.this, R.style.MyAlertDialogStyle);
                dlgAlert.setMessage(Util.getTextofLanguage(Login.this, Util.DETAILS_NOT_FOUND_ALERT, Util.DEFAULT_DETAILS_NOT_FOUND_ALERT));
                dlgAlert.setTitle(Util.getTextofLanguage(Login.this, Util.SORRY, Util.DEFAULT_SORRY));
                dlgAlert.setMessage(Util.getTextofLanguage(Login.this,Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK));
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(Util.getTextofLanguage(Login.this,Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK),
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

                    Intent in = new Intent(Login.this, MainActivity.class);
                    in.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(in);
                    finish();
                } else {
                    android.app.AlertDialog.Builder dlgAlert = new android.app.AlertDialog.Builder(Login.this, R.style.MyAlertDialogStyle);
                    dlgAlert.setMessage(Util.getTextofLanguage(Login.this, Util.EMAIL_PASSWORD_INVALID, Util.DEFAULT_EMAIL_PASSWORD_INVALID));
                    dlgAlert.setTitle(Util.getTextofLanguage(Login.this, Util.SORRY, Util.DEFAULT_SORRY));
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(Login.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                    dlgAlert.setCancelable(false);
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(Login.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    dlgAlert.create().show();
                }
            }else{
                android.app.AlertDialog.Builder dlgAlert = new android.app.AlertDialog.Builder(Login.this, R.style.MyAlertDialogStyle);
                dlgAlert.setMessage(Util.getTextofLanguage(Login.this, Util.NO_DETAILS_AVAILABLE, Util.DEFAULT_NO_DETAILS_AVAILABLE));                dlgAlert.setTitle(Util.getTextofLanguage(Login.this, Util.SORRY, Util.DEFAULT_SORRY));
                dlgAlert.setPositiveButton(Util.getTextofLanguage(Login.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(Util.getTextofLanguage(Login.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
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
            pDialog = new ProgressBarHandler(Login.this);
            pDialog.show();

        }
    }

}
