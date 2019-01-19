package com.example.trussell.wgustudentscheduler.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.trussell.wgustudentscheduler.model.Note;

import java.util.List;

@Dao
public interface DaoNote {

    @Insert
    Long insertNote(Note note);


    @Query("SELECT * FROM notes ORDER BY id desc")
    LiveData<List<Note>> fetchAllNotes();

    @Query("SELECT * FROM notes WHERE courseID = :courseID ORDER BY id desc")
    LiveData<List<Note>> fetchNotesByCourse(int courseID);

    @Query("SELECT * FROM notes WHERE id = :noteId")
    Note getNote(int noteId);


    @Update
    void updateNote(Note note);


    @Delete
    void deleteNote(Note note);
}