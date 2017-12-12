package com.release.aeonaudio.utils;

/**
 * Created by Muvi on 6/12/2017.
 */

import android.app.Dialog;
import android.content.Context;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;


import com.release.aeonaudio.R;
import com.release.aeonaudio.adapter.PlayerDataMultipartAdaptor;
import com.release.aeonaudio.model.APVModel;
import com.release.aeonaudio.model.CurrencyModel;
import com.release.aeonaudio.model.DataModel;
import com.release.aeonaudio.model.LanguageModel;
import com.release.aeonaudio.model.PPVModel;
import com.release.aeonaudio.model.PlayListMultiModel;
import com.release.aeonaudio.model.QueueModel;
import com.release.aeonaudio.ui.PlayerAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.release.aeonaudio.R.style.MyMaterialTheme;

/**
 * Created by User on 24-07-2015.
 */
public class Util {
    ///common


    public static String DATABASE_NAME = "DemoDataBase";
    public static String USER_TABLE_NAME = "UserData";
    public static String ADAPTOR_TABLE_NAME = "AdaptorData";
    public static String TAG = "Nihar";
    public static int DATABASE_VERSION = 1;
    ///media player //
    public static String ABLBUM_NAME = "ALBUM";
    public static String ABLBUM_INDEX = "INDEX";
    public static MediaPlayer mediaPlayer = new MediaPlayer();
    public static Timer updateProgressTimer = null;
    public static int Duration = 0;
    public static int GetCurrentPosition = 0;
    public static int INDEX;
    public static final String LOGINPREFERENCE = "LoginPreference";


    private static final int PRELOAD_TIME_S = 20;
    public static final String IS_CAST_CONNECTED_INTENT_KEY = "IS_CAST_CONNECTED_INTENT_KEY";
    public static final String PERMALINK_INTENT_KEY = "PERMALINK_INTENT_KEY";
    public static final String GENRE_INTENT_KEY = "GENRE";
    public static final String STORY_INTENT_KEY = "STORY";
    public static final String CENSOR_RATING_INTENT_KEY = "CENSORRATING";
    public static final String CAST_INTENT_KEY = "CAST";
    public static final String SEASON_INTENT_KEY = "SEASON";

    public static final String LOGIN_PREF = "CubeLogin";
    public static final String COUNTRY_PREF = "CubeCountry";
    public static String LANGUAGE_SHARED_PRE = "CubeLanguage";
    public static String IS_LOGIN_SHARED_PRE = "CubeLoginCheck";
    public static String IS_LOGIN_PREF_KEY = "CubeisLogin";
    public static final String LANGUAGE_LIST_PREF = "CubeLanguageListPref";
    public static String GENRE_ARRAY_PREF_KEY = "genreArray";
    public static String GENRE_VALUES_ARRAY_PREF_KEY = "genreValueArray";

    public static final String loginUrl = "login";
    public static final String forgotpasswordUrl = "forgotPassword";
    public static final String registrationUrl = "registerUser";
    public static final String detailsUrl = "getContentDetails";
    public static final String episodesUrl = "episodeDetails";
    public static final String loadVideoUrl = "getVideoDetails";
    public static final String listUrl = "getContentList";
    public static final String searchUrl = "searchData";
    public static final String userValidationUrl = "isPPVSubscribed";
    public static final String downloadImageUrl = "GetImageForDownload";
    public static final String loadMenuUrl = "getMenuList";
    public static final String loadCountryUrl = "checkGeoBlock";
    public static final String loadIPUrl = "https://api.ipify.org/?format=json";
    public static final String loadBannerImageUrl = "getBannerSectionList";
    public static final String getFeaturedContent = "homepage";
    public static final String getContent = "getFeaturedContent";
    public static final String LanguageList = "getLanguageList";
    public static final String LanguageTranslation = "textTranslation";
    public static final String loadSectionName = "getSectionName";
    public static final String getGenreListUrl = "getGenreList";
    public static final String logoutUrl = "logout";
    public static final String loadProfileUrl = "getProfileDetails";
    public static final String updateProfileUrl = "updateUserProfile";
    public static final String videoLogUrl = "videoLogs";
    public static final String bufferLogUrl = "bufferLogs";
    public static final String updateBufferLogUrl = "updateBufferLogs";
    public static final String CsatAndCrew = "getCelibrity";
    public static final String isRegistrationEnabledurl = "isRegistrationEnabled";
    public static final String fbRegUrl = "socialAuth";
    public static final String fbUserExistsUrl = "getFbUserStatus";
    public static final String myLibrary = "MyLibrary";
    public static final String AboutUs = "getStaticPagedetails";
    public static final String ContactUs = "contactUs";
    public static final String PurchaseHistory = "PurchaseHistory";
    public static final String DeleteInvoicePath = "DeleteInvoicePath";
    public static final String PurchaseHistoryDetails = "Transaction";
    public static final String GetInvoicePDF = "GetInvoicePDF";
    public static final String getStudioPlanLists = "getStudioPlanLists";
    public static final String getCardDetailsUrl = "getCardsListForPPV";
    public static final String couponCodeValidationUrl = "validateCouponCode";
    public static final String authenticatedCardValidationUrl = "authUserPaymentInfo";
    public static final String addSubscriptionUrl = "ppvpayment";
    public static final String subscriptionUrl = "registerUserPayment";
    public static final String CheckDevice = "CheckDevice";
    public static final String ManageDevices = "ManageDevices";
    public static final String RemoveDevice = "RemoveDevice";

    //==========Added For FCM===============//

    public static final String CheckIfUserLoggedIn = "CheckIfUserLoggedIn";
    public static final String LogoutAll = "LogoutAll";
    public static final String UpdateGoogleid = "UpdateGoogleid";

    //=========End========================//

    public static boolean drawer_line_visibility = true;
    public static PPVModel ppvModel = null;
    public static APVModel apvModel = null;
    public static CurrencyModel currencyModel = null;
    public static DataModel dataModel = null;
    public static ArrayList<LanguageModel> languageModel = null;
    public static boolean goToLibraryplayer = false;
    public static boolean my_library_visibility = false;
    public static ArrayList<Integer> image_orentiation = new ArrayList<>();

    public static String GOOGLE_FCM_TOKEN = "GOOGLE_FCM_TOKEN";
    public static String DEFAULT_GOOGLE_FCM_TOKEN = "0";

    public static int check_for_subscription = 0;

    public static String selected_season_id = "0";
    public static String selected_episode_id = "0";
    public static boolean MianActivityDestoryed = false;
    public static boolean DownloadChange = false;
    public static ArrayList<PlayerAdapter.ViewHolder> multiholder = new ArrayList<>();


    public static ViewPager viewPager = null;

    //    public static final String authTokenStr = "e8ae05a2ef3fd0c6688952c8f7557823"; //vishwam.tv
//    public static final String authTokenStr = "4298851905443517c9f161e6b0471969"; //Aatto
//    public static final String authTokenStr = "6a9c6e9bfeee69e63d4bab668c01dc0a"; //Cube
//    public static final String authTokenStr = "d95a752be6de25c4949be14258656097"; //Cube
//    public static final String authTokenStr = "00d7ae780e9aff77f85378dd470e9317"; //Audio demo
    public static final String authTokenStr = "ac6a501e0fc65f40870a70aa91ea7903"; //soundStudio
//    public static final String authTokenStr = "ecd415cd0f5eda272c5e23152a73872e"; //soundStudio
//    public static final String authTokenStr = "d95a752be6de25c4949be14258656097"; //Audio new

//  public static final String authTokenStr = "4c687891cb68cc3186182d78878febf5"; //classic demo

    public static String Dwonload_pdf_rootUrl = "https://www.muvi.com/docs/";

    public static final String AddtoFavlist = "AddtoFavlist";
    public static final String DeleteFavList = "DeleteFavList";
    public static final String ViewFavorite = "ViewFavourite";
    public static final String AllPlaylist = "allPlaylist";
    public static final String AllUserPlaylist = "AudioAllUserPlayList";
    public static final String AudioUserPlayListDetail = "AudioUserPlayListDetail";
    public static final String AddToPlaylist = "addToPlaylist ";
    public static final String PlayListNameEdit = "PlayListNameEdit";
    public static final String DeletePlaylist = "DeletePlaylist";
    public static final String DeleteContent = "DeleteContent";
    /////////////
    public static ArrayList<PlayListMultiModel> PLAYLIST_ITEM_ARRAY = new ArrayList<>();
    //    public static ArrayList<String> PLAYLIST_ITEM_ARRAY=new ArrayList<>();
    public static ArrayList<QueueModel> QUEUE_ARRAY = new ArrayList<>();
    public static String ArtistName;
    public static String SongNameGlobal;


    public static boolean checkNetwork(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (isConnected == false) {
            return false;
        }
        return true;
    }

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        float density = context.getResources().getDisplayMetrics().density;
        int noOfColumns;
        if (density >= 3.5 && density <= 4.0) {
            noOfColumns = (int) (dpWidth / 120);
        } else if (density <= 1.5) {
            noOfColumns = (int) (dpWidth / 140);
        } else {
            noOfColumns = (int) (dpWidth / 120);
        }
        return noOfColumns;
    }

    //Array Contains a string
    public static boolean containsString(String[] list, String str) {
        if (Arrays.asList(list).contains(str)) {
            return true;
        }
        return false;
    }


    public static String rootUrl() {
        //String rootUrl = "https://sonydadc.muvi.com/rest/";
//        String rootUrl = "http://muvistudio.edocent.com/rest/";
        String rootUrl = "https://www.muvi.com/rest/";
        //String rootUrl = "https://www.idogic.com/rest/";
        return rootUrl;

    }

    public static int isDouble(String str) {
        Double d = Double.parseDouble(str);
        int i = d.intValue();
        return i;

    }

    public static String formateDateFromstring(String inputFormat, String outputFormat, String inputDate) {

        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, java.util.Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, java.util.Locale.getDefault());

        try {

            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);

        } catch (ParseException e) {
            e.printStackTrace();
            outputDate = "";
        }
        return outputDate;

    }

    public static long calculateDays(Date dateEarly, Date dateLater) {
        return (dateLater.getTime() - dateEarly.getTime()) / (24 * 60 * 60 * 1000);
    }

    //Email Validation for login
    public static boolean isValidMail(String email2) {
        boolean check;
        Pattern p;
        Matcher m;
        String EMAIL_STRING = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        p = Pattern.compile(EMAIL_STRING);
        m = p.matcher(email2);
        check = m.matches();
        if (!check) {
        }
        return check;
    }

    public static boolean isConfirmPassword(String password, String confirmPassword) {
        Pattern pattern = Pattern.compile(password, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(confirmPassword);

        if (!matcher.matches()) {
            // do your Toast("passwords are not matching");
            return false;
        }
        return true;
    }

    public static boolean containsIgnoreCase(List<Integer> list, int soughtFor) {
        for (Integer current : list) {
            if (current == soughtFor) {
                return true;
            }
        }
        return false;
    }


    public static boolean itemclicked = false;

    public
    @Nullable
    static SharedPreferences getLanguageSharedPrefernce(Context context) {
        SharedPreferences languageSharedPref = null;
        try {
             languageSharedPref = context.getSharedPreferences(Util.LANGUAGE_SHARED_PRE, 0); // 0 - for private mode

        }
        catch (Exception e){}
        return languageSharedPref;
    }

    public static void setLanguageSharedPrefernce(Context context, String Key, String Value) {
        SharedPreferences languageSharedPref = context.getSharedPreferences(Util.LANGUAGE_SHARED_PRE, 0); // 0 - for private mode
        SharedPreferences.Editor editor = languageSharedPref.edit();
        editor.putString(Key, Value);
        editor.commit();
    }

    public
    @Nullable
    static String getTextofLanguage(Context context, String tempKey, String defaultValue) {
        String str= null;
        try {
            SharedPreferences sp = Util.getLanguageSharedPrefernce(context);
             str = sp.getString(tempKey, defaultValue);
        }
        catch (Exception e){

        }


        return str;
    }

    public static Dialog LoadingCircularDialog(Context mContext) {
        Dialog pd = new Dialog(mContext, R.style.MyMaterialTheme);
        View view = LayoutInflater.from(mContext).inflate(R.layout.progress_bar_layout, null);
        pd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pd.getWindow().setBackgroundDrawableResource(R.color.transparent);
        pd.setContentView(view);
        return pd;
    }

    public static String IS_MYLIBRARY = "IS_MYLIBRARY";
    public static String DEFAULT_IS_MYLIBRARY = "0";

    public static String SELECTED_LANGUAGE_CODE = "SELECTED_LANGUAGE_CODE";

    public static String SEARCH_PLACEHOLDER = "SEARCH_PLACEHOLDER";
    public static String VIEW_TRAILER = "VIEW_TRAILER";
    public static String WATCH_NOW = "WATCH_NOW";
    public static String WATCH = "WATCH";
    public static String DESCRIPTION = "DESCRIPTION";
    public static String GENRE = "GENRE";
    public static String CENSOR_RATING = "CENSOR_RATING";
    public static String CAST = "CAST";
    public static String DIRECTOR = "DIRECTOR";
    public static String VIEW_MORE = "VIEW_MORE";
    public static String VIEW_LESS = "VIEW_LESS";
    public static String FILTER_BY = "FILTER_BY";
    public static String SORT_BY = "SORT_BY";
    public static String FORGOT_PASSWORD = "FORGOT_PASSWORD";
    public static String LOGIN = "LOGIN";
    public static String CONFIRM_PASSWORD = "CONFIRM_PASSWORD";

    public static String UPDATE_PROFILE = "UPDATE_PROFILE";
    public static String DEFAULT_UPDATE_PROFILE = "Update Profile";
    public static String DEFAULT_APP_ON = "On";
    public static String APP_ON = "APP_ON";
    public static String DEFAULT_APP_SELECT_LANGUAGE = "Select Language";
    public static String APP_SELECT_LANGUAGE = "APP_SELECT_LANGUAGE";
    public static String RESUME_MESSAGE = "RESUME_MESSAGE";


    public static String PROFILE = "PROFILE";
    public static String PURCHASE_HISTORY = "PURCHASE_HISTORY";
    public static String LOGOUT = "LOGOUT";
    public static String CHANGE_PASSWORD = "Change Password";
    public static String TRANSACTION = "TRANSACTION";
    public static String INVOICE = "INVOICE";
    public static String TRANSACTION_DATE = "TRANSACTION_DATE";
    public static String ORDER = "ORDER";
    public static String AMOUNT = "AMOUNT";
    public static String TRANSACTION_STATUS = "TRANSACTION_STATUS";
    public static String PLAN_NAME = "PLAN_NAME";
    public static String SEASON = "SEASON";
    public static String SELECT_PLAN = "SELECT_PLAN";
    public static String PURCHASE = "PURCHASE";
    public static String CREDIT_CARD_DETAILS = "CREDIT_CARD_DETAILS";
    public static String CARD_WILL_CHARGE = "CARD_WILL_CHARGE";
    public static String SAVE_THIS_CARD = "SAVE_THIS_CARD";
    public static String USE_NEW_CARD = "USE_NEW_CARD";
    public static String BUTTON_APPLY = "BUTTON_APPLY";
    public static String BUTTON_OK = "BUTTON_OK";
    public static String AGREE_TERMS = "AGREE_TERMS";
    public static String TERMS = "TERMS";
    public static String OOPS_INVALID_EMAIL = "OOPS_INVALID_EMAIL";
    public static String VALID_CONFIRM_PASSWORD = "VALID_CONFIRM_PASSWORD";
    public static String PASSWORDS_DO_NOT_MATCH = "PASSWORDS_DO_NOT_MATCH";
    public static String EMAIL_EXISTS = "EMAIL_EXISTS";
    public static String EMAIL_DOESNOT_EXISTS = "EMAIL_DOESNOT_EXISTS";
    public static String PASSWORD_RESET_LINK = "PASSWORD_RESET_LINK";
    public static String YES = "YES";
    public static String NO = "NO";
    public static String PROFILE_UPDATED = "PROFILE_UPDATED";
    public static String INVALID_COUPON = "INVALID_COUPON";
    public static String DISCOUNT_ON_COUPON = "DISCOUNT_ON_COUPON";
    public static String HOME = "HOME";

    public static String ACTIVATE_SUBSCRIPTION_WATCH_VIDEO = "ACTIVATE_SUBSCRIPTION_WATCH_VIDEO";
    public static String CROSSED_MAXIMUM_LIMIT = "CROSSED_MAXIMUM_LIMIT";
    public static String CONTENT_NOT_AVAILABLE_IN_YOUR_COUNTRY = "CONTENT_NOT_AVAILABLE_IN_YOUR_COUNTRY";
    public static String ALREADY_PURCHASE_THIS_CONTENT = "ALREADY_PURCHASE_THIS_CONTENT";
    public static String SORT_ALPHA_A_Z = "SORT_ALPHA_A_Z";
    public static String SORT_ALPHA_Z_A = "SORT_ALPHA_Z_A";
    public static String SORT_LAST_UPLOADED = "SORT_LAST_UPLOADED";
    public static String SEARCH_HINT = "SEARCH_HINT";
    public static String GEO_BLOCKED_ALERT = "GEO_BLOCKED_ALERT";
    public static String NO_INTERNET_NO_DATA = "NO_INTERNET_NO_DATA";
    public static String TRY_AGAIN = "TRY_AGAIN";
    public static String SLOW_INTERNET_CONNECTION = "SLOW_INTERNET_CONNECTION";
    public static String NO_INTERNET_CONNECTION = "NO_INTERNET_CONNECTION";
    public static String NEW_HERE_TITLE = "NEW_HERE_TITLE";
    public static String SIGN_UP_TITLE = "SIGN_UP_TITLE";
    public static String NAME_HINT = "NAME_HINT";
    public static String ALREADY_MEMBER = "ALREADY_MEMBER";
    public static String LANGUAGE_POPUP_LOGIN = "LANGUAGE_POPUP_LOGIN";
    public static String LANGUAGE_POPUP_LANGUAGE = "LANGUAGE_POPUP_LANGUAGE";
    public static String OLD_PASSWORD = "OLD_PASSWORD";
    public static String NEW_PASSWORD = "NEW_PASSWORD";
    public static String TRANSACTION_STATUS_ACTIVE = "TRANSACTION_STATUS_ACTIVE";
    public static String TRANSACTION_STATUS_EXPIRED = "TRANSACTION_STATUS_EXPIRED";
    public static String TRANSACTION_DETAIL_PURCHASE_DATE = "TRANSACTION_DETAIL_PURCHASE_DATE";
    public static String DOWNLOAD_BUTTON_TITLE = "DOWNLOAD_BUTTON_TITLE";
    public static String CAST_CREW_BUTTON_TITLE = "CAST_CREW_BUTTON_TITLE";
    public static String EPISODE_TITLE = "EPISODE_TITLE";

    public static String ACTIAVTE_PLAN_TITLE = "ACTIAVTE_PLAN_TITLE";
    public static String SELECT_OPTION_TITLE = "SELECT_OPTION_TITLE";
    public static String CREDIT_CARD_NAME_HINT = "CREDIT_CARD_NAME_HINT";
    public static String CREDIT_CARD_NUMBER_HINT = "CREDIT_CARD_NUMBER_HINT";
    public static String CREDIT_CARD_CVV_HINT = "CREDIT_CARD_CVV_HINT";
    public static String COUPON_CODE_HINT = "COUPON_CODE_HINT";
    public static String PAYMENT_OPTIONS_TITLE = "PAYMENT_OPTIONS_TITLE";
    public static String UPDATE_PROFILE_ALERT = "UPDATE_PROFILE_ALERT";
    public static String ALERT = "ALERT";
    public static String STORY_TITLE = "STORY_TITLE";
    public static String NO_DETAILS_AVAILABLE = "NO_DETAILS_AVAILABLE";
    public static String SORRY = "SORRY";
    public static String NO_VIDEO_AVAILABLE = "NO_VIDEO_AVAILABLE";
    public static String NO_DATA = "NO_DATA";
    public static String NO_CONTENT = "NO_CONTENT";
    public static String VIDEO_ISSUE = "VIDEO_ISSUE";
    public static String ERROR_IN_REGISTRATION = "ERROR_IN_REGISTRATION";
    public static String LOGOUT_SUCCESS = "LOGOUT_SUCCESS";
    public static String PAY_WITH_CREDIT_CARD = "PAY_WITH_CREDIT_CARD";
    public static String ENTER_EMPTY_FIELD = "ENTER_EMPTY_FIELD";

    public static String EMAIL_PASSWORD_INVALID = "EMAIL_PASSWORD_INVALID";
    public static String ADVANCE_PURCHASE = "ADVANCE_PURCHASE";
    public static String FAILURE = "FAILURE";
    public static String COUPON_CANCELLED = "COUPON_CANCELLED";
    public static String COUPON_ALERT = "COUPON_ALERT";
    public static String DETAILS_NOT_FOUND_ALERT = "DETAILS_NOT_FOUND_ALERT";
    public static String DEFAULT_DETAILS_NOT_FOUND_ALERT = "Failed to find details.";

    public static String UNPAID = "UNPAID";
    public static String ERROR_IN_PAYMENT_VALIDATION = "ERROR_IN_PAYMENT_VALIDATION";
    public static String PURCHASE_SUCCESS_ALERT = "PURCHASE_SUCCESS_ALERT";
    public static String NO_RECORD = "NO_RECORD";

    public static String CANCEL_BUTTON = "CANCEL_BUTTON";
    public static String CONTINUE_BUTTON = "CONTINUE_BUTTON";


    public static String ADD_TO_FAV = "ADD_TO_FAV";
    public static String ADDED_TO_FAV = "ADDED_TO_FAV";
    public static String SIGN_OUT_WARNING = "SIGN_OUT_WARNING";
    public static String SEARCH_ALERT = "SEARCH_ALERT";
    public static String TEXT_EMIAL = "TEXT_EMIAL";
    public static String TEXT_PASSWORD = "TEXT_PASSWORD";
    public static String MY_FAVOURITE = "MY_FAVOURITE";
    public static String TRANSACTION_DETAILS_ORDER_ID = "TRANSACTION_DETAILS_ORDER_ID";
    public static String PAY_BY_PAYPAL = "PAY_BY_PAYPAL";
    public static String BTN_PAYNOW = "BTN_PAYNOW";
    public static String BTN_REGISTER = "BTN_REGISTER";
    public static String SORT_RELEASE_DATE = "SORT_RELEASE_DATE";
    public static String TEXT_SEARCH_PLACEHOLDER = "TEXT_SEARCH_PLACEHOLDER";
    public static String SLOW_ISSUE_INTERNET_CONNECTION = "SLOW_ISSUE_INTERNET_CONNECTION";
    public static String SIGN_OUT_ERROR = "SIGN_OUT_ERROR";
    public static String BTN_SUBMIT = "BTN_SUBMIT";


    public static String TRANASCTION_DETAIL = "TRANASCTION_DETAIL";
    public static String SIGN_OUT_ALERT = "SIGN_OUT_ALERT";
    public static String IS_ONE_STEP_REGISTRATION = "IS_ONE_STEP_REGISTRATION";
    public static final String HAS_FAVORITE = "HAS_FAVORITE";
    public static final String DEFAULT_HAS_FAVORITE = "DEFAULT_HAS_FAVORITE";
    public static final String CHROMECAST = "CHROMECAST";
    public static final String ISPLAYLIST = "ISPLAYLIST";
    public static final String DEFAULT_ISPLAYLIST = "DEFAULT_ISPLAYLIST";
    public static final String ISQUEUE = "ISQUEUE";
    public static final String DEFAULT_ISQUEUE= "DEFAULT_ISQUEUE";
    public static final String HASDOWNLOAD = "HASDOWNLOAD";
    public static final String DEFAULT_HASDOWNLOAD = "DEFAULT_HASDOWNLOAD";
    public static final String DEFAULT_CHROMECAST = "DEFAULT_CHROMECAST";

    public static String DEFAULT_IS_ONE_STEP_REGISTRATION = "0";

    public static String ENTER_REGISTER_FIELDS_DATA = "ENTER_REGISTER_FIELDS_DATA";

    public static String MY_LIBRARY = "MY_LIBRARY";
    public static String ABOUT_US = "ABOUT_US";
    public static String FILL_FORM_BELOW = "FILL_FORM_BELOW";
    public static String MESSAGE = "MESSAGE";

    // Added Later for fcm

    public static String SIMULTANEOUS_LOGOUT_SUCCESS_MESSAGE = "SIMULTANEOUS_LOGOUT_SUCCESS_MESSAGE";
    public static String ANDROID_VERSION = "ANDROID_VERSION";
    public static String MANAGE_DEVICE = "MANAGE_DEVICE";
    public static String YOUR_DEVICE = "YOUR_DEVICE";
    public static String DEREGISTER = "DEREGISTER";
    public static String LOGIN_STATUS_MESSAGE = "LOGIN_STATUS_MESSAGE";
    public static String UPADTE_TITLE = "UPADTE_TITLE";
    public static String UPADTE_MESSAGE = "UPADTE_MESSAGE";
    public static String REGISTER_FACEBOOK = "REGISTER_FACEBOOK";
    public static String LOGIN_FACEBOOK = "LOGIN_FACEBOOK";


    public static String IS_RESTRICT_DEVICE = "IS_RESTRICT_DEVICE";
    public static String DEFAULT_IS_RESTRICT_DEVICE = "0";
// ======================= Constants For The Language Default Key =========================//


    // Added Later for fcm

    public static String DEFAULT_LOGIN_STATUS_MESSAGE = "You are no longer logged in . Please log in again.";
    public static String DEFAULT_SIMULTANEOUS_LOGOUT_SUCCESS_MESSAGE = "Logout process has been successfully completed. Now you are authorized to login.";
    public static String DEFAULT_ANDROID_VERSION = "Android Version";
    public static String DEFAULT_MANAGE_DEVICE = "Manage Device";
    public static String DEFAULT_YOUR_DEVICE = "Your Devices";
    public static String DEFAULT_DEREGISTER = "Deregister";
    public static String DEFAULT_UPADTE_TITLE = "Upadte Available";
    public static String DEFAULT_UPADTE_MESSAGE = "A new update is available . Would you like to update now ?";

    public static String DEFAULT_VIDEO_ISSUE = "There's a problem with a video or Internet connection";
    public static String DEFAULT_REGISTER_FACEBOOK = "Register With Facebook";
    public static String DEFAULT_LOGIN_FACEBOOK = "Login With Facebook";


// ======================= Constants For The Language Default Key =========================//


    //ADD LATER FOR PURCHASE AND TRANSACTION DETAILS
    public static String PLAN_ID = "PLAN_ID";
    public static String DEFAULT_PLAN_ID = "0";

    public static String NO_PDF = "NO_PDF";
    public static String DEFAULT_NO_PDF = "PDF Not Available.";

    public static String DOWNLOAD_INTERRUPTED = "DOWNLOAD_INTERRUPTED";
    public static String DEFAULT_DOWNLOAD_INTERRUPTED = "Download Interrupted.";

    public static String DOWNLOAD_COMPLETED = "DOWNLOAD_COMPLETED";
    public static String DEFAULT_DOWNLOAD_COMPLETED = "Download Completed.";

    public static String TRANSACTION_TITLE = "TRANSACTION_TITLE";
    public static String DEFAULT_TRANSACTION_TITLE = "Transaction";

    public static String FREE = "FREE";
    public static String DEFAULT_FREE = "FREE";

    public static String SUBSCRIPTION_COMPLETED = "SUBSCRIPTION_COMPLETED";
    public static String DEFAULT_SUBSCRIPTION_COMPLETED = "Your subscription process completed successfully.";

    public static String CVV_ALERT = "CVV_ALERT";
    public static String DEFAULT_CVV_ALERT = "Please enter your CVV.";

    public static String DEFAULT_CANCEL_BUTTON = "Cancel";
    public static String DEFAULT_INVOICE = "Invoice";
    public static String TRANSACTION_ORDER_ID = "TRANSACTION_ORDER_ID";
    public static String DEFAULT_TRANSACTION_ORDER_ID = "Order Id";
    public static String DEFAULT_TRANSACTION_DETAIL_PURCHASE_DATE = "Purchase Date";
    public static String DEFAULT_TRANSACTION_DETAILS_TITLE = "Transaction Details";
    public static String DEFAULT_TRANSACTION_DATE = "Transaction Date";
    public static String DEFAULT_ORDER = "Order";
    public static String DEFAULT_AMOUNT = "Amount";
    public static String DEFAULT_TRANSACTION_STATUS = "Transaction Status";
    public static String DEFAULT_PLAN_NAME = "Plan Name";
    public static String DEFAULT_TRANASCTION_DETAIL = "Transaction Details";
    public static String DEFAULT_DOWNLOAD_BUTTON_TITLE = "DOWNLOAD";
    public static String DEFAULT_ACTIAVTE_PLAN_TITLE = "Activate Plan";
    public static String DEFAULT_SAVE_THIS_CARD = "Save this card for faster checkouts";
    public static String BUTTON_PAY_NOW = "BUTTON_PAY_NOW";
    public static String DEFAULT_CARD_WILL_CHARGE = "Your card will be charged now :";
    public static String DEFAULT_CREDIT_CARD_NAME_HINT = "Enter your Name on Card";
    public static String DEFAULT_CREDIT_CARD_NUMBER_HINT = "Enter your Card Number";
    public static String DEFAULT_COUPON_CANCELLED = "Applied coupon is cancelled.";
    public static String DEFAULT_CREDIT_CARD_DETAILS = "Credit Card Details";
    public static String DEFAULT_BUTTON_PAY_NOW = "Pay Now";
    public static String DEFAULT_SELECT_PLAN = "Select Your Plan";
    public static String DEFAULT_CONTINUE_BUTTON = "Continue";
    public static String DEFAULT_USE_NEW_CARD = "Use New Card";
    public static String DEFAULT_DISCOUNT_ON_COUPON = "Awesome, You just saved";
    public static String DEFAULT_INVALID_COUPON = "Invalid Coupon!";
    public static String DEFAULT_ERROR_IN_PAYMENT_VALIDATION = "Error in payment validation";
    public static String ERROR_IN_SUBSCRIPTION = "ERROR_IN_SUBSCRIPTION";
    public static String DEFAULT_ERROR_IN_SUBSCRIPTION = "Error in Subscription";
    public static String DEFAULT_PURCHASE_SUCCESS_ALERT = "You have successfully purchased the content.";
    public static String DEFAULT_COUPON_CODE_HINT = "Enter Coupon Code";

    //ADD LATER FOR PURCHASE AND TRANSACTION DETAILS


    public static String DEFAULT_TRY_AGAIN = "Try Again !";
    public static String DEFAULT_FILL_FORM_BELOW = "Fill the form below.";
    public static String DEFAULT_MESSAGE = "Message";

    public static String DEFAULT_MY_LIBRARY = "My Library";
    public static String DEFAULT_SELECTED_LANGUAGE_CODE = "en";
    public static String DEFAULT_HOME = "Home";
    public static String DEFAULT_ENTER_REGISTER_FIELDS_DATA = "Fill the empty field(s)";
    public static String DEFAULT_TERMS_AND_CONDITIONS = "Termes & Conditions";
    public static String DEFAULT_ABOUT_US = "About Us";
    public static String DEFAULT_CONTACT_US = "Contact Us";

    public static String DEFAULT_SEARCH_PLACEHOLDER = "Search";
    public static String DEFAULT_VIEW_TRAILER = "View Trailer";
    public static String DEFAULT_VIEW_MORE = "View More";
    public static String DEFAULT_VIEW_LESS = "View Less";
    public static String DEFAULT_FILTER_BY = "Filter By";
    public static String DEFAULT_SORT_BY = "Sort By";
    public static String DEFAULT_FORGOT_PASSWORD = "Forgot Password?";
    public static String DEFAULT_LOGIN = "SIGN IN";
    public static String DEFAULT_CONFIRM_PASSWORD = "Confirm Password";
    public static String DEFAULT_PROFILE = "Profile";
    public static String DEFAULT_PURCHASE_HISTORY = "Purchase History";
    public static String DEFAULT_LOGOUT = "Logout";
    public static String DEFAULT_CHANGE_PASSWORD = "Change Password";
    public static String DEFAULT_SEASON = "Season";

    public static String DEFAULT_PURCHASE = "Purchase";
    public static String DEFAULT_BUTTON_APPLY = "Apply";
    public static String DEFAULT_BUTTON_OK = "Ok";
    public static String DEFAULT_OOPS_INVALID_EMAIL = "Oops! Invalid email.";
    public static String DEFAULT_PASSWORDS_DO_NOT_MATCH = "Passwords do not match";
    public static String DEFAULT_EMAIL_EXISTS = "Email already exists";
    public static String DEFAULT_EMAIL_DOESNOT_EXISTS = "Email does not exist. Please enter correct email.";
    public static String DEFAULT_PASSWORD_RESET_LINK = "Password Reset link has been emailed to your registered email ID. Please check your email to reset password.";
    public static String DEFAULT_YES = "Yes";
    public static String DEFAULT_NO = "No";
    public static String DEFAULT_PROFILE_UPDATED = "Profile updated successfully.";

    public static String DEFAULT_ACTIVATE_SUBSCRIPTION_WATCH_VIDEO = "You are not authorised to view this content. Please activate";
    public static String DEFAULT_CROSSED_MAXIMUM_LIMIT = "Sorry, you have exceeded the maximum number of views for this content.";
    public static String DEFAULT_CONTENT_NOT_AVAILABLE_IN_YOUR_COUNTRY = "This content is not available to stream in your country";
    public static String DEFAULT_ALREADY_PURCHASE_THIS_CONTENT = "Sorry, you have already purchased this content earlier.";
    public static String DEFAULT_SORT_ALPHA_A_Z = "Alphabetic A-Z";
    public static String DEFAULT_SORT_ALPHA_Z_A = "Alphabetic Z-A";
    public static String DEFAULT_SORT_LAST_UPLOADED = "Last Uploaded";
    public static String DEFAULT_GEO_BLOCKED_ALERT = "Sorry, this app is not available in your country.";
    public static String DEFAULT_NO_INTERNET_NO_DATA = "No Internet Connection / No Data";
    public static String DEFAULT_SLOW_INTERNET_CONNECTION = "Slow Internet Connection";
    public static String DEFAULT_NO_INTERNET_CONNECTION = "No Internet Connection";
    public static String DEFAULT_NEW_HERE_TITLE = "New here ?";
    public static String DEFAULT_SIGN_UP_TITLE = "Sign Up";

    public static String DEFAULT_NAME_HINT = "Enter your Name";
    public static String DEFAULT_ALREADY_MEMBER = "Already Member";
    public static String DEFAULT_LANGUAGE_POPUP_LOGIN = "Log in";
    public static String DEFAULT_LANGUAGE_POPUP_LANGUAGE = "Language";
    public static String DEFAULT_OLD_PASSWORD = "New Password";
    public static String DEFAULT_NEW_PASSWORD = "Confirm Password";
    public static String DEFAULT_CAST_CREW_BUTTON_TITLE = "Cast and Crew";
    public static String DEFAULT_EPISODE_TITLE = "All Episodes";

    public static String DEFAULT_UPDATE_PROFILE_ALERT = "We could not be able to update your profile. Please try again.";
    public static String DEFAULT_NO_DETAILS_AVAILABLE = "No details available";
    public static String DEFAULT_SORRY = "Sorry !";
    public static String DEFAULT_NO_VIDEO_AVAILABLE = "There's some error. Please try again !";
    public static String DEFAULT_NO_DATA = "No Data";
    public static String DEFAULT_NO_CONTENT = "There's no matching content found.";
    public static String DEFAULT_ERROR_IN_REGISTRATION = "Error in registration";
    public static String DEFAULT_LOGOUT_SUCCESS = "Logout Success";
    public static String DEFAULT_ENTER_EMPTY_FIELD = "Fill the empty field(s)";

    public static String DEFAULT_EMAIL_PASSWORD_INVALID = "Email or Password is invalid!";
    public static String DEFAULT_ADVANCE_PURCHASE = "Advance Purchase";
    public static String DEFAULT_FAILURE = "Failure !";
    public static String DEFAULT_NO_RECORD = "No record found!!!";
    public static String DEFAULT_SIGN_OUT_WARNING = "Are you sure you want to sign out ?";
    public static String DEFAULT_SEARCH_ALERT = "Enter some text to search ...";
    public static String DEFAULT_TEXT_EMIAL = "Enter your Email-Id";
    public static String DEFAULT_TEXT_PASSWORD = "Enter Password";
    public static String DEFAULT_BTN_REGISTER = "SIGN UP";
    public static String DEFAULT_SORT_RELEASE_DATE = "Release Date";
    public static String DEFAULT_TEXT_SEARCH_PLACEHOLDER = "Search";
    public static String DEFAULT_SIGN_OUT_ERROR = "Sorry, we can not be able to log out. Try again!.";
    public static String DEFAULT_BTN_SUBMIT = "Submit";
    public static String DEFAULT_RESUME_MESSAGE = "Continue watching where you left?";
    public static String DEAFULT_CANCEL_BUTTON = "Cancel";
    public static String DEAFULT_CONTINUE_BUTTON = "Continue";

    //////////////////////////////////////////////////////////////////////////////
    public static String DEFAULT_DELETE_BTN = "Delete";


    public static void showToast(Context context, String message) {


        final Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
//        toast.setGravity(Gravity.BOTTOM, 0, 0);
        //*/  View view = toast.getView();
//        view.setBackgroundResource(R.drawable.toast_boder1);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        //v.setTextColor(getResources().getColor(R.color.muvilogocolor));
        v.setTextColor(Color.WHITE);//*/

        toast.show();

        new CountDownTimer(10000, 1000) {

            public void onTick(long millisUntilFinished) {
                toast.show();
            }

            public void onFinish() {
                toast.show();
            }

        }.start();


    }


    //////////////chromecast///////////////////////////
    public static boolean isOrientationPortrait(Context context) {
        return context.getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_PORTRAIT;
    }
        int DownloadPosition = 0 ;
}
