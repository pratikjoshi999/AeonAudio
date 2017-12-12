package com.release.aeonaudio.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.release.aeonaudio.R;
import com.release.aeonaudio.model.FilterListModel;
import com.release.aeonaudio.utils.Util;

import java.util.ArrayList;

/**
 * Created by Muvi on 9/6/2016.
 */
public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.MyViewHolder> {

    public static int prevPosition = 0;
    private Context context;

    private ArrayList<FilterListModel> moviesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView filterTextName;
        private ImageView selectButton;
        private RelativeLayout listviewLay1;

        public MyViewHolder(View view) {
            super(view);
            filterTextName = (TextView) view.findViewById(R.id.listTextView);
            selectButton = (ImageView) view.findViewById(R.id.listCheckBox);
            listviewLay1 = (RelativeLayout) view.findViewById(R.id.listviewLay1);

        }
    }


    public FilterAdapter(ArrayList<FilterListModel> moviesList, Context context) {
        this.context = context;
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.filter_row_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        FilterListModel movie = moviesList.get(position);


        SharedPreferences isLoginPref = context.getSharedPreferences(Util.IS_LOGIN_SHARED_PRE, 0); // 0 - for private mode

        String genreString = isLoginPref.getString(Util.GENRE_ARRAY_PREF_KEY, null);
        final String[] genreTempArr = genreString.split(",");

        if (movie.isSelected() == false){


            if (position >= 1 && position <=(genreTempArr.length -5)) {
                holder.selectButton.setImageResource(R.drawable.ic_check_box_default);
            }else{
                holder.selectButton.setImageResource(R.drawable.ic_radio_button_default);

            }
            //holder.snap.setVisibility(View.INVISIBLE);

        }else{
            if (position >= 1 && position <=(genreTempArr.length -5)) {
                holder.selectButton.setImageResource(R.drawable.ic_check_box_selected);
            }else{
                holder.selectButton.setImageResource(R.drawable.ic_radio_button_selected);


            }
            //holder.snap.setVisibility(View.VISIBLE);

        }

        if (position == 0) {
            holder.selectButton.setVisibility(View.GONE);

            holder.listviewLay1.setClickable(false);
            holder.filterTextName.setTextColor(context.getResources().getColor(R.color.castCrewTitleTextColor));
            holder.filterTextName.setTextSize(18);

          //  holder.filterTextName.setTypeface(holder.filterTextName.getTypeface(), Typeface.BOLD);
           // holder.filterTextName.setGravity(Gravity.LEFT);
            holder.filterTextName.setGravity(Gravity.LEFT);
            Typeface filterTextNameTypeface = Typeface.createFromAsset(context.getAssets(), context.getResources().getString(R.string.regular_fonts));
            holder.filterTextName.setTypeface(filterTextNameTypeface);
        }
       else if (position == (genreTempArr.length - 5)){
            holder.selectButton.setVisibility(View.GONE);
            holder.listviewLay1.setClickable(false);
            holder.filterTextName.setTextColor(context.getResources().getColor(R.color.castCrewTitleTextColor));
            holder.filterTextName.setTextSize(18);
            Typeface filterTextNameTypeface = Typeface.createFromAsset(context.getAssets(), context.getResources().getString(R.string.regular_fonts));
            holder.filterTextName.setTypeface(filterTextNameTypeface);
          //  holder.filterTextName.setTypeface(holder.filterTextName.getTypeface(), Typeface.BOLD);
            holder.filterTextName.setGravity(Gravity.LEFT);

        }
        else{
            holder.listviewLay1.setClickable(true);
            holder.filterTextName.setTextColor(context.getResources().getColor(android.R.color.white));
            holder.filterTextName.setTextSize(14);
          //  holder.filterTextName.setTypeface(holder.filterTextName.getTypeface(), Typeface.BOLD);
            Typeface filterTextNameTypeface = Typeface.createFromAsset(context.getAssets(), context.getResources().getString(R.string.regular_fonts));
            holder.filterTextName.setTypeface(filterTextNameTypeface);
            holder.selectButton.setVisibility(View.VISIBLE);

        }

        holder.filterTextName.setText(movie.getTitle());



    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

}
