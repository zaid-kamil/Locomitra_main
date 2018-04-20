package com.digipodium.derish.locomitra;

/**
 * Created by My on 4/10/2018.
 */

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.digipodium.derish.locomitra.Models.Invite_Task;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.List;

public class AllContactsAdapter extends RecyclerView.Adapter<AllContactsAdapter.ContactViewHolder>{


    public static final int REQUEST_INVITE=23;
    private final Activity activity;
    private List<ContactVO> contactVOList;
    private Context mContext;
    public DatabaseReference taskdb;
    FirebaseDatabase fbase = FirebaseDatabase.getInstance();
    long ran;
    private BroadcastReceiver smsSentReceiver;
    private BroadcastReceiver smsDeliveredReceiver;

    public AllContactsAdapter(List<ContactVO> contactVOList, Activity mContext) {
        this.contactVOList = contactVOList;
        activity = mContext;
        this.mContext = mContext;

    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.single_contact_view, null);
        ContactViewHolder contactViewHolder = new ContactViewHolder(view);
        return contactViewHolder;
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        ContactVO contactVO = contactVOList.get(position);
        holder.tvContactName.setText(contactVO.getContactName());
        holder.tvPhoneNumber.setText(contactVO.getContactNumber());
    }

    @Override
    public int getItemCount() {
        return contactVOList.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {

        ImageView ivContactImage;
        TextView tvContactName;
        TextView tvPhoneNumber;
        TextView inviteButton;

        public ContactViewHolder(final View itemView) {

            super(itemView);
            ivContactImage = itemView.findViewById(R.id.ivContactImage);
            tvContactName = itemView.findViewById(R.id.tvContactName);
            tvPhoneNumber = itemView.findViewById(R.id.tvPhoneNumber);
            inviteButton = itemView.findViewById(R.id.invitebut);


            inviteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  ran=  (new Date()).getTime();
                    final String contact_name=tvContactName.getText().toString();
                    final String contact_number=tvPhoneNumber.getText().toString();
                    Invite_Task obj=new Invite_Task(contact_name,contact_number,ran,"",true);

                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    taskdb = fbase.getReference("Invited_Contacts").child(uid);
                    taskdb.push().setValue(obj).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                            if (task.isSuccessful()) {
                                Snackbar.make(itemView,"Invite Successful",2000).show();

                            } else {
                                Snackbar.make(itemView,"Fail",2000).show();
                            }
                        }
                    });
                   onInviteClicked(ran,contact_number);


                }
            });

        }





    }

    private void onInviteClicked(long ran_no,String no) {

        SmsManager sms=SmsManager.getDefault();
        PendingIntent piSent=PendingIntent.getBroadcast(mContext, 0, new Intent("SMS_SENT"), 0);
        PendingIntent piDelivered=PendingIntent.getBroadcast(mContext, 0, new Intent("SMS_DELIVERED"), 0);
        sms.sendTextMessage(no, null, ""+ran_no, piSent, piDelivered);

    }


}
