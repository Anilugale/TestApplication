package com.anilugale.testapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.CallLog;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anilugale.testapplication.R;
import com.anilugale.testapplication.model.Call;

import java.util.Date;
import java.util.List;

/**
 Created by anil on 14/10/2015.
 */
public class CallAdapter extends RecyclerView.Adapter<CallAdapter.ViewHolder> {

    private int lastPosition = -1;
    List<Call> dataCall;
    Context context;

    public CallAdapter(List<Call> dataContact, Context context) {
        this.dataCall = dataContact;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder( LayoutInflater.from(context).inflate(R.layout.call_item,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String date =getDaysAgo(new Date(Long.parseLong(dataCall.get(position).getDate())));


        holder.date.setText(date);
        holder.number.setText(dataCall.get(position).getNumber());
        holder.duration.setText(timeConversion(Integer.parseInt(dataCall.get(position).getDuration())));
        holder.contact_holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataCall.get(position).getNumber() != null) {
                    dialNumber(dataCall.get(position).getNumber());
                } else
                    Toast.makeText(context, "Number Not Found", Toast.LENGTH_SHORT).show();
            }
        });
        setType(dataCall.get(position).getType(),holder.type);

        setAnimation( holder.contact_holder,position);
    }

    private void setType(String type, ImageView type1) {

        int dircode = Integer.parseInt(type);
        switch (dircode) {
            case CallLog.Calls.OUTGOING_TYPE:
                type1.setImageDrawable(ContextCompat.getDrawable(context,android.R.drawable.sym_call_outgoing));
                break;
            case CallLog.Calls.INCOMING_TYPE:
                type1.setImageDrawable(ContextCompat.getDrawable(context,android.R.drawable.sym_call_incoming));
                break;

            case CallLog.Calls.MISSED_TYPE:
                type1.setImageDrawable(ContextCompat.getDrawable(context,android.R.drawable.sym_call_missed));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return dataCall.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView date,number,duration;
        CardView contact_holder;
        ImageView type;

        public ViewHolder(View itemView) {

            super(itemView);
            date=(TextView)itemView.findViewById(R.id.date);
            number=(TextView)itemView.findViewById(R.id.number);
            duration=(TextView)itemView.findViewById(R.id.duration);
            contact_holder=(CardView) itemView.findViewById(R.id.contact_holder);
            type=(ImageView) itemView.findViewById(R.id.type);
        }
    }

    void dialNumber(String number)
    {
        Intent dial = new Intent();
        dial.setAction("android.intent.action.DIAL");
        dial.setData(Uri.parse("tel:" + number));
        context.startActivity(dial);
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

    private static String timeConversion(int totalSeconds) {

        final int MINUTES_IN_AN_HOUR = 60;
        final int SECONDS_IN_A_MINUTE = 60;

        int seconds = totalSeconds % SECONDS_IN_A_MINUTE;
        int totalMinutes = totalSeconds / SECONDS_IN_A_MINUTE;
        int minutes = totalMinutes % MINUTES_IN_AN_HOUR;
        int hours = totalMinutes / MINUTES_IN_AN_HOUR;

        return hours + ":" + minutes + ":" + seconds;
    }

    private String getDaysAgo(Date date){
        long days = (new Date().getTime() - date.getTime()) / 86400000;

        if(days == 0) return "Today";
        else if(days == 1) return "Yesterday";
        else return days + " days ago";
    }
}
