package com.release.aeonaudio.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.release.aeonaudio.R;
import com.release.aeonaudio.activity.MultiPartFragment;
import com.release.aeonaudio.model.ListModel;

import java.util.ArrayList;

/**
 * Created by Muvi on 8/23/2017.
 */

public class ListAdapterItem extends ArrayAdapter<ListModel> {
    private  Context context;
    private  ArrayList<ListModel> customDatas;

    public ListAdapterItem(Context context, int list_view_row_item, ArrayList<ListModel> customDatas) {
        super(context, list_view_row_item, customDatas);
        this.context = context;
        this.customDatas = customDatas;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_view_row_item, parent, false);
        TextView playlist_row_id = (TextView) rowView.findViewById(R.id.playlist_row_id);
        playlist_row_id.setText(customDatas.get(position).getPlayListName());
        return rowView;
    }
}
