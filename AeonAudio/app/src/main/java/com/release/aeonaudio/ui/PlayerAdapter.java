package com.release.aeonaudio.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
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
import com.release.aeonaudio.model.QueueModel;
import com.release.aeonaudio.utils.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.release.aeonaudio.R.id.downloadLayout;
import static com.release.aeonaudio.utils.Util.QUEUE_ARRAY;

/**
 * Created by Muvi on 9/1/2017.
 */
public  class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.ViewHolder> {
    Context mContext;
    static int prevPosition =  -1;
    static int adaptorPosition = -1;
    MediaHelper mediaHelper = new MainActivity();
    public PlayerAdapter(Context context){
        this.mContext=context;
    }
    @Override
    public PlayerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_list_data_item, parent, false);
        return new PlayerAdapter.ViewHolder(view);
    }
    public ArrayList<QueueModel> getQueueArray(){
        return QUEUE_ARRAY;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.list_songName.setText(QUEUE_ARRAY.get(position).getSongName());
        holder.list_artistName.setText(QUEUE_ARRAY.get(position).getArtist_Name());

        if(adaptorPosition!=-1 && adaptorPosition==position)
        {
            Log.v("nihar_adaptorPosition","adaptorPosition  :"+adaptorPosition);
            holder.list_songName.setTextColor(mContext.getResources().getColor(R.color.button_background));
            holder.list_artistName.setTextColor(mContext.getResources().getColor(R.color.button_background));
         /*   Drawable drawable = getDrawableByState(mContext.getApplicationContext());
            holder.mImageView.setImageDrawable(drawable);*/

            adaptorPosition = -1;
        }
        else{
            if(prevPosition != -1 && position == prevPosition )
            {
                //change color like
                holder.list_songName.setTextColor(mContext.getResources().getColor(R.color.button_background));
                holder.list_artistName.setTextColor(mContext.getResources().getColor(R.color.button_background));
          /*      Drawable drawable = getDrawableByState(mContext.getApplicationContext());
                holder.mImageView.setImageDrawable(drawable);*/

            }

            else
            {
                //revert back to regular color
                holder.list_songName.setTextColor(Color.WHITE);
                holder.list_artistName.setTextColor(Color.WHITE);
//                holder.mImageView.setImageDrawable(null);


            }
        }

        String link = QUEUE_ARRAY.get(position).getAlbumArt();
        Picasso.with(mContext)
                .load(link)
                .into(holder.list_albumart);
        holder.listsong_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaHelper.Transporter(QUEUE_ARRAY.get(position).getSongUrl(),QUEUE_ARRAY.get(position).getAlbumArt(),
                       QUEUE_ARRAY.get(position).getSongName(),mContext,QUEUE_ARRAY.get(position).getArtist_Name());
            }
        });
        holder.list_option_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaHelper.BottomOptionMenu(mContext,QUEUE_ARRAY.get(position).getSongName(),QUEUE_ARRAY.get(position).getArtist_Name(),position);
            }
        });

    }
    @Override
    public int getItemCount() {
        return QUEUE_ARRAY.size();
    }


    public static void changePlayedSong(boolean click,int AdaptorPosition,Context context) {

        for (int i = 0 ;i<Util.multiholder.size();i++){
            if (i==AdaptorPosition){
                Util.multiholder.get(i).list_songName.setTextColor(context.getResources().getColor(R.color.button_background));
                Util.multiholder.get(i).list_artistName.setTextColor(context.getResources().getColor(R.color.button_background));

                prevPosition = AdaptorPosition;

            }
            else {
                Util.multiholder.get(i).list_songName.setTextColor(Color.WHITE);
                Util.multiholder.get(i).list_artistName.setTextColor(Color.WHITE);
            }

        }

    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView list_songName;
        private TextView list_artistName;
        private ImageView list_albumart,list_option_menu;
        private RelativeLayout list_view,downloadLayout;
        private RelativeLayout listsong_layout;
        public ViewHolder(View view) {
            super(view);

            this.listsong_layout = (RelativeLayout) view.findViewById(R.id.list_view);
            this.list_songName = (TextView) view.findViewById(R.id.list_songName);
            Typeface list_songName_tf = Typeface.createFromAsset(mContext.getAssets(), mContext.getString(R.string.regular_fonts));
            list_songName.setTypeface(list_songName_tf);


            this.list_artistName = (TextView) view.findViewById(R.id.list_artistName);
            Typeface list_artistName_tf = Typeface.createFromAsset(mContext.getAssets(), mContext.getString(R.string.regular_fonts));
            list_artistName.setTypeface(list_artistName_tf);

            this.list_albumart = (ImageView) view.findViewById(R.id.list_albumart);


            this.list_option_menu = (ImageView) view.findViewById(R.id.list_option_menu);
            list_option_menu.setVisibility(View.GONE);

            this.downloadLayout = (RelativeLayout) view.findViewById(R.id.downloadLayout);
            downloadLayout.setVisibility(View.GONE);
        }
    }
}
