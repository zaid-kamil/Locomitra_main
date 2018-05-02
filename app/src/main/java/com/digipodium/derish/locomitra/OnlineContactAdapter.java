package com.digipodium.derish.locomitra;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class OnlineContactAdapter extends RecyclerView.Adapter<OnlineContactAdapter.MyHoder> {

    List<FireModel> list;
    Context context;


    public OnlineContactAdapter(List<FireModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyHoder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.call_list_view, parent, false);
        return new MyHoder(view);
    }

    public void filterList(ArrayList<FireModel> newlist) {
        list = newlist;
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(@NonNull final MyHoder holder, int position) {
        final FireModel mylist = list.get(position);
        holder.uname.setText(mylist.getName());
        holder.phone.setText(mylist.getPhone());
        holder.location.setText(mylist.getLocation());
        holder.address.setText(mylist.getAddress());
        holder.msg.setTag(mylist);
        holder.msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FireModel obj = (FireModel) holder.msg.getTag();
                String phone = obj.getPhone();

                Intent smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phone));
                smsIntent.putExtra("sms_body", "Please give me the information about the '"+mylist.getAddress()+"' area.\nCall: "+mylist.getPhone());
                context.startActivity(smsIntent);
            }
        });


        holder.call.setTag(mylist);
        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FireModel obj = (FireModel) holder.msg.getTag();
                String phone = obj.getPhone();
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + phone));
                context.startActivity(callIntent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyHoder extends RecyclerView.ViewHolder {
        TextView uname, phone,location,address;
        ImageView call;
        ImageView msg;

        public MyHoder(View itemView) {
            super(itemView);
            uname = itemView.findViewById(R.id.contact_name);
            phone = itemView.findViewById(R.id.contact_number);
address=itemView.findViewById(R.id.address2);
            msg = itemView.findViewById(R.id.etMsg);
            call = itemView.findViewById(R.id.etcall);
            location=itemView.findViewById(R.id.address);


        }


    }
}

