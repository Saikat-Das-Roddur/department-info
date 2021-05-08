package com.app.departmentinfos.Models;

import com.google.gson.annotations.SerializedName;

public class Faculty {
      @SerializedName("id") int id;
      @SerializedName("name") String name;
      @SerializedName("image")String  image;
      @SerializedName("degree1")String  degree1;
      @SerializedName("degree2")String  degree2;
      @SerializedName("degree3")String  degree3;
      @SerializedName("designation")String  designation;
      @SerializedName("department") String department;
      @SerializedName("contact_no")String  contact_no;
      @SerializedName("email") String email;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDegree1() {
        return degree1;
    }

    public void setDegree1(String degree1) {
        this.degree1 = degree1;
    }

    public String getDegree2() {
        return degree2;
    }

    public void setDegree2(String degree2) {
        this.degree2 = degree2;
    }

    public String getDegree3() {
        return degree3;
    }

    public void setDegree3(String degree3) {
        this.degree3 = degree3;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
