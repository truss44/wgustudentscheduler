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

@Entity(tableName = "assessments",
        foreignKeys = @ForeignKey(entity = Term.class,
                parentColumns = "id",
                childColumns = "termID",
                onDelete = CASCADE),
        indices = {@Index(value = {"termID"})})
public class Assessment implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "startDate")
    @TypeConverters({TimestampConverter.class})
    private Date startDate;

    @ColumnInfo(name = "endDate")
    @TypeConverters({TimestampConverter.class})
    private Date endDate;

    @ColumnInfo(name = "alertStart")
    @TypeConverters({TimestampConverter.class})
    private Date alertStart;

    @ColumnInfo(name = "alertEnd")
    @TypeConverters({TimestampConverter.class})
    private Date alertEnd;

    @ColumnInfo(name = "termID")
    @TypeConverters({TimestampConverter.class})
    private int termID;

    @Ignore
    private static ArrayList<Assessment> assessmentsList;

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

    public Date getAlertStart() {
        return alertStart;
    }

    public void setAlertStart(Date alertStart) {
        this.alertStart = alertStart;
    }

    public Date getAlertEnd() {
        return alertEnd;
    }

    public void setAlertEnd(Date alertEnd) {
        this.alertEnd = alertEnd;
    }

    public int getTermID() {
        return termID;
    }

    public void setTermID(int termID) {
        this.termID = termID;
    }

    public static ArrayList<Assessment> getAssessmentsList() {
        return assessmentsList;
    }

    public static void addAssessments(Assessment assessment) {
        assessmentsList.add(assessment);
    }
}