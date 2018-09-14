package com.appdesigndm.sharedlists.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class MainListsModel implements Parcelable {

    private String mListName;
    private ArrayList<SecondaryListModel> mSecondaryList;

    public MainListsModel() {
        init();
    }

    private void init() {
        mSecondaryList = new ArrayList<>();
    }

    protected MainListsModel(Parcel in) {
        mListName = in.readString();
        mSecondaryList = in.createTypedArrayList(SecondaryListModel.CREATOR);
    }

    public static final Creator<MainListsModel> CREATOR = new Creator<MainListsModel>() {
        @Override
        public MainListsModel createFromParcel(Parcel in) {
            return new MainListsModel(in);
        }

        @Override
        public MainListsModel[] newArray(int size) {
            return new MainListsModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public String getListName() {
        return mListName;
    }

    public void setListName(String listName) {
        mListName = listName;
    }

    public ArrayList<SecondaryListModel> getSecondaryList() {
        return mSecondaryList;
    }

    public void setSecondaryList(ArrayList<SecondaryListModel> secondaryList) {
        mSecondaryList = secondaryList;
    }

    public void addItemToSecondaryList(SecondaryListModel item){
        mSecondaryList.add(item);
    }

    @Override
    public String toString() {
        return "MainListsModel{" +
                "mListName='" + mListName + '\'' +
                ", mSecondaryList=" + mSecondaryList +
                '}';
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mListName);
        parcel.writeTypedList(mSecondaryList);
    }
}
