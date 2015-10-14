package com.anilugale.testapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;


import com.anilugale.testapplication.R;
import com.anilugale.testapplication.model.Contact;

import java.util.List;

/**
 Created by anil on 14/10/2015.
 */
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    private int lastPosition = -1;
    List<Contact> dataContact;
    Context context;

    public ContactAdapter(List<Contact> dataContact, Context context) {
        this.dataContact = dataContact;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder( LayoutInflater.from(context).inflate(R.layout.contact_item,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.name.setText(dataContact.get(position).getName());
        holder.number.setText(dataContact.get(position).getMobile());
        holder.contact_holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataContact.get(position).getMobile() != null) {
                    dialNumber(dataContact.get(position).getMobile());
                } else
                    Toast.makeText(context, "Number Not Found", Toast.LENGTH_SHORT).show();
            }
        });
        setAnimation( holder.contact_holder,position);
    }

    @Override
    public int getItemCount() {
        return dataContact.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView name,number;
        CardView contact_holder;

        public ViewHolder(View itemView) {
            super(itemView);

            name=(TextView)itemView.findViewById(R.id.name);
            number=(TextView)itemView.findViewById(R.id.number);
            contact_holder=(CardView) itemView.findViewById(R.id.contact_holder);
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

}
