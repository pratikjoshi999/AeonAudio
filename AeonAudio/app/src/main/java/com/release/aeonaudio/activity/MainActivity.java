package com.release.aeonaudio.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.ShortcutManager;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.CastState;
import com.google.android.gms.cast.framework.CastStateListener;
import com.google.android.gms.cast.framework.IntroductoryOverlay;
import com.google.android.gms.cast.framework.SessionManagerListener;
import com.google.gson.Gson;
import com.home.apisdk.APIUrlConstant;
import com.home.apisdk.apiController.GetValidateUserAsynTask;
import com.home.apisdk.apiController.LogoutAsynctask;
import com.home.apisdk.apiModel.ContentDetailsOutput;
import com.home.apisdk.apiModel.Episode_Details_output;
import com.home.apisdk.apiModel.LogoutInput;
import com.home.apisdk.apiModel.ValidateUserInput;
import com.home.apisdk.apiModel.ValidateUserOutput;
import com.release.aeonaudio.R;
import com.release.aeonaudio.adapter.PlayerDataAdaptor;
import com.release.aeonaudio.adapter.PlayerDataMultipartAdaptor;
import com.release.aeonaudio.adapter.QueueAdaptor;
import com.release.aeonaudio.model.QueueModel;
import com.release.aeonaudio.service.MusicService;
import com.release.aeonaudio.ui.MediaHelper;
import com.release.aeonaudio.ui.PlayerAdapter;
import com.release.aeonaudio.ui.PlayerViewPagerAdapter;
import com.release.aeonaudio.utils.Constants;
import com.release.aeonaudio.utils.CustomTypefaceSpan;
import com.release.aeonaudio.utils.ProgressBarHandler;
import com.release.aeonaudio.utils.Util;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;

import static android.R.attr.name;
import static android.R.id.message;
import static android.R.id.text1;
import static android.media.CamcorderProfile.get;
import static com.release.aeonaudio.R.layout.item;
import static com.release.aeonaudio.utils.Util.ArtistName;
import static com.release.aeonaudio.utils.Util.LOGINPREFERENCE;
import static com.release.aeonaudio.utils.Util.QUEUE_ARRAY;
import static com.release.aeonaudio.utils.Util.SongNameGlobal;
import static com.release.aeonaudio.utils.Util.mediaPlayer;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, LogoutAsynctask.Logout, GetValidateUserAsynTask.GetValidateUser, PlayListFragment.Communicate, MediaHelper {

    private static final int STATE_PLAYING = 1;
    private static final int STATE_PAUSED = 2;
    FrameLayout frameLayout;
    Menu SearchMenu = null;
    SearchView searchView;
    String[] genreArrToSend;
    ViewPager SquizeViewPager;
    String[] genreValueArrayToSend;
    boolean bootomsheet_open = false;
    DrawerLayout drawer;
    ImageView close_drawer, player_image_main, player_close, open_bottomSheet, albumArt_player, player_play_ic, player_prev_ic, player_next_icc;
    NavigationView navigationView;
    Fragment fragment = null;
    String title, AlbumArtUrl;
    int adaptorPosition;
    String song_status = null;
    String Song_name, Artist;
    private static final int TIME_DELAY = 2000;
    private static long back_pressed;
    Toolbar toolbar, playerToolbar;
    View bottomSheet;
    ImageView editprofile, profile_iv_navigation, equalizer, background_player;
    TextView slash, edit_profile_tv, login_tv, songname_player, signup_tv, profilename, option_songName, option_artist_name, remove_from_list, rename, Remove_from_Queue, add_to_Queue, add_to_Playlist, remove_Playlist_content;
    TextView curent_duration, total_duration, song_p_name, Artist_p_name, song_name_toolbar, artist_name_toolbar;
    RelativeLayout duration_layout;
    LinearLayout open_bottomSheet_controller;
    Timer durationTimer;
    ContentDetailsOutput contentDetailsOutput;
    boolean isRunning = false;
    SeekBar seekBar, seekbar_botomSht;
    ProgressBar musicProgress;
    ProgressBarHandler pDialog;
    private BottomSheetBehavior mBottomSheetBehavior;
    private BottomSheetBehavior mQueueBottomSheetBehavior;
    public static String internetSpeed;
    public static ProgressBarHandler internetSpeedDialog;
    private String next_responce;
    PlayerDataAdaptor adapter;
    //    PlayerDataMultipartAdaptor multipartAdaptor;
    QueueAdaptor QueueAdaptor;
    StringBuffer Sb;
    SQLiteDatabase DB;
    private RecyclerView playerRecycle;
    ActionBarDrawerToggle toggle;
    PlayerAdapter queueadapter;
    int corePoolSize = 60;
    int maximumPoolSize = 80;
    int keepAliveTime = 10;
    int position;
    String userid, emailid;
    String album_name, song_url, song_imageUrl, song_name;
    Button ClearQueueButton;
    boolean bootomsheetOpen = false;
    CoordinatorLayout minicontroller, mainLayout;
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
    Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);
    ArrayList<QueueModel> QueueList = new ArrayList<>();
    View sheetView;
    LogoutInput logoutInput;
    BottomSheetDialog mBottomSheetDialog;
    SharedPreferences QueuePref;
    ArrayList<String> display_names = new ArrayList<>();
    ArrayList<String> fdisplay_names = new ArrayList<>();
    ArrayList<String> permalinks = new ArrayList<>();
    ArrayList<String> fpermalinks = new ArrayList<>();

    ///network
    private final int MSG_UPDATE_STATUS = 0;
    private final int MSG_UPDATE_CONNECTION_TIME = 1;
    private final int MSG_COMPLETE_STATUS = 2;
    //logout
    SharedPreferences prefs;
    String loginHistory;
    LogoutAsynctask logoutAsynctask;
    SharedPreferences isLoginPref;
    private int currentAdapterPosition;
    ///CHROMECAST/////
    private Timer mControllersTimer;
    private PlaybackLocation mLocation;
    private PlaybackState mPlaybackState;
    private final float mAspectRatio = 72f / 128;
    private AQuery mAquery;
    private MediaInfo mSelectedMedia;

    private CastContext mCastContext;
    private SessionManagerListener<CastSession> mSessionManagerListener =
            new MySessionManagerListener();
    private CastSession mCastSession;
    private MenuItem mediaRouteMenuItem;
    private IntroductoryOverlay mIntroductoryOverlay;
    private CastStateListener mCastStateListener;
    android.support.v7.app.MediaRouteButton mediaRouteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String userInfo = intent.getStringExtra("Signing");

    /*  playerRecycle = (RecyclerView) findViewById(R.id.playerlist_recycle);
        playerRecycle.addItemDecoration(new MainActivity.SimpleDividerItemDecoration(MainActivity.this));
        playerRecycle.setHasFixedSize(true);*/

        isLoginPref = getSharedPreferences(Util.IS_LOGIN_SHARED_PRE, 0);
        QueuePref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
////////////////////////////////Navigation Drawer and Bottom sheet/////////////////
        /*
        player page recycle view with adaptorF
         */
        albumArt_player = (ImageView) findViewById(R.id.miniControl_play);
        player_play_ic = (ImageView) findViewById(R.id.player_play_ic);

        player_next_icc = (ImageView) findViewById(R.id.player_next_icc);
        player_prev_ic = (ImageView) findViewById(R.id.player_prev_ic);
        background_player = (ImageView) findViewById(R.id.background_player);

        songname_player = (TextView) findViewById(R.id.songname_player);

        mediaRouteButton = (android.support.v7.app.MediaRouteButton) findViewById(R.id.media_route_button);
        player_prev_ic.setClickable(true);

        player_image_main = (ImageView) findViewById(R.id.player_image_main);
//        player_image_background = (ImageView) findViewById(R.id.player_image_background);
        String link = "https://i.ytimg.com/vi/IjdRUM1Nyp8/maxresdefault.jpg";


        //////////////////////^////////////////////////////
        minicontroller = (CoordinatorLayout) findViewById(R.id.bottomSheetLayout);
        mainLayout = (CoordinatorLayout) findViewById(R.id.mainLayout);
        //////////////////////////MAIN ACTIVITY ///////////////////////
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);
        getSupportActionBar().setIcon(R.drawable.logo);
        final ActionBar ab = getSupportActionBar();
        toolbar.post(new Runnable() {
            @Override
            public void run() {
                Drawable d = ResourcesCompat.getDrawable(getResources(), R.drawable.nav_drawer, null);
                toolbar.setNavigationIcon(d);
            }
        });
//        ab.setHomeAsUpIndicator(R.drawable.nav_drawer);
        ab.setDisplayHomeAsUpEnabled(true);
        open_bottomSheet = (ImageView) findViewById(R.id.open_bottomSheet);
        player_close = (ImageView) findViewById(R.id.player_close);
        equalizer = (ImageView) findViewById(R.id.equalizer);
        open_bottomSheet_controller = (LinearLayout) findViewById(R.id.details);
        title = getString(R.string.app_name);
        pDialog = new ProgressBarHandler(this);
        ////////////////////////BOTTOM SHEET////////////////////////////////////

        /*View  QueuebottomSheet = minicontroller.findViewById(R.id.myQueue);
        mQueueBottomSheetBehavior =BottomSheetBehavior.from(QueuebottomSheet);
        mQueueBottomSheetBehavior.setState(BottomSheetBehavior.STATE_DRAGGING);*/
     /*   View  QueuebottomSheet = minicontroller.findViewById(R.id.myQueue);

        mQueueBottomSheetBehavior =BottomSheetBehavior.from(QueuebottomSheet);
        mQueueBottomSheetBehavior.setState(BottomSheetBehavior.STATE_DRAGGING);
*/
        bottomSheet = findViewById(R.id.bottomSheetLayout);
        frameLayout = (FrameLayout) findViewById(R.id.main_content);
        song_name_toolbar = (TextView) findViewById(R.id.song_name_toolbar);
        artist_name_toolbar = (TextView) findViewById(R.id.artist_name_toolbar);
        Typeface artist_name_toolbar_tf = Typeface.createFromAsset(getAssets(), getString(R.string.light_fonts));
        artist_name_toolbar.setTypeface(artist_name_toolbar_tf);
        Typeface song_name_toolbar_tf = Typeface.createFromAsset(getAssets(), getString(R.string.light_fonts));
        song_name_toolbar.setTypeface(song_name_toolbar_tf);

        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_DRAGGING);
        open_bottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                bootomsheet_open = true;
            }
        });
        open_bottomSheet_controller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                newState = BottomSheetBehavior.STATE_DRAGGING;

            }

            @Override
            public void onSlide(@NonNull final View bottomSheet, float slideOffset) {
                if (slideOffset == 1) {
                    bootomsheet_open = true;
                    songname_player.setText(SongNameGlobal);
                    player_close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        }
                    });
                    getSupportActionBar().hide();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toolbar.setVisibility(View.GONE);
                        }
                    });
                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                }
                if (slideOffset > .7) {
                    getSupportActionBar().hide();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toolbar.setVisibility(View.GONE);
                        }
                    });
                }
                if (slideOffset < 1) {
                    getSupportActionBar().show();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toolbar.setVisibility(View.VISIBLE);
                        }
                    });

//                    SearchMenu.getItem(3).setVisible(true);
                    getSupportActionBar().setIcon(R.drawable.logo);
                    song_name_toolbar.setVisibility(View.GONE);
                    artist_name_toolbar.setVisibility(View.GONE);
                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNDEFINED);

                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setHomeButtonEnabled(true);
                    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
                            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNDEFINED);

                        }
                    });
                    toolbar.post(new Runnable() {
                        @Override
                        public void run() {
                            Drawable d = ResourcesCompat.getDrawable(getResources(), R.drawable.nav_drawer, null);
                            toolbar.setNavigationIcon(d);
                        }
                    });
                }

                if (slideOffset > .5) {
                    ((LinearLayout) findViewById(R.id.miniControl)).setVisibility(View.GONE);
                    getSupportActionBar().hide();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toolbar.setVisibility(View.GONE);
                        }
                    });
                } else {
                    getSupportActionBar().show();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toolbar.setVisibility(View.VISIBLE);
                        }
                    });
                    ((LinearLayout) findViewById(R.id.miniControl)).setVisibility(View.VISIBLE);
                    bootomsheet_open = false;
                }
            }
        });
/////////////////////////////////////////chrome cast button//////////////////
        Context castContext = new ContextThemeWrapper(MainActivity.this, android.support.v7.mediarouter.R.style.Theme_MediaRouter);
        Drawable drawable = null;
        TypedArray a = castContext.obtainStyledAttributes(null, android.support.v7.mediarouter.R.styleable.MediaRouteButton, android.support.v7.mediarouter.R.attr.mediaRouteButtonStyle, 0);
        drawable = a.getDrawable(android.support.v7.mediarouter.R.styleable.MediaRouteButton_externalRouteEnabledDrawable);
        a.recycle();
        DrawableCompat.setTint(drawable, getResources().getColor(R.color.unselected_color));

        CastButtonFactory.setUpMediaRouteButton(MainActivity.this, mediaRouteButton);
        mediaRouteButton.setRemoteIndicatorDrawable(drawable);
///////////////////////////////NavigationDrawer///////////////////////////////////////////
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        drawer.setScrimColor(getResources().getColor(R.color.blur));
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                String Profilename = prefs.getString("display_namePref", null);
                profilename.setText(Profilename);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset - 1);
                toolbar.setAlpha(1);
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();



        /*
        @setChecked used for get the first content
        @View for accessing the #app:layout  in Navigation Drawer
        */
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        //below line used for select homeFragment
        //Async task called for navitems
        if (Util.checkNetwork(MainActivity.this) == true) {
            AsynLoadMenuDetails asynLoadMenuDetails = new AsynLoadMenuDetails();
            asynLoadMenuDetails.executeOnExecutor(threadPoolExecutor);
        } else {
            Util.showToast(MainActivity.this, Util.getTextofLanguage(MainActivity.this, Util.SLOW_INTERNET_CONNECTION, Util.DEFAULT_SLOW_INTERNET_CONNECTION));

        }

        ClearQueueButton = (Button) findViewById(R.id.ClearQueueButton);
        musicProgress = (ProgressBar) findViewById(R.id.progressBar);
        curent_duration = (TextView) findViewById(R.id.curent_duration);
        total_duration = (TextView) findViewById(R.id.total_duration);
        song_p_name = (TextView) findViewById(R.id.song_p_name);
        Artist_p_name = (TextView) findViewById(R.id.song_p_Genre);
//        duration_layout = (RelativeLayout) findViewById(R.id.duration_layout);
//        duration_layout.setVisibility(View.GONE);

        Typeface curent_durationTypeface = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.light_fonts));
        curent_duration.setTypeface(curent_durationTypeface);
        Typeface total_durationTypeface = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.light_fonts));
        total_duration.setTypeface(total_durationTypeface);
        Typeface song_p_name_tf = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.regular_fonts));
        song_p_name.setTypeface(song_p_name_tf);
        Typeface Genere_p_name_tf = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.light_fonts));
        Artist_p_name.setTypeface(Genere_p_name_tf);

        onNavigationItemSelected(navigationView.getMenu().getItem(0));
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        /////close drawer ///
//        close_drawer = (ImageView) header.findViewById(R.id.close_drawer);
//        close_drawer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                drawer.closeDrawers();
//            }
//        });
        ///////profile name
        profilename = (TextView) header.findViewById(R.id.profilename);
        profile_iv_navigation = (ImageView) header.findViewById(R.id.profile_iv_navigation);
        SharedPreferences prefs;
        prefs = getSharedPreferences(Util.LOGINPREFERENCE, MODE_PRIVATE);
        String proname = prefs.getString("display_namePref", null);
        Log.v("PrefName", "" + proname);

        final String email = prefs.getString("emailPref", null);
        userid = prefs.getString("useridPref", null);
        emailid = prefs.getString("emailPref", null);
        profilename.setText(proname);
        ///edit profile functionality
        editprofile = (ImageView) header.findViewById(R.id.editprofile);
        login_tv = (TextView) header.findViewById(R.id.login_tv);
        slash = (TextView) header.findViewById(R.id.slash);
        signup_tv = (TextView) header.findViewById(R.id.signup_tv);
        Typeface login_tvTypeface = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.light_fonts));
        login_tv.setTypeface(login_tvTypeface);
        Typeface signup_tvTypeface = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.light_fonts));
        signup_tv.setTypeface(signup_tvTypeface);

        edit_profile_tv = (TextView) header.findViewById(R.id.edit_profile_tv);
        if (userid.equals("0101D")) {
            edit_profile_tv.setVisibility(View.GONE);
            editprofile.setVisibility(View.GONE);
            login_tv.setVisibility(View.VISIBLE);
            signup_tv.setVisibility(View.VISIBLE);
            slash.setVisibility(View.VISIBLE);
        } else {
            edit_profile_tv.setVisibility(View.VISIBLE);
            editprofile.setVisibility(View.VISIBLE);
        }

        login_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signinIntent = new Intent(MainActivity.this, Login.class);
                signinIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(signinIntent);
            }
        });
        signup_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signupIntent = new Intent(MainActivity.this, Register.class);
                signupIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(signupIntent);
            }
        });


        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             /*   ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) frameLayout.getLayoutParams();
                params.setMargins(0, 0, 0, 0);
                frameLayout.setLayoutParams(params);
*/
                minicontroller.setVisibility(View.GONE);
                fragment = new ProfileActivity();
                Bundle arguments = new Bundle();
                arguments.putString("EMAIL", email);
                arguments.putString("LOGID", userid);
                fragment.setArguments(arguments);
                if (fragment != null) {

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.main_content, fragment, "Profile");
                    fragmentTransaction.addToBackStack("Profile");
                    drawer.closeDrawers();
                    fragmentTransaction.commit();


                    //////close drawer ///


                }
            }
        });
        ClearQueueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                QUEUE_ARRAY.clear();
                QueuePref.edit().clear().apply();
                PlayerViewPagerAdapter testPagerAdapter = new PlayerViewPagerAdapter(getSupportFragmentManager());
                SquizeViewPager.setAdapter(testPagerAdapter);
                testPagerAdapter.notifyDataSetChanged();
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeButtonEnabled(true);
                toolbar.post(new Runnable() {
                    @Override
                    public void run() {
                        Drawable d = ResourcesCompat.getDrawable(getResources(), R.drawable.nav_drawer, null);
                        toolbar.setNavigationIcon(d);
                    }
                });
                testPagerAdapter.notifyDataSetChanged();
                mediaPlayer.pause();
                onBackPressed();
                minicontroller.setVisibility(View.GONE);
                Intent Pintent = new Intent("Constants.ACTION.QUEUE_CLEAR");
                LocalBroadcastManager.getInstance(MainActivity.this).sendBroadcast(Pintent);
            }
        });
        edit_profile_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) frameLayout.getLayoutParams();
                params.setMargins(0, 0, 0, 0);
                frameLayout.setLayoutParams(params);*/
                minicontroller.setVisibility(View.GONE);

                fragment = new ProfileActivity();
                Bundle arguments = new Bundle();
                arguments.putString("EMAIL", email);
                arguments.putString("LOGID", userid);
                fragment.setArguments(arguments);
                if (fragment != null) {

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.main_content, fragment, "Profile");
                    fragmentTransaction.addToBackStack("Profile");
                    drawer.closeDrawers();

                    fragmentTransaction.commit();

                }

            }
        });
        if (Util.checkNetwork(MainActivity.this) == true) {
            AsynGetGenreList asynGetGenreList = new AsynGetGenreList();
            asynGetGenreList.executeOnExecutor(threadPoolExecutor);
        } else {
            Util.showToast(MainActivity.this, Util.getTextofLanguage(MainActivity.this, Util.SLOW_INTERNET_CONNECTION, Util.DEFAULT_SLOW_INTERNET_CONNECTION));

        }


        //logout/////
        prefs = getSharedPreferences(LOGINPREFERENCE, MODE_PRIVATE);
        loginHistory = prefs.getString("login_history_idPref", null);
        logoutInput = new LogoutInput();
        logoutInput.setAuthToken(Util.authTokenStr.trim());
        logoutInput.setLogin_history_id(loginHistory);
        logoutAsynctask = new LogoutAsynctask(logoutInput, this, MainActivity.this);
        /////////////////////////////////////////////////////////////////////////////////
        //----------------------player functionality-----------------------------------//
        /*
        @ Song Status and player Details are registered here
         */
        LocalBroadcastManager.getInstance(this).registerReceiver(SONG_STATUS_NEXT, new IntentFilter("SONG_STATUS_NEXT"));
        LocalBroadcastManager.getInstance(this).registerReceiver(SONG_STATUS_PREVIOUS, new IntentFilter("SONG_STATUS_PREVIOUS"));
        LocalBroadcastManager.getInstance(this).registerReceiver(SongStatusReciver, new IntentFilter("SONG_STATUS"));
        LocalBroadcastManager.getInstance(this).registerReceiver(PLAYER_DETAILS, new IntentFilter("PLAYER_DETAILS"));
        LocalBroadcastManager.getInstance(this).registerReceiver(CONTENT_OUTPUT1, new IntentFilter("CONTENT_OUTPUT1"));
        LocalBroadcastManager.getInstance(this).registerReceiver(CONTENT_OUTPUT2, new IntentFilter("CONTENT_OUTPUT2"));
        LocalBroadcastManager.getInstance(this).registerReceiver(CLOSE_NOTIFiCATION, new IntentFilter("CLOSE_NOTI"));
        LocalBroadcastManager.getInstance(this).registerReceiver(ADD_TO_QUEUE, new IntentFilter("ADD_TO_QUEUE"));
        registerReceiver(OPTION_MENU, new IntentFilter("OPTION_MENU"));


        String imgurl_up = "https://sampledesign.muvi.com/mobileaudio/testimage1.png";
//        ImageView player_overlay_up = (ImageView) findViewById(R.id.player_overlay_up);

       /* Picasso.with(this)
                .load(imgurl_up)
                .error(R.drawable.no_image)
                .into(player_overlay_up);*/
/*//        String imgurl_bottom= "https://sampledesign.muvi.com/mobileaudio/testimage2.png";
        ImageView image_overlay = (ImageView) findViewById(R.id.image_overlay);

        Picasso.with(this)
                .load(imgurl_bottom)
                .error(R.drawable.no_image)
                .into(image_overlay);*/
        DB = MainActivity.this.openOrCreateDatabase(Util.DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor = DB.rawQuery("SELECT * FROM " + Util.USER_TABLE_NAME + " ", null);
        int count = cursor.getCount();
        if (count > 0) {

            if (cursor.moveToFirst()) {
                do {
                    Glide.with(getApplicationContext()).load(cursor.getString(1)).asBitmap().centerCrop().into(new BitmapImageViewTarget(player_image_main) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            player_image_main.setImageDrawable(circularBitmapDrawable);
                        }
                    });
                    /*Picasso.with(getApplicationContext())
                            .load(cursor.getString(0))
                            .into(null);*/
                    song_p_name.setText(cursor.getString(2));

                } while (cursor.moveToNext());
            }
        }


        Cursor cursor1 = DB.rawQuery("SELECT ALBUM_URL,PERMALINK,ALBUM_ART,ALBUM_NAME,ALBUM_SONG_NAME FROM " + Util.ADAPTOR_TABLE_NAME + " ", null);
        int count1 = cursor.getCount();
        if (count1 > 0) {
            ArrayList<ContentDetailsOutput> filelist = new ArrayList<ContentDetailsOutput>();

            if (cursor1.moveToFirst()) {
                do {
                    ContentDetailsOutput contentDetailsOutput = new ContentDetailsOutput();
                    contentDetailsOutput.setMovieUrl(cursor1.getString(0));
                    contentDetailsOutput.setPermalink(cursor1.getString(1));
                    contentDetailsOutput.setPoster(cursor1.getString(2));
                    contentDetailsOutput.setName(cursor1.getString(4));

                    filelist.add(contentDetailsOutput);

                } while (cursor.moveToNext());
                albumArt_player.setImageResource(R.drawable.ic_pause_black_24dp);
                player_play_ic.setImageResource(R.drawable.player_player_pause_ic);
                /*adapter = new PlayerDataAdaptor(MainActivity.this, filelist, MainActivity.this);
                playerRecycle.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
                playerRecycle.setAdapter(adapter);
                adapter.notifyDataSetChanged();*/
            }
        }


        albumArt_player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Pintent = new Intent("SERVICE_ACTION_NEXT");
                LocalBroadcastManager.getInstance(MainActivity.this).sendBroadcast(Pintent);
            }
        });

        player_play_ic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Pintent = new Intent("SERVICE_ACTION_NEXT");
                LocalBroadcastManager.getInstance(MainActivity.this).sendBroadcast(Pintent);
                StartTimer();
            }
        });

        player_next_icc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("Nihar_next", "button called");
                Next();

            }
        });
        player_prev_ic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previous();
            }
        });

        seekBar = (SeekBar) findViewById(R.id.Progress_music_sliderpanel);
        seekBar.setPadding(0, 0, 0, 0);
        seekbar_botomSht = (SeekBar) findViewById(R.id.miniController_seekbar);
        seekbar_botomSht.setPadding(0, 0, 0, 0);

        if (Util.checkNetwork(MainActivity.this) == true) {
            AsynLoadProfileDetails asynLoadProfileDetails = new AsynLoadProfileDetails();
            asynLoadProfileDetails.executeOnExecutor(threadPoolExecutor);
        } else {
            Util.showToast(MainActivity.this, Util.getTextofLanguage(MainActivity.this, Util.SLOW_INTERNET_CONNECTION, Util.DEFAULT_SLOW_INTERNET_CONNECTION));

        }
        sheetView = this.getLayoutInflater().inflate(R.layout.opt, null);
        option_songName = (TextView) sheetView.findViewById(R.id.option_songName);
        option_artist_name = (TextView) sheetView.findViewById(R.id.option_artist_name);
        remove_from_list = (TextView) sheetView.findViewById(R.id.remove_from_list);
        remove_Playlist_content = (TextView) sheetView.findViewById(R.id.remove_Playlist_content);
        add_to_Playlist = (TextView) sheetView.findViewById(R.id.add_to_Playlist);
        add_to_Queue = (TextView) sheetView.findViewById(R.id.add_to_Queue);
        rename = (TextView) sheetView.findViewById(R.id.rename);
        mBottomSheetDialog = new BottomSheetDialog(MainActivity.this);
        mBottomSheetDialog.setContentView(sheetView);
        mBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

            }
        });
        if (QueuePref.contains("key")) {
            String Song_Name, AlbumArt, SongUrl, ArtistUrl;

            Gson gson = new Gson();
            String jsonText = QueuePref.getString("key", null);
            Log.v("Nihar_json", jsonText);
            try {
                JSONArray jsonArray = new JSONArray(jsonText);
                Log.v("Nihar_json", "" + jsonArray.length());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Song_Name = jsonObject.getString("SongName");
                    AlbumArt = jsonObject.getString("AlbumArt");
                    ArtistUrl = jsonObject.getString("ArtistUrl");
                    SongUrl = jsonObject.getString("SongUrl");
                    QUEUE_ARRAY.add(new QueueModel(Song_Name, AlbumArt, SongUrl, ArtistUrl));
                    PlayerViewPagerAdapter testPagerAdapter = new PlayerViewPagerAdapter(getSupportFragmentManager());
                    SquizeViewPager.setAdapter(testPagerAdapter);
                    testPagerAdapter.notifyDataSetChanged();
            /*        queueadapter = new QueueAdaptor(this, QUEUE_ARRAY, MainActivity.this,"mainactivity");
                    playerRecycle.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                    playerRecycle.setAdapter(queueadapter);
                    adapter.notifyDataSetChanged();*/
                    minicontroller.setVisibility(View.VISIBLE);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        //*** chromecast************
        mCastStateListener = new CastStateListener() {
            @Override
            public void onCastStateChanged(int newState) {
                if (newState != CastState.NO_DEVICES_AVAILABLE) {

                    showIntroductoryOverlay();
                }
            }
        };

        mCastContext = CastContext.getSharedInstance(this);
        mCastContext.registerLifecycleCallbacksBeforeIceCreamSandwich(this, savedInstanceState);

        setupCastListener();
        mCastSession = mCastContext.getSessionManager().getCurrentCastSession();

        //////////////////

        SquizeViewPager = (ViewPager) findViewById(R.id.viewPager1);
        SquizeViewPager.setAdapter(new PlayerViewPagerAdapter(getSupportFragmentManager()));
        ////


    }


    private BroadcastReceiver OPTION_MENU = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            final String option_songNAme = intent.getStringExtra("option");
            String artist_opt = intent.getStringExtra("artist_opt");
            final String playlist_id = intent.getStringExtra("playlist_id");
            option_songName.setText(option_songNAme);
            option_artist_name.setText(artist_opt);
            //////////////////////////////
            final String AlbumArt = intent.getStringExtra("MusicAlbumArt");
            final String MusicUrl = intent.getStringExtra("MusicUrl");
            final String ArtistUrl = intent.getStringExtra("ArtistUrl");
            Log.v("nihar_exception", ArtistUrl + artist_opt);

            ///set visibility gone
            remove_from_list.setVisibility(View.GONE);
            rename.setVisibility(View.GONE);
            remove_Playlist_content.setVisibility(View.GONE);
            add_to_Queue.setVisibility(View.GONE);
            add_to_Playlist.setVisibility(View.GONE);
            //cheak condition
            if (intent.getStringExtra("sender").equals("MultipartContent")) {

                if ((Util.getTextofLanguage(MainActivity.this, Util.ISQUEUE, Util.DEFAULT_ISQUEUE)
                        .trim()).equals("1")) {
                    add_to_Queue.setVisibility(View.VISIBLE);
                } else {
                    add_to_Queue.setVisibility(View.GONE);
                }

                if ((Util.getTextofLanguage(MainActivity.this, Util.ISPLAYLIST, Util.DEFAULT_ISPLAYLIST)
                        .trim()).equals("1")) {
                    add_to_Playlist.setVisibility(View.VISIBLE);
                } else {
                    add_to_Playlist.setVisibility(View.GONE);
                }

            } else if (intent.getStringExtra("sender").equals("SinglepartContent")) {
                if ((Util.getTextofLanguage(MainActivity.this, Util.ISQUEUE, Util.DEFAULT_ISQUEUE)
                        .trim()).equals("1")) {
                    add_to_Queue.setVisibility(View.VISIBLE);
                } else {
                    add_to_Queue.setVisibility(View.GONE);
                }

                if ((Util.getTextofLanguage(MainActivity.this, Util.ISPLAYLIST, Util.DEFAULT_ISPLAYLIST)
                        .trim()).equals("1")) {
                    add_to_Playlist.setVisibility(View.VISIBLE);
                } else {
                    add_to_Playlist.setVisibility(View.GONE);
                }
            } else if (intent.getStringExtra("sender").equals("PlayList")) {
                remove_from_list.setVisibility(View.VISIBLE);
                rename.setVisibility(View.VISIBLE);
            } else if (intent.getStringExtra("sender").equals("PlayListData")) {
                remove_Playlist_content.setVisibility(View.VISIBLE);
            }
            //perform click listioner for all option

            add_to_Playlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentResponse = new Intent("OPTION_RESPONSE");
                    intentResponse.putExtra("response", "add_to_playlist");
                    intentResponse.putExtra("playlistid", intent.getStringExtra("playlist_id"));
                    intentResponse.putExtra("PlaylistName", option_songNAme);
                    intentResponse.putExtra("movie_stream_id", intent.getStringExtra("movie_stream_id"));
                    intentResponse.putExtra("isEpisode", intent.getStringExtra("isEpisode"));
                    intentResponse.putExtra("position", intent.getIntExtra("position", 0));
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intentResponse);
                    mBottomSheetDialog.dismiss();
                }
            });
            remove_Playlist_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentResponse = new Intent("OPTION_RESPONSE");
                    intentResponse.putExtra("response", "remove_Playlist_content");
                    intentResponse.putExtra("playlistid", intent.getStringExtra("playlist_id"));
                    intentResponse.putExtra("PlaylistName", option_songNAme);
                    intentResponse.putExtra("movie_stream_id_playlist", intent.getStringExtra("movie_stream_id_playlist"));
                    intentResponse.putExtra("position", intent.getIntExtra("position", 0));
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intentResponse);

                    mBottomSheetDialog.dismiss();
                }
            });
            rename.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentResponse = new Intent("OPTION_RESPONSE");
                    intentResponse.putExtra("response", "rename");
                    intentResponse.putExtra("playlistid", playlist_id);
                    intentResponse.putExtra("PlaylistName", option_songNAme);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intentResponse);
                    mBottomSheetDialog.dismiss();
                }
            });
            remove_from_list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentResponse = new Intent("OPTION_RESPONSE");
                    intentResponse.putExtra("response", "remove");
                    intentResponse.putExtra("playlistid", playlist_id);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intentResponse);
                    mBottomSheetDialog.dismiss();
                }
            });
            add_to_Queue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    QUEUE_ARRAY.add(new QueueModel(option_songNAme, AlbumArt, MusicUrl, ArtistUrl));
                    if (QUEUE_ARRAY.size() == 1) {
                        album_name = QUEUE_ARRAY.get(0).getSongName();
                        song_url = QUEUE_ARRAY.get(0).getSongUrl();
                        song_imageUrl = QUEUE_ARRAY.get(0).getAlbumArt();
                        song_name = QUEUE_ARRAY.get(0).getSongName();
                        ArtistName = ArtistUrl;
                        SongNameGlobal = option_songNAme;
                        Player_State(1);
                    }

                    PlayerViewPagerAdapter testPagerAdapter = new PlayerViewPagerAdapter(getSupportFragmentManager());
                    SquizeViewPager.setAdapter(testPagerAdapter);
                    testPagerAdapter.notifyDataSetChanged();
                    saveArray();
                    mBottomSheetDialog.dismiss();
                }
            });
            mBottomSheetDialog.show();
        }
    };
    private BroadcastReceiver CLOSE_NOTIFiCATION = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String responce = intent.getStringExtra("closeNotification");
            LocalBroadcastManager.getInstance(context).unregisterReceiver(SongStatusReciver);

            if (responce.equals("close")) {
                minicontroller.setVisibility(View.GONE);
            }

        }
    };

    private BroadcastReceiver SONG_STATUS_NEXT = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Next();
        }
    };
    private BroadcastReceiver SONG_STATUS_PREVIOUS = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            previous();
        }

    };
    private BroadcastReceiver ADD_TO_QUEUE = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            PlayerViewPagerAdapter testPagerAdapter = new PlayerViewPagerAdapter(getSupportFragmentManager());
            if (SquizeViewPager != null){
                SquizeViewPager.setAdapter(testPagerAdapter);
                testPagerAdapter.notifyDataSetChanged();
                saveArray();
            }


        }

    };

    private BroadcastReceiver PLAYER_DETAILS = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Song_name = intent.getStringExtra("SongName");
            Artist = intent.getStringExtra("Artist");
            Log.v("Nihar_artist", "onReceive Artist" + Artist);
            AlbumArtUrl = intent.getStringExtra("songImageUrl");
            song_url = intent.getStringExtra("song_url");
            Picasso.with(context)
                    .load(AlbumArtUrl)
                    .into(background_player);

            Glide.with(getApplicationContext()).load(AlbumArtUrl).asBitmap().centerCrop().into(new BitmapImageViewTarget(player_image_main) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    player_image_main.setImageDrawable(circularBitmapDrawable);
                }
            });

            ///recent update
          /*  Picasso.with(getApplicationContext())
                    .load(AlbumArtUrl)
                    .into(player_image_background);*/
            song_p_name.setText(Song_name);
            Artist_p_name.setText(Artist);
            Typeface Artist_p_name_tf = Typeface.createFromAsset(getAssets(), getString(R.string.light_fonts));
            Artist_p_name.setTypeface(Artist_p_name_tf);

            /*queueadapter = new QueueAdaptor(context, QUEUE_ARRAY, MainActivity.this);
            playerRecycle.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            playerRecycle.setAdapter(queueadapter);*/
//            queueadapter.notifyDataSetChanged();
            try {
                PlayerViewPagerAdapter testPagerAdapter = new PlayerViewPagerAdapter(getSupportFragmentManager());
                SquizeViewPager.setAdapter(testPagerAdapter);
                testPagerAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                Log.v("Nihar", e.toString());
            }

        }

    };
    private BroadcastReceiver CONTENT_OUTPUT1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            ArrayList<ContentDetailsOutput> filelist = (ArrayList<ContentDetailsOutput>) intent.getSerializableExtra("Content");
            ContentDetailsOutput contentDetailsOutput = new ContentDetailsOutput();
            contentDetailsOutput = filelist.get(0);
            String Song_name = contentDetailsOutput.getName();
            Log.v("Nihar555", "" + Song_name);
        /*    player_next_ic.setClickable(false);
            player_prev_ic.setClickable(false);*/
//            adapter = new PlayerDataAdaptor(context, filelist, MainActivity.this);
//            playerRecycle.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
//            playerRecycle.setAdapter(adapter);
            ///////////////////Queeue Update changes///////////
           /* queueadapter = new QueueAdaptor(context, QUEUE_ARRAY, MainActivity.this);
            playerRecycle.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            playerRecycle.setAdapter(queueadapter);
            queueadapter.notifyDataSetChanged();*/


        }

    };
    private BroadcastReceiver CONTENT_OUTPUT2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Util.multiholder.clear();
            player_prev_ic.setClickable(true);
            ArrayList<Episode_Details_output> filelist2 = (ArrayList<Episode_Details_output>) intent.getSerializableExtra("Content_multipart");
            adaptorPosition = intent.getIntExtra("position_item", 0);
            String Artist_multi = intent.getStringExtra("artist");
            Episode_Details_output episode_details_output = new Episode_Details_output();
            Song_name = episode_details_output.getName();
            albumArt_player.setImageResource(R.drawable.ic_pause_black_24dp);
            player_play_ic.setImageResource(R.drawable.player_player_pause_ic);
//            multipartAdaptor = new PlayerDataMultipartAdaptor(context, filelist2, MainActivity.this, adaptorPosition, Artist_multi);
//            playerRecycle.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
//            playerRecycle.setAdapter(multipartAdaptor);

            ///////////////////Queeue Update changes///////////
        /*    queueadapter = new QueueAdaptor(context, QUEUE_ARRAY, MainActivity.this);
            playerRecycle.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            playerRecycle.setAdapter(queueadapter);
            queueadapter.notifyDataSetChanged();*/
        }

    };
    private BroadcastReceiver SongStatusReciver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.v("nIHARnOTI", "CLOSED");
            // Extract data included in the Intent
            song_status = (intent.getStringExtra("songStatus")).trim();
            if (song_status.equals("play")) {
                albumArt_player.setImageResource(R.drawable.ic_pause_black_24dp);
                player_play_ic.setImageResource(R.drawable.player_player_pause_ic);
                minicontroller.setVisibility(View.VISIBLE);
                equalizer.setImageDrawable(getDrawableByState(context, 1));
                Log.v("nihar_play", "called");
            }
            if (song_status.equals("pause")) {
                albumArt_player.setImageResource(R.drawable.play_icon);
                player_play_ic.setImageResource(R.drawable.player_play_ic);
                minicontroller.setVisibility(View.VISIBLE);
                equalizer.setImageDrawable(getDrawableByState(context, 2));
            }
            if (song_status.equals("close")) {
                minicontroller.setVisibility(View.GONE);
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (song_status.equals("next")) {
                        Next();
                    }
                    if (song_status.contains("@@@@@")) {
                        final String data[] = song_status.split("@@@@@");


                        total_duration.setText(timeC(Long.parseLong(data[1])));
                        curent_duration.setText(timeC(Long.parseLong(data[0])));
                        seekBar.setMax(Integer.parseInt(data[1]));
                        seekBar.setProgress(Integer.parseInt(data[0]));
                        seekbar_botomSht.setMax(Integer.parseInt(data[1]));
                        seekbar_botomSht.setProgress(Integer.parseInt(data[0]));
                        musicProgress.setMax(Integer.parseInt(data[1]));
                        musicProgress.setProgress(Integer.parseInt(data[0]));

                        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                            @Override
                            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {
//                                duration_layout.setVisibility(View.VISIBLE);
                                StartTimer();
                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {
                                mediaPlayer.seekTo(seekBar.getProgress());
                            }
                        });
                    }
                }

            });
        }
    };


    ////////Feacture Desabled for now /////////
    //menu functionality and display *(as of now only shows @Search Activity).
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
/************chromecast***********/
        SharedPreferences languageSharedPref = getSharedPreferences(Util.LANGUAGE_SHARED_PRE, 0);
        String cast_key = languageSharedPref.getString("CHROMECAST", "0");
        Log.v("NiharChrome", "chromecast called" + cast_key);
        /////////////////////////////
        if ((Util.getTextofLanguage(MainActivity.this, Util.CHROMECAST, Util.DEFAULT_CHROMECAST)
                .trim()).equals("1")) {
            Log.v("NiharChrome", "chromecast called");
            mediaRouteMenuItem = CastButtonFactory.setUpMediaRouteButton(getApplicationContext(), menu,
                    R.id.media_route_menu_item);
            if (!isFirstTime()) {
                showIntroductoryOverlay();
            }
        }
        SearchMenu = menu;
        menu.getItem(0).setVisible(false);
        menu.getItem(1).setVisible(true);
        menu.getItem(2).setVisible(false);
        menu.getItem(3).setVisible(true);
        menu.getItem(4).setVisible(true);


        if ((Util.getTextofLanguage(MainActivity.this, Util.ISPLAYLIST, Util.DEFAULT_ISPLAYLIST)
                .trim()).equals("1")) {
            menu.getItem(4).setVisible(true);
        } else {
            menu.getItem(4).setVisible(false);
        }
        if ((Util.getTextofLanguage(MainActivity.this, Util.HAS_FAVORITE, Util.DEFAULT_HAS_FAVORITE)
                .trim()).equals("1")) {
            menu.getItem(5).setVisible(true);
        } else {
            menu.getItem(5).setVisible(false);
        }
        if ((Util.getTextofLanguage(MainActivity.this, Util.HASDOWNLOAD, Util.DEFAULT_HASDOWNLOAD)
                .trim()).equals("1")) {
            menu.getItem(7).setVisible(true);
        } else {
            menu.getItem(7).setVisible(false);
        }
        applyFontToMenuItem(menu.getItem(0));
        applyFontToMenuItem(menu.getItem(1));
        applyFontToMenuItem(menu.getItem(2));
        applyFontToMenuItem(menu.getItem(3));
        applyFontToMenuItem(menu.getItem(4));
        applyFontToMenuItem(menu.getItem(5));
        applyFontToMenuItem(menu.getItem(7));

        MenuItem menuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(menuItem);

        return super.onCreateOptionsMenu(menu);
//
    }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface externalFont = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.regular_fonts));
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", externalFont), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle.toString());
    }

    private boolean isFirstTime() {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        boolean ranBefore = preferences.getBoolean("RanBefore", false);
        if (!ranBefore) {
            // first time
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("RanBefore", true);
            editor.commit();
        }
        return !ranBefore;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.v("nihar_meniId", "" + item.getItemId());
        switch (item.getItemId()) {

            case R.id.nav_Search:

                fragment = new SearchActivity();
                if (fragment != null) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.main_content, fragment, "SearchFragment");
                    fragmentTransaction.addToBackStack("SearchFragment");
                    fragmentTransaction.commit();
                    minicontroller.setVisibility(View.GONE);

                    getSupportActionBar().setIcon(null);


                    return true;
                }
            case R.id.payment_history: {
                if (userid.equals("0101D")) {
                    Intent signinIntent = new Intent(MainActivity.this, Login.class);
                    signinIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(signinIntent);
                } else {
                    Intent intent = new Intent(MainActivity.this, PurchaseHistoryActivity.class);
                    startActivity(intent);
                }
                return false;

            }
            case R.id.PlayList: {
                if (userid.equals("0101D")) {
                    Intent signinIntent = new Intent(MainActivity.this, Login.class);
                    signinIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(signinIntent);
                } else {
                    Fragment fragmentPlaylist = new PlayListFragment();
                    if (fragment != null) {
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.main_content, fragmentPlaylist, "PlayListFragment");
                        fragmentTransaction.addToBackStack("PlayListFragment");
                        fragmentTransaction.commit();
                    }
                }
                return false;
            }
            case R.id.favourites: {

                if (userid.equals("0101D")) {
                    Intent signinIntent = new Intent(MainActivity.this, Login.class);
                    signinIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(signinIntent);
                } else {
                    Fragment Favoritefragment = new FavouriteFragment();
                    if (fragment != null) {
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.main_content, Favoritefragment, "FavoriteFragment");
                        fragmentTransaction.addToBackStack("FavoriteFragment");
                        fragmentTransaction.commit();
                    }
                }
                return false;
            }
            case R.id.myDownload: {

                if (userid.equals("0101D")) {
                    Intent signinIntent = new Intent(MainActivity.this, Login.class);
                    signinIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(signinIntent);
                } else {
                    Intent downintent = new Intent(MainActivity.this, MyDownloads.class);
                    downintent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(downintent);
                }
                return false;
            }
        }
        return false;
    }

    ///////////--------***------------///////////////

    /*
    @back button pressed
    @DRAWER open , close and #backstack for fragment
     */
    @Override
    public void onBackPressed() {

        if (bootomsheet_open) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            bootomsheet_open = false;
            return;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }
        if (getSupportFragmentManager().findFragmentByTag("MultiPartFragment") != null) {
            getSupportFragmentManager().popBackStack("MultiPartFragment",
                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }  else if (getSupportFragmentManager().findFragmentByTag("favorite") != null) {
            getSupportFragmentManager().popBackStack("favorite",
                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } else if (getSupportFragmentManager().findFragmentByTag("ViewallFragment") != null) {
            getSupportFragmentManager().popBackStack("ViewallFragment",
                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } else if (getSupportFragmentManager().findFragmentByTag("ViewAll") != null) {
            getSupportFragmentManager().popBackStack("ViewAll",
                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } else if (getSupportFragmentManager().findFragmentByTag("profile") != null) {
            getSupportFragmentManager().popBackStack("profile",
                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } else if (getSupportFragmentManager().findFragmentByTag("FavoriteFragment") != null) {
            getSupportFragmentManager().popBackStack("FavoriteFragment",
                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } else if (getSupportFragmentManager().findFragmentByTag("PlayListFragment") != null) {
            getSupportFragmentManager().popBackStack("PlayListFragment",
                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } else if (getSupportFragmentManager().findFragmentByTag("PlaylistDetails") != null) {
            getSupportFragmentManager().popBackStack("PlaylistDetails",
                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } else if (getSupportFragmentManager().findFragmentByTag("SearchFragment") != null) {
            getSupportFragmentManager().popBackStack("SearchFragment",
                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
            ((AppCompatActivity) this).getSupportActionBar().setIcon(R.drawable.logo);
            if (mediaPlayer.isPlaying()) {
                minicontroller.setVisibility(View.VISIBLE);
            }
            SearchMenu.getItem(0).setVisible(false);
            SearchMenu.getItem(1).setVisible(true);
            SearchMenu.getItem(7).setVisible(false);
            if ((Util.getTextofLanguage(MainActivity.this, Util.HAS_FAVORITE, Util.DEFAULT_HAS_FAVORITE)
                    .trim()).equals("1")) {
                SearchMenu.getItem(5).setVisible(true);
            } else {
                SearchMenu.getItem(5).setVisible(false);
            }
            if ((Util.getTextofLanguage(MainActivity.this, Util.HASDOWNLOAD, Util.DEFAULT_HASDOWNLOAD)
                    .trim()).equals("1")) {
                SearchMenu.getItem(7).setVisible(true);
            } else {
                SearchMenu.getItem(7).setVisible(false);
            }


            getSupportActionBar().setIcon(R.drawable.logo);


        } else {
            super.onBackPressed();
        }


    }



    //navigation bar onclick functionality
    /*
    @homeFragment for mmain fragment
    @ drawerList Fragment - all drawer item click fragment
    @Contack us and about us is Dynamic and contains all dynaic  functionaloty .
    @
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        navigationView.setCheckedItem(item.getItemId());
        int id = item.getItemId();
        if (id == 0) {
            getSupportActionBar().setIcon(R.drawable.logo);
            SearchMenu.getItem(0).setVisible(false);
            SearchMenu.getItem(1).setVisible(true);
            SearchMenu.getItem(2).setVisible(false);
            SearchMenu.getItem(3).setVisible(true);
            SearchMenu.getItem(4).setVisible(true);
            if ((Util.getTextofLanguage(MainActivity.this, Util.HASDOWNLOAD, Util.DEFAULT_HASDOWNLOAD)
                    .trim()).equals("1")) {
                SearchMenu.getItem(7).setVisible(true);
            } else {
                SearchMenu.getItem(7).setVisible(false);
            }

            if ((Util.getTextofLanguage(MainActivity.this, Util.HAS_FAVORITE, Util.DEFAULT_HAS_FAVORITE)
                    .trim()).equals("1")) {
                SearchMenu.getItem(5).setVisible(true);
            } else {
                SearchMenu.getItem(5).setVisible(false);
            }
         /*   if ((Util.getTextofLanguage(MainActivity.this, Util.CHROMECAST, Util.DEFAULT_CHROMECAST)
                    .trim()).equals("1")) {
                SearchMenu.getItem(7).setVisible(true);
            }else{
                SearchMenu.getItem(7).setVisible(false);
            }*/
            item = SearchMenu.findItem(R.id.filter);
            item.setVisible(false);


            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
            );
            fragment = new HomeFragment();
            if (fragment != null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_content, fragment);
                fragmentTransaction.commit();
                SearchMenu.getItem(0).setVisible(false);
                SearchMenu.getItem(1).setVisible(true);
                SearchMenu.getItem(2).setVisible(false);
                SearchMenu.getItem(3).setVisible(true);
                SearchMenu.getItem(4).setVisible(true);
                SearchMenu.getItem(7).setVisible(false);

                if ((Util.getTextofLanguage(MainActivity.this, Util.HAS_FAVORITE, Util.DEFAULT_HAS_FAVORITE)
                        .trim()).equals("1")) {
                    SearchMenu.getItem(5).setVisible(true);
                } else {
                    SearchMenu.getItem(5).setVisible(false);
                }

                item = SearchMenu.findItem(R.id.filter);
                item.setVisible(false);
                if (mediaPlayer.isPlaying()) {
                    minicontroller.setVisibility(View.VISIBLE);
                }

            }
        } else {
            getSupportActionBar().setIcon(R.drawable.logo);
            getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
            );
            try {


                for (int i = 1; i < display_names.size() + 1; i++) {
                    if (i == id) {
                        SearchMenu.getItem(1).setVisible(false);
                        SearchMenu.getItem(4).setVisible(false);
                        SearchMenu.getItem(3).setVisible(false);
                        SearchMenu.getItem(0).setVisible(true);
                        SearchMenu.getItem(7).setVisible(false);
                        SearchMenu.getItem(5).setVisible(false);

                        fragment = new VideosListFragment();
                        Bundle arguments = new Bundle();
                        arguments.putString("item", permalinks.get(id - 1));
                        arguments.putString("title", display_names.get(id - 1));
                        fragment.setArguments(arguments);
                        if (fragment != null) {

                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.main_content, fragment, "VideosListFragment");
                            fragmentTransaction.disallowAddToBackStack();
                            fragmentTransaction.commit();
                            if (mediaPlayer.isPlaying()) {
                                minicontroller.setVisibility(View.VISIBLE);
                            } else {
                                minicontroller.setVisibility(View.INVISIBLE);

                            }


                        }

                    }
                }
            } catch (Exception e) {
            }
        }


        if (id == display_names.size() + 1) {
            String fpermalinkn = fpermalinks.get(id - display_names.size() - 1);
            Intent intent = new Intent(MainActivity.this, AboutUsActivity.class);
            intent.putExtra("fpermalink", fpermalinkn);
            startActivity(intent);
        }


        if (id == display_names.size() + 2) {

            String fpermalink = fpermalinks.get(id - display_names.size() - 1);
            Intent intent = new Intent(MainActivity.this, ContactUsActivity.class);
            intent.putExtra("fpermalink", fpermalink);
            startActivity(intent);


        }


        if (id == display_names.size() + fdisplay_names.size() + 2) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
            builder1.setMessage("Do you want to Logout?");
            builder1.setCancelable(false);

            builder1.setPositiveButton(
                    "Confirm",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // dialog.cancel();
                            if (Util.checkNetwork(MainActivity.this) == true) {

                                LogoutAsyncTask logoutAsyncTask = new LogoutAsyncTask();
                                logoutAsyncTask.execute();
                            } else {
                                Util.showToast(MainActivity.this, Util.getTextofLanguage(MainActivity.this, Util.SLOW_INTERNET_CONNECTION, Util.DEFAULT_SLOW_INTERNET_CONNECTION));

                            }


                        }
                    });

            builder1.setNegativeButton(
                    "Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;


    }

    @Override
    public void onLogoutPreExecuteStarted() {
        pDialog.show();
    }

    @Override
    public void onLogoutPostExecuteCompleted(int code, String status, String message) {
        pDialog.hide();
        if (code == 200) {

            Intent j = new Intent(MainActivity.this, MusicService.class);
            j.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
            startService(j);

            String DEL_QRY = "DELETE FROM " + Util.USER_TABLE_NAME + "";
            DB.execSQL(DEL_QRY);
           /* ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) frameLayout.getLayoutParams();
            params.setMargins(0,0,0,0);
            FrameLayout.LayoutParams layoutParams1 = new FrameLayout.LayoutParams(params);
            frameLayout.setLayoutParams(layoutParams1);*/

            LocalBroadcastManager.getInstance(this).unregisterReceiver(SongStatusReciver);
            LocalBroadcastManager.getInstance(this).unregisterReceiver(PLAYER_DETAILS);
            LocalBroadcastManager.getInstance(this).unregisterReceiver(CONTENT_OUTPUT1);
            LocalBroadcastManager.getInstance(this).unregisterReceiver(CONTENT_OUTPUT2);
            LocalBroadcastManager.getInstance(this).unregisterReceiver(SONG_STATUS_NEXT);
            LocalBroadcastManager.getInstance(this).unregisterReceiver(SONG_STATUS_PREVIOUS);
            LocalBroadcastManager.getInstance(this).unregisterReceiver(CLOSE_NOTIFiCATION);
            LocalBroadcastManager.getInstance(this).unregisterReceiver(ADD_TO_QUEUE);


            minicontroller.setVisibility(View.GONE);
            prefs = getSharedPreferences(LOGINPREFERENCE, MODE_PRIVATE);
            prefs.edit().clear().commit();
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), SplashScreenActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);

            finish();
        } else {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    public void PlaySongs(ContentDetailsOutput itemsList, boolean itemClicked) {
        if (itemClicked) {
            ValidateUserInput validateUserInput = new ValidateUserInput();
            validateUserInput.setAuthToken(Util.authTokenStr);
            validateUserInput.setUserId(userid);
            Log.v("niharId", "" + itemsList.getMuviUniqId());
            validateUserInput.setMuviUniqueId(itemsList.getMuviUniqId());
            validateUserInput.setSeasonId("0");
            validateUserInput.setEpisodeStreamUniqueId("0");
            if (Util.checkNetwork(MainActivity.this) == true) {
                GetValidateUserAsynTask getValidateUserAsynTask = new GetValidateUserAsynTask(validateUserInput, this, this);
                getValidateUserAsynTask.execute();
            } else {
                Util.showToast(MainActivity.this, Util.getTextofLanguage(MainActivity.this, Util.SLOW_INTERNET_CONNECTION, Util.DEFAULT_SLOW_INTERNET_CONNECTION));

            }
            contentDetailsOutput = itemsList;
            album_name = itemsList.getName();
            song_url = itemsList.getMovieUrl();
            song_imageUrl = itemsList.getPoster();
            song_name = itemsList.getName();
        } else {

        }
        Log.v("nihar_nihar", "" + itemsList.getName());

    }

    public void PlayQueue(QueueModel itemsList, boolean itemClicked) {
        if (itemClicked) {
            album_name = itemsList.getSongName();
            song_url = itemsList.getSongUrl();
            song_imageUrl = itemsList.getAlbumArt();
            song_name = itemsList.getSongName();
            Player_State(1);
        } else {

        }


    }

    public void PlayMultiSongs(Episode_Details_output itemsList, boolean itemClicked, int currentAdapterPosition) {
        if (itemClicked) {
            ValidateUserInput validateUserInput = new ValidateUserInput();
            validateUserInput.setAuthToken(Util.authTokenStr);
            validateUserInput.setUserId(userid);
            validateUserInput.setMuviUniqueId(itemsList.getMuvi_uniq_id());
            validateUserInput.setSeasonId("0");
            validateUserInput.setEpisodeStreamUniqueId("0");
            if (Util.checkNetwork(MainActivity.this) == true) {
                GetValidateUserAsynTask getValidateUserAsynTask = new GetValidateUserAsynTask(validateUserInput, this, this);
                getValidateUserAsynTask.execute();
            } else {
                Util.showToast(MainActivity.this, Util.getTextofLanguage(MainActivity.this, Util.SLOW_INTERNET_CONNECTION, Util.DEFAULT_SLOW_INTERNET_CONNECTION));

            }
            this.currentAdapterPosition = currentAdapterPosition;
            Intent position = new Intent("SONG_POSITION");
            position.putExtra("songStatusNumber", currentAdapterPosition);
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(position);

            album_name = itemsList.getName();
            song_url = itemsList.getVideo_url();
            song_imageUrl = itemsList.getPoster_url();
            song_name = itemsList.getEpisode_title();
        } else {

        }


    }

    @Override
    public void onGetValidateUserPreExecuteStarted() {

    }

    @Override
    public void onGetValidateUserPostExecuteCompleted(ValidateUserOutput validateUserOutput, int status, String message) {
        if (pDialog.isShowing()) {
            pDialog.hide();
        }
        if (status == 429) {

            Player_State(1);
        } else if (status == 430) {
            Player_State(1);
        } else {
            Player_State(1);
        }


    }

    public void Player_State(int funId) {


        Intent playerData = new Intent("PLAYER_DETAIL");
        playerData.putExtra("songName", album_name);
        Log.v("nihar3", "" + album_name);
        LocalBroadcastManager.getInstance(this).sendBroadcast(playerData);


   /*     String DEL_QRY = "DELETE FROM " + Util.USER_TABLE_NAME + "";
        DB.execSQL(DEL_QRY);

        String INS_QRY = "INSERT INTO " + Util.USER_TABLE_NAME + "(ALBUM_ART_PATH,ALBUM_SONG_NAME) VALUES ( '" + song_imageUrl + "','" + song_name + "')";
        DB.execSQL(INS_QRY);*/

        Intent j = new Intent(MainActivity.this, MusicService.class);
        j.putExtra("ALBUM", song_url);
        j.putExtra("ALBUM_ART", song_imageUrl);
        j.putExtra("ALBUM_NAME", name);
        j.putExtra("ALBUM_SONG_NAME", song_name);
        j.putExtra("STATE", funId);
        j.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
        startService(j);
        Log.v("NiharTranspoter", "called   ");

    }

    @Override
    public Void CommunicateName(int j) {
        Log.v("Nihar_fragment", "" + j);
        return null;
    }

    @Override
    public void Transporter(String SongUrl, String ImageUrl, String SongName, Context context, String artist) {
        this.song_url = SongUrl;
        this.song_imageUrl = ImageUrl;
        this.song_name = SongName;
        this.Artist = artist;
        Intent j = new Intent(context, MusicService.class);
        j.putExtra("ALBUM", song_url);
        j.putExtra("ALBUM_ART", song_imageUrl);
        j.putExtra("ALBUM_SONG_NAME", song_name);
        j.putExtra("Artist", Artist);
        j.putExtra("STATE", 1);
        j.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
        context.startService(j);
    }

    @Override
    public void BottomOptionMenu(Context context, String SongName, String ArtistName, int Position) {
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View sheetView = inflater.inflate(R.layout.opt, null);
        option_songName = (TextView) sheetView.findViewById(R.id.option_songName);
        option_artist_name = (TextView) sheetView.findViewById(R.id.option_artist_name);
        remove_from_list = (TextView) sheetView.findViewById(R.id.remove_from_list);
        remove_Playlist_content = (TextView) sheetView.findViewById(R.id.remove_Playlist_content);
        add_to_Playlist = (TextView) sheetView.findViewById(R.id.add_to_Playlist);
        add_to_Queue = (TextView) sheetView.findViewById(R.id.add_to_Queue);
        Remove_from_Queue = (TextView) sheetView.findViewById(R.id.Remove_from_Queue);

        Remove_from_Queue.setVisibility(View.VISIBLE);
        Remove_from_Queue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View sheetView = inflater.inflate(R.layout.activity_main, null);
                final ViewPager SquizeViewPagers = (ViewPager) sheetView.findViewById(R.id.viewPager1);
                ;
                if (position == 0) {
                    QUEUE_ARRAY.clear();
//                    QueuePref.edit().clear().apply();
//                    minicontroller.setVisibility(View.GONE);
                } else {
                    QUEUE_ARRAY.remove(position);
                    PlayerViewPagerAdapter testPagerAdapter = new PlayerViewPagerAdapter(getSupportFragmentManager());
                    SquizeViewPagers.setAdapter(testPagerAdapter);
                    testPagerAdapter.notifyDataSetChanged();

                }

            }
        });
        option_songName.setText(SongName);
        BottomSheetDialog BottomSheetDialog = new BottomSheetDialog(context);
        BottomSheetDialog.setContentView(sheetView);
        BottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {

            }
        });
        BottomSheetDialog.show();
    }


    //line draw below every item in RecyclerView
    private class SimpleDividerItemDecoration extends RecyclerView.ItemDecoration {
        private Drawable mDivider;

        public SimpleDividerItemDecoration(Context context) {
            mDivider = context.getResources().getDrawable(R.drawable.line_divider);
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();

            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int top = child.getBottom();
                int bottom = top + mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }

    }

    //its used convertng milisec to mm:ss time format
    public static String timeC(long dur) {
        Log.v("Nihar", " duration=" + dur);
        String strDate = String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(dur) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(dur)),
                TimeUnit.MILLISECONDS.toSeconds(dur) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(dur)));
        Log.v("Nihar", "audio duration=" + strDate);
        return strDate;
    }

    //this timer is used for seekbar with duration functionality(*for now its only 3 sec if u want to change do 3000=(ur time)).
    public void StartTimer() {
        if (isRunning) {
            durationTimer.cancel();
            isRunning = false;
        }


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                duration_layout.setVisibility(View.VISIBLE);

            }
        });

//        Toast.makeText(getApplicationContext(), "timer start", Toast.LENGTH_SHORT).show();
        durationTimer = new Timer();
        durationTimer.schedule(new TimerTask() {
            @Override
            public void run() {

                durationTimer.cancel();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        duration_layout.setVisibility(View.GONE);
                    }
                });
                isRunning = false;

            }
        }, 3000, 3000);
        isRunning = true;
    }

    //Async task for menuLoader
    private class AsynLoadMenuDetails extends AsyncTask<Void, Integer, Void> {

        String responseStr;
        int status;
        String msg, display_name, permalink;
        ProgressBarHandler progressHandler;

        @Override
        protected Void doInBackground(Void... params) {

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("https://www.muvi.com/rest/getMenuList");
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr);

                HttpResponse response = httpclient.execute(httppost);
                responseStr = EntityUtils.toString(response.getEntity());

                Log.v("pratik", "ressssponn===" + responseStr);

                JSONObject myJson = null;
                JSONArray jsonArray = null;
                JSONArray jsonArrayf = null;
                if (responseStr != null) {
                    myJson = new JSONObject(responseStr);
                    status = Integer.parseInt(myJson.optString("code"));
                    msg = myJson.optString("msg");

                    Log.v("pratik", "*****" + msg + status);


                }

                if (status == 200) {

                    jsonArray = myJson.getJSONArray("menu");
                    for (int i = 0; i < jsonArray.length(); i++) {

                        if ((jsonArray.getJSONObject(i).has("display_name")) && jsonArray.getJSONObject(i).getString("display_name").trim() != null && !jsonArray.getJSONObject(i).getString("display_name").trim().isEmpty() && !jsonArray.getJSONObject(i).getString("display_name").trim().equals("null") && !jsonArray.getJSONObject(i).getString("display_name").trim().matches("")) {
                            display_name = jsonArray.getJSONObject(i).optString("display_name");
                            Log.v("pratik", "dispaly name===" + display_name);
                            display_names.add(display_name);

                        }
                        if ((jsonArray.getJSONObject(i).has("permalink")) && jsonArray.getJSONObject(i).getString("permalink").trim() != null && !jsonArray.getJSONObject(i).getString("permalink").trim().isEmpty() && !jsonArray.getJSONObject(i).getString("permalink").trim().equals("null") && !jsonArray.getJSONObject(i).getString("permalink").trim().matches("")) {
                            permalink = jsonArray.getJSONObject(i).optString("permalink");
                            Log.v("pratik", "permalink name===" + permalink);
                            permalinks.add(permalink);

                        }

                    }
                    jsonArrayf = myJson.getJSONArray("footer_menu");
                    for (int i = 0; i < jsonArrayf.length(); i++) {

                        if ((jsonArrayf.getJSONObject(i).has("display_name")) && jsonArrayf.getJSONObject(i).getString("display_name").trim() != null && !jsonArrayf.getJSONObject(i).getString("display_name").trim().isEmpty() && !jsonArrayf.getJSONObject(i).getString("display_name").trim().equals("null") && !jsonArrayf.getJSONObject(i).getString("display_name").trim().matches("")) {
                            display_name = jsonArrayf.getJSONObject(i).optString("display_name");
                            Log.v("pratik", "dispaly name===" + display_name);
                            fdisplay_names.add(display_name);

                        }
                        if ((jsonArrayf.getJSONObject(i).has("permalink")) && jsonArrayf.getJSONObject(i).getString("permalink").trim() != null && !jsonArrayf.getJSONObject(i).getString("permalink").trim().isEmpty() && !jsonArrayf.getJSONObject(i).getString("permalink").trim().equals("null") && !jsonArrayf.getJSONObject(i).getString("permalink").trim().matches("")) {
                            permalink = jsonArrayf.getJSONObject(i).optString("permalink");
                            Log.v("pratik", "permalink name===" + permalink);
                            fpermalinks.add(permalink);

                        }

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();


            }
            return null;
        }


        protected void onPostExecute(Void result) {
            navigationView.getMenu().clear();
            Menu mn = navigationView.getMenu();
            // Menu menu = navigationView.getMenu();
            int i;
            mn.add(R.id.dynamic_nav_items, 0, 0, "Home");
            for (i = 0; i < display_names.size(); i++) {
                mn.add(R.id.dynamic_nav_items, i + 1, 0, display_names.get(i));
                MenuItem mi = mn.getItem(i);
                Log.v("pratik", "***==" + display_names.get(i));
            }

            for (i = 0; i < fdisplay_names.size(); i++) {
                mn.add(R.id.static_nav_items, display_names.size() + i + 1, 0, fdisplay_names.get(i));
                MenuItem mi = mn.getItem(i);
            }
            if (!userid.equals("0101D")) {
                mn.add(R.id.static_nav_items, display_names.size() + fdisplay_names.size() + 2, 0, "Logout");
            }
            onNavigationItemSelected(navigationView.getMenu().getItem(0));
            navigationView.setNavigationItemSelectedListener(MainActivity.this);
            View header = navigationView.getHeaderView(0);
            //////close drawer ///
//            close_drawer = (ImageView) header.findViewById(R.id.close_drawer);
//            close_drawer.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    drawer.closeDrawers();
//                }
//
//            });
            if (progressHandler.isShowing()) {
                progressHandler.hide();
                progressHandler = null;
            }
            /*mn.add("email");

            mn.add("email");

            mn.add("email");*/

           /* navigationView(mn);
            Log.v("pratik","size=="+display_names.size());
            for (int i = 0; i <= display_names.size(); i++) {
                menu.add(display_names.get(i));
                Log.v("pratik","***=="+display_names.get(i));
            }*/
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressHandler == null) {
                progressHandler = new ProgressBarHandler(MainActivity.this);

            } else if (progressHandler != null && !progressHandler.isShowing()) {

            }
            progressHandler.show();

        }
    }

    private class LogoutAsyncTask extends AsyncTask<String, Void, Void> {

        String contName;
        JSONObject myJson = null;
        String status;
        ProgressBarHandler pDialog;
        String contMessage;
        String responseStr;
        String sucessMsg;
        private int code;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressBarHandler(MainActivity.this);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(String... strings) {

            String urlRouteList = Util.rootUrl().trim() + Util.AddtoFavlist.trim();
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(APIUrlConstant.getLogoutUrl());
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");

                httppost.addHeader("authToken", Util.authTokenStr);
                httppost.addHeader("login_history_id", loginHistory);


                // Execute HTTP Post Request
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());

                } catch (org.apache.http.conn.ConnectTimeoutException e) {
                    code = 0;

                    status = "";

                } catch (IOException e) {
                    code = 0;


                }
                JSONObject myJson = null;
                if (responseStr != null) {
                    myJson = new JSONObject(responseStr);
                    code = Integer.parseInt(myJson.optString("code"));
                    sucessMsg = myJson.optString("msg");
                    status = myJson.optString("status");
                }


            } catch (Exception e) {
                code = 0;

            }
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {

            if (pDialog.isShowing() && pDialog != null) {
                pDialog.hide();
            }
            pDialog.hide();


            Intent j = new Intent(MainActivity.this, MusicService.class);
            j.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
            startService(j);

            String DEL_QRY = "DELETE FROM " + Util.USER_TABLE_NAME + "";
            DB.execSQL(DEL_QRY);
           /* ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) frameLayout.getLayoutParams();
            params.setMargins(0,0,0,0);
            FrameLayout.LayoutParams layoutParams1 = new FrameLayout.LayoutParams(params);
            frameLayout.setLayoutParams(layoutParams1);*/

            LocalBroadcastManager.getInstance(MainActivity.this).unregisterReceiver(SongStatusReciver);
            LocalBroadcastManager.getInstance(MainActivity.this).unregisterReceiver(PLAYER_DETAILS);
            LocalBroadcastManager.getInstance(MainActivity.this).unregisterReceiver(CONTENT_OUTPUT1);
            LocalBroadcastManager.getInstance(MainActivity.this).unregisterReceiver(CONTENT_OUTPUT2);
            LocalBroadcastManager.getInstance(MainActivity.this).unregisterReceiver(SONG_STATUS_NEXT);
            LocalBroadcastManager.getInstance(MainActivity.this).unregisterReceiver(SONG_STATUS_PREVIOUS);
            LocalBroadcastManager.getInstance(MainActivity.this).unregisterReceiver(CLOSE_NOTIFiCATION);
            LocalBroadcastManager.getInstance(MainActivity.this).unregisterReceiver(ADD_TO_QUEUE);


            minicontroller.setVisibility(View.GONE);
            prefs = getSharedPreferences(LOGINPREFERENCE, MODE_PRIVATE);
            prefs.edit().clear().commit();
            Toast.makeText(MainActivity.this, sucessMsg, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), SplashScreenActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);

            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportActionBar().setIcon(R.drawable.logo);
        toolbar.post(new Runnable() {
            @Override
            public void run() {
                artist_name_toolbar.setText(ArtistName);
                songname_player.setText(SongNameGlobal);
                Log.v("NIhar_toolbar", "" + SongNameGlobal + ArtistName);
            }
        });
        if (mediaPlayer.isPlaying()) {
           /* ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) frameLayout.getLayoutParams();
            params.setMargins(0,0,0,50);
            FrameLayout.LayoutParams layoutParams1 = new FrameLayout.LayoutParams(params);
            frameLayout.setLayoutParams(layoutParams1);*/

            Artist_p_name.setText(ArtistName);
            minicontroller.setVisibility(View.VISIBLE);
        }

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        toolbar.post(new Runnable() {
            @Override
            public void run() {
                Drawable d = ResourcesCompat.getDrawable(getResources(), R.drawable.nav_drawer, null);
                toolbar.setNavigationIcon(d);
            }
        });
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                prefs = getSharedPreferences(Util.LOGINPREFERENCE, MODE_PRIVATE);
                String Profilename = prefs.getString("display_namePref", null);
                profilename.setText(Profilename);
            }
        });

        registerReceiver(OPTION_MENU, new IntentFilter("OPTION_MENU"));
    }

    //////////////////////////////////////////


    private class AsynGetGenreList extends AsyncTask<Void, Void, Void> {
        String responseStr;
        int status;
        ArrayList<String> genreArrayList = new ArrayList<String>();
        ArrayList<String> genreValueArrayList = new ArrayList<String>();

        @Override
        protected Void doInBackground(Void... params) {

            String urlRouteList = Util.rootUrl().trim() + Util.getGenreListUrl.trim();

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httppost = new HttpGet(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());

                // Execute HTTP Post Request
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());


                } catch (org.apache.http.conn.ConnectTimeoutException e) {
                    runOnUiThread(new Runnable() {
                        public void run() {


                        }
                    });

                } catch (UnsupportedEncodingException e) {

                    runOnUiThread(new Runnable() {
                        public void run() {


                        }
                    });

                } catch (IOException e) {
                    runOnUiThread(new Runnable() {
                        public void run() {


                        }
                    });

                }
                JSONObject myJson = null;
                if (responseStr != null) {
                    myJson = new JSONObject(responseStr);
                    status = Integer.parseInt(myJson.optString("code"));
                }

                if (status > 0) {
                    if (status == 200) {

                        JSONArray jsonMainNode = myJson.getJSONArray("genre_list");

                        int lengthJsonArr = jsonMainNode.length();
                        if (lengthJsonArr > 0) {
                            genreArrayList.add(0, Util.getTextofLanguage(MainActivity.this, Util.FILTER_BY, Util.DEFAULT_FILTER_BY));
                            Log.v("SUBHA", "filter by = " + Util.getTextofLanguage(MainActivity.this, Util.FILTER_BY, Util.DEFAULT_FILTER_BY));

                            genreValueArrayList.add(0, "");

                        }
                        for (int i = 0; i < lengthJsonArr; i++) {
                            genreArrayList.add(jsonMainNode.get(i).toString());
                            genreValueArrayList.add(jsonMainNode.get(i).toString());


                        }

                        if (genreArrayList.size() > 1) {

                            genreArrayList.add(genreArrayList.size(), Util.getTextofLanguage(MainActivity.this, Util.SORT_BY, Util.DEFAULT_SORT_BY));
                            genreValueArrayList.add(genreValueArrayList.size(), "");


                            genreArrayList.add(genreArrayList.size(), Util.getTextofLanguage(MainActivity.this, Util.SORT_LAST_UPLOADED, Util.DEFAULT_SORT_LAST_UPLOADED));
                            genreValueArrayList.add(genreValueArrayList.size(), "lastupload");

                            genreArrayList.add(genreArrayList.size(), Util.getTextofLanguage(MainActivity.this, Util.SORT_RELEASE_DATE, Util.DEFAULT_SORT_RELEASE_DATE));
                            genreValueArrayList.add(genreValueArrayList.size(), "releasedate");

                            genreArrayList.add(genreArrayList.size(), Util.getTextofLanguage(MainActivity.this, Util.SORT_ALPHA_A_Z, Util.DEFAULT_SORT_ALPHA_A_Z));
                            genreValueArrayList.add(genreValueArrayList.size(), "sortasc");

                            genreArrayList.add(genreArrayList.size(), Util.getTextofLanguage(MainActivity.this, Util.SORT_ALPHA_Z_A, Util.DEFAULT_SORT_ALPHA_Z_A));
                            genreValueArrayList.add(genreValueArrayList.size(), "sortdesc");


                        }

                    } else {
                        responseStr = "0";

                    }
                }

            } catch (final Exception e) {
                runOnUiThread(new Runnable() {
                    public void run() {
                    }
                });


            }

            return null;
        }


        protected void onPostExecute(Void result) {

            if (responseStr == null) {

            } else {
                if (status > 0 && status == 200) {
                    genreArrToSend = new String[genreArrayList.size()];
                    genreArrToSend = genreArrayList.toArray(genreArrToSend);


                    genreValueArrayToSend = new String[genreValueArrayList.size()];
                    genreValueArrayToSend = genreValueArrayList.toArray(genreValueArrayToSend);


                } else {
                   /* genreArrToSend = new String[0];
                    genreArrToSend = genreArrayList.toArray(genreArrToSend);


                    genreValueArrayToSend = new String[0];
                    genreValueArrayToSend = genreValueArrayList.toArray(genreValueArrayToSend);*/


                    genreArrayList.add(genreArrayList.size(), Util.getTextofLanguage(MainActivity.this, Util.SORT_BY, Util.DEFAULT_SORT_BY));
                    genreValueArrayList.add(genreValueArrayList.size(), "");


                    genreArrayList.add(genreArrayList.size(), Util.getTextofLanguage(MainActivity.this, Util.SORT_LAST_UPLOADED, Util.DEFAULT_SORT_LAST_UPLOADED));
                    genreValueArrayList.add(genreValueArrayList.size(), "lastupload");

                    genreArrayList.add(genreArrayList.size(), Util.getTextofLanguage(MainActivity.this, Util.SORT_RELEASE_DATE, Util.DEFAULT_SORT_RELEASE_DATE));
                    genreValueArrayList.add(genreValueArrayList.size(), "releasedate");

                    genreArrayList.add(genreArrayList.size(), Util.getTextofLanguage(MainActivity.this, Util.SORT_ALPHA_A_Z, Util.DEFAULT_SORT_ALPHA_A_Z));
                    genreValueArrayList.add(genreValueArrayList.size(), "sortasc");

                    genreArrayList.add(genreArrayList.size(), Util.getTextofLanguage(MainActivity.this, Util.SORT_ALPHA_Z_A, Util.DEFAULT_SORT_ALPHA_Z_A));
                    genreValueArrayList.add(genreValueArrayList.size(), "sortdesc");

                    genreArrToSend = new String[genreArrayList.size()];
                    genreArrToSend = genreArrayList.toArray(genreArrToSend);


                    genreValueArrayToSend = new String[genreValueArrayList.size()];
                    genreValueArrayToSend = genreValueArrayList.toArray(genreValueArrayToSend);


                }

            }
            try {

                SharedPreferences.Editor isLoginPrefEditor = isLoginPref.edit();
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < genreArrToSend.length; i++) {
                    sb.append(genreArrToSend[i]).append(",");
                }
                isLoginPrefEditor.putString(Util.GENRE_ARRAY_PREF_KEY, sb.toString());
                StringBuilder sb1 = new StringBuilder();
                for (int i = 0; i < genreValueArrayToSend.length; i++) {
                    sb1.append(genreValueArrayToSend[i]).append(",");
                }
                isLoginPrefEditor.putString(Util.GENRE_VALUES_ARRAY_PREF_KEY, sb1.toString());
                isLoginPrefEditor.commit();
            } catch (Exception e) {
            }


        }

        protected void onPreExecute() {

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
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("user_id", userid);
                httppost.addHeader("email", emailid);

                // Execute HTTP Post Request
                try {

                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());


                } catch (org.apache.http.conn.ConnectTimeoutException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            responseStr = "0";

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
                            profileImage = Util.getTextofLanguage(MainActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA);

                        }


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

            } catch (IllegalArgumentException ex) {
                responseStr = "0";
            }
            if (responseStr == null) {
                responseStr = "0";
            }

            if ((responseStr.trim().equalsIgnoreCase("0"))) {


            } else {


                if (profileImage.matches(Util.NO_DATA)) {

                    profile_iv_navigation.setImageResource(R.drawable.profile_default_icon);
                } else {
                    Picasso.with(MainActivity.this)
                            .load(profileImage)
                            .into(profile_iv_navigation);

                    if (profileImage != null && profileImage.length() > 0) {
                        int pos = profileImage.lastIndexOf("/");
                        String x = profileImage.substring(pos + 1, profileImage.length());

                        if (x.equalsIgnoreCase("no-user.png")) {
                            profile_iv_navigation.setImageResource(R.drawable.no_image);
                            //imagebg.setBackgroundColor(Color.parseColor("#969393"));

                        } else {
                            Picasso.with(MainActivity.this)
                                    .load(profileImage)
                                    .placeholder(R.drawable.logo).error(R.drawable.logo).noFade().resize(200, 200).into(profile_iv_navigation, new Callback() {

                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError() {
//                                    profile_iv_navigation.setImageResource(R.drawable.no_image);
                                    //imagebg.setBackgroundColor(Color.parseColor("#969393"));
                                }

                            });
                        }
                    }
                }

            }
        }


    }

    @Override
    protected void onDestroy() {
        Util.MianActivityDestoryed = true;

        SquizeViewPager.setAdapter(null);
        super.onDestroy();

    }

    public void Next() {
        Log.v("Nihar_next", "called");
        try {
            if (adaptorPosition < QUEUE_ARRAY.size()) {
                if (adaptorPosition == QUEUE_ARRAY.size() - 1) {
                    adaptorPosition = -1;
                }
                Log.v("Nihar_next", "called");
                final QueueModel queueModel = QUEUE_ARRAY.get(adaptorPosition + 1);
                PlayQueue(queueModel, true);
                adaptorPosition = adaptorPosition + 1;
                try {
                    PlayerAdapter.changePlayedSong(true, adaptorPosition, MainActivity.this);
                } catch (Exception e) {

                }

            } else {
                Log.v("Nihar_next", "called else");
                adaptorPosition = 0;
                final QueueModel queueModel = QUEUE_ARRAY.get(0);
                PlayQueue(queueModel, true);
                try {
                    PlayerAdapter.changePlayedSong(true, adaptorPosition, MainActivity.this);
                } catch (Exception e) {

                }

            }
        } catch (Exception e) {
            Log.v("Nihar_next", "called" + e.toString());
        } finally {

        }
    }

    public void previous() {
        if (adaptorPosition > 0) {
            final QueueModel queueModel = QUEUE_ARRAY.get(adaptorPosition - 1);
            PlayQueue(queueModel, true);
            adaptorPosition = adaptorPosition - 1;
            try {
                PlayerAdapter.changePlayedSong(true, adaptorPosition, MainActivity.this);
            } catch (Exception e) {

            }


        } else {
            final QueueModel queueModel = QUEUE_ARRAY.get(adaptorPosition);
            PlayQueue(queueModel, true);
            try {
                PlayerAdapter.changePlayedSong(true, adaptorPosition, MainActivity.this);
            } catch (Exception e) {

            }

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(OPTION_MENU);
    }


    public boolean saveArray() {

        SharedPreferences.Editor editor = QueuePref.edit();
        Gson gson = new Gson();
        List<QueueModel> textList = new ArrayList<QueueModel>();
        textList.addAll(QUEUE_ARRAY);
        String jsonText = gson.toJson(textList);
        editor.putString("key", jsonText);
        return editor.commit();
    }

    /*** chromecast**************/

    public enum PlaybackLocation {
        LOCAL,
        REMOTE
    }

    public enum PlaybackState {
        PLAYING, PAUSED, BUFFERING, IDLE
    }


    private class MySessionManagerListener implements SessionManagerListener<CastSession> {

        @Override
        public void onSessionEnded(CastSession session, int error) {
            if (session == mCastSession) {
                mCastSession = null;
            }
            invalidateOptionsMenu();
        }

        @Override
        public void onSessionResumed(CastSession session, boolean wasSuspended) {
            mCastSession = session;
            invalidateOptionsMenu();
        }

        @Override
        public void onSessionStarted(CastSession session, String sessionId) {
            mCastSession = session;
            invalidateOptionsMenu();
        }

        @Override
        public void onSessionStarting(CastSession session) {
        }

        @Override
        public void onSessionStartFailed(CastSession session, int error) {
        }

        @Override
        public void onSessionEnding(CastSession session) {
        }

        @Override
        public void onSessionResuming(CastSession session, String sessionId) {
        }

        @Override
        public void onSessionResumeFailed(CastSession session, int error) {
        }

        @Override
        public void onSessionSuspended(CastSession session, int reason) {
        }
    }

    /*** chromecast**************/
    //*** chromecast************
    private void showIntroductoryOverlay() {
        if (mIntroductoryOverlay != null) {
            mIntroductoryOverlay.remove();
        }
        if ((mediaRouteMenuItem != null) && mediaRouteMenuItem.isVisible()) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    mIntroductoryOverlay = new IntroductoryOverlay.Builder(
                            MainActivity.this, mediaRouteMenuItem)
                            .setTitleText(getString(R.string.introducing_cast))
                            .setOverlayColor(R.color.colorPrimary)
                            .setSingleTime()
                            .setOnOverlayDismissedListener(
                                    new IntroductoryOverlay.OnOverlayDismissedListener() {
                                        @Override
                                        public void onOverlayDismissed() {
                                            mIntroductoryOverlay = null;
                                        }
                                    })
                            .build();
                    mIntroductoryOverlay.show();
                }
            });
        }
    }

    private void setupCastListener() {
        mSessionManagerListener = new SessionManagerListener<CastSession>() {

            @Override
            public void onSessionEnded(CastSession session, int error) {
                onApplicationDisconnected();
            }

            @Override
            public void onSessionResumed(CastSession session, boolean wasSuspended) {
                onApplicationConnected(session);
            }

            @Override
            public void onSessionResumeFailed(CastSession session, int error) {
                onApplicationDisconnected();
            }

            @Override
            public void onSessionStarted(CastSession session, String sessionId) {
                onApplicationConnected(session);
            }

            @Override
            public void onSessionStartFailed(CastSession session, int error) {
                onApplicationDisconnected();
            }

            @Override
            public void onSessionStarting(CastSession session) {

            }

            @Override
            public void onSessionEnding(CastSession session) {

            }

            @Override
            public void onSessionResuming(CastSession session, String sessionId) {

            }

            @Override
            public void onSessionSuspended(CastSession session, int reason) {

            }

            private void onApplicationConnected(CastSession castSession) {
                mCastSession = castSession;

                Log.v("praikc", "in onApplicationConnected..");

                if (null != mSelectedMedia) {
                   /* if (mCastSession != null && mCastSession.isConnected()) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                watchMovieButton.setText(getResources().getString(R.string.movie_details_cast_now_button_title));
                            }
                        });

                    }*/
                    if (mPlaybackState == PlaybackState.PLAYING) {
                       /* mVideoView.pause();
                        loadRemoteMedia(mSeekbar.getProgress(), true);*/
                        return;
                    } else {

                        mPlaybackState = PlaybackState.IDLE;
                        updatePlaybackLocation(PlaybackLocation.REMOTE);
                    }
                }
                //   updatePlayButton(mPlaybackState);
                invalidateOptionsMenu();
            }

            private void onApplicationDisconnected() {
               /* if (mCastSession != null && mCastSession.isConnected()) {
                    watchMovieButton.setText(getResources().getString(R.string.movie_details_watch_video_button_title));
                }*/
                //watchMovieButton.setText(getResources().getString(R.string.movie_details_watch_video_button_title));

                Log.v("praikc", "in onApplicationConnected..");

                updatePlaybackLocation(PlaybackLocation.LOCAL);
                mPlaybackState = PlaybackState.IDLE;
                mLocation = PlaybackLocation.LOCAL;

                invalidateOptionsMenu();
            }
        };
    }

    private void updatePlaybackLocation(PlaybackLocation location) {
        mLocation = location;

        Log.v("praikc", "in updatePlaybackLocation..");

        if (location == PlaybackLocation.LOCAL) {
            if (mPlaybackState == PlaybackState.PLAYING
                    || mPlaybackState == PlaybackState.BUFFERING) {
                //setCoverArtStatus(null);
                startControllersTimer();
            } else {
                stopControllersTimer();
                //setCoverArtStatus(MediaUtils.getImageUrl(mSelectedMedia, 0));
            }
        } else {
            stopControllersTimer();
            //setCoverArtStatus(MediaUtils.getImageUrl(mSelectedMedia, 0));
            updateControllersVisibility(false);
        }
    }

    private void startControllersTimer() {
        if (mControllersTimer != null) {
            mControllersTimer.cancel();
        }
        if (mLocation == PlaybackLocation.REMOTE) {
            return;
        }
        mControllersTimer = new Timer();
        mControllersTimer.schedule(new HideControllersTask(), 5000);

        Log.v("praikc", "in startControllersTimer..");
    }

    private void stopControllersTimer() {
        if (mControllersTimer != null) {
            mControllersTimer.cancel();
        }

        Log.v("praikc", "in stopControllersTimer..");
    }

    private void updateControllersVisibility(boolean show) {
        if (show) {
            getSupportActionBar().show();
            // mControllers.setVisibility(View.VISIBLE);
        } else {
            if (!Util.isOrientationPortrait(this)) {
                getSupportActionBar().hide();
            }
            //  mControllers.setVisibility(View.INVISIBLE);
        }

        Log.v("praikc", "in updateControllersVisibility..");
    }

    private class HideControllersTask extends TimerTask {

        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    // updateControllersVisibility(false);
                    //  mControllersVisible = false;
                }
            });

            Log.v("praikc", "in hidecontroller task..");
        }
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            boolean isNetwork = Util.checkNetwork(MainActivity.this);
            if (isNetwork == true) {
                switch (msg.what) {
                    case MSG_UPDATE_STATUS:
                        break;
                    case MSG_UPDATE_CONNECTION_TIME:

                        break;
                    case MSG_COMPLETE_STATUS:
                        final SpeedInfo info2 = (SpeedInfo) msg.obj;
                        String downloadedSpeed = String.format("%.1f", info2.megabits);
                        internetSpeed = downloadedSpeed;

                        break;
                    default:
                        internetSpeed = "0";

                        super.handleMessage(msg);
                }
            } else {
                internetSpeed = "0";

            }

            Log.v("praikc", "in mhandler..");
        }


    };

    private static class SpeedInfo {
        public double kilobits = 0;
        public double megabits = 0;
        public double downspeed = 0;


    }

    public static Drawable getDrawableByState(Context context, int state) {
        switch (state) {
            case STATE_PLAYING:
                AnimationDrawable animation = (AnimationDrawable)
                        ContextCompat.getDrawable(context, R.drawable.ic_equalizer_white_36dp);

                animation.start();
                return animation;
            case STATE_PAUSED:
                Drawable playDrawable = ContextCompat.getDrawable(context,
                        R.drawable.ic_equalizer1_white_36dp);

                return playDrawable;
            default:
                return null;
        }
    }

    public void ShowToast(String sucessMsg) {

        Context context = getApplicationContext();
        // Create layout inflator object to inflate toast.xml file
        LayoutInflater inflater = getLayoutInflater();

        // Call toast.xml file for toast layout
        View toastRoot = inflater.inflate(R.layout.custom_toast, null);
        TextView customToastMsg = (TextView) toastRoot.findViewById(R.id.toastMsg);
        customToastMsg.setText(sucessMsg);
        Toast toast = new Toast(context);

        // Set layout to toast
        toast.setView(toastRoot);
//        toast.setText("Added to Favorites");
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        try {
            Field[] fields = popup.getClass().getDeclaredFields();
            for (Field field : fields) {
                if ("mPopup".equals(field.getName())) {
                    field.setAccessible(true);
                    Object menuPopupHelper = field.get(popup);
                    Class<?> classPopupHelper = Class.forName(menuPopupHelper
                            .getClass().getName());
                    Method setForceIcons = classPopupHelper.getMethod(
                            "setForceShowIcon", boolean.class);
                    setForceIcons.invoke(menuPopupHelper, true);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.actions, popup.getMenu());
        Menu menu_popup = popup.getMenu();
        if ((Util.getTextofLanguage(MainActivity.this, Util.HAS_FAVORITE, Util.DEFAULT_HAS_FAVORITE)
                .trim()).equals("1")) {
            menu_popup.getItem(0).setVisible(true);
        } else {
            menu_popup.getItem(0).setVisible(false);
        }
        if ((Util.getTextofLanguage(MainActivity.this, Util.ISPLAYLIST, Util.DEFAULT_ISPLAYLIST)
                .trim()).equals("1")) {
            menu_popup.getItem(1).setVisible(true);
        } else {
            menu_popup.getItem(1).setVisible(false);
        }
        if ((Util.getTextofLanguage(MainActivity.this, Util.ISQUEUE, Util.DEFAULT_ISQUEUE)
                .trim()).equals("1")) {
            menu_popup.getItem(2).setVisible(true);
        } else {
            menu_popup.getItem(2).setVisible(false);
        }
        applyFontToMenuItem(menu_popup.getItem(0));
        applyFontToMenuItem(menu_popup.getItem(1));
        applyFontToMenuItem(menu_popup.getItem(2));
        applyFontToMenuItem(menu_popup.getItem(3));
        popup.show();

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getOrder()) {
                    ///my favorites
                    case 100:

                        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        if (userid.equals("0101D")) {
                            Intent signinIntent = new Intent(MainActivity.this, Login.class);
                            signinIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(signinIntent);
                        } else {
                            Fragment Favoritefragment = new FavouriteFragment();
                            if (fragment != null) {
                                FragmentManager fragmentManager = getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.main_content, Favoritefragment, "FavoriteFragment");
                                fragmentTransaction.addToBackStack("FavoriteFragment");
                                fragmentTransaction.commit();
                            }
                        }
                        break;
                    //my playlists
                    case 101:
                        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        if (userid.equals("0101D")) {
                            Intent signinIntent = new Intent(MainActivity.this, Login.class);
                            signinIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(signinIntent);
                        } else {
                            Fragment fragmentPlaylist = new PlayListFragment();
                            if (fragment != null) {
                                FragmentManager fragmentManager = getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.main_content, fragmentPlaylist, "PlayListFragment");
                                fragmentTransaction.addToBackStack("PlayListFragment");
                                fragmentTransaction.commit();
                            }
                        }
                        break;
                    //clear Queue
                    case 102:
//                        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        QUEUE_ARRAY.clear();
                        QueuePref.edit().clear().apply();
                        PlayerViewPagerAdapter testPagerAdapter = new PlayerViewPagerAdapter(getSupportFragmentManager());
                        SquizeViewPager.setAdapter(testPagerAdapter);
                        testPagerAdapter.notifyDataSetChanged();
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        getSupportActionBar().setHomeButtonEnabled(true);
                        toolbar.post(new Runnable() {
                            @Override
                            public void run() {
                                Drawable d = ResourcesCompat.getDrawable(getResources(), R.drawable.nav_drawer, null);
                                toolbar.setNavigationIcon(d);
                            }
                        });
                        testPagerAdapter.notifyDataSetChanged();
                        mediaPlayer.pause();
                        onBackPressed();
                        minicontroller.setVisibility(View.GONE);
                        Intent Pintent = new Intent("Constants.ACTION.QUEUE_CLEAR");
                        LocalBroadcastManager.getInstance(MainActivity.this).sendBroadcast(Pintent);
                        break;
                }

                return true;
            }
        });
    }

}
