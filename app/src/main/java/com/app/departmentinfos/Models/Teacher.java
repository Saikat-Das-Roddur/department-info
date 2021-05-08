package com.app.departmentinfos.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Teacher implements Parcelable {
    @SerializedName("file") String file;
    @SerializedName("name") String name;
    @SerializedName("code") String code;
    @SerializedName("department") String dept;
    @SerializedName("contact") String contact;
    @SerializedName("email") String email;
    @SerializedName("section") String section;
    @SerializedName("status") String status;
    @SerializedName("add_request") String addRequest;
    //@SerializedName("session") String session;
    @SerializedName("password") String password;
    @SerializedName("designation") String designation;
    @SerializedName("value") String value;
    @SerializedName("message") String message;
    @SerializedName("type") String type;
    @SerializedName("token") String token;

    protected Teacher(Parcel in) {
        file = in.readString();
        name = in.readString();
        code = in.readString();
        dept = in.readString();
        contact = in.readString();
        email = in.readString();
        section = in.readString();
        status = in.readString();
        addRequest = in.readString();
        password = in.readString();
        designation = in.readString();
        value = in.readString();
        message = in.readString();
        type = in.readString();
        token = in.readString();
    }

    public static final Creator<Teacher> CREATOR = new Creator<Teacher>() {
        @Override
        public Teacher createFromParcel(Parcel in) {
            return new Teacher(in);
        }

        @Override
        public Teacher[] newArray(int size) {
            return new Teacher[size];
        }
    };

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAddRequest() {
        return addRequest;
    }

    public void setAddRequest(String addRequest) {
        this.addRequest = addRequest;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(file);
        dest.writeString(name);
        dest.writeString(code);
        dest.writeString(dept);
        dest.writeString(contact);
        dest.writeString(email);
        dest.writeString(section);
        dest.writeString(status);
        dest.writeString(addRequest);
        dest.writeString(password);
        dest.writeString(designation);
        dest.writeString(value);
        dest.writeString(message);
        dest.writeString(type);
        dest.writeString(token);
    }
}
