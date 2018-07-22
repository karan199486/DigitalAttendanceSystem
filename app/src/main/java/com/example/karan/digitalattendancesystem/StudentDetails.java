package com.example.karan.digitalattendancesystem;



import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class StudentDetails implements Serializable {

    @SerializedName("rollno")
    @Expose
    private String rollno;
    @SerializedName("course")
    @Expose
    private String course;
    @SerializedName("subject")
    @Expose
    private String subject;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("attendancecount")
    @Expose
    private String attendancecount;


    public String getRollno() {
        return rollno;
    }

    public void setRollno(String rollno) {
        this.rollno = rollno;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getAttendancecount() {
        return attendancecount;
    }

    public void getAttendancecount(String attendancecount) { this.attendancecount = attendancecount; }

}