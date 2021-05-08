package com.app.departmentinfos.Models;

import com.google.gson.annotations.SerializedName;

public class Department {
    @SerializedName("department_name") String departmentName;
    @SerializedName("department_head") String departmentHead;
    @SerializedName("department_contact") String departmentContact;
    @SerializedName("section_name") String sectionName;
    @SerializedName("semester") String semester;
    @SerializedName("session") String session;

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDepartmentHead() {
        return departmentHead;
    }

    public void setDepartmentHead(String departmentHead) {
        this.departmentHead = departmentHead;
    }

    public String getDepartmentContact() {
        return departmentContact;
    }

    public void setDepartmentContact(String departmentContact) {
        this.departmentContact = departmentContact;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }
}
