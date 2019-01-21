package com.example.trussell.wgustudentscheduler.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.trussell.wgustudentscheduler.model.Course;

import java.util.List;

@Dao
public interface DaoCourse {

    @Insert
    Long insertCourse(Course course);


    @Query("SELECT * FROM courses ORDER BY id desc")
    LiveData<List<Course>> fetchAllCourses();

    @Query("SELECT * FROM courses WHERE termID = :termID ORDER BY id desc")
    LiveData<List<Course>> fetchCoursesByTerm(int termID);

    @Query("SELECT * FROM courses WHERE id = :courseID")
    Course getCourse(int courseID);

    @Query("SELECT count(id) AS total FROM courses WHERE termID = :termID")
    int getTermCourseCount(int termID);

    @Update
    void updateCourse(Course course);


    @Delete
    void deleteCourse(Course course);
}