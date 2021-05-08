package com.app.departmentinfos.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Forum implements Parcelable {
    @SerializedName("user_name") String user_name;
    @SerializedName("user_image") String userImage;
    @SerializedName("forum_code") String forumCode;
    @SerializedName("description") String description;
    @SerializedName("date") String date;
    @SerializedName("file") String file;
    @SerializedName("department") String department;
    @SerializedName("status") String status;
    @SerializedName("designation") String designation;
    @SerializedName("code") String code;
    @SerializedName("value") String value;
    @SerializedName("message") String message;

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getForumCode() {
        return forumCode;
    }

    public void setForumCode(String forumCode) {
        this.forumCode = forumCode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    protected Forum(Parcel in) {
        user_name = in.readString();
        forumCode = in.readString();
        description = in.readString();
        date = in.readString();
        file = in.readString();
        userImage = in.readString();
        status = in.readString();
        department = in.readString();
        designation = in.readString();
        code = in.readString();
        value = in.readString();
        message = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(user_name);
        dest.writeString(forumCode);
        dest.writeString(description);
        dest.writeString(date);
        dest.writeString(file);
        dest.writeString(userImage);
        dest.writeString(status);
        dest.writeString(department);
        dest.writeString(designation);
        dest.writeString(code);
        dest.writeString(value);
        dest.writeString(message);
    }

    @Override
    public String toString() {
        return "Forum{" +
                "user_name='" + user_name + '\'' +
                ", userImage='" + userImage + '\'' +
                ", forumCode='" + forumCode + '\'' +
                ", description='" + description + '\'' +
                ", date='" + date + '\'' +
                ", file='" + file + '\'' +
                ", department='" + department + '\'' +
                ", status='" + status + '\'' +
                ", code='" + code + '\'' +
                '}';
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Forum> CREATOR = new Parcelable.Creator<Forum>() {
        @Override
        public Forum createFromParcel(Parcel in) {
            return new Forum(in);
        }

        @Override
        public Forum[] newArray(int size) {
            return new Forum[size];
        }
    };
}