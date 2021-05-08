package com.app.departmentinfos.Models;

import com.google.gson.annotations.SerializedName;

public class Courses {
    @SerializedName("course_name") String title;
    @SerializedName("course_details") String desc;
    @SerializedName("course_code") String courseCode;
    @SerializedName("student_code") String student_code;
    @SerializedName("teacher_code") String teacher_code;
    @SerializedName("semester") String semester;
    @SerializedName("course_semester") String course_semester;
    @SerializedName("section") String section;
    @SerializedName("credit_hours") String credit;
    @SerializedName("teacher_name") String teacherName;
    @SerializedName("availability") String availability;
    @SerializedName("status") String status;
    @SerializedName("value") String value;
    @SerializedName("message") String message;
    @SerializedName("request") String request;

    public String getCourse_semester() {
        return course_semester;
    }

    public void setCourse_semester(String course_semester) {
        this.course_semester = course_semester;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
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

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
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

    public String getStudentCode() {
        return student_code;
    }

    public void setStudentCode(String student_code) {
        this.student_code = student_code;
    }

    public String getTeacherCode() {
        return teacher_code;
    }

    public void setTeacher_code(String teacher_code) {
        this.teacher_code = teacher_code;
    }

    @Override
    public String toString() {
        return "Courses{" +
                "title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", courseCode='" + courseCode + '\'' +
                ", student_code='" + student_code + '\'' +
                ", teacher_code='" + teacher_code + '\'' +
                ", semester='" + semester + '\'' +
                ", credit='" + credit + '\'' +
                ", teacherName='" + teacherName + '\'' +
                ", availability='" + availability + '\'' +
                ", status='" + status + '\'' +
                ", value='" + value + '\'' +
                ", message='" + message + '\'' +
                ", request='" + request + '\'' +
                '}';
    }
}
