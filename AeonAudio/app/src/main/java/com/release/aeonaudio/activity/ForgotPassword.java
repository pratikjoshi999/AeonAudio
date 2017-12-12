package com.release.aeonaudio.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.home.apisdk.apiController.ForgotpassAsynTask;
import com.home.apisdk.apiModel.Forgotpassword_input;
import com.home.apisdk.apiModel.Forgotpassword_output;
import com.release.aeonaudio.R;
import com.release.aeonaudio.utils.ProgressBarHandler;
import com.release.aeonaudio.utils.Util;

/**
 * Created by Muvi on 6/27/2017.
 */

public class ForgotPassword  extends AppCompatActivity implements ForgotpassAsynTask.ForgotpassDetails {
    Toolbar mActionBarToolbar;
    ImageView logoImageView;
    EditText editEmailStr;
    Button submitButton;
    TextView logintextView;
    String loginEmailStr;
    boolean navigation=false;
    ProgressBarHandler pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        mActionBarToolbar.setBackgroundColor(getResources().getColor(R.color.transparent));
        mActionBarToolbar.setTitle(" ");
        setSupportActionBar(mActionBarToolbar);
        pDialog = new ProgressBarHandler(this);
        mActionBarToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_icon_close));
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        logoImageView = (ImageView) findViewById(R.id.logoImageView);
        editEmailStr = (EditText) findViewById(R.id.editEmailStr);
        logintextView = (TextView) findViewById(R.id.loginTextView);
        submitButton = (Button) findViewById(R.id.submitButton);

        Typeface submitButtonTypeface = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.regular_fonts));
        submitButton.setTypeface(submitButtonTypeface);

        Typeface editEmailStrTypeface = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.light_fonts));
        editEmailStr.setTypeface(editEmailStrTypeface);

        Typeface logintextViewTypeface = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.light_fonts));
        logintextView.setTypeface(logintextViewTypeface);


        editEmailStr.setHint(Util.getTextofLanguage(ForgotPassword.this, Util.TEXT_EMIAL, Util.DEFAULT_TEXT_EMIAL));
        submitButton.setText(Util.getTextofLanguage(ForgotPassword.this, Util.BTN_SUBMIT, Util.DEFAULT_BTN_SUBMIT));
        logintextView.setText(Util.getTextofLanguage(ForgotPassword.this, Util.LOGIN, Util.DEFAULT_LOGIN));

        logintextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent detailsIntent = new Intent(ForgotPassword.this, Login.class);
                detailsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(detailsIntent);
                finish();
            }
        });



        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotPasswordButtonClicked();
            }
        });
    }
    public void forgotPasswordButtonClicked() {

        loginEmailStr = editEmailStr.getText().toString().trim();
        boolean isNetwork = Util.checkNetwork(ForgotPassword.this);
        if (isNetwork) {
            boolean isValidEmail = Util.isValidMail(loginEmailStr);
            if (isValidEmail == true) {
                Forgotpassword_input forgotpassword_input = new Forgotpassword_input();
                forgotpassword_input.setAuthToken(Util.authTokenStr);
                forgotpassword_input.setEmail(loginEmailStr);
                if (Util.checkNetwork(ForgotPassword.this) == true) {
                    ForgotpassAsynTask asynForgotPasswordDetails = new ForgotpassAsynTask(forgotpassword_input,this,ForgotPassword.this);
                    asynForgotPasswordDetails.execute();
                }else{
                    Util.showToast(this,Util.getTextofLanguage(ForgotPassword.this,Util.SLOW_INTERNET_CONNECTION,Util.DEFAULT_SLOW_INTERNET_CONNECTION));

                }

            } else {

                ShowDialog(Util.getTextofLanguage(ForgotPassword.this,Util.FAILURE,Util.DEFAULT_FAILURE), Util.getTextofLanguage(ForgotPassword.this,Util.OOPS_INVALID_EMAIL,Util.DEFAULT_OOPS_INVALID_EMAIL));

            }
        } else {
            ShowDialog(Util.getTextofLanguage(ForgotPassword.this,Util.SORRY,Util.DEFAULT_SORRY), Util.getTextofLanguage(ForgotPassword.this,Util.NO_INTERNET_CONNECTION,Util.DEFAULT_NO_INTERNET_CONNECTION));

        }

    }
    public void ShowDialog(String Title,String msg)
    {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ForgotPassword.this, R.style.MyAlertDialogStyle);
        dlgAlert.setMessage(msg);
        dlgAlert.setTitle(Title);
        dlgAlert.setPositiveButton(Util.getTextofLanguage(ForgotPassword.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
        dlgAlert.setCancelable(false);
        dlgAlert.setPositiveButton(Util.getTextofLanguage(ForgotPassword.this,Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (navigation) {
                            Intent in = new Intent(ForgotPassword.this, SplashScreenActivity.class);
                            in.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(in);
                            dialog.cancel();
                        } else {
                            dialog.cancel();
                        }
                    }
                });
        dlgAlert.create().show();
    }
    @Override
    public void onForgotpassDetailsPreExecuteStarted() {
        if (!pDialog.isShowing()){
            pDialog.show();
        }

    }

    @Override
    public void onForgotpassDetailsPostExecuteCompleted(Forgotpassword_output forgotpassword_output, int status, String message) {
        if (!pDialog.isShowing()){
            pDialog.show();
        }
        Log.v("nihaar",""+status);
        if (status == 200) {

            navigation=true;
            ShowDialog("",Util.getTextofLanguage(ForgotPassword.this,Util.PASSWORD_RESET_LINK,Util.DEFAULT_PASSWORD_RESET_LINK));



        } else {
            ShowDialog(Util.getTextofLanguage(ForgotPassword.this,Util.FAILURE,Util.DEFAULT_FAILURE), Util.getTextofLanguage(ForgotPassword.this,Util.EMAIL_DOESNOT_EXISTS,Util.DEFAULT_EMAIL_DOESNOT_EXISTS));
        }
    }
}
