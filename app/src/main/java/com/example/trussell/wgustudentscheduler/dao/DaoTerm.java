package com.example.trussell.wgustudentscheduler.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.trussell.wgustudentscheduler.model.Term;

import java.util.List;

@Dao
public interface DaoTerm {

    @Insert
    Long insertTerm(Term term);


    @Query("SELECT * FROM terms ORDER BY id")
    LiveData<List<Term>> fetchAllTerms();


    @Query("SELECT * FROM terms WHERE id = :termID")
    Term getTerm(int termID);


    @Update
    void updateTerm(Term term);


    @Delete
    void deleteTerm(Term term);
}