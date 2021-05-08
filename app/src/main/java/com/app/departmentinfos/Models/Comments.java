package com.app.departmentinfos.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Comments implements Parcelable {
    @SerializedName("comment") String comment;
    @SerializedName("id") int id;
    @SerializedName("forum_code") String forumCode;
    @SerializedName("user_name") String userName;
    @SerializedName("date") String date;
    @SerializedName("code") String code;
    @SerializedName("image") String image;
    @SerializedName("value") String value;
    @SerializedName("message") String message;
    @SerializedName("status") String status;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getForumCode() {
        return forumCode;
    }

    public void setForumCode(String forumCode) {
        this.forumCode = forumCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    protected Comments(Parcel in) {
        comment = in.readString();
        id = in.readInt();
        forumCode = in.readString();
        userName = in.readString();
        date = in.readString();
        code = in.readString();
        image = in.readString();
        value = in.readString();
        message = in.readString();
        status = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(comment);
        dest.writeInt(id);
        dest.writeString(forumCode);
        dest.writeString(userName);
        dest.writeString(date);
        dest.writeString(code);
        dest.writeString(image);
        dest.writeString(value);
        dest.writeString(message);
        dest.writeString(status);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Comments> CREATOR = new Parcelable.Creator<Comments>() {
        @Override
        public Comments createFromParcel(Parcel in) {
            return new Comments(in);
        }

        @Override
        public Comments[] newArray(int size) {
            return new Comments[size];
        }
    };
}