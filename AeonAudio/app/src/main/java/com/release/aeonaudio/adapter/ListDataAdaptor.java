package com.release.aeonaudio.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.release.aeonaudio.R;

import com.release.aeonaudio.activity.MainActivity;
import com.release.aeonaudio.activity.MultiPartFragment;
import com.release.aeonaudio.activity.PlaylistDetails;
import com.release.aeonaudio.model.GridItem;
import com.release.aeonaudio.utils.Util;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

import static com.release.aeonaudio.R.layout.item;

/**
 * Created by Muvi on 6/8/2017.
 */

public class ListDataAdaptor extends RecyclerView.Adapter<ListDataAdaptor.ItemHolder> {
    private Context mContext;
    Fragment fragment ;
    MultiPartFragment fragment2;
    ArrayList<GridItem> itemData = new ArrayList<>();


    public ListDataAdaptor(Context context, ArrayList<GridItem> itemData) {
        this.itemData = itemData;
        this.mContext = context;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listdata_item, null);
        ItemHolder mh = new ItemHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(final ItemHolder holder, int i) {
        holder.position=i;
        final GridItem item = itemData.get(i);
        holder.list_songName.setText(item.getTitle());
        holder.list_artistName.setText(item.getArtist());
        String link = item.getImage();
        Picasso.with(mContext)
                .load(link)
                .into(holder.list_albumart);



    }

    @Override
    public int getItemCount() {
        return itemData.size();
    }

    public void removeItem(int position) {
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, itemData.size());
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        private TextView list_songName,list_artistName;
        private ImageView list_albumart,list_option_menu;
        private RelativeLayout downloadLayout,list_view;
        protected int position;
        public ItemHolder(View view) {
            super(view);
            this.list_songName = (TextView) view.findViewById(R.id.list_songName);
            this.list_artistName = (TextView) view.findViewById(R.id.list_artistName);
            this.list_albumart = (ImageView) view.findViewById(R.id.list_albumart);
            this.list_option_menu = (ImageView) view.findViewById(R.id.list_option_menu);
            this.downloadLayout = (RelativeLayout) view.findViewById(R.id.downloadLayout);
            this.list_view = (RelativeLayout) view.findViewById(R.id.list_view);

            if ((Util.getTextofLanguage(mContext, Util.HASDOWNLOAD, Util.DEFAULT_HASDOWNLOAD)
                    .trim()).equals("1")) {
                downloadLayout.setVisibility(View.GONE);
            } else {
                downloadLayout.setVisibility(View.GONE);
            }

            list_option_menu.setVisibility(View.GONE);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragment2 = new MultiPartFragment();
                    if (fragment == null) {
                        Bundle arguments = new Bundle();
                        arguments.putString("PERMALINK", itemData.get(position).getPermalink());
                        arguments.putString("CONTENT_TYPE", itemData.get(position).getVideoTypeId());
                        fragment2.setArguments(arguments);
                        FragmentManager fragmentManager = ((AppCompatActivity) mContext).getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.main_content, fragment2, "favorite");
                        fragmentTransaction.addToBackStack("favorite");
                        fragmentTransaction.commit();

                    }
                }
            });
        }
    }
}
