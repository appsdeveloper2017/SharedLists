package com.appdesigndm.sharedlists.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class UserModel implements Parcelable {

    private String mUserName;
    private String mUserEmail;
    private ArrayList<MainListsModel> mLists;

    public UserModel() {
        init();
    }

    private void init() {
        mLists = new ArrayList<>();
    }

    protected UserModel(Parcel in) {
        mUserName = in.readString();
        mUserEmail = in.readString();
        mLists = in.createTypedArrayList(MainListsModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mUserName);
        dest.writeString(mUserEmail);
        dest.writeTypedList(mLists);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public String getUserEmail() {
        return mUserEmail;
    }

    public void setUserEmail(String userEmail) {
        mUserEmail = userEmail;
    }

    public ArrayList<MainListsModel> getLists() {
        return mLists;
    }

    public void setLists(ArrayList<MainListsModel> lists) {
        mLists = lists;
    }

    public void addNewList(MainListsModel newList) {
        mLists.add(newList);
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "mUserName='" + mUserName + '\'' +
                ", mUserEmail='" + mUserEmail + '\'' +
                ", mLists=" + mLists +
                '}';
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };
}
