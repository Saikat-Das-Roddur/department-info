package com.app.departmentinfos.Models;

import com.google.gson.annotations.SerializedName;

public class Assignment {
    @SerializedName("title") String title;
    @SerializedName("assignment_code") String assignmentCode;
    @SerializedName("description") String desc;
    @SerializedName("course") String course;
    @SerializedName("course_code") String courseCode;
    //@SerializedName("semester") String semester;
    @SerializedName("section") String section;
    //@SerializedName("credit") String credit;
    @SerializedName("teacher_code") String teacherCode;
    @SerializedName("student") String student;
    @SerializedName("code") String code;
    @SerializedName("posting_date") String postingDate;
    @SerializedName("submission_date") String submissionDate;
    @SerializedName("file") String file;
    //@SerializedName("notice") ArrayList<String> notice;
   // @SerializedName("session") String session;
   // @SerializedName("password") String password;
    @SerializedName("value") String value;
    @SerializedName("message") String message;
    @SerializedName("status") String status;

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getAssignmentCode() {
        return assignmentCode;
    }

    public void setAssignmentCode(String assignmentCode) {
        this.assignmentCode = assignmentCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getTeacherCode() {
        return teacherCode;
    }

    public void setTeacherCode(String teacherCode) {
        this.teacherCode = teacherCode;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPostingDate() {
        return postingDate;
    }

    public void setPostingDate(String postingDate) {
        this.postingDate = postingDate;
    }

    public String getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(String submissionDate) {
        this.submissionDate = submissionDate;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
