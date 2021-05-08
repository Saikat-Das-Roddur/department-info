package com.app.departmentinfos.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Message implements Parcelable {
    @SerializedName("sender_name") private String senderName;
    @SerializedName("sender_message") private String senderMessage;
    @SerializedName("sender_code") private String senderCode;
    @SerializedName("sender_file") private String senderFile;
    @SerializedName("receiver_code") private String receiverCode;
    @SerializedName("receiver_message") private String receiverMessage;
    @SerializedName("receiver_name") private String receiverName;
    @SerializedName("receiver_file") private String receiverFile;
    @SerializedName("section") private String section;
    @SerializedName("designation") private String designation;
    @SerializedName("date") private String date;
    @SerializedName("time") private String time;
    @SerializedName("status") private String status;
    @SerializedName("value") private String value;
    @SerializedName("message") private String massage;

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getSenderFile() {
        return senderFile;
    }

    public void setSenderFile(String senderFile) {
        this.senderFile = senderFile;
    }

    public String getReceiverFile() {
        return receiverFile;
    }

    public void setReceiverFile(String receiverFile) {
        this.receiverFile = receiverFile;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReceiverCode() {
        return receiverCode;
    }

    public void setReceiverCode(String receiverCode) {
        this.receiverCode = receiverCode;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderMessage() {
        return senderMessage;
    }

    public void setSenderMessage(String senderMessage) {
        this.senderMessage = senderMessage;
    }

    public String getSenderCode() {
        return senderCode;
    }

    public void setSenderCode(String senderCode) {
        this.senderCode = senderCode;
    }

    public String getReceiverMessage() {
        return receiverMessage;
    }

    public void setReceiverMessage(String receiverMessage) {
        this.receiverMessage = receiverMessage;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getMassage() {
        return massage;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    protected Message(Parcel in) {
        senderName = in.readString();
        senderMessage = in.readString();
        senderCode = in.readString();
        receiverCode = in.readString();
        receiverMessage = in.readString();
        receiverName = in.readString();
        section = in.readString();
        designation = in.readString();
        date = in.readString();
        time = in.readString();
        status = in.readString();
        value = in.readString();
        massage = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(senderName);
        dest.writeString(senderMessage);
        dest.writeString(senderCode);
        dest.writeString(receiverCode);
        dest.writeString(receiverMessage);
        dest.writeString(receiverName);
        dest.writeString(section);
        dest.writeString(designation);
        dest.writeString(date);
        dest.writeString(time);
        dest.writeString(status);
        dest.writeString(value);
        dest.writeString(massage);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Message> CREATOR = new Parcelable.Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    @Override
    public String toString() {
        return "Message{" +
                "senderName='" + senderName + '\'' +
                ", senderMessage='" + senderMessage + '\'' +
                ", senderCode='" + senderCode + '\'' +
                ", senderFile='" + senderFile + '\'' +
                ", receiverCode='" + receiverCode + '\'' +
                ", receiverMessage='" + receiverMessage + '\'' +
                ", receiverName='" + receiverName + '\'' +
                ", receiverFile='" + receiverFile + '\'' +
                ", date='" + date + '\'' +
                ", status='" + status + '\'' +
                ", value='" + value + '\'' +
                ", massage='" + massage + '\'' +
                '}';
    }
}