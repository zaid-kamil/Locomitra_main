package com.digipodium.derish.locomitra.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by My on 4/1/2018.
 */

public class Profile implements Parcelable {

    public String uname;
    public String user;
    public String phone;
    public String email;
    public String gender;
    public String imageUrl;
    public boolean isCompleted = false;

    public Profile() {
    }

    public Profile(String uname, String user, String phone, String email, String gender, String imageUrl, boolean isCompleted) {
        this.uname = uname;
        this.user = user;
        this.phone = phone;
        this.email = email;
        this.gender = gender;
        this.imageUrl = imageUrl;
        this.isCompleted = isCompleted;
    }

    protected Profile(Parcel in) {
        uname = in.readString();
        user = in.readString();
        phone = in.readString();
        email = in.readString();
        gender = in.readString();
        imageUrl = in.readString();
        isCompleted = in.readByte() != 0;
    }

    public static final Creator<Profile> CREATOR = new Creator<Profile>() {
        @Override
        public Profile createFromParcel(Parcel in) {
            return new Profile(in);
        }

        @Override
        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uname);
        dest.writeString(user);
        dest.writeString(phone);
        dest.writeString(email);
        dest.writeString(gender);
        dest.writeString(imageUrl);
        dest.writeByte((byte) (isCompleted ? 1 : 0));
    }
}
