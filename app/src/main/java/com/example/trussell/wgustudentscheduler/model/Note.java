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

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "notes",
        foreignKeys = @ForeignKey(entity = Course.class,
                parentColumns = "id",
                childColumns = "courseID",
                onDelete = CASCADE),
        indices = {@Index(value = {"courseID"})})
public class Note implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "entry")
    private String entry;

    @ColumnInfo(name = "courseID")
    @TypeConverters({TimestampConverter.class})
    private int courseID;

    @Ignore
    private static ArrayList<Note> notesList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEntry() {
        return entry;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public static ArrayList<Note> getNotesList() {
        return notesList;
    }

    public static void addTerms(Note note) {
        notesList.add(note);
    }
}