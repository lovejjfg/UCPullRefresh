package com.example.tt.fragments.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Joe on 2016/10/14.
 * Email lovejjfg@gmail.com
 */

public class ModelBean implements Parcelable {
    @SerializedName("sTittle")
    private String tittle;
    @SerializedName("sContent")
    private String content;
    @SerializedName("sImgUrl")
    private String imgUrl;

    private String jumpUrl;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getJumpUrl() {
        return jumpUrl;
    }

    public void setJumpUrl(String jumpUrl) {
        this.jumpUrl = jumpUrl;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.tittle);
        dest.writeString(this.content);
        dest.writeString(this.imgUrl);
        dest.writeString(this.jumpUrl);
    }

    public ModelBean() {
    }

    protected ModelBean(Parcel in) {
        this.tittle = in.readString();
        this.content = in.readString();
        this.imgUrl = in.readString();
        this.jumpUrl = in.readString();
    }

    public static final Parcelable.Creator<ModelBean> CREATOR = new Parcelable.Creator<ModelBean>() {
        @Override
        public ModelBean createFromParcel(Parcel source) {
            return new ModelBean(source);
        }

        @Override
        public ModelBean[] newArray(int size) {
            return new ModelBean[size];
        }
    };

    public ModelBean(String tittle) {
        this.tittle = tittle;
    }

    @Override
    public String toString() {
        return "ModelBean{" +
                "content='" + content + '\'' +
                ", tittle='" + tittle + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", jumpUrl='" + jumpUrl + '\'' +
                '}';
    }
}
