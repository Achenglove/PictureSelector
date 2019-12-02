package com.ccr.pictureselector.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * 在此写用途
 *
 * @Author: Acheng
 * @Email: 345887272@qq.com
 * @Date: 2017-09-21 16:40
 * @Version: V1.0 <描述当前版本功能>
 */

public class Moment implements Parcelable {
    public String content;
    public ArrayList<String> photos;

    public Moment(String content, ArrayList<String> photos) {
        this.content = content;
        this.photos = photos;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ArrayList<String> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<String> photos) {
        this.photos = photos;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.content);
        dest.writeStringList(this.photos);
    }

    public Moment() {
    }

    protected Moment(Parcel in) {
        this.content = in.readString();
        this.photos = in.createStringArrayList();
    }

    public static final Creator<Moment> CREATOR = new Creator<Moment>() {
        @Override
        public Moment createFromParcel(Parcel source) {
            return new Moment(source);
        }

        @Override
        public Moment[] newArray(int size) {
            return new Moment[size];
        }
    };
}
