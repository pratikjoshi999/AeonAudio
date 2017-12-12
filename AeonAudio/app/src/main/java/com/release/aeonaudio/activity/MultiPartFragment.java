package com.release.aeonaudio.activity;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.androidquery.AQuery;
import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaMetadata;
import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.CastState;
import com.google.android.gms.cast.framework.CastStateListener;
import com.google.android.gms.cast.framework.IntroductoryOverlay;
import com.google.android.gms.cast.framework.SessionManagerListener;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
import com.google.android.gms.common.images.WebImage;
import com.home.apisdk.apiController.GetContentDetailsAsynTask;
import com.home.apisdk.apiController.GetEpisodeDeatailsAsynTask;
import com.home.apisdk.apiController.GetValidateUserAsynTask;
import com.home.apisdk.apiModel.ContentDetailsInput;
import com.home.apisdk.apiModel.ContentDetailsOutput;
import com.home.apisdk.apiModel.Episode_Details_input;
import com.home.apisdk.apiModel.Episode_Details_output;
import com.home.apisdk.apiModel.ValidateUserInput;
import com.home.apisdk.apiModel.ValidateUserOutput;
import com.release.aeonaudio.R;
import com.release.aeonaudio.adapter.ListAdapterItem;
import com.release.aeonaudio.adapter.MultiDataAdaptor;
import com.release.aeonaudio.adapter.MultiDataPlayAdaptor;
import com.release.aeonaudio.adapter.PlayListAdapter;
import com.release.aeonaudio.adapter.QueueAdaptor;
import com.release.aeonaudio.adapter.SingleDataAdaptor;
import com.release.aeonaudio.model.ListModel;
import com.release.aeonaudio.model.PlayListModel;
import com.release.aeonaudio.model.PlayListMultiModel;
import com.release.aeonaudio.model.QueueModel;
import com.release.aeonaudio.service.MusicService;
import com.release.aeonaudio.utils.Constants;
import com.release.aeonaudio.utils.ExpandedControlsActivity;
import com.release.aeonaudio.utils.ProgressBarHandler;
import com.release.aeonaudio.utils.Util;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static android.R.attr.name;
import static android.content.Context.MODE_PRIVATE;
import static android.media.CamcorderProfile.get;
import static com.facebook.FacebookSdk.getApplicationContext;
import static com.release.aeonaudio.R.id.Download;
import static com.release.aeonaudio.R.id.cancelButton;
import static com.release.aeonaudio.R.id.saveButton;
import static com.release.aeonaudio.R.layout.item;
import static com.release.aeonaudio.R.layout.minicontroller;
import static com.release.aeonaudio.R.string.add_to_playlist;
import static com.release.aeonaudio.utils.Util.ArtistName;
import static com.release.aeonaudio.utils.Util.QUEUE_ARRAY;

/**
 * A simple {@link Fragment} subclass.
 */
public class MultiPartFragment extends Fragment implements GetValidateUserAsynTask.GetValidateUser, GetEpisodeDeatailsAsynTask.GetEpisodeDetails {
    ///////////////

    private Paint p = new Paint();
    ContentDetailsOutput contentDetailsOutput;
    Episode_Details_output episode_details_output_model;
    ArrayList<Episode_Details_output> episode_details_output;
    ArrayList<ListModel> customDatas = new ArrayList<>();
    private View view;
    String Poster;
    private String movie_stream_id_playlist;
    ProgressBarHandler progressHandler;
    String play_list_id;
    SingleDataAdaptor adapter;
    MultiDataPlayAdaptor multiDataPlayAdaptor;
    MultiDataAdaptor multiAdapter;
    String desired_string, content_types_id, ImageUrl, PlayListName;
    ImageView banner_image, favourite;
    Button play_all;
    String banner, artist_multi;
    SQLiteDatabase DB;
    int Position;
    ListAdapterItem listadapter;
    TextView SongCount;
    TextView albumName_multipart, new_playlist;
    ListView listViewItems;
    ProgressBar List_wait_progressBar;
    ArrayList<Episode_Details_output> ItemListDetails = new ArrayList<>();
    ////////////////////
    RecyclerView my_recycler_view;
    SharedPreferences prefs;
    String userId;
    Toolbar toolbar;
    private int position_id;
    String album_name, song_url, song_imageUrl, song_name, genere, artist_name;
    ProgressBarHandler pDialog;
    String movieId;
    private String movieUniqueIdmulti;
    private int isFavorite;
    private String movieUniqueId;
    private String poster;
    private String MutiArtist;
    Bundle arguments;
    String movie_stream_id, playlistid;
    private String playlist_id;
    private String isEpisode;
    private String PlayListId;
    ArrayList<PlayListModel> ItemList;
    private String movieuniqueid;
    private String MovieStramId_del_cont;
    private Dialog progress_dialog;
    private String movie_strm;

////////////////chromeCast////////////////////////////

    /***************chromecast**********************/
    public enum PlaybackLocation {
        LOCAL,
        REMOTE
    }

    public enum PlaybackState {
        PLAYING, PAUSED, BUFFERING, IDLE
    }


    MediaInfo mediaInfo;
    private VideoView mVideoView;
    private TextView mTitleView;
    private TextView mDescriptionView;
    private TextView mStartText;
    private TextView mEndText;
    private SeekBar mSeekbar;
    private ImageView mPlayPause;
    private ProgressBar mLoading;
    private View mControllers;
    private View mContainer;
    private ImageView mCoverArt;
    private Timer mSeekbarTimer;
    private Timer mControllersTimer;
    private PlaybackLocation mLocation;
    private PlaybackState mPlaybackState;
    private final Handler mHandler = new Handler();
    private final float mAspectRatio = 72f / 128;
    private AQuery mAquery;
    private MediaInfo mSelectedMedia;
    private boolean mControllersVisible;
    private int mDuration;
    private TextView mAuthorView;
    private ImageButton mPlayCircle;
    private RelativeLayout MultipartView;


    private CastContext mCastContext;

    private CastSession mCastSession;
    private MenuItem mediaRouteMenuItem;
    private IntroductoryOverlay mIntroductoryOverlay;
    private CastStateListener mCastStateListener;
    private SessionManagerListener<CastSession> mSessionManagerListener =
            new MySessionManagerListener();

    ////////////////////////chromeCast////////////
    public MultiPartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.RESULT_HIDDEN);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (pDialog.isShowing()) {
            pDialog.hide();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        arguments = getArguments();
        desired_string = arguments.getString("PERMALINK");
        Log.v("Nihar_fev",""+desired_string);
        content_types_id = arguments.getString("CONTENT_TYPE");

        View v = inflater.inflate(R.layout.fragment_multi_part, container, false);
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );


        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(v, InputMethodManager.RESULT_HIDDEN);

        String imgurl_up = "https://sampledesign.muvi.com/mobileaudio/testimage1.png";
        ImageView banner_overlay = (ImageView) v.findViewById(R.id.overlay);
        favourite = (ImageView) v.findViewById(R.id.favourite_multi);
        Picasso.with(getActivity())
                .load(imgurl_up)
                .error(R.drawable.no_image)
                .into(banner_overlay);
        String imgurl_bottom = "https://sampledesign.muvi.com/mobileaudio/testimage2.png";
        ImageView banner_overlay_down = (ImageView) v.findViewById(R.id.overlay2);

        progress_dialog = Util.LoadingCircularDialog(getActivity());
        Picasso.with(getActivity())
                .load(imgurl_bottom)
                .error(R.drawable.no_image)
                .into(banner_overlay_down);
        DB = getActivity().openOrCreateDatabase(Util.DATABASE_NAME, MODE_PRIVATE, null);
        /*
        @Bundle arguments capture from Adaptor(permalink)
         @#GetContaint details used for unique data .
         */
        setHasOptionsMenu(true);

        ImageUrl = arguments.getString("ImageUrl");
        PlayListName = arguments.getString("PlayListName");
        pDialog = new ProgressBarHandler(getActivity());
        progressHandler = new ProgressBarHandler(getActivity());
        my_recycler_view = (RecyclerView) v.findViewById(R.id.list_recyclerView);
        //  my_recycler_view.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        my_recycler_view.setHasFixedSize(true);

        MultipartView = (RelativeLayout) v.findViewById(R.id.multipart_view);

        banner_image = (ImageView) v.findViewById(R.id.banner_image);
        play_all = (Button) v.findViewById(R.id.play_all);
        albumName_multipart = (TextView) v.findViewById(R.id.albumName_multipart);
        SongCount = (TextView) v.findViewById(R.id.SongCount);
        Typeface albumName_multipart_tf = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.regular_fonts));
        albumName_multipart.setTypeface(albumName_multipart_tf);
        Typeface SongCount_tf = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.light_fonts));
        SongCount.setTypeface(SongCount_tf);
        prefs = getActivity().getSharedPreferences(Util.LOGINPREFERENCE, MODE_PRIVATE);
        userId = prefs.getString("useridPref", null);
        PlayListId = arguments.getString("PlayListId");
        ItemList = new ArrayList<>();
        switch (content_types_id) {
            case "6":
                MultiPartView();
                break;
            case "5":
                SinglePartView();
                break;
            case "playlist":
                PlayListView();
                break;
        }
        favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!userId.equals("0101D")) {
                    Log.v("Nihar_isfavorite", "" + isFavorite);
                    if (isFavorite == 1) {


                        AsynFavoriteDelete asynFavoriteDelete = new AsynFavoriteDelete();
                        asynFavoriteDelete.execute();
                    } else {
                        AsynFavoriteAdd asynFavoriteAdd = new AsynFavoriteAdd();
                        asynFavoriteAdd.execute();


                    }
                } else {
                    Toast.makeText(getActivity(), "Please Register For Acessing this service ", Toast.LENGTH_SHORT).show();
                }
            }
        });


        play_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (content_types_id.equals("6")) {
                    album_name = episode_details_output.get(0).getName();
                    song_url = episode_details_output.get(0).getVideo_url();
                    song_imageUrl = episode_details_output.get(0).getPoster_url();
                    genere = episode_details_output.get(0).getGenre();
                    song_name = episode_details_output.get(0).getEpisode_title();
                    artist_name = MutiArtist;

                    /////////////////////////////////////////////////////////////////////////
                    ValidateUserInput validateUserInput = new ValidateUserInput();
                    validateUserInput.setAuthToken(Util.authTokenStr);
                    validateUserInput.setUserId(userId);
                    validateUserInput.setMuviUniqueId(movieuniqueid);
                    validateUserInput.setSeasonId("0");
                    validateUserInput.setEpisodeStreamUniqueId("0");

                    GetValidateUserAsynTask getValidateUserAsynTask = new GetValidateUserAsynTask(validateUserInput, MultiPartFragment.this, getActivity());
                    getValidateUserAsynTask.execute();

                    QUEUE_ARRAY.clear();
                    for (int i = 0; i < episode_details_output.size(); i++) {
                        QUEUE_ARRAY.add(new QueueModel(episode_details_output.get(i).getEpisode_title(), episode_details_output.get(i).getPoster_url(), episode_details_output.get(i).getVideo_url(), MutiArtist));
                    }
                    Intent addtoqueue = new Intent("ADD_TO_QUEUE");
                    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(addtoqueue);
                } else if (content_types_id.equals("playlist")) {
                    if (ItemListDetails.size() != 0) {
                        album_name = ItemListDetails.get(0).getName();
                        song_url = ItemListDetails.get(0).getVideo_url();
                        song_imageUrl = ItemListDetails.get(0).getPoster_url();
                        genere = ItemListDetails.get(0).getGenre();
                        song_name = ItemListDetails.get(0).getEpisode_title();
                        artist_name = MutiArtist;
                   /* ValidateUserInput validateUserInput = new ValidateUserInput();
                    validateUserInput.setAuthToken(Util.authTokenStr);
                    validateUserInput.setUserId(userId);
                    validateUserInput.setMuviUniqueId(movieuniqueid);
                    validateUserInput.setSeasonId("0");
                    validateUserInput.setEpisodeStreamUniqueId("0");

                    GetValidateUserAsynTask getValidateUserAsynTask = new GetValidateUserAsynTask(validateUserInput, MultiPartFragment.this, getActivity());
                    getValidateUserAsynTask.execute();*/


                        Player_State(1);
                        QUEUE_ARRAY.clear();
                        for (int i = 0; i < ItemListDetails.size(); i++) {
                            QUEUE_ARRAY.add(new QueueModel(ItemListDetails.get(i).getName(), ItemListDetails.get(i).getPoster_url(), ItemListDetails.get(i).getVideo_url(), MutiArtist));
                        }
                    } else {
                        play_all.setClickable(false);
                    }
                } else {
                    MultipartView.setClickable(false);
                    album_name = contentDetailsOutput.getName();
                    song_url = contentDetailsOutput.getMovieUrl();
                    genere = contentDetailsOutput.getGenre();
                    song_imageUrl = contentDetailsOutput.getPoster();
                    song_name = contentDetailsOutput.getName();
                    artist_name = contentDetailsOutput.getArtist();

                    ValidateUserInput validateUserInput = new ValidateUserInput();
                    validateUserInput.setAuthToken(Util.authTokenStr);
                    validateUserInput.setUserId(userId);
                    validateUserInput.setMuviUniqueId(movieuniqueid);
                    validateUserInput.setSeasonId("0");
                    validateUserInput.setEpisodeStreamUniqueId("0");

                    GetValidateUserAsynTask getValidateUserAsynTask = new GetValidateUserAsynTask(validateUserInput, MultiPartFragment.this, getActivity());
                    getValidateUserAsynTask.execute();

                    QUEUE_ARRAY.clear();
                    QUEUE_ARRAY.add(new QueueModel(contentDetailsOutput.getName(), contentDetailsOutput.getPoster(), contentDetailsOutput.getMovieUrl(), contentDetailsOutput.getArtist()));

                }

            }
        });

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(OPTION_RESPONSE, new IntentFilter("OPTION_RESPONSE"));


        //////////Chromecast//////////////////////

        boolean shouldStartPlayback = false;
        int startPosition = 0;
        // int startPosition = getInt("startPosition", 0);
        // mVideoView.setVideoURI(Uri.parse(item.getContentId()));

        mCastStateListener = new CastStateListener() {
            @Override
            public void onCastStateChanged(int newState) {
                if (newState != CastState.NO_DEVICES_AVAILABLE) {

//                    showIntroductoryOverlay();
                }
            }
        };


        mCastContext = CastContext.getSharedInstance(getActivity());
        mCastContext.registerLifecycleCallbacksBeforeIceCreamSandwich(getActivity(), savedInstanceState);

        setupCastListener();
        mCastSession = mCastContext.getSessionManager().getCurrentCastSession();

        if (shouldStartPlayback) {
            // this will be the case only if we are coming from the
            // CastControllerActivity by disconnecting from a device
            mPlaybackState = PlaybackState.PLAYING;
            updatePlaybackLocation(PlaybackLocation.LOCAL);
            updatePlayButton(mPlaybackState);
            if (startPosition > 0) {
                // mVideoView.seekTo(startPosition);
            }
            // mVideoView.start();
            //startControllersTimer();
        } else {
            // we should load the video but pause it
            // and show the album art.
            if (mCastSession != null && mCastSession.isConnected()) {
                //watchMovieButton.setText(getResources().getString(R.string.movie_details_cast_now_button_title));
                updatePlaybackLocation(PlaybackLocation.REMOTE);
            } else {
                //watchMovieButton.setText(getResources().getString(R.string.movie_details_watch_video_button_title));

                updatePlaybackLocation(PlaybackLocation.LOCAL);
            }
            mPlaybackState = PlaybackState.IDLE;
            updatePlayButton(mPlaybackState);
        }

        //////////Chromecast//////////////////////


        return v;
    }


    private BroadcastReceiver OPTION_RESPONSE = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            movie_stream_id = intent.getStringExtra("movie_stream_id");
            movie_stream_id_playlist = intent.getStringExtra("movie_stream_id_playlist");
            playlistid = intent.getStringExtra("playlistid");
            position_id = intent.getIntExtra("position", 0);
            isEpisode = intent.getStringExtra("isEpisode");

            if (intent.getStringExtra("response").equals("remove_Playlist_content")) {
                if (Util.checkNetwork(getActivity())) {
                    Log.v("nihar_position", "Called");
                    AsyncDeletePlaylistContent asyncDeletePlaylistContent = new AsyncDeletePlaylistContent();
                    asyncDeletePlaylistContent.execute();

                } else {
                    Util.showToast(getActivity(), Util.getTextofLanguage(getActivity(), Util.SLOW_INTERNET_CONNECTION, Util.DEFAULT_SLOW_INTERNET_CONNECTION));
                }

            } else if (intent.getStringExtra("response").equals("add_to_playlist")) {
                if (!userId.equals("0101D")) {
                    LayoutInflater li = LayoutInflater.from(getContext());
                    View promptsView = li.inflate(R.layout.custom_dialog_view, null);
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                    alertDialogBuilder.setView(promptsView);
                    listViewItems = (ListView) promptsView.findViewById(R.id.playList_content_listView);
                    new_playlist = (TextView) promptsView.findViewById(R.id.new_playlist);
                    AsynShowPlaylist asynShowPlaylist = new AsynShowPlaylist();
                    asynShowPlaylist.execute();
                    final AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
                    alertDialog.show();

                    listViewItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            alertDialog.hide();
                            play_list_id = customDatas.get(position).getPlayListId();

                            AsyncAddToPlaylist asyncAddToPlaylist = new AsyncAddToPlaylist();
                            asyncAddToPlaylist.execute();

                        }
                    });
                    new_playlist.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.hide();
                            LayoutInflater li = LayoutInflater.from(getContext());
                            View promptsView = li.inflate(R.layout.custom_dialog, null);
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                            alertDialogBuilder.setView(promptsView);

                            Button saveButton = (Button) promptsView.findViewById(R.id.saveButton);
                            Button cancelButton = (Button) promptsView.findViewById(R.id.cancelButton);
                            TextView dialog_text = (TextView) promptsView.findViewById(R.id.dialog_text);
                            Typeface dialog_text_tf = Typeface.createFromAsset(getActivity().getAssets(), getActivity().getString(R.string.regular_fonts));
                            dialog_text.setTypeface(dialog_text_tf);
                            final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);
                            final AlertDialog alertDialogadd = alertDialogBuilder.create();
                            alertDialogadd.getWindow().setBackgroundDrawableResource(R.color.transparent);
                            alertDialogadd.setCancelable(false);
                            cancelButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // alertDialog.cancel;
                                    alertDialogadd.hide();
                                }
                            });
                            saveButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //Do some thing
                                    alertDialogadd.hide();
                                    PlayListName = String.valueOf(userInput.getText());
                                    AsyncAddToNewPlaylist asyncAddToNewPlaylist = new AsyncAddToNewPlaylist();
                                    asyncAddToNewPlaylist.execute();
                                }
                            });
                            alertDialogadd.show();
                        }
                    });
                } else {
                    Toast.makeText(context, "Please Register to Avail this Service", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };


    @Override
    public void onDetach() {
        super.onDetach();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(OPTION_RESPONSE);
    }


    public void PlaySongs(ContentDetailsOutput item, boolean isClicked) {
        if (isClicked) {


            contentDetailsOutput = item;
            album_name = item.getName();
            song_url = item.getMovieUrl();
            genere = item.getGenre();
            song_imageUrl = item.getPoster();
            song_name = item.getName();
            artist_name = item.getArtist();
            if (userId.equals("0101D")) {
                Player_State(1);
            } else {
                ValidateUserInput validateUserInput = new ValidateUserInput();
                validateUserInput.setAuthToken(Util.authTokenStr);
                validateUserInput.setUserId(userId);
                Log.v("niharId", "" + item.getMuviUniqId());
                validateUserInput.setMuviUniqueId(item.getMuviUniqId());
                validateUserInput.setSeasonId("0");
                validateUserInput.setEpisodeStreamUniqueId("0");

                GetValidateUserAsynTask getValidateUserAsynTask = new GetValidateUserAsynTask(validateUserInput, this, getActivity());
                getValidateUserAsynTask.execute();
            }


        } else {

        }
        Log.v("nihar_nihar", "" + item.getName());


    }

    public void    PlaySongsmulti(Episode_Details_output item, boolean isClicked, int adapterPosition) {
        if (isClicked) {


            episode_details_output_model = item;
            album_name = item.getName();
            song_url = item.getVideo_url();
            song_imageUrl = item.getPoster_url();
            genere = item.getGenre();
            song_name = item.getEpisode_title();
            artist_name = MutiArtist;

            Intent CONTENT_OUTPUT = new Intent("CONTENT_OUTPUT2");
            CONTENT_OUTPUT.putExtra("Content_multipart", episode_details_output);
            CONTENT_OUTPUT.putExtra("position_item", adapterPosition);
            CONTENT_OUTPUT.putExtra("artist", artist_name);
            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(CONTENT_OUTPUT);
            if (userId.equals("0101D")) {
                Player_State(1);
            } else {
                ValidateUserInput validateUserInput = new ValidateUserInput();
                validateUserInput.setAuthToken(Util.authTokenStr);
                validateUserInput.setUserId(userId);
                validateUserInput.setMuviUniqueId(item.getMuvi_uniq_id());
                Log.v("namo","muiid="+item.getMuvi_uniq_id());
                validateUserInput.setSeasonId("0");
                validateUserInput.setEpisodeStreamUniqueId("0");
                GetValidateUserAsynTask getValidateUserAsynTask = new GetValidateUserAsynTask(validateUserInput, this, getActivity());
                getValidateUserAsynTask.execute();
            }

        }


    }

    public void Player_State(int funId) {

        Intent playerData = new Intent("PLAYER_DETAIL");
        playerData.putExtra("songName", album_name);
        Log.v("nihar3", "" + album_name);
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(playerData);


        String DEL_QRY = "DELETE FROM " + Util.USER_TABLE_NAME + "";
        DB.execSQL(DEL_QRY);

        String INS_QRY = "INSERT INTO " + Util.USER_TABLE_NAME + "(ALBUM_ART_PATH,ALBUM_SONG_NAME) VALUES ( '" + song_imageUrl + "','" + song_name + "')";
        DB.execSQL(INS_QRY);
        try {
            Intent j = new Intent(getContext(), MusicService.class);
            j.putExtra("ALBUM", song_url);
            j.putExtra("PERMALINK", desired_string);
            j.putExtra("POSITION", Position);
            j.putExtra("ALBUM_ART", song_imageUrl);
            j.putExtra("ALBUM_NAME", name);
            j.putExtra("Artist", artist_name);
            j.putExtra("ALBUM_SONG_NAME", song_name);
            j.putExtra("STATE", funId);
            j.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
            getContext().startService(j);
        } catch (Exception e) {

        }


    }

    @Override
    public void onGetValidateUserPreExecuteStarted() {
//        progress_dialog.show();
//        MultipartView.setClickable(false);

        if (!pDialog.isShowing()) {
            pDialog.show();
        }
    }

    @Override
    public void onGetValidateUserPostExecuteCompleted(ValidateUserOutput validateUserOutput, int status, String message) {
        if (pDialog.isShowing()) {
            pDialog.hide();
        }

        Log.v("namo","status 15s="+status);
//        progress_dialog.hide();
        if ((Util.getTextofLanguage(getActivity(), Util.HAS_FAVORITE, Util.DEFAULT_HAS_FAVORITE)
                .trim()).equals("1")) {
            favourite.setVisibility(View.VISIBLE);
        }else{
            favourite.setVisibility(View.GONE);
        }
        play_all.setVisibility(View.VISIBLE);

        Log.v("nihar_payment", "===========================" + message + "    " + validateUserOutput.getValiduser_str());
        if (status == 427) {

            Toast.makeText(getActivity(), "Sorry , This content is  not available in your country . ", Toast.LENGTH_SHORT).show();
        } else if (status == 425 || status == 426) {

            Intent intent = new Intent(getActivity(), SubscriptionActivity.class);
            intent.putExtra("movieId", movieId);
            Bundle arguments = getArguments();
            intent.putExtra("permalink", arguments.getString("PERMALINK"));
            intent.putExtra("content_types_id", arguments.getString("CONTENT_TYPE"));
            startActivity(intent);
        } else if (status == 429) {

            Log.v("pratikc", "status checked");
            Log.v("pratikc", "mCastSession==" + mCastSession);


            mCastSession = mCastContext.getSessionManager().getCurrentCastSession();
            Log.v("pratikc", "status checked");
            Log.v("pratikc", "mCastSession==" + mCastSession);

            if (mCastSession != null && mCastSession.isConnected()) {

                Log.v("pratikc", "chrome cast");
                MediaMetadata movieMetadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_MUSIC_TRACK);
                movieMetadata.putString(MediaMetadata.KEY_TITLE, song_name);
                movieMetadata.addImage(new WebImage(Uri.parse(song_imageUrl.trim())));

                mediaInfo = new MediaInfo.Builder(song_url.trim())
                        .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                        .setContentType("videos/mp4")
                        .setMetadata(movieMetadata)
                        .setStreamDuration(15 * 1000)
                        .build();

                mSelectedMedia = mediaInfo;

                togglePlayback();
            } else {
                Player_State(1);
            }


        } else if (status == 430) {
//            message.equals("Unpaid") &&
//            Log.v("nihar_payment",""+validateUserOutput.getIsMemberSubscribed());
//            Toast.makeText(getActivity(), validateUserOutput.getIsMemberSubscribed() , Toast.LENGTH_SHORT).show();
            if (validateUserOutput.getIsMemberSubscribed().equals("0")) {
                Intent intent = new Intent(getActivity(), SubscriptionActivity.class);
                intent.putExtra("movieId", movieId);
                Bundle arguments = getArguments();
                intent.putExtra("permalink", arguments.getString("PERMALINK"));
                intent.putExtra("content_types_id", arguments.getString("CONTENT_TYPE"));
                startActivity(intent);
            } else {

                mCastSession = mCastContext.getSessionManager().getCurrentCastSession();
                Log.v("pratikc", "status checked");
                Log.v("pratikc", "mCastSession==" + mCastSession);

                if (mCastSession != null && mCastSession.isConnected()) {

                    MediaMetadata movieMetadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_MUSIC_TRACK);
                    movieMetadata.putString(MediaMetadata.KEY_TITLE, song_name);
                    movieMetadata.addImage(new WebImage(Uri.parse(song_imageUrl.trim())));

                    mediaInfo = new MediaInfo.Builder(song_url.trim())
                            .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                            .setContentType("videos/mp4")
                            .setMetadata(movieMetadata)
                            .setStreamDuration(15 * 1000)
                            .build();

                    mSelectedMedia = mediaInfo;

                    togglePlayback();
                } else {
                    Player_State(1);
                }


            }
        }
        MultipartView.setClickable(true);

    }

    @Override
    public void onGetEpisodeDetailsPreExecuteStarted() {
        pDialog.show();
    }

    @Override
    public void onGetEpisodeDetailsPostExecuteCompleted(ArrayList<Episode_Details_output> episode_details_output,
                                                        int i, int status, String message) {
      /*  if (pDialog.isShowing()) {
            pDialog.hide();
        }*/
        play_all.setVisibility(View.VISIBLE);
        if ((Util.getTextofLanguage(getActivity(), Util.HAS_FAVORITE, Util.DEFAULT_HAS_FAVORITE)
                .trim()).equals("1")) {
            favourite.setVisibility(View.VISIBLE);
        }else {
            favourite.setVisibility(View.GONE);
        }
        SongCount.setText(episode_details_output.size() + " " + "Tracks");
        this.episode_details_output = episode_details_output;
        Log.v("NIahr_bnner", "+poster" + poster);
        String containt_name = episode_details_output.get(0).getName();
        albumName_multipart.setText(containt_name);
        if (banner != null && !banner.equals("")) {
            Picasso.with(getActivity())
                    .load(banner)
                    .error(R.drawable.no_image)
                    .into(banner_image);
            Log.v("NIahr_bnner", "+banner" + banner);
        } else {
            Picasso.with(getActivity())
                    .load(poster)
                    .error(R.drawable.no_image)
                    .into(banner_image);
            Log.v("NIahr_bnner", "+poster" + poster);
        }
        pDialog.hide();
        Log.v("Nihar_artist", "fragment" + artist_multi);
        multiAdapter = new MultiDataAdaptor(getActivity(), episode_details_output, MultiPartFragment.this, MutiArtist, "MultipartContent");
        my_recycler_view.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        my_recycler_view.setAdapter(multiAdapter);
        multiAdapter.notifyDataSetChanged();


    }

    public void PlayListView() {
        favourite.setVisibility(View.GONE);
        play_all.setVisibility(View.GONE);

        AsynAudioUserPlayListDetail asynAudioUserPlayListDetail = new AsynAudioUserPlayListDetail();
        asynAudioUserPlayListDetail.execute();
      /*  if (pDialog.isShowing() && pDialog != null) {
            pDialog.hide();
        }*/
    }

    public void SinglePartView() {
        AsynLoadContentDetails asynLoadContentDetails = new AsynLoadContentDetails();
        asynLoadContentDetails.execute();
    }

    public void MultiPartView() {
        if (Util.checkNetwork(getActivity())) {
            AsyncgetMultiDetails asyncgetMultiDetails = new AsyncgetMultiDetails();
            asyncgetMultiDetails.execute();
        } else {
            Util.showToast(getActivity(), Util.getTextofLanguage(getActivity(), Util.SLOW_INTERNET_CONNECTION, Util.DEFAULT_SLOW_INTERNET_CONNECTION));

        }
        Episode_Details_input episode_details_input = new Episode_Details_input();
        episode_details_input.setAuthtoken(Util.authTokenStr);
        episode_details_input.setPermalink(desired_string);
        episode_details_input.setUserid(userId);
        episode_details_input.setOffset("0");
        episode_details_input.setLimit("10");
        GetEpisodeDeatailsAsynTask getEpisodeDeatailsAsynTask = new GetEpisodeDeatailsAsynTask(episode_details_input, this, getActivity());
        getEpisodeDeatailsAsynTask.execute();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here

        /************chromecast***********/

//        showIntroductoryOverlay();
        /////////////////////////////

        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);
        menu.findItem(R.id.filter).setVisible(false);
        menu.findItem(R.id.nav_Search).setVisible(true);

    }

    @Override
    public void onPause() {
        super.onPause();
        if (pDialog.isShowing()) {
            pDialog.hide();
        }
       /* if (progress_dialog.isShowing()) {
            progress_dialog.hide();
        }*/
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(OPTION_RESPONSE);
    }

    @Override
    public void onResume() {

        super.onResume();
        if (Util.DownloadChange) {
            switch (content_types_id) {
                case "6":
                    multiAdapter = new MultiDataAdaptor(getActivity(), episode_details_output, MultiPartFragment.this, MutiArtist, "MultipartContent");
                    my_recycler_view.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                    my_recycler_view.setAdapter(multiAdapter);
                    multiAdapter.notifyDataSetChanged();
                    Util.DownloadChange = false;
                    Log.v("Nihar_doenload", "called multi");
                    break;
                case "5":
                    adapter = new SingleDataAdaptor(getActivity(), contentDetailsOutput, MultiPartFragment.this);
                    my_recycler_view.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                    my_recycler_view.setAdapter(adapter);
                    Util.DownloadChange = false;
                    Log.v("Nihar_doenload", "called single");
                    break;

            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (pDialog.isShowing()) {
            pDialog.hide();
        }
    }

    /*************AsyncTask****************/
    public class AsyncgetMultiDetails extends AsyncTask<Void, Void, Void> {

        ContentDetailsInput contentDetailsInput;
        String responseStr, message;
        int status;

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(Util.rootUrl().trim() + Util.detailsUrl.trim());
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr);
                httppost.addHeader("permalink", desired_string);
                try {
                    prefs = getActivity().getSharedPreferences(Util.LOGINPREFERENCE, MODE_PRIVATE);
                    httppost.addHeader("user_id", prefs.getString("useridPref", null));
                } catch (Exception e) {
                }
                // Execute HTTP Post Request
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());


                } catch (org.apache.http.conn.ConnectTimeoutException e) {

                    status = 0;


                } catch (IOException e) {
                    status = 0;
                }

                JSONObject myJson = null;
                if (responseStr != null) {
                    myJson = new JSONObject(responseStr);
                    Log.v("SUBHA", "SB" + responseStr);
                    status = Integer.parseInt(myJson.optString("code"));
                    message = myJson.optString("msg");
                }

                if (status > 0) {

                    if (status == 200) {
                        JSONObject mainJson = myJson.getJSONObject("movie");
                        movieUniqueIdmulti = mainJson.getString("movie_stream_uniq_id");
                        movieuniqueid = mainJson.getString("muvi_uniq_id");
                        try {
                            isFavorite = Integer.parseInt(mainJson.getString("is_favorite"));
                            poster = mainJson.getString("poster");

                            if (mainJson.has("cast_detail")) {
                                JSONArray castArray = mainJson.getJSONArray("cast_detail");
                                Log.v("SUBHA", "cast_detail" + castArray.length());


                                StringBuilder sb = new StringBuilder();
                                if (castArray.length() > 0) {
                                    for (int i = 0; i < castArray.length(); i++) {
                                        JSONObject jsonChildNode = castArray.getJSONObject(i);
                                        if (jsonChildNode.has("celeb_name")) {
                                            sb.append(jsonChildNode.getString("celeb_name").toString());
                                            if (i != castArray.length() - 1) {
                                                sb.append(",");
                                            }

                                        }

                                    }
                                    MutiArtist = sb.toString();
                                    ArtistName = MutiArtist;
//                                    contentDetailsOutput.setArtist(sb.toString());
                                    Log.v("SUBHA", "SB" + sb.toString());

                                } else {

                                }
                            } else {


                            }

                        } catch (Exception e) {
                            Log.v("nihare", "isfavourite===errror==" + e.toString());
                        }
                        Log.v("Nihar_multi", "poster==================================================" + poster);
                        Log.v("Nihar_multi", "movieUniqueIdmulti" + movieUniqueIdmulti);


                        if ((mainJson.has("banner")) && mainJson.getString("banner").trim() != null && !mainJson.getString("banner").trim().isEmpty() && !mainJson.getString("banner").trim().equals("null") && !mainJson.getString("banner").trim().matches("")) {
                            Log.v("Nihar_multi", "============================EnterName");
                            banner = mainJson.getString("banner");
//                            contentDetailsOutput.setName(mainJson.getString("name"));
                        } else {
                            contentDetailsOutput.setName("");

                        }
                        if ((mainJson.has("name")) && mainJson.getString("name").trim() != null && !mainJson.getString("name").trim().isEmpty() && !mainJson.getString("name").trim().equals("null") && !mainJson.getString("name").trim().matches("")) {
                            Log.v("Nihar_multi", "============================EnterName");
                            Log.v("Nihar_multi", "============================Enter" + mainJson.getString("name"));
                            contentDetailsOutput.setName(mainJson.getString("name"));


                        } else {
                            contentDetailsOutput.setName("");

                        }

                        if ((mainJson.has("genre")) && mainJson.getString("genre").trim() != null && !mainJson.getString("genre").trim().isEmpty() && !mainJson.getString("genre").trim().equals("null") && !mainJson.getString("genre").trim().matches("")) {
                            contentDetailsOutput.setGenre(mainJson.getString("genre"));


                        } else {
                            contentDetailsOutput.setGenre("");

                        }
                        if ((mainJson.has("censor_rating")) && mainJson.getString("censor_rating").trim() != null && !mainJson.getString("censor_rating").trim().isEmpty() && !mainJson.getString("censor_rating").trim().equals("null") && !mainJson.getString("censor_rating").trim().matches("")) {
                            contentDetailsOutput.setCensorRating(mainJson.getString("censor_rating"));


                        } else {
                            contentDetailsOutput.setCensorRating("");

                        }
                        if ((mainJson.has("story")) && mainJson.getString("story").trim() != null && !mainJson.getString("story").trim().isEmpty() && !mainJson.getString("story").trim().equals("null") && !mainJson.getString("story").trim().matches("")) {
                            contentDetailsOutput.setStory(mainJson.getString("story"));
                        } else {
                            contentDetailsOutput.setStory("");

                        }
                        if ((mainJson.has("trailerUrl")) && mainJson.getString("trailerUrl").trim() != null && !mainJson.getString("trailerUrl").trim().isEmpty() && !mainJson.getString("trailerUrl").trim().equals("null") && !mainJson.getString("trailerUrl").trim().matches("")) {
                            contentDetailsOutput.setTrailerUrl(mainJson.getString("trailerUrl"));
                        } else {
                            contentDetailsOutput.setTrailerUrl("");

                        }

                        if ((mainJson.has("movie_uniq_id")) && mainJson.getString("movie_uniq_id").trim() != null && !mainJson.getString("movie_uniq_id").trim().isEmpty() && !mainJson.getString("movie_uniq_id").trim().equals("null") && !mainJson.getString("movie_uniq_id").trim().matches("")) {
                            contentDetailsOutput.setMovieStreamUniqId(mainJson.getString("movie_uniq_id"));
                            movieUniqueIdmulti = mainJson.getString("movie_uniq_id");
                            Log.v("pratikf", "multi mui+++=" + movieUniqueIdmulti);

                        } else {
                            contentDetailsOutput.setMovieStreamUniqId("");

                        }

                        try {
                            movieUniqueIdmulti = mainJson.getString("muvi_uniq_id");
                            Log.v("pratikf", "multi mui+++=" + movieUniqueIdmulti);
                        } catch (Exception e) {

                            Log.v("pratikf", "errrr=" + e.toString());
                        }

                        if ((mainJson.has("muvi_uniq_id")) && mainJson.getString("muvi_uniq_id").trim() != null && !mainJson.getString("muvi_uniq_id").trim().isEmpty() && !mainJson.getString("muvi_uniq_id").trim().equals("null") && !mainJson.getString("muvi_uniq_id").trim().matches("")) {

                            contentDetailsOutput.setMuviUniqId(mainJson.getString("movie_stream_uniq_id"));


                        } else {
                            contentDetailsOutput.setMuviUniqId("");

                        }

                        if ((mainJson.has("is_favorite")) && mainJson.getString("is_favorite").trim() != null && !mainJson.getString("is_favorite").trim().isEmpty() && !mainJson.getString("is_favorite").trim().equals("null") && !mainJson.getString("is_episode").trim().matches("")) {
                            isFavorite = Integer.parseInt(mainJson.getString("is_favorite"));

                        }

                        if ((mainJson.has("movieUrl")) && mainJson.getString("movieUrl").trim() != null && !mainJson.getString("movieUrl").trim().isEmpty() && !mainJson.getString("movieUrl").trim().equals("null") && !mainJson.getString("movieUrl").trim().matches("")) {
                            contentDetailsOutput.setMovieUrl(mainJson.getString("movieUrl"));

                        } else {
                            contentDetailsOutput.setMovieUrl("");

                        }

//                        if ((mainJson.has("banner")) && mainJson.getString("banner").trim() != null && !mainJson.getString("banner").trim().isEmpty() && !mainJson.getString("banner").trim().equals("null") && !mainJson.getString("banner").trim().matches("")) {
//                            banner =  mainJson.getString("banner");
//                            Log.v("Nihar_multi","============================Enter"+mainJson.getString("banner"));
//
//                        } else {
//                            contentDetailsOutput.setBanner("");
//
//                        }

                        Log.v("android_poster", "=======================banner" + "called" + mainJson.getString("banner"));

                       /* if ((mainJson.has("poster")) && mainJson.getString("poster").trim() != null && !mainJson.getString("poster").trim().isEmpty() && !mainJson.getString("poster").trim().equals("null") && !mainJson.getString("poster").trim().matches("")) {
                            Poster = mainJson.getString("poster");
                        } else {
                            contentDetailsOutput.setPoster("");

                        }
                        Log.v("android_poster","=======================poster"+"called"+Poster);*/

                        if ((mainJson.has("isFreeContent")) && mainJson.getString("isFreeContent").trim() != null && !mainJson.getString("isFreeContent").trim().isEmpty() && !mainJson.getString("isFreeContent").trim().equals("null") && !mainJson.getString("isFreeContent").trim().matches("")) {
                            contentDetailsOutput.setIsFreeContent(mainJson.getString("isFreeContent"));
                        } else {
                            contentDetailsOutput.setIsFreeContent(mainJson.getString("isFreeContent"));

                        }
                        if ((mainJson.has("release_date")) && mainJson.getString("release_date").trim() != null && !mainJson.getString("release_date").trim().isEmpty() && !mainJson.getString("release_date").trim().equals("null") && !mainJson.getString("release_date").trim().matches("")) {
                            contentDetailsOutput.setReleaseDate(mainJson.getString("release_date"));
                        } else {
                            contentDetailsOutput.setReleaseDate(mainJson.getString("isFreeContent"));

                        }
                        if ((mainJson.has("is_ppv")) && mainJson.getString("is_ppv").trim() != null && !mainJson.getString("is_ppv").trim().isEmpty() && !mainJson.getString("is_ppv").trim().equals("null") && !mainJson.getString("is_ppv").trim().matches("")) {
                            contentDetailsOutput.setIsPpv(Integer.parseInt(mainJson.getString("is_ppv")));
                        } else {
                            contentDetailsOutput.setIsPpv(0);

                        }
                        if ((mainJson.has("is_converted")) && mainJson.getString("is_converted").trim() != null && !mainJson.getString("is_converted").trim().isEmpty() && !mainJson.getString("is_converted").trim().equals("null") && !mainJson.getString("is_converted").trim().matches("")) {
                            contentDetailsOutput.setIsConverted(Integer.parseInt(mainJson.getString("is_converted")));
                        } else {
                            contentDetailsOutput.setIsConverted(0);

                        }
                        if ((mainJson.has("is_advance")) && mainJson.getString("is_advance").trim() != null && !mainJson.getString("is_advance").trim().isEmpty() && !mainJson.getString("is_advance").trim().equals("null") && !mainJson.getString("is_advance").trim().matches("")) {
                            contentDetailsOutput.setIsApv(Integer.parseInt(mainJson.getString("is_advance")));
                        } else {
                            contentDetailsOutput.setIsApv(0);

                        }

                    }
                } else {

                    responseStr = "0";
                    status = 0;
                    message = "Error";
                }
            } catch (final JSONException e1) {

                responseStr = "0";
                status = 0;
                message = "Error";

            } catch (Exception e) {

                responseStr = "0";
                status = 0;
                message = "Error";
            }
            return null;


        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {


                if ((Util.getTextofLanguage(getActivity(), Util.HAS_FAVORITE, Util.DEFAULT_HAS_FAVORITE)
                        .trim()).equals("1")) {
                    if (isFavorite == 1) {

                        favourite.setImageResource(R.drawable.favorite_red);
                    } else {
                        favourite.setImageResource(R.drawable.favorite_unselect);

                    }
                }
            }catch (NullPointerException e){}
        }
    }

    private class AsynFavoriteAdd extends AsyncTask<String, Void, Void> {

        String contName;
        JSONObject myJson = null;
        int status;
        ProgressBarHandler pDialog;
        String contMessage;
        String responseStr;
        String sucessMsg;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressBarHandler(getActivity());
            pDialog.show();

        }

        @Override
        protected Void doInBackground(String... strings) {

            String urlRouteList = Util.rootUrl().trim() + Util.AddtoFavlist.trim();

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("user_id", prefs.getString("useridPref", null));
                if (content_types_id.equals("5")) {
                    httppost.addHeader("movie_uniq_id", movieUniqueId);
                    httppost.addHeader("is_episode", "0");
                    Log.v("Nihar_feb", "single," + movieUniqueId);
                } else {
                    httppost.addHeader("movie_uniq_id", movieuniqueid);
                    httppost.addHeader("is_episode", "1");
                    Log.v("Nihar_feb", "single," + movieUniqueIdmulti);
                }


                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());

                    Log.v("Nihar_feb", "add to fav response=" + responseStr);
                    Toast.makeText(getActivity().getApplicationContext(), "" + responseStr, Toast.LENGTH_SHORT).show();

                } catch (org.apache.http.conn.ConnectTimeoutException e) {

                }
            } catch (Exception e) {

            }

            if (responseStr != null) {
                try {
                    myJson = new JSONObject(responseStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                status = Integer.parseInt(myJson.optString("code"));
                sucessMsg = myJson.optString("msg");


//                statusmsg = myJson.optString("status");


            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            if (pDialog != null && pDialog.isShowing()) {
                pDialog.hide();
            }
            try {
                Toast.makeText(getActivity().getApplicationContext(), sucessMsg, Toast.LENGTH_SHORT).show();
            }catch (Exception e){

            }
            isFavorite = 1;
            favourite.setImageResource(R.drawable.favorite_red);


        }
    }    ////////Asyn deleteFav

    private class AsynFavoriteDelete extends AsyncTask<String, Void, Void> {

        String contName;
        JSONObject myJson = null;
        int status;
        ProgressBarHandler pDialog;

        String contMessage;
        String responseStr;
        private String sucessMsg;

        @Override
        protected Void doInBackground(String... strings) {
            String urlRouteList = Util.rootUrl().trim() + Util.DeleteFavList.trim();

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("user_id", prefs.getString("useridPref", null));
                if (content_types_id.equals("5")) {
                    httppost.addHeader("movie_uniq_id", movieUniqueId);
                    httppost.addHeader("content_type", "0");
                    Log.v("Nihar_sdk","called 5");
                } else {
                    httppost.addHeader("movie_uniq_id", movieuniqueid);
                    httppost.addHeader("content_type", "0");
                    Log.v("Nihar_sdk","called 6");
                }

                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());

Log.v("Nihar_sdk",""+responseStr+"   "+userId+"  "+movieUniqueIdmulti);
                } catch (org.apache.http.conn.ConnectTimeoutException e) {

                }
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
//                statusmsg = myJson.optString("status");


            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {


            if (pDialog != null && pDialog.isShowing()) {
                pDialog.hide();
            }
            try {
                if ((Util.getTextofLanguage(getActivity().getApplicationContext(), Util.HAS_FAVORITE, Util.DEFAULT_HAS_FAVORITE)
                        .trim()).equals("1")) {
                    favourite.setImageResource(R.drawable.favorite_unselect);
                }
                isFavorite = 0;

                Toast.makeText(getActivity().getApplicationContext(), "" + sucessMsg, Toast.LENGTH_SHORT).show();
                //  adapter.removeItem(position);
            }catch (Exception e){

            }


        }

        @Override
        protected void onPreExecute() {

            pDialog = new ProgressBarHandler(getActivity());
            pDialog.show();

        }
    }

    private class AsyncDeletePlaylistContent extends AsyncTask<String, Void, Void> {
        JSONObject myJson = null;
        int status;
        ProgressBarHandler pDialog;
        String responseStr;
        String sucessMsg;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressBarHandler(getActivity());
            pDialog.show();


        }

        @Override
        protected Void doInBackground(String... strings) {

            String urlRouteList = Util.rootUrl().trim() + Util.DeleteContent.trim();

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("user_id", prefs.getString("useridPref", null));
                httppost.addHeader("playlist_id", PlayListId);
                httppost.addHeader("content_id", movie_stream_id_playlist);
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());
                    Log.v("Nihar_flow2", responseStr + "     " + playlist_id + "movie_stream_id   " + prefs.getString("useridPref", null) + " " + MovieStramId_del_cont);
                } catch (org.apache.http.conn.ConnectTimeoutException e) {
                }
            } catch (Exception e) {
            }
            if (responseStr != null) {
                try {
                    myJson = new JSONObject(responseStr);
                    sucessMsg = myJson.optString("msg");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
//            Toast.makeText(getActivity(), ""+sucessMsg, Toast.LENGTH_SHORT).show();

            if (pDialog != null && pDialog.isShowing()) {
                pDialog.hide();
            }
            AsynAudioUserPlayListDetail asynAudioUserPlayListDetail = new AsynAudioUserPlayListDetail();
            asynAudioUserPlayListDetail.execute();
        }
    }

    private class AsyncAddToNewPlaylist extends AsyncTask<String, Void, Void> {
        JSONObject myJson = null;
        int status;

        String responseStr;
        String sucessMsg;
        ProgressBarHandler progressBarHandler = new ProgressBarHandler(getActivity());
        ProgressDialog progressdialog;

        @Override
        protected void onPreExecute() {
            progressBarHandler.show();
           /* progressdialog = new ProgressDialog(getActivity());
            progressdialog.setMessage("Please Wait....");
            progressdialog.show();
            progressdialog.setCancelable(false);*/
        }

        @Override
        protected Void doInBackground(String... strings) {

            String urlRouteList = Util.rootUrl().trim() + Util.AddToPlaylist.trim();

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("user_id", prefs.getString("useridPref", null));
                httppost.addHeader("playlistname", PlayListName);
                httppost.addHeader("is_episode", "0");
                httppost.addHeader("is_content", "1");

                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());


                } catch (org.apache.http.conn.ConnectTimeoutException e) {
                }
            } catch (Exception e) {
            }
            if (responseStr != null) {
                try {
                    myJson = new JSONObject(responseStr);
                    sucessMsg = myJson.optString("msg");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
//            progressdialog.hide();
            progressBarHandler.hide();
            Toast.makeText(getActivity(), "" + sucessMsg, Toast.LENGTH_SHORT).show();
        }
    }


    private class AsyncAddToPlaylist extends AsyncTask<String, Void, Void> {


        JSONObject myJson = null;
        int status;
        ProgressBarHandler progressBarHandler = new ProgressBarHandler(getActivity());
        String responseStr;
        String sucessMsg;
        ProgressDialog progressdialog;

        @Override
        protected void onPreExecute() {
            progressBarHandler.show();
            Log.v("Nihar_flow", "called add new" + "onpre");
         /*   progressdialog = new ProgressDialog(getActivity());
            progressdialog.setMessage("Please Wait....");
            progressdialog.show();
            progressdialog.setCancelable(false);*/
        }

        @Override
        protected Void doInBackground(String... strings) {
            Log.v("Nihar_flow", "called add new");
            String urlRouteList = Util.rootUrl().trim() + Util.AddToPlaylist.trim();

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("user_id", prefs.getString("useridPref", null));
                httppost.addHeader("playlist_id", play_list_id);
                httppost.addHeader("content_id", movie_stream_id);
                Log.v("Nihar_flow", movie_stream_id + "" + play_list_id);
                httppost.addHeader("is_episode", isEpisode);
                httppost.addHeader("is_content", "1");
                Log.v("Nihar_flow", "called add new" + play_list_id + movie_stream_id + isEpisode);
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());
                    Log.v("Nihar_flow", responseStr);

                } catch (org.apache.http.conn.ConnectTimeoutException e) {
                }
            } catch (Exception e) {
                Log.v("Nihar_flow", e.toString());
            }
            if (responseStr != null) {
                try {
                    myJson = new JSONObject(responseStr);
                    sucessMsg = myJson.optString("msg");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
//            progressdialog.hide();
            progressBarHandler.hide();
            Toast.makeText(getActivity(), "" + sucessMsg, Toast.LENGTH_SHORT).show();
        }
    }

    private class AsynShowPlaylist extends AsyncTask<String, Void, Void> {
        JSONObject myJson = null;
        ProgressBarHandler pDialog;
        String responseStr;
        String sucessMsg;
        String playlist_name;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressBarHandler(getActivity());
            pDialog.show();
//            List_wait_progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected Void doInBackground(String... strings) {

            String urlRouteList = Util.rootUrl().trim() + Util.AllUserPlaylist.trim();

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("user_id", prefs.getString("useridPref", null));
                Log.v("Nihar_flow", prefs.getString("useridPref", null));
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());
                    Log.v("Nihar_flow", responseStr);
                } catch (org.apache.http.conn.ConnectTimeoutException e) {
                }
            } catch (Exception e) {
            }
            if (responseStr != null) {
                try {
                    myJson = new JSONObject(responseStr);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    JSONObject jsonObject = myJson.getJSONObject("msg");
                    JSONArray jsonArray = jsonObject.optJSONArray("userplaylist");
                    int lengthJsonArr = jsonArray.length();
                    customDatas.clear();
                    for (int i = 0; i < lengthJsonArr; i++) {
                        JSONObject jsonChildNode;
                        try {
                            jsonChildNode = jsonArray.getJSONObject(i);
                            if ((jsonChildNode.has("list_name")) && jsonChildNode.getString("list_name").trim() != null && !jsonChildNode.getString("list_name").trim().isEmpty() && !jsonChildNode.getString("list_name").trim().equals("null") && !jsonChildNode.getString("list_name").trim().matches("")) {
                                playlist_name = jsonChildNode.getString("list_name");
                            }
                            if ((jsonChildNode.has("list_id")) && jsonChildNode.getString("list_id").trim() != null && !jsonChildNode.getString("list_id").trim().isEmpty() && !jsonChildNode.getString("list_id").trim().equals("null") && !jsonChildNode.getString("list_id").trim().matches("")) {
                                play_list_id = jsonChildNode.getString("list_id");
                                Log.v("Nihar_flow", playlist_id);
                            }
                        } catch (Exception e) {
                        }
                        customDatas.add(new ListModel(playlist_name, play_list_id));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
                sucessMsg = myJson.optString("msg");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
//            List_wait_progressBar.setVisibility(View.GONE);
            if ( pDialog != null && pDialog.isShowing() ) {
                pDialog.hide();
            }
            listadapter = new ListAdapterItem(getActivity(), R.layout.list_view_row_item, customDatas);
            listViewItems.setAdapter(listadapter);

        }
    }

    private class AsynAudioUserPlayListDetail extends AsyncTask<String, Void, Void> {
        JSONObject myJson = null;
        String responseStr;
        String sucessMsg;
        String playlist_name;
        private String playlist_poster;
        int count;
        private String title;

        @Override
        protected void onPreExecute() {
//            pDialog.show();

            try{
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.hide();
                }else {
                    if (pDialog != null){
                        pDialog.show();
                    }
                }
            }catch (Exception e){
              /*  videoPDialog = new ProgressBarHandler(getActivity());
                videoPDialog.show();*/
            }


        }

        @Override
        protected Void doInBackground(String... strings) {

            String urlRouteList = Util.rootUrl().trim() + Util.AudioUserPlayListDetail.trim();

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("user_id", prefs.getString("useridPref", null));
                httppost.addHeader("list_id", PlayListId);
                Log.v("Nihar", "Playlist details user id" + PlayListId);
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());
                    Log.v("Nihar_flow", "Playlist details user id" + responseStr);

                } catch (org.apache.http.conn.ConnectTimeoutException e) {
                }
            } catch (Exception e) {
            }
            if (responseStr != null) {
                try {
                    myJson = new JSONObject(responseStr);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    JSONObject jsonObject = myJson.getJSONObject("data");


                    customDatas.clear();
                    try {

                        if ((jsonObject.has("poster_playlist")) && jsonObject.getString("poster_playlist").trim() != null && !jsonObject.getString("poster_playlist").trim().isEmpty() && !jsonObject.getString("poster_playlist").trim().equals("null") && !jsonObject.getString("poster_playlist").trim().matches("")) {
                            playlist_poster = jsonObject.getString("poster_playlist");
                        }
                        if ((jsonObject.has("list_name")) && jsonObject.getString("list_name").trim() != null && !jsonObject.getString("list_name").trim().isEmpty() && !jsonObject.getString("list_name").trim().equals("null") && !jsonObject.getString("list_name").trim().matches("")) {
                            playlist_name = jsonObject.getString("list_name");
                        }
                        if ((jsonObject.has("list_id")) && jsonObject.getString("list_id").trim() != null && !jsonObject.getString("list_id").trim().isEmpty() && !jsonObject.getString("list_id").trim().equals("null") && !jsonObject.getString("list_id").trim().matches("")) {
                            playlist_id = jsonObject.getString("list_id");
                        }
                        if ((jsonObject.has("counts")) && jsonObject.getString("counts").trim() != null && !jsonObject.getString("counts").trim().isEmpty() && !jsonObject.getString("counts").trim().equals("null") && !jsonObject.getString("counts").trim().matches("")) {
                            count = Integer.parseInt(jsonObject.getString("counts"));
                        }


                        JSONArray jsonListArray = jsonObject.optJSONArray("lists");
                        ItemListDetails = new ArrayList<>();
                        int jsonListArrayLength = jsonListArray.length();
                        for (int j = 0; j < jsonListArrayLength; j++) {
                            episode_details_output_model = new Episode_Details_output();
                            JSONObject jsonChildNode;
                            try {
                                jsonChildNode = jsonListArray.optJSONObject(j);

                                if ((jsonChildNode.has("title")) && jsonChildNode.getString("title").trim() != null && !jsonChildNode.getString("title").trim().isEmpty() && !jsonChildNode.getString("title").trim().equals("null") && !jsonChildNode.getString("title").trim().matches("")) {
                                    episode_details_output_model.setEpisode_title(jsonChildNode.getString("title"));
                                    episode_details_output_model.setName(jsonChildNode.getString("title"));
                                }
                                if ((jsonChildNode.has("cast")) && jsonChildNode.getString("cast").trim() != null && !jsonChildNode.getString("cast").trim().isEmpty() && !jsonChildNode.getString("cast").trim().equals("null") && !jsonChildNode.getString("cast").trim().matches("")) {
                                    MutiArtist = jsonChildNode.getString("cast");
                                }
                                if ((jsonChildNode.has("url")) && jsonChildNode.getString("url").trim() != null && !jsonChildNode.getString("url").trim().isEmpty() && !jsonChildNode.getString("url").trim().equals("null") && !jsonChildNode.getString("url").trim().matches("")) {
                                    episode_details_output_model.setVideo_url(jsonChildNode.getString("url"));
                                }
                                if ((jsonChildNode.has("audio_poster")) && jsonChildNode.getString("audio_poster").trim() != null && !jsonChildNode.getString("audio_poster").trim().isEmpty() && !jsonChildNode.getString("audio_poster").trim().equals("null") && !jsonChildNode.getString("audio_poster").trim().matches("")) {
                                    episode_details_output_model.setPoster_url(jsonChildNode.getString("audio_poster"));
                                }
                                if ((jsonChildNode.has("movie_id")) && jsonChildNode.getString("movie_id").trim() != null && !jsonChildNode.getString("movie_id").trim().isEmpty() && !jsonChildNode.getString("movie_id").trim().equals("null") && !jsonChildNode.getString("movie_id").trim().matches("")) {
                                    episode_details_output_model.setId(jsonChildNode.getString("movie_id"));
                                }
                                if ((jsonChildNode.has("movie_stream_id")) && jsonChildNode.getString("movie_stream_id").trim() != null && !jsonChildNode.getString("movie_stream_id").trim().isEmpty() && !jsonChildNode.getString("movie_stream_id").trim().equals("null") && !jsonChildNode.getString("movie_stream_id").trim().matches("")) {
                                    MovieStramId_del_cont = jsonChildNode.getString("movie_stream_id");
                                    episode_details_output_model.setMuvi_uniq_id(jsonChildNode.getString("movie_stream_id"));
                                }

                                ItemListDetails.add(episode_details_output_model);

                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.v("Nihar_flow", "Exception=-=====" + e.toString());

                            }
                        }
                    } catch (Exception e) {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
                sucessMsg = myJson.optString("msg");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
//            List_wait_progressBar.setVisibility(View.GONE);
            favourite.setVisibility(View.GONE);
            play_all.setVisibility(View.VISIBLE);
            Log.v("Nihar_flow", "playlist_id" + playlist_id + " " + MovieStramId_del_cont);

            if (ItemListDetails.size() == 1 || ItemListDetails.size() == 0) {
                SongCount.setText(ItemListDetails.size() + " " + "Track");
            } else {
                SongCount.setText(ItemListDetails.size() + " " + "Tracks");
            }
            albumName_multipart.setText(PlayListName);
            if (playlist_poster != null && !playlist_poster.equals("")) {
                Picasso.with(getActivity())
                        .load(ImageUrl)
                        .error(R.drawable.no_image)
                        .into(banner_image);
            }
            pDialog.hide();

            multiAdapter = new MultiDataAdaptor(getActivity(), ItemListDetails, MultiPartFragment.this, MutiArtist, "PlayListData");
            my_recycler_view.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            my_recycler_view.setAdapter(multiAdapter);
            multiAdapter.notifyDataSetChanged();

        }
    }


    //Load Video Details Like VideoUrl,Release Date,Details,BannerUrl,rating,popularity etc.

    private class AsynLoadContentDetails extends AsyncTask<Void, Void, Void> {
        String responseStr, loggedInStr;
        int status;
        String movieNameStr;
        String movieTrailerUrlStr;
        String movieThirdPartyUrl;
        String videoduration, movieIdStr;
        String movieTypeStr;
        String movieStreamUniqueId;
        int contentTypesId;

        @Override
        protected void onPreExecute() {
            if (pDialog == null && !pDialog.isShowing()) {
                pDialog.show();
            }

//            progress_dialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(Util.rootUrl().trim() + Util.detailsUrl.trim());
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("permalink", desired_string);
                httppost.addHeader("user_id", userId);
                Log.v("Nihar", "Content details user id " + userId);
                // Execute HTTP Post Request
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());

                } catch (org.apache.http.conn.ConnectTimeoutException e) {

                } catch (IOException e) {

                    e.printStackTrace();
                }

                JSONObject myJson = null;
                if (responseStr != null) {
                    myJson = new JSONObject(responseStr);
                    Log.v("Nihar", responseStr);
                    status = Integer.parseInt(myJson.optString("code"));
                }

                if (status > 0) {

                    if (status == 200) {

                        JSONObject mainJson = myJson.getJSONObject("movie");
                        contentDetailsOutput = new ContentDetailsOutput();
                        if ((mainJson.has("name")) && mainJson.getString("name").trim() != null && !mainJson.getString("name").trim().isEmpty() && !mainJson.getString("name").trim().equals("null") && !mainJson.getString("name").trim().matches("")) {
                            movieNameStr = mainJson.getString("name");
                            contentDetailsOutput.setName(mainJson.getString("name"));
                        } /*else {

                            movieNameStr = Util.getTextofLanguage(getActivity(), Util.NO_DATA, Util.DEFAULT_NO_DATA);

                        }*/
                        if ((mainJson.has("movieUrl")) && mainJson.getString("movieUrl").trim() != null && !mainJson.getString("movieUrl").trim().isEmpty() && !mainJson.getString("movieUrl").trim().equals("null") && !mainJson.getString("movieUrl").trim().matches("")) {

                            contentDetailsOutput.setMovieUrl(mainJson.getString("movieUrl"));
                        } else {
                            contentDetailsOutput.setMovieUrl("");

                        }
                        if ((mainJson.has("id")) && mainJson.getString("id").trim() != null && !mainJson.getString("id").trim().isEmpty() && !mainJson.getString("id").trim().equals("null") && !mainJson.getString("id").trim().matches("")) {
                            movieIdStr = mainJson.getString("id");

                        }

                        if ((mainJson.has("trailerThirdpartyUrl")) && mainJson.getString("trailerThirdpartyUrl").trim() != null && !mainJson.getString("trailerThirdpartyUrl").trim().isEmpty() && !mainJson.getString("trailerThirdpartyUrl").trim().equals("null") && !mainJson.getString("trailerThirdpartyUrl").trim().matches("")) {
                            movieTrailerUrlStr = mainJson.getString("trailerThirdpartyUrl");
                        } else {

                            if ((mainJson.has("trailerUrl")) && mainJson.getString("trailerUrl").trim() != null && !mainJson.getString("trailerUrl").trim().isEmpty() && !mainJson.getString("trailerUrl").trim().equals("null") && !mainJson.getString("trailerUrl").trim().matches("")) {
                                movieTrailerUrlStr = mainJson.getString("trailerUrl");

                            } /*else {
                                movieTrailerUrlStr = Util.getTextofLanguage(getActivity(), Util.NO_DATA, Util.DEFAULT_NO_DATA);
                            }*/
                        }

                        if ((mainJson.has("thirdparty_url")) && mainJson.getString("thirdparty_url").trim() != null && !mainJson.getString("thirdparty_url").trim().isEmpty() && !mainJson.getString("thirdparty_url").trim().equals("null") && !mainJson.getString("thirdparty_url").trim().matches("")) {
                            movieThirdPartyUrl = mainJson.getString("thirdparty_url");

                        } /*else {
                            movieThirdPartyUrl = Util.getTextofLanguage(getActivity(), Util.NO_DATA, Util.DEFAULT_NO_DATA);

                        }*/
                        if ((mainJson.has("video_duration")) && mainJson.getString("video_duration").trim() != null && !mainJson.getString("video_duration").trim().isEmpty() && !mainJson.getString("video_duration").trim().equals("null") && !mainJson.getString("video_duration").trim().matches("")) {
                            videoduration = mainJson.getString("video_duration");


                        } /*else {
                            videoduration = Util.getTextofLanguage(getActivity(), Util.NO_DATA, Util.DEFAULT_NO_DATA);

                        }
*/
                        if ((mainJson.has("genre")) && mainJson.getString("genre").trim() != null && !mainJson.getString("genre").trim().isEmpty() && !mainJson.getString("genre").trim().equals("null") && !mainJson.getString("genre").trim().matches("")) {
                            movieTypeStr = mainJson.getString("genre");

                        }/* else {
                            movieTypeStr = Util.getTextofLanguage(getActivity(), Util.NO_DATA, Util.DEFAULT_NO_DATA);

                        }*/


                        if ((mainJson.has("movie_stream_uniq_id")) && mainJson.getString("movie_stream_uniq_id").trim() != null && !mainJson.getString("movie_stream_uniq_id").trim().isEmpty() && !mainJson.getString("movie_stream_uniq_id").trim().equals("null") && !mainJson.getString("movie_stream_uniq_id").trim().matches("")) {
                            movieStreamUniqueId = mainJson.getString("movie_stream_uniq_id");
                            contentDetailsOutput.setMovieStreamUniqId(mainJson.getString("movie_stream_uniq_id"));
                        } /*else {
                            movieStreamUniqueId = Util.getTextofLanguage(getActivity(), Util.NO_DATA, Util.DEFAULT_NO_DATA);

                        }
*/
                        if ((mainJson.has("muvi_uniq_id")) && mainJson.getString("muvi_uniq_id").trim() != null && !mainJson.getString("muvi_uniq_id").trim().isEmpty() && !mainJson.getString("muvi_uniq_id").trim().equals("null") && !mainJson.getString("muvi_uniq_id").trim().matches("")) {
                            movieuniqueid = mainJson.getString("muvi_uniq_id");
                            contentDetailsOutput.setMuviUniqId(mainJson.getString("muvi_uniq_id"));
                        } /*else {
                            movieUniqueId = Util.getTextofLanguage(getActivity(), Util.NO_DATA, Util.DEFAULT_NO_DATA);

                        }*/
                        if ((mainJson.has("movie_stream_id")) && mainJson.getString("movie_stream_id").trim() != null && !mainJson.getString("movie_stream_id").trim().isEmpty() && !mainJson.getString("movie_stream_id").trim().equals("null") && !mainJson.getString("movie_stream_id").trim().matches("")) {

                            contentDetailsOutput.setMovie_stream_id(mainJson.getString("movie_stream_id"));
                        } /*else {
                            movieUniqueId = Util.getTextofLanguage(getActivity(), Util.NO_DATA, Util.DEFAULT_NO_DATA);

                        }*/

                        if ((mainJson.has("is_episode")) && mainJson.getString("is_episode").trim() != null && !mainJson.getString("is_episode").trim().isEmpty() && !mainJson.getString("is_episode").trim().equals("null") && !mainJson.getString("is_episode").trim().matches("")) {
                            isEpisode = mainJson.getString("is_episode");
                        }
                        if ((mainJson.has("is_favorite")) && mainJson.getString("is_favorite").trim() != null && !mainJson.getString("is_favorite").trim().isEmpty() && !mainJson.getString("is_favorite").trim().equals("null") && !mainJson.getString("is_episode").trim().matches("")) {
                            isFavorite = Integer.parseInt(mainJson.getString("is_favorite"));
                            contentDetailsOutput.setIsFavorite(Integer.parseInt(mainJson.getString("is_favorite")));

                        }

                        if ((mainJson.has("banner")) && mainJson.getString("banner").trim() != null && !mainJson.getString("banner").trim().isEmpty() && !mainJson.getString("banner").trim().equals("null") && !mainJson.getString("banner").trim().matches("")) {
                            banner = mainJson.getString("banner");
                            contentDetailsOutput.setBanner(mainJson.getString("banner"));
                        }

                        if ((mainJson.has("poster")) && mainJson.getString("poster").trim() != null && !mainJson.getString("poster").trim().isEmpty() && !mainJson.getString("poster").trim().equals("null") && !mainJson.getString("poster").trim().matches("")) {
                            poster = mainJson.getString("poster");
                            contentDetailsOutput.setPoster(mainJson.getString("poster"));
                        }
                        if ((mainJson.has("content_types_id")) && mainJson.getString("content_types_id").trim() != null && !mainJson.getString("content_types_id").trim().isEmpty() && !mainJson.getString("content_types_id").trim().equals("null") && !mainJson.getString("content_types_id").trim().matches("")) {
                            contentTypesId = Integer.parseInt(mainJson.getString("content_types_id"));
                        } else {
                            contentTypesId = 0;
                        }
                        if (mainJson.has("cast_detail")) {
                            JSONArray castArray = mainJson.getJSONArray("cast_detail");
                            Log.v("SUBHA", "cast_detail" + castArray.length());


                            StringBuilder sb = new StringBuilder();
                            if (castArray.length() > 0) {
                                for (int i = 0; i < castArray.length(); i++) {
                                    JSONObject jsonChildNode = castArray.getJSONObject(i);
                                    if (jsonChildNode.has("celeb_name")) {
                                        sb.append(jsonChildNode.getString("celeb_name").toString());
                                        if (i != castArray.length() - 1) {
                                            sb.append(",");
                                        }

                                    }

                                }
                                contentDetailsOutput.setArtist(sb.toString());
                                Log.v("SUBHA", "SB" + sb.toString());

                            } else {

                            }
                        } else {


                        }
                    }
                }
            } catch (final JSONException e1) {
            }
            return null;

        }

        protected void onPostExecute(Void result) {

            if (pDialog.isShowing()) {
                pDialog.hide();
            }

//            progress_dialog = Util.LoadingCircularDialog(getActivity());
//            progress_dialog.hide();
            try {


                if ((Util.getTextofLanguage(getActivity(), Util.HAS_FAVORITE, Util.DEFAULT_HAS_FAVORITE)
                        .trim()).equals("1")) {

                    favourite.setVisibility(View.VISIBLE);
                } else {
                    favourite.setVisibility(View.GONE);
                }
            }catch (Exception e){

            }
            play_all.setVisibility(View.VISIBLE);

            String bannerimage = contentDetailsOutput.getBanner();
            String posterimage = contentDetailsOutput.getPoster();
            String containt_name = contentDetailsOutput.getName();
            String url = contentDetailsOutput.getMovieUrl();
            isFavorite = contentDetailsOutput.getIsFavorite();

            movieUniqueId = contentDetailsOutput.getMuviUniqId();

            Log.v("Nihar_isFavorite", "" + isFavorite + "   " + userId);

                if (isFavorite == 1) {
                    favourite.setImageResource(R.drawable.favorite_red);
                } else {

                    favourite.setImageResource(R.drawable.favorite_unselect);

                }

            String artist_name = contentDetailsOutput.getArtist();
            albumName_multipart.setText(containt_name);
            try {


                if (bannerimage != null) {
                    Picasso.with(getActivity())
                            .load(bannerimage)
                            .error(R.drawable.no_image)
                            .into(banner_image);

                } else {
                    Picasso.with(getActivity())
                            .load(posterimage)
                            .error(R.drawable.no_image)
                            .into(banner_image);

                }
            } catch (Exception e) {

            } finally {
                Picasso.with(getActivity())
                        .load(posterimage)
                        .error(R.drawable.no_image)
                        .into(banner_image);

            }

            ArrayList<ContentDetailsOutput> contentDetailsOutputArrayList = new ArrayList<ContentDetailsOutput>();
            SongCount.setText("1" + " " + "Track");
            contentDetailsOutputArrayList.add(contentDetailsOutput);
            Intent CONTENT_OUTPUT = new Intent("CONTENT_OUTPUT1");
            CONTENT_OUTPUT.putExtra("Content", contentDetailsOutputArrayList);
            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(CONTENT_OUTPUT);


            if (getActivity() != null) {
                adapter = new SingleDataAdaptor(getActivity(), contentDetailsOutput, MultiPartFragment.this);
                my_recycler_view.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                my_recycler_view.setAdapter(adapter);
            }

        }


    }

    /***************chromecast**********************/
    private class MySessionManagerListener implements SessionManagerListener<CastSession> {

        @Override
        public void onSessionEnded(CastSession session, int error) {
            if (session == mCastSession) {
                mCastSession = null;
            }
            //invalidateOptionsMenu();
        }

        @Override
        public void onSessionResumed(CastSession session, boolean wasSuspended) {
            mCastSession = session;
            //invalidateOptionsMenu();
        }

        @Override
        public void onSessionStarted(CastSession session, String sessionId) {
            mCastSession = session;
            //invalidateOptionsMenu();
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
                        mVideoView.pause();
                        loadRemoteMedia(mSeekbar.getProgress(), true);
                        return;
                    } else {

                        mPlaybackState = PlaybackState.IDLE;
                        updatePlaybackLocation(PlaybackLocation.REMOTE);
                    }
                }
                //   updatePlayButton(mPlaybackState);
                //invalidateOptionsMenu();
            }

            private void onApplicationDisconnected() {
               /* if (mCastSession != null && mCastSession.isConnected()) {
                    watchMovieButton.setText(getResources().getString(R.string.movie_details_watch_video_button_title));
                }*/
                //watchMovieButton.setText(getResources().getString(R.string.movie_details_watch_video_button_title));
                updatePlaybackLocation(PlaybackLocation.LOCAL);
                mPlaybackState = PlaybackState.IDLE;
                mLocation = PlaybackLocation.LOCAL;
                updatePlayButton(mPlaybackState);

                //invalidateOptionsMenu();
            }
        };
    }

    private void updatePlayButton(PlaybackState state) {
           /* boolean isConnected = (mCastSession != null)
                    && (mCastSession.isConnected() || mCastSession.isConnecting());*/
        //mControllers.setVisibility(isConnected ? View.GONE : View.VISIBLE);

        switch (state) {
            case PLAYING:

                //mLoading.setVisibility(View.INVISIBLE);
                // mPlayPause.setVisibility(View.VISIBLE);
                //mPlayPause.setImageDrawable(getResources().getDrawable(R.drawable.ic_av_pause_dark));

                break;
            case IDLE:
                if (mLocation == PlaybackLocation.LOCAL) {
                   /* if (isAPV == 1) {
                        watchMovieButton.setText(getResources().getString(R.string.advance_purchase_str));
                    }else {
                        watchMovieButton.setText(getResources().getString(R.string.movie_details_watch_video_button_title));
                    }*/

                } else {
                   /* if (isAPV == 1) {
                        watchMovieButton.setText(getResources().getString(R.string.advance_purchase_str));
                    }else {
                        watchMovieButton.setText(getResources().getString(R.string.movie_details_cast_now_button_title));
                    }*/
                }
                //mCon
                // trollers.setVisibility(View.GONE);
                // mCoverArt.setVisibility(View.VISIBLE);
                // mVideoView.setVisibility(View.INVISIBLE);
                break;
            case PAUSED:
                //mLoading.setVisibility(View.INVISIBLE);
              /*  mPlayPause.setVisibility(View.VISIBLE);
                mPlayPause.setImageDrawable(getResources().getDrawable(R.drawable.ic_av_play_dark));*/

                break;
            case BUFFERING:
                //mPlayPause.setVisibility(View.INVISIBLE);
                //mLoading.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    private void loadRemoteMedia(int position, boolean autoPlay) {
        if (mCastSession == null) {
            return;
        }
        final RemoteMediaClient remoteMediaClient = mCastSession.getRemoteMediaClient();
        if (remoteMediaClient == null) {
            return;
        }
        remoteMediaClient.addListener(new RemoteMediaClient.Listener() {
            @Override
            public void onStatusUpdated() {
                Intent intent = new Intent(getActivity(), ExpandedControlsActivity.class);
                startActivity(intent);
                remoteMediaClient.removeListener(this);
            }

            @Override
            public void onMetadataUpdated() {
            }

            @Override
            public void onQueueStatusUpdated() {
            }

            @Override
            public void onPreloadStatusUpdated() {
            }

            @Override
            public void onSendingRemoteMediaRequest() {
            }
        });
        remoteMediaClient.load(mSelectedMedia, autoPlay, position);
    }

    private void updatePlaybackLocation(PlaybackLocation location) {
        mLocation = location;
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

    private void stopControllersTimer() {
        if (mControllersTimer != null) {
            mControllersTimer.cancel();
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
    }

    // should be called from the main thread
    private void updateControllersVisibility(boolean show) {
        if (show) {
            //getSupportActionBar().show();
            mControllers.setVisibility(View.VISIBLE);
        } else {
        }
    }

    private class HideControllersTask extends TimerTask {

        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    // updateControllersVisibility(false);
                    mControllersVisible = false;
                }
            });

        }
    }

    private void togglePlayback() {
        //stopControllersTimer();
        Log.v("pratikc", "toggle plauback");
        switch (mPlaybackState) {
            case PAUSED:
                switch (mLocation) {
                    case LOCAL:


                        break;

                    case REMOTE:

                        loadRemoteMedia(0, true);

                        break;
                    default:
                        break;
                }
                break;

            case PLAYING:
                mPlaybackState = PlaybackState.PAUSED;

                //  mVideoView.pause();
                break;

            case IDLE:
                switch (mLocation) {
                    case LOCAL:

                        break;
                    case REMOTE:
                        // mPlayCircle.setVisibility(View.VISIBLE);
                        if (mCastSession != null && mCastSession.isConnected()) {
                            // watchMovieButton.setText(getResources().getString(R.string.movie_details_cast_now_button_title));
                            loadRemoteMedia(0, true);


                            // Utils.showQueuePopup(this, mPlayCircle, mSelectedMedia);
                        } else {
                        }
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
        updatePlayButton(mPlaybackState);
    }


}
