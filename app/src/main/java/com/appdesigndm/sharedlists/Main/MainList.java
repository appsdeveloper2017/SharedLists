package com.appdesigndm.sharedlists.Main;

import android.os.Parcel;
import android.os.Parcelable;

public class MainList implements Parcelable{

    private String title;

    public MainList() {
    }

    protected MainList(Parcel in) {
        title = in.readString();
    }

    public static final Creator<MainList> CREATOR = new Creator<MainList>() {
        @Override
        public MainList createFromParcel(Parcel in) {
            return new MainList(in);
        }

        @Override
        public MainList[] newArray(int size) {
            return new MainList[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "MainList{" +
                "title='" + title + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
    }
}
