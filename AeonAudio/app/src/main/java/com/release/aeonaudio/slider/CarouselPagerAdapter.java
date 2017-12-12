package com.release.aeonaudio.slider;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


import com.release.aeonaudio.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Muvi Guest on 8/1/2017.
 */

public class CarouselPagerAdapter extends BaseAdapter {

    Context context;
    String[] banner;
    LayoutInflater layoutInflater;
    public CarouselPagerAdapter(Context context, String[] banner){
        this.context = context;
        this.banner= banner;
    }

    @Override
    public int getCount() {
        return banner.length;
    }

    @Override
    public Object getItem(int i) {
        return banner[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder;
       if(convertView==null){


           LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
           convertView = inflater.inflate(R.layout.item_flow_view, null, false);

           viewHolder = new ViewHolder(convertView);
           convertView.setTag(viewHolder);
       } else {
           viewHolder = (ViewHolder) convertView.getTag();
       }

        Picasso.with(context)
                .load(banner[i])
                .into(viewHolder.itemImage);
        return convertView;
    }

    private static class ViewHolder {

        private ImageView itemImage;

        public ViewHolder(View v) {
            itemImage = (ImageView) v.findViewById(R.id.singleimage);

        }
    }
}
