package com.release.aeonaudio.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.release.aeonaudio.R;
import com.release.aeonaudio.activity.ListFragment;
import com.release.aeonaudio.activity.MainActivity;
import com.release.aeonaudio.activity.MultiPartFragment;
import com.release.aeonaudio.model.QueueModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.media.CamcorderProfile.get;

/**
 * Created by Muvi on 6/23/2017.
 */

public class QueueAdaptor extends RecyclerView.Adapter<QueueAdaptor.ItemHolder> {


    private Context mContext;
    Fragment fragment = null;
    Fragment fragment2 = null;
    private ListFragment listFragment;
    private MultiPartFragment multiPartFragment;
    boolean itemClicked = true;
    private ArrayList<QueueModel> list = new ArrayList<>();
    MainActivity mainActivity ;
//    DetailsActivity detailsActivity;
    String path;



    public QueueAdaptor(Context context, ArrayList<QueueModel> list, MainActivity mainActivity,String path) {
        this.list = list;
        this.mContext = context;
        this.mainActivity= mainActivity;
        this.path = path;
        Log.v("NiharQueue", "Queue Adaptor called" );
    }
/*
    public QueueAdaptor(Context context, ArrayList<QueueModel> list,DetailsActivity detailsActivity,String path) {
        this.list = list;
        this.mContext = context;
        this.detailsActivity= detailsActivity;
        this.path = path;

    }
*/

    public ArrayList<QueueModel> getQueueArray(){
        return this.list;
    }


    @Override
    public ItemHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listdata_item, null);
        ItemHolder mh = new ItemHolder(v);

        return mh;
    }

    @Override
    public void onBindViewHolder(final ItemHolder holder,  int i) {
        final  QueueModel itemsList =  list.get(i);
        holder.list_songName.setTextColor(mContext.getResources().getColor(R.color.button_background));
        holder.list_artistName.setTextColor(mContext.getResources().getColor(R.color.button_background));

        holder.list_songName.setText(itemsList.getSongName());
        holder.list_artistName.setText(itemsList.getArtist_Name());
        Log.v("NiharQueue", "Queue Size" + itemsList.getArtist_Name());
        holder.listsong_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (path.equals("mainactivity")){
                    mainActivity.PlayQueue(itemsList,itemClicked);
                }

            }
        });

        holder.list_option_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent playerData = new Intent("OPTION_MENU");
                playerData.putExtra("option", itemsList.getSongName());
                (mContext).sendBroadcast(playerData);
            }
        });


//        if (currentWord.hasImage()) {

//        Glide.with(mContext).load(link).asBitmap().centerCrop().into(new BitmapImageViewTarget(holder.list_albumart) {
//            @Override
//            protected void setResource(Bitmap resource) {
//                RoundedBitmapDrawable circularBitmapDrawable =
//                        RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
//                circularBitmapDrawable.setCircular(true);
//                holder.list_albumart.setImageDrawable(circularBitmapDrawable);
//            }
//        });
        String link = itemsList.getAlbumArt();
        Picasso.with(mContext)
                .load(link)
                .into(holder.list_albumart);


//        holder.itemImage.setImageResource();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    ///swipe left and right


//    public void removeItem(int position) {
//        itemsList.remove(position);
//        notifyItemRemoved(position);
//        notifyItemRangeChanged(position, itemsList.size());
//    }




    public class ItemHolder extends RecyclerView.ViewHolder {
        private TextView list_songName;
        private TextView list_artistName;
        private ImageView list_albumart,list_option_menu;
        private RelativeLayout list_view;
        private RelativeLayout listsong_layout;

        public ItemHolder(View view) {
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



