package com.digipodium.derish.locomitra.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by My on 4/12/2018.
 */

public class Invite_Task implements Parcelable {

    public String contact_name;
    public String contact_number;
public long invite_code;
    public String user;
    public boolean msg;

    public Invite_Task() {
    }

    public Invite_Task(String contact_name, String contact_number,long invite_code, String user, boolean msg) {
        this.contact_name = contact_name;
        this.contact_number = contact_number;
        this.invite_code=invite_code;
        this.msg=msg;
        this.user=user;


    }

    protected Invite_Task(Parcel in) {
        this.contact_name=in.readString();
        this.contact_number=in.readString();
        this.invite_code=in.readLong();

    }

    public static final Creator<Invite_Task> CREATOR = new Creator<Invite_Task>() {
        @Override
        public Invite_Task createFromParcel(Parcel in) {
            return new Invite_Task(in);
        }

        @Override
        public Invite_Task[] newArray(int size) {
            return new Invite_Task[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.contact_name);
        dest.writeString(this.contact_number);
        dest.writeLong(this.invite_code);

    }
}
