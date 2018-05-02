package com.digipodium.derish.locomitra.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by My on 4/12/2018.
 */

public class InviteTask implements Parcelable {

    public String contact_name;
    public String contact_number;
    public long invite_code;
    public String requestPersonId;
    public boolean msg;

    public InviteTask() {
    }

    public InviteTask(String contact_name, String contact_number, long invite_code, String requestPersonId, boolean msg) {
        this.contact_name = contact_name;
        this.contact_number = contact_number;
        this.invite_code = invite_code;
        this.requestPersonId = requestPersonId;
        this.msg = msg;
    }

    protected InviteTask(Parcel in) {
        contact_name = in.readString();
        contact_number = in.readString();
        invite_code = in.readLong();
        requestPersonId = in.readString();
        msg = in.readByte() != 0;
    }

    public static final Creator<InviteTask> CREATOR = new Creator<InviteTask>() {
        @Override
        public InviteTask createFromParcel(Parcel in) {
            return new InviteTask(in);
        }

        @Override
        public InviteTask[] newArray(int size) {
            return new InviteTask[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(contact_name);
        dest.writeString(contact_number);
        dest.writeLong(invite_code);
        dest.writeString(requestPersonId);
        dest.writeByte((byte) (msg ? 1 : 0));
    }
}
