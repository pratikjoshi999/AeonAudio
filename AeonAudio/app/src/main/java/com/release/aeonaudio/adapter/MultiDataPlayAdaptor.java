package com.release.aeonaudio.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.home.apisdk.apiModel.Episode_Details_output;
import com.release.aeonaudio.R;
import com.release.aeonaudio.activity.ListFragment;
import com.release.aeonaudio.activity.MultiPartFragment;
import com.release.aeonaudio.model.PlayListMultiModel;
import com.release.aeonaudio.utils.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Muvi on 6/23/2017.
 */

public class MultiDataPlayAdaptor extends RecyclerView.Adapter<MultiDataPlayAdaptor.ItemHolder> {

    private Episode_Details_output itemsList;
    private Context mContext;
    Fragment fragment = null;
    Fragment fragment2 = null;
    private ListFragment listFragment;
    private MultiPartFragment multiPartFragment;
    boolean itemClicked = true;
    ArrayList<PlayListMultiModel> ItemListDetails;
    int adaptorPosition = -1;
    int prevPosition =  -1;
    int position;
    ItemHolder myholder;
    String artist_multi_data;

    public MultiDataPlayAdaptor(FragmentActivity activity, ArrayList<PlayListMultiModel> itemListDetails, MultiPartFragment multiPartFragment, String mutiArtist) {
        this.ItemListDetails = itemListDetails;
        this.mContext = activity;
        this.multiPartFragment = multiPartFragment;
        this.artist_multi_data=mutiArtist;

    }



    @Override
    public ItemHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listdata_item, null);
        ItemHolder mh = new ItemHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(final ItemHolder holder,   final int i) {
        final PlayListMultiModel multi_song_list_adaptor =  ItemListDetails.get(i);

        SQLiteDatabase DB = mContext.openOrCreateDatabase(Util.DATABASE_NAME, MODE_PRIVATE, null);

       /* String DEL_QRY = "DELETE FROM " + Util.ADAPTOR_TABLE_NAME +"";
        DB.execSQL(DEL_QRY);

        String INS_QRY = "INSERT INTO " + Util.ADAPTOR_TABLE_NAME + "(ALBUM_URL,PERMALINK,ALBUM_ART,ALBUM_NAME,ALBUM_SONG_NAME)" +
                " VALUES ( '" + multi_song_list_adaptor.getVideo_url() + "','" + multi_song_list_adaptor.getPermalink() + "','" + multi_song_list_adaptor.getPoster_url() + "'," +
                "'" + "" + "','" + multi_song_list_adaptor.getName() + "')";
        DB.execSQL(INS_QRY);*/
       // itemsList = episode_details_output.get(i);

        holder.list_songName.setText(multi_song_list_adaptor.getName());
        holder.artist_multi.setText(artist_multi_data);
        Log.v("Nihar_artist","adaptor"+artist_multi_data);
        if(adaptorPosition!=-1 )
        {
            holder.list_songName.setTextColor(mContext.getResources().getColor(R.color.button_background));
            holder.artist_multi.setTextColor(mContext.getResources().getColor(R.color.button_background));
            adaptorPosition = -1;
        }
        else{
            if(prevPosition != -1 && i == prevPosition )
            {
                //change color like
                holder.list_songName.setTextColor(mContext.getResources().getColor(R.color.button_background));
                holder.artist_multi.setTextColor(mContext.getResources().getColor(R.color.button_background));
            }

            else
            {
                //revert back to regular color
                holder.list_songName.setTextColor(Color.WHITE);
                holder.artist_multi.setTextColor(Color.WHITE);
            }
        }

        holder.list_option_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent playerData = new Intent("OPTION_MENU");
                playerData.putExtra("sender","PlayListData");
                playerData.putExtra("option", multi_song_list_adaptor.getName());
                playerData.putExtra("artist_opt", artist_multi_data);
                playerData.putExtra("position", i);
                playerData.putExtra("playlist_id", multi_song_list_adaptor.getPlaylist_id());
                playerData.putExtra("movie_stream_id", multi_song_list_adaptor.getMovie_unique_id());
                playerData.putExtra("MusicAlbumArt",  multi_song_list_adaptor.getImageUrl());

                Log.v("Nihar_content",multi_song_list_adaptor.getMovie_unique_id()+"    "+multi_song_list_adaptor.getPlaylist_id());
                (mContext).sendBroadcast(playerData);
            }
        });
        holder.listsong_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*multiPartFragment.PlaySongsmulti(ItemListDetails,itemClicked,holder.getAdapterPosition());*/


                prevPosition = i;
                notifyItemRangeChanged(0, ItemListDetails.size());

            }
        });


//        if (currentWord.hasImage()) {
        String link = multi_song_list_adaptor.getImageUrl();
//        Glide.with(mContext).load(link).asBitmap().centerCrop().into(new BitmapImageViewTarget(holder.list_albumart) {
//            @Override
//            protected void setResource(Bitmap resource) {
//                RoundedBitmapDrawable circularBitmapDrawable =
//                        RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
//                circularBitmapDrawable.setCircular(true);
//                holder.list_albumart.setImageDrawable(circularBitmapDrawable);
//            }
//        });
        Picasso.with(mContext)
                .load(link)
                .into(holder.list_albumart);


        if (i == ItemListDetails.size()-1){
         holder.listDummy.setVisibility(View.VISIBLE);
           // holder.dividerView.setVisibility(View.VISIBLE);

        }else{
            holder.listDummy.setVisibility(View.GONE);
          //  holder.dividerView.setVisibility(View.VISIBLE);

        }

//        holder.itemImage.setImageResource();
    }

    @Override
    public int getItemCount() {
        return ItemListDetails.size();
    }

    ///swipe left and right


//    public void removeItem(int position) {
//        itemsList.remove(position);
//        notifyItemRemoved(position);
//        notifyItemRangeChanged(position, itemsList.size());
//    }




    public class ItemHolder extends RecyclerView.ViewHolder {
        private TextView list_songName,listDummy;
        private TextView artist_multi;
        private ImageView list_albumart,list_option_menu;
        private RelativeLayout listsong_layout;
        private View dividerView;


        public ItemHolder(View view) {
            super(view);
            this.listsong_layout = (RelativeLayout) view.findViewById(R.id.listsong_layout);
            this.list_songName = (TextView) view.findViewById(R.id.list_songName);
            this.listDummy = (TextView) view.findViewById(R.id.listDummy);
            Typeface list_songName_tf = Typeface.createFromAsset(mContext.getAssets(), mContext.getString(R.string.regular_fonts));
            list_songName.setTypeface(list_songName_tf);
          this.artist_multi = (TextView) view.findViewById(R.id.list_artistName);
            Typeface artist_multi_tf = Typeface.createFromAsset(mContext.getAssets(), mContext.getString(R.string.regular_fonts));
            artist_multi.setTypeface(artist_multi_tf);
            this.list_albumart = (ImageView) view.findViewById(R.id.list_albumart);
            this.dividerView = (View) view.findViewById(R.id.divider);

            this.list_option_menu = (ImageView) view.findViewById(R.id.list_option_menu);

//            mSlider = (RelativeLayout) view.findViewById(R.id.mSlider);


//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//
//                    fragment2 = new ListFragment();
//                    if (fragment == null){
//                        FragmentManager fragmentManager = ((AppCompatActivity)mContext).getSupportFragmentManager();
//                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                        fragmentTransaction.replace(R.id.home_content,fragment2).addToBackStack("MuliFragment");
//                        fragmentTransaction.commit();
//
//                        // set the toolbar title
////                        getSupportActionBar().setTitle(title);
//                    }
//
//                }
//            });
        }
    }
}
