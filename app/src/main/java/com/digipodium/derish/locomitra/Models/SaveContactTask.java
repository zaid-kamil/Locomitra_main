package com.digipodium.derish.locomitra.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by My on 4/17/2018.
 */

public class SaveContactTask implements Parcelable {
    String name;
    String mail;
    String phone;
    boolean isCmplte=false;

    public SaveContactTask() {
    }

    public SaveContactTask(String name, String mail, String phone, boolean isCmplte) {

        this.name = name;
        this.mail = mail;
        this.phone = phone;
        this.isCmplte = isCmplte;
    }

    protected SaveContactTask(Parcel in) {
        name = in.readString();
        mail = in.readString();
        phone = in.readString();
        isCmplte = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(mail);
        dest.writeString(phone);
        dest.writeByte((byte) (isCmplte ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SaveContactTask> CREATOR = new Creator<SaveContactTask>() {
        @Override
        public SaveContactTask createFromParcel(Parcel in) {
            return new SaveContactTask(in);
        }

        @Override
        public SaveContactTask[] newArray(int size) {
            return new SaveContactTask[size];
        }
    };
}
