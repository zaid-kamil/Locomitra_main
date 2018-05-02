package com.digipodium.derish.locomitra;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by My on 4/23/2018.
 */

class FireModel implements Parcelable {

    public String uname;
    public String phone;
    public String city;
    public String state;
    public String location;
    public String area;
   public String address;
  public String lat;
    public String lng;

    protected FireModel(Parcel in) {
        uname = in.readString();
        phone = in.readString();
        city = in.readString();
        state = in.readString();
        location = in.readString();
        area = in.readString();
        address = in.readString();
        lat = in.readString();
        lng = in.readString();
    }

    public static final Creator<FireModel> CREATOR = new Creator<FireModel>() {
        @Override
        public FireModel createFromParcel(Parcel in) {
            return new FireModel(in);
        }

        @Override
        public FireModel[] newArray(int size) {
            return new FireModel[size];
        }
    };

    public FireModel() {

    }

    public String getName() {
        return uname;
    }

    public void setName(String uname) {
        this.uname = uname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public String getState() {
        return state;
    }

    public String getArea() {
        return area;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLngg(String lng) {
        this.lng = lng;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uname);
        dest.writeString(phone);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeString(location);
        dest.writeString(area);
        dest.writeString(address);
        dest.writeString(lat);
        dest.writeString(lng);
    }
}
