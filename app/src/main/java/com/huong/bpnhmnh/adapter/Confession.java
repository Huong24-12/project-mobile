package com.huong.bpnhmnh.adapter;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Random;

public class Confession implements Parcelable {
    private String creatorName;
    private String creatorUid;
    private String contentConfession;
    private long approvedTime;
    private String imageConfession;

    public Confession() {
    }

    public Confession(String creatorName, String creatorUid, String contentConfession, long approvedTime, String imageConfession) {
        this.creatorName = creatorName;
        this.creatorUid = creatorUid;
        this.contentConfession = contentConfession;
        this.approvedTime = approvedTime;
        this.imageConfession = imageConfession;
    }

    public String getCreatorUid() {
        return creatorUid;
    }

    public void setCreatorUid(String creatorUid) {
        this.creatorUid = creatorUid;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getContentConfession() {
        return contentConfession;
    }

    public void setContentConfession(String contentConfession) {
        this.contentConfession = contentConfession;
    }

    public long getApprovedTime() {
        return approvedTime;
    }

    public void setApprovedTime(long approvedTime) {
        this.approvedTime = approvedTime;
    }

    public String getImageConfession() {
        return imageConfession;
    }

    public void setImageConfession(String imageConfession) {
        this.imageConfession = imageConfession;
    }

    protected Confession(Parcel in) {
        creatorName = in.readString();
        creatorUid = in.readString();
        contentConfession = in.readString();
        approvedTime = in.readLong();
        imageConfession = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(creatorName);
        dest.writeString(contentConfession);
        dest.writeString(creatorUid);
        dest.writeLong(approvedTime);
        dest.writeString(imageConfession);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Confession> CREATOR = new Creator<Confession>() {
        @Override
        public Confession createFromParcel(Parcel in) {
            return new Confession(in);
        }

        @Override
        public Confession[] newArray(int size) {
            return new Confession[size];
        }
    };
}
