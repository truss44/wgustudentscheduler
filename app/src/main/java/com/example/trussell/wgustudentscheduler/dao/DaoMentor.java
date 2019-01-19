package com.example.trussell.wgustudentscheduler.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.trussell.wgustudentscheduler.model.Mentor;

import java.util.List;

@Dao
public interface DaoMentor {

    @Insert
    Long insertMentor(Mentor mentor);


    @Query("SELECT * FROM mentors ORDER BY id desc")
    LiveData<List<Mentor>> fetchAllMentors();

    @Query("SELECT * FROM mentors WHERE courseID = :courseID ORDER BY id desc")
    LiveData<List<Mentor>> fetchMentorsByCourse(int courseID);

    @Query("SELECT * FROM mentors WHERE id = :mentorId")
    Mentor getMentor(int mentorId);


    @Update
    void updateMentor(Mentor mentor);


    @Delete
    void deleteMentor(Mentor mentor);
}