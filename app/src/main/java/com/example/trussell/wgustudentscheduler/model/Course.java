package com.example.trussell.wgustudentscheduler.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.example.trussell.wgustudentscheduler.util.TimestampConverter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "courses",
        foreignKeys = @ForeignKey(entity = Term.class,
                parentColumns = "id",
                childColumns = "termID",
                onDelete = CASCADE),
        indices = {@Index(value = {"termID"})})
public class Course implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "status")
    private String status;

    @ColumnInfo(name = "startDate")
    @TypeConverters({TimestampConverter.class})
    private Date startDate;

    @ColumnInfo(name = "endDate")
    @TypeConverters({TimestampConverter.class})
    private Date endDate;

    @ColumnInfo(name = "alertStart")
    private int alertStart;

    @ColumnInfo(name = "alertEnd")
    private int alertEnd;

    @ColumnInfo(name = "termID")
    private int termID;

    @ColumnInfo(name = "alertStartID")
    private String alertStartID;

    @ColumnInfo(name = "alertEndID")
    private String alertEndID;

    @Ignore
    private static ArrayList<Course> coursesList;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getAlertStart() {
        return alertStart;
    }

    public void setAlertStart(int alertStart) {
        this.alertStart = alertStart;
    }

    public int getAlertEnd() {
        return alertEnd;
    }

    public void setAlertEnd(int alertEnd) {
        this.alertEnd = alertEnd;
    }

    public int getTermID() {
        return termID;
    }

    public void setTermID(int termID) {
        this.termID = termID;
    }

    public static ArrayList<Course> getCoursesList() {
        return coursesList;
    }

    public static void addCourses(Course course) {
        coursesList.add(course);
    }

    public String getAlertStartID() {
        return alertStartID;
    }

    public void setAlertStartID(String alertStartID) {
        this.alertStartID = alertStartID;
    }

    public String getAlertEndID() {
        return alertEndID;
    }

    public void setAlertEndID(String alertEndID) {
        this.alertEndID = alertEndID;
    }
}