package com.digipodium.derish.locomitra.Models;

import android.os.Parcel;
import android.os.Parcelable;


public class Location_Task implements Parcelable {

   public String lat;
    public String lng;
    String user;
    public boolean msg=false;

    public Location_Task() {
    }

    public Location_Task(String lat, String lng,String user,boolean msg) {
        this.lat = lat;
        this.lng = lng;
        this.msg=msg;
        this.user=user;

    }

    protected Location_Task(Parcel in) {
      lat=in.readString();
      lng=in.readString();
      user=in.readString();


    }

    public static final Creator<Location_Task> CREATOR = new Creator<Location_Task>() {
        @Override
        public Location_Task createFromParcel(Parcel in) {
            return new Location_Task(in);
        }

        @Override
        public Location_Task[] newArray(int size) {
            return new Location_Task[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(lat);
        dest.writeString(lng);
        dest.writeString(user);
    }
}
