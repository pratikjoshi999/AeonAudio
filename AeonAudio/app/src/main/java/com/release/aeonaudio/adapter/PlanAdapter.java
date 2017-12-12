package com.release.aeonaudio.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.release.aeonaudio.R;
import com.release.aeonaudio.activity.SubscriptionActivity;
import com.release.aeonaudio.model.PlanModel;

import java.util.ArrayList;

/**
 * Created by Muvi on 9/6/2016.
 */
public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.MyViewHolder> {
    public   int layoutPosition = -1;
    boolean value=false;
    Context context;
    private ArrayList<PlanModel> moviesList;
    Boolean isClicked=false;
    SubscriptionActivity subscriptionActivity;
    private int selectedItem;
    boolean ft;
    public PlanAdapter(Context context, ArrayList<PlanModel> moviesList,boolean ft) {
        this.moviesList = moviesList;
        this.context = context;
        this.subscriptionActivity= (SubscriptionActivity) context;
        selectedItem = 0;
        this.ft =ft;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.plan_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder,  final int position) {
         final PlanModel movie = moviesList.get(position);
      /*  if (position == 0){
            holder.select_planMark_relative.setVisibility(View.VISIBLE);
           // subscriptionActivity.getPlanDetails(moviesList.get(position),position);
           // holder.planName.setText(moviesList.get(position).getPlanNameStr());

        }*/
      if (ft == true){
          subscriptionActivity.getPlanDetails(moviesList.get(position),position);
          holder.planName.setText(movie.getPlanNameStr());
          layoutPosition=0;
          ft = false;
          isClicked = true;
      }
      selectedItem = position;

        if(isClicked && layoutPosition == position){
            holder.select_planMark_relative.setVisibility(View.VISIBLE);
            isClicked=false;
            layoutPosition = -1;
        }
        else{
            holder.select_planMark_relative.setVisibility(View.GONE);
        }

        holder.planName.setText(movie.getPlanNameStr());


        holder.relativeplannamelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subscriptionActivity.getPlanDetails(moviesList.get(position),position);
                isClicked=true;
                notifyItemRangeChanged(0,moviesList.size());
                layoutPosition=position;
            }
        });


    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }




    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView planName, purchaseValue, freeTrial,subcriptionmonth,planPurchaseCurrenyTextView;
        RelativeLayout relativeplannamelayout,select_planMark_relative;



        public MyViewHolder(View view) {
            super(view);
            planName = (TextView) view.findViewById(R.id.planNameTextView);
            relativeplannamelayout = (RelativeLayout) view.findViewById(R.id.relativeplannamelayout);
            select_planMark_relative = (RelativeLayout) view.findViewById(R.id.select_planMark_relative);

        }
    }

}
