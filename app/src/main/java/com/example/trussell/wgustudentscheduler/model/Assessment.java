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
        foreignKeys = @ForeignKey(entity = Course.class,
                parentColumns = "id",
                childColumns = "courseID",
                onDelete = CASCADE),
        indices = {@Index(value = {"courseID"})})
public class Assessment implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "type")
    private String type;

    @ColumnInfo(name = "dueDate")
    @TypeConverters({TimestampConverter.class})
    private Date dueDate;

    @ColumnInfo(name = "goalDate")
    @TypeConverters({TimestampConverter.class})
    private Date goalDate;

    @ColumnInfo(name = "alertGoal")
    private int alertGoal;

    @ColumnInfo(name = "courseID")
    private int courseID;

    @ColumnInfo(name = "alertGoalID")
    private String alertGoalID;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getGoalDate() {
        return goalDate;
    }

    public void setGoalDate(Date goalDate) {
        this.goalDate = goalDate;
    }

    public int getAlertGoal() {
        return alertGoal;
    }

    public void setAlertGoal(int alertGoal) {
        this.alertGoal = alertGoal;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public static ArrayList<Assessment> getAssessmentsList() {
        return assessmentsList;
    }

    public static void addAssessments(Assessment assessment) {
        assessmentsList.add(assessment);
    }

    public String getAlertGoalID() {
        return alertGoalID;
    }

    public void setAlertGoalID(String alertGoalID) {
        this.alertGoalID = alertGoalID;
    }
}