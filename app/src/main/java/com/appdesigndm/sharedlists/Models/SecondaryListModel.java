package com.appdesigndm.sharedlists.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class SecondaryListModel implements Parcelable {

    private String mItem;

    public SecondaryListModel() {
    }

    public SecondaryListModel(String item) {
        mItem = item;
    }

    protected SecondaryListModel(Parcel in) {
        mItem = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mItem);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getItem() {
        return mItem;
    }

    public void setItem(String item) {
        mItem = item;
    }

    @Override
    public String toString() {
        return "SecondaryListModel{" +
                "mItem='" + mItem + '\'' +
                '}';
    }

    public static final Creator<SecondaryListModel> CREATOR = new Creator<SecondaryListModel>() {
        @Override
        public SecondaryListModel createFromParcel(Parcel in) {
            return new SecondaryListModel(in);
        }

        @Override
        public SecondaryListModel[] newArray(int size) {
            return new SecondaryListModel[size];
        }
    };
}
