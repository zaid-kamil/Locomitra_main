package com.digipodium.derish.locomitra.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by My on 4/1/2018.
 */

public class Profile implements Parcelable {

    public String uname;

    public String phone;
    public String email;
    public String gender;
    public String imageUrl;
    public String lat;
    public String lng;
    public String state;
    public String city;
    public String areacode;
    public String area;
    public boolean isCompleted = false;

    public Profile() {
    }


    protected Profile(Parcel in) {
        uname = in.readString();
        phone = in.readString();
        email = in.readString();
        gender = in.readString();
        imageUrl = in.readString();
        lat = in.readString();
        lng = in.readString();
        state = in.readString();
        city = in.readString();
        areacode = in.readString();
        area = in.readString();
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

    public Profile(String uname, String phone, String email, String gender, String imageUrl, String lat, String lng, String state, String city, String areacode, String area, boolean isCompleted) {
        this.uname = uname;
        this.phone = phone;
        this.email = email;
        this.gender = gender;
        this.imageUrl = imageUrl;
        this.lat = lat;
        this.lng = lng;
        this.state = state;
        this.city = city;
        this.areacode = areacode;
        this.area = area;
        this.isCompleted = isCompleted;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uname);
        dest.writeString(phone);
        dest.writeString(email);
        dest.writeString(gender);
        dest.writeString(imageUrl);
        dest.writeString(lat);
        dest.writeString(lng);
        dest.writeString(state);
        dest.writeString(city);
        dest.writeString(areacode);
        dest.writeString(area);
        dest.writeByte((byte) (isCompleted ? 1 : 0));
    }
}
