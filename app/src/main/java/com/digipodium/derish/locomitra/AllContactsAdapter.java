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

import com.digipodium.derish.locomitra.Models.InviteTask;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AllContactsAdapter extends RecyclerView.Adapter<AllContactsAdapter.ContactViewHolder> {


    public static final int REQUEST_INVITE = 23;
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

    public void filterList(ArrayList<ContactVO> newlist) {
        contactVOList = newlist;
        notifyDataSetChanged();
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
                    ran = (new Date()).getTime();
                    final String contact_name = tvContactName.getText().toString();
                    final String contact_number = tvPhoneNumber.getText().toString();
                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    InviteTask obj = new InviteTask(contact_name, contact_number, ran, uid, true);
                    taskdb = fbase.getReference("Invited_Contacts").child(uid);
                    taskdb.push().setValue(obj).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                            if (task.isSuccessful()) {
                                Snackbar.make(itemView, "Invite Successful", 2000).show();

                            } else {
                                Snackbar.make(itemView, "Fail", 2000).show();
                            }
                        }
                    });
                    onInviteClicked(ran, contact_number);


                }
            });

        }


    }

    private void onInviteClicked(long ran_no, String no) {

        SmsManager sms = SmsManager.getDefault();
        PendingIntent piSent = PendingIntent.getBroadcast(mContext, 0, new Intent("SMS_SENT"), 0);
        PendingIntent piDelivered = PendingIntent.getBroadcast(mContext, 0, new Intent("SMS_DELIVERED"), 0);
        
         // Receiver for Sent SMS.
  registerReceiver(new BroadcastReceiver(){
    @Override
    public void onReceive(Context arg0, Intent arg1) {
      switch (getResultCode())
      {
        case Activity.RESULT_OK:
          Toast.makeText(getBaseContext(), "SMS sent",
            Toast.LENGTH_SHORT).show();
          break;
        case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
          Toast.makeText(getBaseContext(), "Check your SMS quota provided by network",
            Toast.LENGTH_SHORT).show();
          break;
        case SmsManager.RESULT_ERROR_NO_SERVICE:
          Toast.makeText(getBaseContext(), "No service",
            Toast.LENGTH_SHORT).show();
          break;
        case SmsManager.RESULT_ERROR_NULL_PDU:
          Toast.makeText(getBaseContext(), "Null PDU",
            Toast.LENGTH_SHORT).show();
          break;
        case SmsManager.RESULT_ERROR_RADIO_OFF:
          Toast.makeText(getBaseContext(), "Radio off",
            Toast.LENGTH_SHORT).show();
          break;
      }
    }
  }, new IntentFilter(smsSent));
 
  // Receiver for Delivered SMS.
  registerReceiver(new BroadcastReceiver(){
    @Override
    public void onReceive(Context arg0, Intent arg1) {
      switch (getResultCode())
      {
        case Activity.RESULT_OK:
          Toast.makeText(getBaseContext(), "SMS delivered",
            Toast.LENGTH_SHORT).show();
          break;
        case Activity.RESULT_CANCELED:
          Toast.makeText(getBaseContext(), "SMS not delivered",
            Toast.LENGTH_SHORT).show();
          break;
        }
      }
    }, new IntentFilter(smsDelivered));
        sms.sendTextMessage(no, null, "" + ran_no, piSent, piDelivered);

    }


}
