package com.codepath.simpletodo;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.Date;

/**
 * Created by Shilpa's on 7/24/2017.
 */

public class Item implements Parcelable {

        private String mTitle;
        private String mNotes;
        private String mPriority;
        private String mStatus;
        private String mDueDate;
        private String mCategory;
        private long mWorkItemId;

        public Item(){
            this.mTitle="";
            this.mWorkItemId=-1;;
        }




        public String getTitle(){
            return mTitle;
        }

        public void setTitle(String text){
            this.mTitle = text;
        }

        public void setWorkItemId(long id){
            this.mWorkItemId=id;
        }

        public long getItemId(){   return this.mWorkItemId;       }

        public void setNotes(String text){ this.mNotes = text; }

        public String getNotes(){ return this.mNotes; }

        public void  setPriority(String text ) { this.mPriority = text; }

        public String getPriority () { return this.mPriority; }

        public void setStatus(String text){ this.mStatus = text; }

        public String getStatus() { return this.mStatus;    }

        public void setDueDate(String date) {  this.mDueDate=date; }

        public String getDueDate(){ return this.mDueDate;   }

        public void setCategory(String category) { this.mCategory = category; }

        public String getCategory() { return mCategory; }

      @Override
      public int describeContents(){
         return 0;
      }

    private Item(Parcel in) {
        mWorkItemId = in.readLong();
        mTitle = in.readString();
        mDueDate = in.readString();
        mNotes = in.readString();
        mPriority = in.readString();
        mStatus = in.readString();
        mCategory = in.readString();
    }
    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(mWorkItemId);
        out.writeString(mTitle);
        out.writeString(mDueDate);
        out.writeString(mNotes);
        out.writeString(mPriority);
        out.writeString(mStatus);
        out.writeString(mCategory);
    }

    public static final Parcelable.Creator<Item> CREATOR
            = new Parcelable.Creator<Item>() {
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        public Item[] newArray(int size) {
            return new Item[size];
        }
    };



}
