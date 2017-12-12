package com.release.aeonaudio.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.home.apisdk.apiModel.HomePageSectionModel;
import com.release.aeonaudio.R;

import com.release.aeonaudio.activity.MultiPartFragment;
import com.release.aeonaudio.activity.PlayListFragment;
import com.release.aeonaudio.activity.PlaylistDetails;
import com.release.aeonaudio.model.GridItem;
import com.release.aeonaudio.model.PlayListModel;
import com.release.aeonaudio.model.PlayListMultiModel;
import com.release.aeonaudio.utils.Util;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

import static android.R.attr.fragment;
import static com.release.aeonaudio.R.id.list_option_menu;
import static com.release.aeonaudio.R.id.list_songName;
import static com.release.aeonaudio.utils.Util.PLAYLIST_ITEM_ARRAY;
import static java.lang.System.load;

/**
 * Created by Muvi on 6/8/2017.
 */

public class PlayListAdapter extends RecyclerView.Adapter<PlayListAdapter.ItemHolder> {
    private Context mContext;
    private ArrayList<PlayListModel> itemData = new ArrayList<>();
    int position;
    PlayListFragment playListFragment;
    Fragment fragment;
    private PlaylistDetails fragment2;
    ItemHolder holder_replace;

    public PlayListAdapter(Context context, ArrayList<PlayListModel> itemList, PlayListFragment playListFragment) {
        this.mContext = context;
        this.itemData = itemList;
        this.playListFragment = playListFragment;

    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.play_list_data_item, null);
        ItemHolder mh = new ItemHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(final ItemHolder holder, int i) {
        holder_replace = holder;
        holder.position = i;
        final PlayListModel item = itemData.get(i);
        if (i == 0) {
            holder.playlist_textview.setVisibility(View.GONE);
        } else {
            holder.playlist_textview.setVisibility(View.GONE);

        }
        if (i == itemData.size() - 1) {

            holder.listDummy.setVisibility(View.VISIBLE);
        }
        holder.list_songName.setText(item.getName());
        String count_str;
        if (Integer.parseInt(item.getCount()) >0){
             count_str = item.getCount()+" Tracks";
        }else{
             count_str = item.getCount()+" Track";
        }

        holder.list_artistName.setText(count_str);
        String link = item.getImageUrl();
        Picasso.with(mContext)
                .load(link)
                .placeholder(R.drawable.audio_circle)
                .error(R.drawable.audio_circle)
                .into(holder.list_albumart);
        holder.delete_playlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentResponse = new Intent("OPTION_RESPONSE");
                intentResponse.putExtra("response", "remove");
                intentResponse.putExtra("playlistid", item.getPlaylist_id());
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intentResponse);
            }
        });

        holder.edit_playlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentResponse = new Intent("OPTION_RESPONSE");
                intentResponse.putExtra("response", "rename");
                intentResponse.putExtra("playlistid", item.getPlaylist_id());
                intentResponse.putExtra("PlaylistName", item.getName());
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intentResponse);
            }
        });
        holder.list_option_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent playerListData = new Intent("OPTION_MENU");
                playerListData.putExtra("sender", "PlayList");

                playerListData.putExtra("option", item.getName());
                playerListData.putExtra("playlist_id", item.getPlaylist_id());
                mContext.sendBroadcast(playerListData);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemData.size();
    }


    public class ItemHolder extends RecyclerView.ViewHolder {
        private TextView list_songName, playlist_tv, listDummy, list_artistName;
        private ImageView list_albumart, list_option_menu,edit_playlist,delete_playlist;
        private LinearLayout playlist_textview;

        protected int position;

        public ItemHolder(View view) {
            super(view);
            this.listDummy = (TextView) view.findViewById(R.id.listDummy);
            this.playlist_tv = (TextView) view.findViewById(R.id.playlist_tv);
            Typeface playlist_tv_tf = Typeface.createFromAsset(mContext.getAssets(), mContext.getString(R.string.regular_fonts));
            playlist_tv.setTypeface(playlist_tv_tf);
            this.list_artistName = (TextView) view.findViewById(R.id.list_artistName);
            Typeface list_artistName_tf = Typeface.createFromAsset(mContext.getAssets(), mContext.getString(R.string.regular_fonts));
            list_artistName.setTypeface(list_artistName_tf);
            this.list_songName = (TextView) view.findViewById(R.id.list_songName);
            Typeface list_songName_tf = Typeface.createFromAsset(mContext.getAssets(), mContext.getString(R.string.regular_fonts));
            list_songName.setTypeface(list_songName_tf);
            this.playlist_textview = (LinearLayout) view.findViewById(R.id.playlist_textview);
            this.list_albumart = (ImageView) view.findViewById(R.id.list_albumart);
            this.list_option_menu = (ImageView) view.findViewById(R.id.list_option_menu);
            this.edit_playlist = (ImageView) view.findViewById(R.id.edit_playlist);
            this.delete_playlist = (ImageView) view.findViewById(R.id.delete_playlist);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragment2 = new PlaylistDetails();
                    if (fragment == null) {
                        Bundle arguments = new Bundle();
                        arguments.putString("PERMALINK", "");
                        arguments.putString("CONTENT_TYPE", "playlist");
                        arguments.putString("PlayListId", itemData.get(position).getPlaylist_id());
                        arguments.putString("ImageUrl", itemData.get(position).getImageUrl());
                        arguments.putString("PlayListName", itemData.get(position).getName());
                        fragment2.setArguments(arguments);
                        FragmentManager fragmentManager = ((AppCompatActivity) mContext).getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.main_content, fragment2, "PlaylistDetails");
                        fragmentTransaction.addToBackStack("PlaylistDetails");
                        fragmentTransaction.commit();

                    }

                }
            });
        }
    }
}
