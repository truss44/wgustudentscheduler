package com.example.trussell.wgustudentscheduler.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.trussell.wgustudentscheduler.model.Assessment;

import java.util.List;

@Dao
public interface DaoAssessment {

    @Insert
    Long insertAssessment(Assessment assessment);


    @Query("SELECT * FROM assessments ORDER BY id desc")
    LiveData<List<Assessment>> fetchAllAssessments();

    @Query("SELECT * FROM assessments WHERE courseID = :courseID ORDER BY id desc")
    LiveData<List<Assessment>> fetchAssessmentsByCourse(int courseID);

    @Query("SELECT * FROM assessments WHERE id = :assessmentID")
    Assessment getAssessment(int assessmentID);

    @Query("SELECT alertGoalID FROM assessments WHERE courseID = :courseID ORDER BY id desc")
    List<String> getAssessmentsInCourse(int courseID);

    @Update
    void updateAssessment(Assessment assessment);


    @Delete
    void deleteAssessment(Assessment assessment);
}