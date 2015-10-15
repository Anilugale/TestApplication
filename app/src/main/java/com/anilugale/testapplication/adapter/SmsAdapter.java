package com.anilugale.testapplication.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.anilugale.testapplication.R;
import com.anilugale.testapplication.model.SMS;

import java.util.List;

/**
 Created by anil on 14/10/2015.
 */
public class SmsAdapter extends RecyclerView.Adapter<SmsAdapter.ViewHolder> {

    private int lastPosition = -1;
    List<SMS> dataSMS;
    Context context;

    public SmsAdapter(List<SMS> dataContact, Context context) {
        this.dataSMS = dataContact;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder( LayoutInflater.from(context).inflate(R.layout.sms_item,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.address.setText(dataSMS.get(position).getAddress());
        holder.body.setText(dataSMS.get(position).getBody());
        setAnimation( holder.contact_holder,position);
    }

    @Override
    public int getItemCount() {
        return dataSMS.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView body,address;
        CardView contact_holder;

        public ViewHolder(View itemView) {
            super(itemView);
            address=(TextView)itemView.findViewById(R.id.address);
            body=(TextView)itemView.findViewById(R.id.body);
            contact_holder=(CardView) itemView.findViewById(R.id.contact_holder);
        }


    }

  private void setAnimation(View viewToAnimate, int position)
    {
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

}
