package com.app.departmentinfos.Models;

import com.google.gson.annotations.SerializedName;

public class Notice {
    @SerializedName("title") String title;
    @SerializedName("notice_code") String noticeCode;
    @SerializedName("description") String description;
    @SerializedName("user_name") String userName;
    @SerializedName("code") String code;
    @SerializedName("image") String image;
    @SerializedName("posting_date") String postingDate;
    //@SerializedName("email") String email;
    @SerializedName("section") String section;
    @SerializedName("department") String dept;
    @SerializedName("type") String type;
    @SerializedName("value") String value;
    @SerializedName("message") String message;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNoticeCode() {
        return noticeCode;
    }

    public void setNoticeCode(String noticeCode) {
        this.noticeCode = noticeCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPostingDate() {
        return postingDate;
    }

    public void setPostingDate(String postingDate) {
        this.postingDate = postingDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
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
}
