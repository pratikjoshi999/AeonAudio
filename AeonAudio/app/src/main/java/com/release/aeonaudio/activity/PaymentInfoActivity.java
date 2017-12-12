package com.release.aeonaudio.activity;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.release.aeonaudio.R;
import com.release.aeonaudio.adapter.CardSpinnerAdapter;
import com.release.aeonaudio.model.CardModel;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class PaymentInfoActivity extends ActionBarActivity {
    CardModel[] cardSavedArray;

    ProgressDialog pDialog;
    String existing_card_id = "";
    String isCheckedToSavetheCard = "1";

    String videoResolution = "BEST";

    SharedPreferences loginPref, countryPref;

    Toolbar mActionBarToolbar;
    boolean isCouponCodeAdded = false;
    String validCouponCode;

    final String TAG = getClass().getName();
    private int MY_SCAN_REQUEST_CODE = 100; // arbitrary int

    Spinner cardExpiryYearSpinner;
    Spinner cardExpiryMonthSpinner;
    Spinner creditCardSaveSpinner;
    Spinner spinnerCardTextView;
    private RelativeLayout creditCardLayout;
    private RelativeLayout withoutCreditCardLayout;
    private LinearLayout cardExpiryDetailsLayout;
    private CheckBox saveCardCheckbox;

    private TextView withoutCreditCardChargedPriceTextView;
    private Button nextButton;
    //private Button paywithCreditCardButton;
    //private Button paywithPaypalButton;

    private String movieVideoUrlStr = null;

    private EditText nameOnCardEditText;
    private EditText cardNumberEditText;
    private EditText securityCodeEditText;
    private Button scanButton;
    private Button payNowButton;
    private ImageButton payByPaypalButton;
    private RadioGroup paymentOptionsRadioGroup;
    private RadioButton payWithCreditCardRadioButton;
    private RadioButton payByPalRadioButton;

    private LinearLayout paymentOptionLinearLayout;

    private TextView paymentOptionsTitle;


    private Button applyButton;
    private EditText couponCodeEditText;
    // private TextView entireShowPrice;

    private TextView selectShowRadioButton;

    private TextView chargedPriceTextView;

    String cardLastFourDigitStr;
    String tokenStr;
    String cardTypeStr;
    String responseText;
    String statusStr;

    String movieStreamUniqueIdStr;
    String muviUniqueIdStr;
    String planPriceStr;
    String videoUrlStr;
    String currencyCountryCodeStr;
    String currencyIdStr;
    String currencySymbolStr;
    String videoPreview;
    String videoName = "No Name";
    private int contentTypesId = 0;
    private String movieThirdPartyUrl = null;

    int isPPV = 0;
    int isAPV = 0;
    int isConverted = 0;

    String profileIdStr;
    int expiryMonthStr = 0;
    int planIdForPaypal = 0;

    String movieNameStr = "";
    String videoduration = "";
    String movieTypeStr = "";
    String censorRatingStr = "";
    String movieDetailsStr = "";

    int expiryYearStr = 0;

    ArrayAdapter<Integer> cardExpiryYearSpinnerAdapter;
    ArrayAdapter<Integer> cardExpiryMonthSpinnerAdapter;
    CardSpinnerAdapter creditCardSaveSpinnerAdapter;
    ;
    List<Integer> yearArray = new ArrayList<Integer>(21);
    List<Integer> monthsIdArray = new ArrayList<Integer>(12);
    /*Asynctask on background thread*/
    int corePoolSize = 60;
    int maximumPoolSize = 80;
    int keepAliveTime = 10;
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
    Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);

    TextView creditCardDetailsTitleTextView;
    private String transaction_status;
    private String transaction_invoice_id;
    private String transaction_order_number;
    private String transaction_dollar_amount;
    private String transaction_amount;
    private String transaction_is_success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_info);
        videoPreview = Util.getTextofLanguage(PaymentInfoActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA);
        creditCardDetailsTitleTextView = (TextView) findViewById(R.id.creditCardDetailsTitleTextView);

        //Set toolbar
        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        mActionBarToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_icon_close));
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(PaymentInfoActivity.this);
                onBackPressed();
            }
        });
        if (pDialog == null) {
            pDialog = new ProgressDialog(PaymentInfoActivity.this, R.style.CustomDialogTheme);
            pDialog.setCancelable(false);
            pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large_Inverse);
            pDialog.setIndeterminate(false);
            pDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progress_rawable));

        }

        countryPref = getSharedPreferences(Util.COUNTRY_PREF, 0);

        loginPref = getSharedPreferences(Util.LOGIN_PREF, 0); // 0 - for private mode


        if (getIntent().getStringExtra("currencyId") != null) {
            currencyIdStr = getIntent().getStringExtra("currencyId");
        } else {
            currencyIdStr = "";
        }

        if (getIntent().getStringExtra("currencyCountryCode") != null) {
            currencyCountryCodeStr = getIntent().getStringExtra("currencyCountryCode");
        } else {
            currencyCountryCodeStr = "";
        }

        if (getIntent().getStringExtra("currencySymbol") != null) {
            currencySymbolStr = getIntent().getStringExtra("currencySymbol");
        } else {
            currencySymbolStr = "";
        }


        nameOnCardEditText = (EditText) findViewById(R.id.nameOnCardEditText);
        cardNumberEditText = (EditText) findViewById(R.id.cardNumberEditText);
        securityCodeEditText = (EditText) findViewById(R.id.securityCodeEditText);
        couponCodeEditText = (EditText) findViewById(R.id.couponCodeEditText);

        Typeface typeface6 = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.light_fonts));
        nameOnCardEditText.setTypeface(typeface6);
        cardNumberEditText.setTypeface(typeface6);
        securityCodeEditText.setTypeface(typeface6);
        couponCodeEditText.setTypeface(typeface6);


        chargedPriceTextView = (TextView) findViewById(R.id.chargeDetailsTextView);
        creditCardLayout = (RelativeLayout) findViewById(R.id.creditCardLayout);
        cardExpiryDetailsLayout = (LinearLayout) findViewById(R.id.cardExpiryDetailsLayout);
        saveCardCheckbox = (CheckBox) findViewById(R.id.saveCardCheckbox);
        withoutCreditCardLayout = (RelativeLayout) findViewById(R.id.withoutPaymentLayout);
        withoutCreditCardLayout.setVisibility(View.GONE);
        withoutCreditCardChargedPriceTextView = (TextView) findViewById(R.id.withoutPaymentChargeDetailsTextView);
        nextButton = (Button) findViewById(R.id.nextButton);
        //paywithCreditCardButton = (Button) findViewById(R.id.payWithCreditCardButton);
        //paywithPaypalButton = (Button) findViewById(R.id.payWithPaypalCardButton);

        //payByPalRadioButton = (RadioButton) findViewById(R.id.payByPalRadioButton);


        cardExpiryMonthSpinner = (Spinner) findViewById(R.id.cardExpiryMonthEditText);
        cardExpiryYearSpinner = (Spinner) findViewById(R.id.cardExpiryYearEditText);
        creditCardSaveSpinner = (Spinner) findViewById(R.id.creditCardSaveEditText);

        scanButton = (Button) findViewById(R.id.scanButton);
        scanButton.setVisibility(View.GONE);
        payNowButton = (Button) findViewById(R.id.payNowButton);
        applyButton = (Button) findViewById(R.id.addCouponButton);


        Typeface typeface2 = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.regular_fonts));
        creditCardDetailsTitleTextView.setTypeface(typeface2);
        creditCardDetailsTitleTextView.setText(Util.getTextofLanguage(PaymentInfoActivity.this, Util.CREDIT_CARD_DETAILS, Util.DEFAULT_CREDIT_CARD_DETAILS));

        Typeface typeface3 = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.light_fonts));
        chargedPriceTextView.setTypeface(typeface3);

        Typeface typeface4 = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.regular_fonts));
        payNowButton.setTypeface(typeface4);
        payNowButton.setText(Util.getTextofLanguage(PaymentInfoActivity.this, Util.BUTTON_PAY_NOW, Util.DEFAULT_BUTTON_PAY_NOW));



       /* if (planIdForPaypal == 0){
            payByPaypalButton.setVisibility(View.GONE);
        }else{
            payByPaypalButton.setVisibility(View.VISIBLE);
        }

        payByPaypalButton.setVisibility(View.GONE);
        creditCardLayout.setVisibility(View.GONE);*/

        /*paywithCreditCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/


        selectShowRadioButton = (TextView) findViewById(R.id.showNameWithPrice);

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        for (int i = 0; i < 21; i++) {
            yearArray.add(year + i);

        }
        for (int i = 1; i < 13; i++) {
            monthsIdArray.add(i);


        }


        cardExpiryMonthSpinnerAdapter = new ArrayAdapter<Integer>(this, R.layout.spinner_new, monthsIdArray);
        cardExpiryMonthSpinner.setAdapter(cardExpiryMonthSpinnerAdapter);


        int mn = c.get(Calendar.MONTH);
        if (Util.containsIgnoreCase(monthsIdArray, mn + 1)) {
            // true
            int mnIndex = monthsIdArray.indexOf(mn + 1);

            cardExpiryMonthSpinner.setSelection(mnIndex);
            expiryMonthStr = monthsIdArray.get(mnIndex);
        } else {
            cardExpiryMonthSpinner.setSelection(0);
            expiryMonthStr = monthsIdArray.get(0);

        }

       /* cardExpiryMonthSpinner.setSelection(0);
        expiryMonthStr = monthsIdArray.get(0);*/

        cardExpiryYearSpinnerAdapter = new ArrayAdapter<Integer>(this, R.layout.spinner_new, yearArray);
        cardExpiryYearSpinner.setAdapter(cardExpiryYearSpinnerAdapter);
        cardExpiryYearSpinner.setSelection(0);
        expiryYearStr = yearArray.get(0);

      /*  creditCardSaveSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                creditCardSaveSpinner.setSelection(position);
                if(position == 0)
                {
                    creditCardLayout.setVisibility(View.VISIBLE);
                }else
                {
                    creditCardLayout.setVisibility(View.INVISIBLE);
                }

            }

        });
*/

        saveCardCheckbox.setChecked(true);

        creditCardSaveSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                creditCardSaveSpinner.setSelection(position);
                if (position == 0) {

                    nameOnCardEditText.setVisibility(View.VISIBLE);
                    cardNumberEditText.setVisibility(View.VISIBLE);
                    cardNumberEditText.setVisibility(View.VISIBLE);
                    cardExpiryDetailsLayout.setVisibility(View.VISIBLE);
                    saveCardCheckbox.setVisibility(View.VISIBLE);
                    isCheckedToSavetheCard = "1";
                    saveCardCheckbox.setChecked(true);
                } else {
                    //withoutCreditCardLayout.setVisibility(View.GONE);
                    nameOnCardEditText.setVisibility(View.GONE);
                    cardNumberEditText.setVisibility(View.GONE);
                    cardNumberEditText.setVisibility(View.GONE);
                    cardExpiryDetailsLayout.setVisibility(View.GONE);
                    saveCardCheckbox.setVisibility(View.GONE);
                    isCheckedToSavetheCard = "0";
                    saveCardCheckbox.setChecked(false);


                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                creditCardSaveSpinner.setSelection(0);

                if (creditCardSaveSpinner.getSelectedItemPosition() == 0) {

                    nameOnCardEditText.setVisibility(View.VISIBLE);
                    cardNumberEditText.setVisibility(View.VISIBLE);
                    cardNumberEditText.setVisibility(View.VISIBLE);
                    cardExpiryDetailsLayout.setVisibility(View.VISIBLE);
                    saveCardCheckbox.setVisibility(View.VISIBLE);
                    isCheckedToSavetheCard = "1";
                    saveCardCheckbox.setChecked(true);

                    //withoutCreditCardLayout.setVisibility(View.VISIBLE);
                    //creditCardLayout.setVisibility(View.GONE);
                    //chargedPriceTextView.setText(Util.getTextofLanguage(PPvPaymentInfoActivity.this,Util.CARD_WILL_CHARGE,Util.DEFAULT_CARD_WILL_CHARGE)+" " +currencySymbolStr+chargedPrice);
                } else {
                    //withoutCreditCardLayout.setVisibility(View.GONE);
                    nameOnCardEditText.setVisibility(View.GONE);
                    cardNumberEditText.setVisibility(View.GONE);
                    cardNumberEditText.setVisibility(View.GONE);
                    cardExpiryDetailsLayout.setVisibility(View.GONE);
                    saveCardCheckbox.setVisibility(View.GONE);
                    isCheckedToSavetheCard = "0";
                    saveCardCheckbox.setChecked(false);
                }

            }
        });


        cardExpiryMonthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cardExpiryMonthSpinner.setSelection(position);
                expiryMonthStr = monthsIdArray.get(position);

            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                Calendar c = Calendar.getInstance();
                int mn = c.get(Calendar.MONTH);
                if (Util.containsIgnoreCase(monthsIdArray, mn + 1)) {
                    // true
                    int mnIndex = monthsIdArray.indexOf(mn + 1);

                    cardExpiryMonthSpinner.setSelection(mnIndex);
                    expiryMonthStr = monthsIdArray.get(mnIndex);
                } else {
                    cardExpiryMonthSpinner.setSelection(0);
                    expiryMonthStr = monthsIdArray.get(0);

                }
            }
        });
        cardExpiryYearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cardExpiryYearSpinner.setSelection(position);
                expiryYearStr = yearArray.get(position);
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                cardExpiryYearSpinner.setSelection(0);
                expiryYearStr = yearArray.get(0);

            }
        });

        chargedPriceTextView.setText(Util.getTextofLanguage(PaymentInfoActivity.this, Util.CARD_WILL_CHARGE, Util.DEFAULT_CARD_WILL_CHARGE) + " : " + currencySymbolStr + getIntent().getStringExtra("price"));
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nameOnCardEditText.setText("");
                securityCodeEditText.setText("");
                cardNumberEditText.setText("");


            }
        });


        payNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.v("Nihar_payment","called");

                String nameOnCardStr = nameOnCardEditText.getText().toString().trim();
                String cardNumberStr = cardNumberEditText.getText().toString().trim();
                String securityCodeStr = securityCodeEditText.getText().toString().trim();

                if (nameOnCardStr.matches("")) {
                    Util.showToast(PaymentInfoActivity.this, Util.getTextofLanguage(PaymentInfoActivity.this, Util.CREDIT_CARD_NAME_HINT, Util.DEFAULT_CREDIT_CARD_NAME_HINT));

                    // Toast.makeText(PaymentInfoActivity.this,Util.getTextofLanguage(PaymentInfoActivity.this,Util.CREDIT_CARD_NAME_HINT,Util.DEFAULT_CREDIT_CARD_NAME_HINT), Toast.LENGTH_LONG).show();

                } else if (cardNumberStr.matches("")) {
                    Util.showToast(PaymentInfoActivity.this, Util.getTextofLanguage(PaymentInfoActivity.this, Util.CREDIT_CARD_NUMBER_HINT, Util.DEFAULT_CREDIT_CARD_NUMBER_HINT));

                    // Toast.makeText(PaymentInfoActivity.this,Util.getTextofLanguage(PaymentInfoActivity.this,Util.CREDIT_CARD_NUMBER_HINT,Util.DEFAULT_CREDIT_CARD_NUMBER_HINT), Toast.LENGTH_LONG).show();

                } else if (securityCodeStr.matches("")) {
                    Util.showToast(PaymentInfoActivity.this, Util.getTextofLanguage(PaymentInfoActivity.this, Util.CVV_ALERT, Util.DEFAULT_CVV_ALERT));

                     //Toast.makeText(PaymentInfoActivity.this,Util.getTextofLanguage(PaymentInfoActivity.this,Util.CVV_ALERT,Util.DEFAULT_CVV_ALERT), Toast.LENGTH_LONG).show();


                } else if (expiryMonthStr <= 0) {
                   Toast.makeText(PaymentInfoActivity.this, "Please enter expiry month", Toast.LENGTH_LONG).show();

                } else if (expiryYearStr <= 0) {
                   Toast.makeText(PaymentInfoActivity.this, "Please enter expiry year", Toast.LENGTH_LONG).show();

                } else {
                    boolean isNetwork = Util.checkNetwork(PaymentInfoActivity.this);
                    if (isNetwork == false) {
                        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(PaymentInfoActivity.this);
                        dlgAlert.setMessage(Util.getTextofLanguage(PaymentInfoActivity.this, Util.NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION));
                        dlgAlert.setTitle(Util.getTextofLanguage(PaymentInfoActivity.this, Util.SORRY, Util.DEFAULT_SORRY));
                        dlgAlert.setPositiveButton(Util.getTextofLanguage(PaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                        dlgAlert.setCancelable(false);
                        dlgAlert.setPositiveButton(Util.getTextofLanguage(PaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        dlgAlert.create().show();

                    } else {



                    }
                    AsynPaymentInfoDetails asyncReg = new AsynPaymentInfoDetails();
                    asyncReg.executeOnExecutor(threadPoolExecutor);
                }
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isNetwork = Util.checkNetwork(PaymentInfoActivity.this);
                if (isNetwork == false) {
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(PaymentInfoActivity.this);
                    dlgAlert.setMessage(Util.getTextofLanguage(PaymentInfoActivity.this, Util.NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION));
                    dlgAlert.setTitle(Util.getTextofLanguage(PaymentInfoActivity.this, Util.SORRY, Util.DEFAULT_SORRY));
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(PaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                    dlgAlert.setCancelable(false);
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(PaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    dlgAlert.create().show();

                } else {

                   /* AsynWithouPaymentSubscriptionRegDetails asyncSubWithoutPayment = new AsynWithouPaymentSubscriptionRegDetails();
                    asyncSubWithoutPayment.executeOnExecutor(threadPoolExecutor);*/
                }
            }

        });

    }


    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    private class AsynPaymentInfoDetails extends AsyncTask<Void, Void, Void> {
        ProgressBarHandler progressBarHandler;
        int status;
        String responseStr;
        String responseMessageStr;
        String emailIdStr = loginPref.getString("PREFS_LOGIN_EMAIL_ID_KEY", null);

        String nameOnCardStr = nameOnCardEditText.getText().toString().trim();
        String cardNumberStr = cardNumberEditText.getText().toString().trim();
        String securityCodeStr = securityCodeEditText.getText().toString().trim();

        @Override
        protected Void doInBackground(Void... params) {
            String urlRouteList = Util.rootUrl().trim() + Util.authenticatedCardValidationUrl.trim();
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("nameOnCard", nameOnCardStr);
                httppost.addHeader("expiryMonth", String.valueOf(expiryMonthStr).trim());
                httppost.addHeader("expiryYear", String.valueOf(expiryYearStr).trim());
                httppost.addHeader("cardNumber", cardNumberStr);
                httppost.addHeader("cvv", securityCodeStr);
                httppost.addHeader("email", emailIdStr);
                httppost.addHeader("authToken", Util.authTokenStr.trim());

            /*    Log.v("SUBHA", "nameOnCardStr = " + nameOnCardStr);
                Log.v("SUBHA", "expiryMonth = " + String.valueOf(expiryMonthStr).trim());
                Log.v("SUBHA", "expiryYear = " + String.valueOf(expiryYearStr).trim());
                Log.v("SUBHA", "cardNumber = " + cardNumberStr);
                Log.v("SUBHA", "cvv = " + securityCodeStr);
                Log.v("SUBHA", "email = " + emailIdStr);
                Log.v("SUBHA", "authToken = " + Util.authTokenStr.trim());
*/

                // Execute HTTP Post Request
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());

                    Log.v("SUBHA", "response of card validation = " + responseStr);

                } catch (org.apache.http.conn.ConnectTimeoutException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (progressBarHandler.isShowing())
                                progressBarHandler.hide();
                            status = 0;
                            Util.showToast(PaymentInfoActivity.this, Util.getTextofLanguage(PaymentInfoActivity.this, Util.SLOW_INTERNET_CONNECTION, Util.DEFAULT_SLOW_INTERNET_CONNECTION));

                            //Toast.makeText(PaymentInfoActivity.this, Util.getTextofLanguage(PaymentInfoActivity.this, Util.SLOW_INTERNET_CONNECTION, Util.DEFAULT_SLOW_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();

                        }

                    });

                } catch (IOException e) {
                    if (progressBarHandler.isShowing())
                        progressBarHandler.hide();
                    status = 0;
                    e.printStackTrace();


                }
                JSONObject myJson = null;

                if (responseStr != null) {
                    myJson = new JSONObject(responseStr);
                    status = Integer.parseInt(myJson.optString("isSuccess"));
                }
                if (status == 1) {
                    JSONObject mainJson = null;

                    if (myJson.has("card")) {
                        mainJson = myJson.getJSONObject("card");
                        if (mainJson.has("status") && mainJson.getString("status").trim() != null && !mainJson.getString("status").trim().isEmpty() && !mainJson.getString("status").trim().equals("null") && !mainJson.getString("status").trim().matches("")) {
                            statusStr = mainJson.getString("status");
                        } else {
                            statusStr = "";

                        }

                        if (mainJson.has("token") && mainJson.getString("token").trim() != null && !mainJson.getString("token").trim().isEmpty() && !mainJson.getString("token").trim().equals("null") && !mainJson.getString("token").trim().matches("")) {
                            tokenStr = mainJson.getString("token");
                        } else {
                            tokenStr = "";

                        }

                        if (mainJson.has("response_text") && mainJson.getString("response_text").trim() != null && !mainJson.getString("response_text").trim().isEmpty() && !mainJson.getString("response_text").trim().equals("null") && !mainJson.getString("response_text").trim().matches("")) {
                            responseText = mainJson.getString("response_text");
                        } else {
                            responseText = "";

                        }

                        if (mainJson.has("profile_id") && mainJson.getString("profile_id").trim() != null && !mainJson.getString("profile_id").trim().isEmpty() && !mainJson.getString("profile_id").trim().equals("null") && !mainJson.getString("profile_id").trim().matches("")) {
                            profileIdStr = mainJson.getString("profile_id");
                        } else {
                            profileIdStr = "";

                        }

                        if (mainJson.has("card_last_fourdigit") && mainJson.getString("card_last_fourdigit").trim() != null && !mainJson.getString("card_last_fourdigit").trim().isEmpty() && !mainJson.getString("card_last_fourdigit").trim().equals("null") && !mainJson.getString("card_last_fourdigit").trim().matches("")) {
                            cardLastFourDigitStr = mainJson.getString("card_last_fourdigit");
                        } else {
                            cardLastFourDigitStr = "";

                        }
                        if (mainJson.has("transaction_status") && mainJson.getString("transaction_status").trim() != null && !mainJson.getString("transaction_status").trim().isEmpty() && !mainJson.getString("transaction_status").trim().equals("null") && !mainJson.getString("transaction_status").trim().matches("")) {
                            transaction_status = mainJson.getString("transaction_status");
                        } else {
                            transaction_status = "";
                        }
                    if (mainJson.has("transaction_invoice_id") && mainJson.getString("transaction_invoice_id").trim() != null && !mainJson.getString("transaction_invoice_id").trim().isEmpty() && !mainJson.getString("transaction_invoice_id").trim().equals("null") && !mainJson.getString("transaction_invoice_id").trim().matches("")) {
                        transaction_invoice_id = mainJson.getString("transaction_invoice_id");
                        } else {
                        transaction_invoice_id = "";
                        }

                    if (mainJson.has("transaction_order_number") && mainJson.getString("transaction_order_number").trim() != null && !mainJson.getString("transaction_order_number").trim().isEmpty() && !mainJson.getString("transaction_order_number").trim().equals("null") && !mainJson.getString("transaction_order_number").trim().matches("")) {
                        transaction_order_number = mainJson.getString("transaction_order_number");
                        } else {
                        transaction_order_number = "";
                        }

                    if (mainJson.has("transaction_dollar_amount") && mainJson.getString("transaction_dollar_amount").trim() != null && !mainJson.getString("transaction_dollar_amount").trim().isEmpty() && !mainJson.getString("transaction_dollar_amount").trim().equals("null") && !mainJson.getString("transaction_dollar_amount").trim().matches("")) {
                        transaction_dollar_amount = mainJson.getString("transaction_dollar_amount");
                        } else {
                        transaction_dollar_amount = "";
                        }

                    if (mainJson.has("transaction_amount") && mainJson.getString("transaction_amount").trim() != null && !mainJson.getString("transaction_amount").trim().isEmpty() && !mainJson.getString("transaction_amount").trim().equals("null") && !mainJson.getString("transaction_amount").trim().matches("")) {
                        transaction_amount = mainJson.getString("transaction_amount");
                        } else {
                        transaction_amount = "";
                        }
                        
                    if (mainJson.has("transaction_is_success") && mainJson.getString("transaction_is_success").trim() != null && !mainJson.getString("transaction_is_success").trim().isEmpty() && !mainJson.getString("transaction_is_success").trim().equals("null") && !mainJson.getString("transaction_is_success").trim().matches("")) {
                        transaction_is_success = mainJson.getString("transaction_is_success");
                        } else {
                        transaction_is_success = "";
                        }

                        if (mainJson.has("card_type") && mainJson.getString("card_type").trim() != null && !mainJson.getString("card_type").trim().isEmpty() && !mainJson.getString("card_type").trim().equals("null") && !mainJson.getString("card_type").trim().matches("")) {
                            cardTypeStr = mainJson.getString("card_type");
                        } else {
                            cardTypeStr = "";

                        }
                    }


                }
                if (status == 0) {
                    if (myJson.has("Message")) {
                        responseMessageStr = myJson.optString("Message");
                    }
                    if (((responseMessageStr.equalsIgnoreCase("null")) || (responseMessageStr.length() <= 0))) {
                        responseMessageStr = "No Details found";

                    }
                }

            } catch (Exception e) {
                if (progressBarHandler.isShowing())
                    progressBarHandler.hide();
                status = 0;

            }

            return null;
        }


        protected void onPostExecute(Void result) {
           /* try {
                if (pDialog.isShowing())
                    pDialog.dismiss();
            } catch (IllegalArgumentException ex) {
                status = 0;
            }*/

            if (status == 0) {
                try {
                    if (progressBarHandler.isShowing())
                        progressBarHandler.hide();
                } catch (IllegalArgumentException ex) {
                    status = 0;
                }
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(PaymentInfoActivity.this);
                dlgAlert.setMessage(responseMessageStr);
                dlgAlert.setTitle("Failure");
                dlgAlert.setPositiveButton(Util.getTextofLanguage(PaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(Util.getTextofLanguage(PaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                dlgAlert.create().show();
            } else if (status == 1) {
                boolean isNetwork = Util.checkNetwork(PaymentInfoActivity.this);
                if (isNetwork == false) {
                    try {
                        if (progressBarHandler.isShowing())
                            progressBarHandler.hide();
                    } catch (IllegalArgumentException ex) {
                        status = 0;
                    }
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(PaymentInfoActivity.this);
                    dlgAlert.setMessage(Util.getTextofLanguage(PaymentInfoActivity.this, Util.NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION));
                    dlgAlert.setTitle(Util.getTextofLanguage(PaymentInfoActivity.this, Util.SORRY, Util.DEFAULT_SORRY));
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(PaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                    dlgAlert.setCancelable(false);
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(PaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    dlgAlert.create().show();

                } else {
                    AsynSubscriptionRegDetails asyncSubsrInfo = new AsynSubscriptionRegDetails();
                    asyncSubsrInfo.executeOnExecutor(threadPoolExecutor);
                }
            }

        }

        @Override
        protected void onPreExecute() {
            progressBarHandler = new ProgressBarHandler(PaymentInfoActivity.this);
            progressBarHandler.show();
        }


    }

    private class AsynSubscriptionRegDetails extends AsyncTask<Void, Void, Void> {
        ProgressBarHandler progressBarHandler;
        int status;
        String responseStr;
        String nameOnCardStr = nameOnCardEditText.getText().toString().trim();
        String cardNumberStr = cardNumberEditText.getText().toString().trim();
        String securityCardStr = securityCodeEditText.getText().toString().trim();

        @Override
        protected Void doInBackground(Void... params) {
            Log.v("SUBHA", "payment at doInBackground called ");
            String userIdStr = loginPref.getString("PREFS_LOGGEDIN_ID_KEY", null);
            String emailIdSubStr = loginPref.getString("PREFS_LOGIN_EMAIL_ID_KEY", null);

            String urlRouteList = Util.rootUrl().trim() + Util.subscriptionUrl.trim();
            Log.v("SUBHA", "payment at urlRouteList = " + urlRouteList);
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("card_name", nameOnCardStr);
                httppost.addHeader("exp_month", String.valueOf(expiryMonthStr).trim());
                httppost.addHeader("card_number", cardNumberStr);
                httppost.addHeader("exp_year", String.valueOf(expiryYearStr).trim());
                httppost.addHeader("email", emailIdSubStr.trim()); //Null pointer
                //  httppost.addHeader("movie_id", muviUniqueIdStr.trim());
                httppost.addHeader("user_id", userIdStr.trim());
                if (isCouponCodeAdded == true) {
                    httppost.addHeader("coupon_code", validCouponCode);
                } else {
                    httppost.addHeader("coupon_code", "");
                }
                httppost.addHeader("card_type", cardTypeStr.trim());
                httppost.addHeader("card_last_fourdigit", cardLastFourDigitStr.trim());
                httppost.addHeader("profile_id", profileIdStr.trim());
                httppost.addHeader("token", tokenStr.trim());
                httppost.addHeader("cvv", securityCardStr);
                // httppost.addHeader("country",currencyCountryCodeStr.trim());
                httppost.addHeader("country", countryPref.getString("countryCode", null));
                httppost.addHeader("season_id", "0");
                httppost.addHeader("episode_id", "0");
                httppost.addHeader("currency_id", currencyIdStr.trim());
                httppost.addHeader("transaction_invoice_id", transaction_invoice_id);
                httppost.addHeader("transaction_order_number", transaction_order_number);
                httppost.addHeader("transaction_is_success", transaction_is_success);
                httppost.addHeader("transaction_status", transaction_status);
                httppost.addHeader("transaction_dollar_amount", transaction_dollar_amount);
                httppost.addHeader("transaction_amount", transaction_amount);

                httppost.addHeader("plan_id", getIntent().getStringExtra("selected_plan_id").toString().trim());
                httppost.addHeader("name", loginPref.getString("PREFS_LOGIN_DISPLAY_NAME_KEY", ""));
Log.v("Nihar_payment",nameOnCardStr+" "+expiryMonthStr+" "+cardNumberStr+" "+expiryYearStr+" "
        +emailIdSubStr+" "+userIdStr+" "+validCouponCode+" "+cardTypeStr+" "+cardLastFourDigitStr+" "
        +profileIdStr+" "+tokenStr+" "+securityCardStr+" "+countryPref+" "+currencyIdStr+" "
        +transaction_invoice_id+" " +transaction_order_number+" "+transaction_is_success+" "
        +transaction_status+" "+transaction_dollar_amount+" "+transaction_amount+" ");
//                httppost.addHeader("is_save_this_card", isCheckedToSavetheCard.trim());


                // Execute HTTP Post Request
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());
                    Log.v("SUBHA", "response of payment = " + responseStr);

                } catch (org.apache.http.conn.ConnectTimeoutException e) {
                    Log.v("SUBHA", "error2=" + e.toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            status = 0;
                            if (progressBarHandler.isShowing())
                                progressBarHandler.hide();
                            Util.showToast(PaymentInfoActivity.this, Util.getTextofLanguage(PaymentInfoActivity.this, Util.SLOW_INTERNET_CONNECTION, Util.DEFAULT_SLOW_INTERNET_CONNECTION));

                            // Toast.makeText(PaymentInfoActivity.this, Util.getTextofLanguage(PaymentInfoActivity.this, Util.SLOW_INTERNET_CONNECTION, Util.DEFAULT_SLOW_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();

                        }

                    });

                } catch (IOException e) {

                    status = 0;
                    if (progressBarHandler.isShowing())
                        progressBarHandler.hide();
                    e.printStackTrace();
                    Log.v("SUBHA", "error1=" + e.toString());
                }

                Log.v("SUBHA", "response of payment = " + responseStr);

                if (responseStr != null) {
                    JSONObject myJson = new JSONObject(responseStr);
                    status = Integer.parseInt(myJson.optString("code"));
                }

            } catch (Exception e) {

                if (progressBarHandler.isShowing())
                    progressBarHandler.hide();
                status = 0;
                Log.v("SUBHA", "error=" + e.toString());
            }

            return null;
        }


        protected void onPostExecute(Void result) {

            if (status == 0) {
                try {
                    if (progressBarHandler.isShowing())
                        progressBarHandler.hide();
                } catch (IllegalArgumentException ex) {
                    status = 0;
                }
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(PaymentInfoActivity.this);
                dlgAlert.setMessage(Util.getTextofLanguage(PaymentInfoActivity.this, Util.ERROR_IN_SUBSCRIPTION, Util.DEFAULT_ERROR_IN_SUBSCRIPTION));
                dlgAlert.setTitle(Util.getTextofLanguage(PaymentInfoActivity.this, Util.FAILURE, Util.DEFAULT_FAILURE));
                dlgAlert.setPositiveButton(Util.getTextofLanguage(PaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(Util.getTextofLanguage(PaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                dlgAlert.create().show();
            } else if (status > 0) {

                if (status == 200) {
                    if (progressBarHandler.isShowing())
                        progressBarHandler.hide();
                    Util.showToast(PaymentInfoActivity.this, Util.getTextofLanguage(PaymentInfoActivity.this, Util.SUBSCRIPTION_COMPLETED, Util.DEFAULT_SUBSCRIPTION_COMPLETED));

                    //   Toast.makeText(PaymentInfoActivity.this, Util.getTextofLanguage(PaymentInfoActivity.this,Util.SUBSCRIPTION_COMPLETED,Util.DEFAULT_SUBSCRIPTION_COMPLETED), Toast.LENGTH_LONG).show();
                    if (Util.check_for_subscription == 0) {
                        Intent intent = new Intent(PaymentInfoActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        finish();
                    } else {
                        if (Util.checkNetwork(PaymentInfoActivity.this) == true) {


                            AsynLoadVideoUrls asynLoadVideoUrls = new AsynLoadVideoUrls();
                            asynLoadVideoUrls.executeOnExecutor(threadPoolExecutor);

                        } else {
                            Intent intent = new Intent(PaymentInfoActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(intent);
                            finish();
                            Util.showToast(PaymentInfoActivity.this, Util.getTextofLanguage(PaymentInfoActivity.this, Util.NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION));

                            //  Toast.makeText(PaymentInfoActivity.this, Util.getTextofLanguage(PaymentInfoActivity.this,Util.NO_INTERNET_CONNECTION,Util.DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
                        }
                    }

                } else {
                    try {
                        if (progressBarHandler.isShowing())
                            progressBarHandler.hide();
                    } catch (IllegalArgumentException ex) {
                        status = 0;
                    }
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(PaymentInfoActivity.this);
                    dlgAlert.setMessage(Util.getTextofLanguage(PaymentInfoActivity.this, Util.ERROR_IN_SUBSCRIPTION, Util.DEFAULT_ERROR_IN_SUBSCRIPTION));
                    dlgAlert.setTitle(Util.getTextofLanguage(PaymentInfoActivity.this, Util.SORRY, Util.DEFAULT_SORRY));
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(PaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                    dlgAlert.setCancelable(false);
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(PaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    dlgAlert.create().show();
                }
            }

        }

        @Override
        protected void onPreExecute() {
            progressBarHandler = new ProgressBarHandler(PaymentInfoActivity.this);
            progressBarHandler.show();

        }


    }

    @Override
    protected void onResume() {
        super.onResume();
       /* if (CardIOActivity.canReadCardWithCamera()) {
            scanButton.setText("Scan");
            scanButton.setEnabled(true);
        }*/ /*else {
            scanButton.setText("Scan");
            final int sdk = android.os.Build.VERSION.SDK_INT;
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                scanButton.setBackgroundDrawable( getResources().getDrawable(R.drawable.button_background_gray) );
            } else {
                scanButton.setBackground( getResources().getDrawable(R.drawable.button_background_gray));
            }
           // scanButton.setBackground(R.drawable.button_background_gray);
            scanButton.setEnabled(false);
        }*/
    }

    /*public void onScanPress(View v) {
        // This method is set up as an onClick handler in the layout xml
        // e.g. android:onClick="onScanPress"

        Intent scanIntent = new Intent(this, CardIOActivity.class);
        scanIntent.putExtra(CardIOActivity.EXTRA_USE_CARDIO_LOGO, false); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_HIDE_CARDIO_LOGO, true); // default: false

        // customize these values to suit your needs.
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_KEEP_APPLICATION_THEME,true);

        // hides the manual entry button
        // if set, developers should provide their own manual entry mechanism in the app
        scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, false); // default: false

        // matches the theme of your application
        scanIntent.putExtra(CardIOActivity.EXTRA_KEEP_APPLICATION_THEME, false); // default: false

        // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
        startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
            CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);

            // Never log a raw card number. Avoid displaying it, but if necessary use getFormattedCardNumber()
            cardNumberEditText.setText(scanResult.cardNumber);

            if (scanResult.isExpiryValid()) {

                if (monthsIdArray.contains(scanResult.expiryMonth)) {

                    // true
                    int index = monthsIdArray.indexOf(scanResult.expiryMonth);
                    expiryMonthStr = monthsIdArray.get(index);
                    cardExpiryMonthSpinner.setSelection(index);
                }
                if (yearArray.contains(scanResult.expiryYear)) {
                    // true
                    int index =yearArray.indexOf(scanResult.expiryYear);
                    expiryYearStr = yearArray.get(index);
                    cardExpiryYearSpinner.setSelection(index);
                }

            }



            if (scanResult.cvv != null) {
                // Never log or display a CVV
                securityCodeEditText.setText(scanResult.cvv) ;
            }

        } else {
            Toast.makeText(PaymentInfoActivity.this, "Please enter credit card details manually", Toast.LENGTH_LONG).show();
        }

    }
*/
    /*@Override
    public void onBackPressed()
    {

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        if (getIntent().getStringExtra("activity").trim()!=null && getIntent().getStringExtra("activity").trim().equalsIgnoreCase("generic")){

            runOnUiThread(new Runnable() {
                public void run() {
                    Intent newIn = new Intent(PaymentInfoActivity.this, MainActivity.class);
                    newIn.putExtra("activity","generic");
                    newIn.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(newIn);
                    finish();
                    overridePendingTransition(0, 0);
                }
            });



        }
        super.onBackPressed();

    }*/


    private class AsynLoadVideoUrls extends AsyncTask<Void, Void, Void> {
        ProgressBarHandler pDialog;
        String responseStr;
        int statusCode;

        @Override
        protected Void doInBackground(Void... params) {
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(Util.rootUrl().trim() + Util.loadVideoUrl.trim());
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("content_uniq_id", Util.dataModel.getMovieUniqueId().trim());
                httppost.addHeader("stream_uniq_id", Util.dataModel.getStreamUniqueId().trim());
                httppost.addHeader("internet_speed", MainActivity.internetSpeed.trim());

                // Execute HTTP Post Request
                try {

                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());

                } catch (org.apache.http.conn.ConnectTimeoutException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (pDialog != null && pDialog.isShowing()) {
                                pDialog.hide();
                                pDialog = null;
                            }
                            responseStr = "0";
                            Util.dataModel.setVideoUrl(Util.getTextofLanguage(PaymentInfoActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));

                            Util.showToast(PaymentInfoActivity.this, Util.getTextofLanguage(PaymentInfoActivity.this, Util.SLOW_INTERNET_CONNECTION, Util.DEFAULT_SLOW_INTERNET_CONNECTION));

                            //  Toast.makeText(PaymentInfoActivity.this, Util.getTextofLanguage(PaymentInfoActivity.this,Util.SLOW_INTERNET_CONNECTION,Util.DEFAULT_SLOW_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();

                        }

                    });

                } catch (IOException e) {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.hide();
                        pDialog = null;
                    }
                    responseStr = "0";
                    Util.dataModel.setVideoUrl(Util.getTextofLanguage(PaymentInfoActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));
                    e.printStackTrace();
                }

                JSONObject myJson = null;
                if (responseStr != null) {
                    myJson = new JSONObject(responseStr);
                    statusCode = Integer.parseInt(myJson.optString("code"));
                }

                if (statusCode >= 0) {
                    if (statusCode == 200) {
                        if (Util.dataModel.getThirdPartyUrl().matches("") || Util.dataModel.getThirdPartyUrl().equalsIgnoreCase(Util.getTextofLanguage(PaymentInfoActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA))) {
                            if ((myJson.has("videoUrl")) && myJson.getString("videoUrl").trim() != null && !myJson.getString("videoUrl").trim().isEmpty() && !myJson.getString("videoUrl").trim().equals("null") && !myJson.getString("videoUrl").trim().matches("")) {
                                Util.dataModel.setVideoUrl(myJson.getString("videoUrl"));
                                videoUrlStr = myJson.getString("videoUrl");
                            } else {
                                Util.dataModel.setVideoUrl(Util.getTextofLanguage(PaymentInfoActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));
                            }
                        } else {
                            if ((myJson.has("thirdparty_url")) && myJson.getString("thirdparty_url").trim() != null && !myJson.getString("thirdparty_url").trim().isEmpty() && !myJson.getString("thirdparty_url").trim().equals("null") && !myJson.getString("thirdparty_url").trim().matches("")) {
                                Util.dataModel.setVideoUrl(myJson.getString("thirdparty_url"));


                            } else {
                                Util.dataModel.setVideoUrl(Util.getTextofLanguage(PaymentInfoActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));

                            }
                        }
                        if ((myJson.has("videoResolution")) && myJson.getString("videoResolution").trim() != null && !myJson.getString("videoResolution").trim().isEmpty() && !myJson.getString("videoResolution").trim().equals("null") && !myJson.getString("videoResolution").trim().matches("")) {
                            Util.dataModel.setVideoResolution(myJson.getString("videoResolution"));

                        }

                    }

                } else {

                    responseStr = "0";
                    Util.dataModel.setVideoUrl(Util.getTextofLanguage(PaymentInfoActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));
                }
            } catch (JSONException e1) {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.hide();
                    pDialog = null;
                }
                responseStr = "0";
                Util.dataModel.setVideoUrl(Util.getTextofLanguage(PaymentInfoActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));
                e1.printStackTrace();
            } catch (Exception e) {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.hide();
                    pDialog = null;
                }
                responseStr = "0";
                Util.dataModel.setVideoUrl(Util.getTextofLanguage(PaymentInfoActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));

                e.printStackTrace();

            }
            return null;

        }

        protected void onPostExecute(Void result) {

      /*  try{
            if(pDialog.isShowing())
                pDialog.dismiss();
        }
        catch(IllegalArgumentException ex)
        {
            responseStr = "0";
            movieVideoUrlStr = getResources().getString(R.string.no_data_str);
        }*/
            if (responseStr == null) {
                responseStr = "0";
                Util.dataModel.setVideoUrl(Util.getTextofLanguage(PaymentInfoActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));
                //movieThirdPartyUrl = getResources().getString(R.string.no_data_str);
            }

            if ((responseStr.trim().equalsIgnoreCase("0"))) {
                try {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.hide();
                        pDialog = null;
                    }
                } catch (IllegalArgumentException ex) {
                    Util.dataModel.setVideoUrl(Util.getTextofLanguage(PaymentInfoActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));
                    // movieThirdPartyUrl = getResources().getString(R.string.no_data_str);
                }
                Util.dataModel.setVideoUrl(Util.getTextofLanguage(PaymentInfoActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));
                //movieThirdPartyUrl = getResources().getString(R.string.no_data_str);
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(PaymentInfoActivity.this);
                dlgAlert.setMessage(Util.getTextofLanguage(PaymentInfoActivity.this, Util.NO_VIDEO_AVAILABLE, Util.DEFAULT_NO_VIDEO_AVAILABLE));
                dlgAlert.setTitle(Util.getTextofLanguage(PaymentInfoActivity.this, Util.SORRY, Util.DEFAULT_SORRY));
                dlgAlert.setPositiveButton(Util.getTextofLanguage(PaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(Util.getTextofLanguage(PaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                finish();
                            }
                        });
                dlgAlert.create().show();
            } else {

                if (Util.dataModel.getVideoUrl() == null) {
                    try {
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.hide();
                            pDialog = null;
                        }
                    } catch (IllegalArgumentException ex) {
                        Util.dataModel.setVideoUrl(Util.getTextofLanguage(PaymentInfoActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));
                    }
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(PaymentInfoActivity.this);
                    dlgAlert.setMessage(Util.getTextofLanguage(PaymentInfoActivity.this, Util.NO_VIDEO_AVAILABLE, Util.DEFAULT_NO_VIDEO_AVAILABLE));
                    dlgAlert.setTitle(Util.getTextofLanguage(PaymentInfoActivity.this, Util.SORRY, Util.DEFAULT_SORRY));
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(PaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                    dlgAlert.setCancelable(false);
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(PaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    finish();
                                }
                            });
                    dlgAlert.create().show();
                } else if (Util.dataModel.getVideoUrl().matches("") || Util.dataModel.getVideoUrl().equalsIgnoreCase(Util.getTextofLanguage(PaymentInfoActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA))) {
                    try {
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.hide();
                            pDialog = null;
                        }
                    } catch (IllegalArgumentException ex) {
                        Util.dataModel.setVideoUrl(Util.getTextofLanguage(PaymentInfoActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));
                    }
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(PaymentInfoActivity.this);
                    dlgAlert.setMessage(Util.getTextofLanguage(PaymentInfoActivity.this, Util.NO_VIDEO_AVAILABLE, Util.DEFAULT_NO_VIDEO_AVAILABLE));
                    dlgAlert.setTitle(Util.getTextofLanguage(PaymentInfoActivity.this, Util.SORRY, Util.DEFAULT_SORRY));
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(PaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                    dlgAlert.setCancelable(false);
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(PaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    finish();
                                }
                            });
                    dlgAlert.create().show();
                } else {/*
                    try {
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.hide();
                            pDialog = null;
                        }
                    } catch (IllegalArgumentException ex) {
                        Util.dataModel.setVideoUrl(Util.getTextofLanguage(PaymentInfoActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));
                    }
                    if (!videoUrlStr.equals("")) {
                        final Intent playVideoIntent = new Intent(PaymentInfoActivity.this, ExoPlayerActivity.class);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(playVideoIntent);
                                finish();

                            }
                        });
                    }else{
                        if (Util.dataModel.getVideoUrl().contains("://www.youtube") || Util.dataModel.getVideoUrl().contains("://www.youtu.be")){
                            if(Util.dataModel.getVideoUrl().contains("live_stream?channel")) {
                                final Intent playVideoIntent = new Intent(PaymentInfoActivity.this, ThirdPartyPlayer.class);
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        startActivity(playVideoIntent);
                                        finish();

                                    }
                                });
                            }else{

                                final Intent playVideoIntent = new Intent(PaymentInfoActivity.this, YouTubeAPIActivity.class);
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        startActivity(playVideoIntent);
                                        finish();


                                    }
                                });

                            }
                        }else{
                            final Intent playVideoIntent = new Intent(PaymentInfoActivity.this, ThirdPartyPlayer.class);
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    startActivity(playVideoIntent);
                                    finish();

                                }
                            });
                        }
                    }
                }*/


                }
            }
        }

            @Override
            protected void onPreExecute () {
                pDialog = new ProgressBarHandler(PaymentInfoActivity.this);
                pDialog.show();

            }


        }
    }

