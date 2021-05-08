package com.app.departmentinfos.Models;

import com.google.gson.annotations.SerializedName;

public class Student {
    @SerializedName("file")
    String file;
    @SerializedName("user_image")
    String userImage;
    @SerializedName("name")
    String name;
    @SerializedName("code")
    String code;
    @SerializedName("department")
    String dept;
    @SerializedName("contact")
    String contact;
    @SerializedName("email")
    String email;
    @SerializedName("status")
    String status;
    @SerializedName("section")
    String section;
    @SerializedName("session")
    String session;
    @SerializedName("designation")
    String designation;
    @SerializedName("password")
    String password;
    @SerializedName("value")
    String value;
    @SerializedName("message")
    String message;
    @SerializedName("add_request")
    String addRequest;
    @SerializedName("type")
    String type;
    @SerializedName("token")
    String token;

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

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
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

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", dept='" + dept + '\'' +
                ", contact='" + contact + '\'' +
                ", email='" + email + '\'' +
                ", status='" + status + '\'' +
                ", section='" + section + '\'' +
                ", session='" + session + '\'' +
                ", password='" + password + '\'' +
                ", value='" + value + '\'' +
                ", message='" + message + '\'' +
                ", addRequest='" + addRequest + '\'' +
                ", type='" + type + '\'' +
                ", token='" + token + '\'' +
                ", file='" + file + '\'' +
                '}';
    }
}
