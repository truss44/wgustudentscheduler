package com.example.trussell.wgustudentscheduler.repo;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import com.example.trussell.wgustudentscheduler.db.SchedulerDatabase;
import com.example.trussell.wgustudentscheduler.model.Note;
import com.example.trussell.wgustudentscheduler.util.Constants;

import java.util.List;

public class NoteRepository implements Constants {

    private SchedulerDatabase schedulerDatabase;
    public NoteRepository(Context context) {
        schedulerDatabase = Room.databaseBuilder(context, SchedulerDatabase.class, DB_NAME)
                .fallbackToDestructiveMigration().build();
    }

    public void insertNote(String entry, int setCourseID) {

        Note note = new Note();
        note.setEntry(entry);
        note.setCourseID(setCourseID);

        insertNote(note);
    }

    @SuppressLint("StaticFieldLeak")
    private void insertNote(final Note note) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                schedulerDatabase.daoNote().insertNote(note);
                return null;
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void updateNote(final Note note) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                schedulerDatabase.daoNote().updateNote(note);
                return null;
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void deleteNote(final int id) {
        final LiveData<Note> note = getNote(id);
        if (note != null) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    schedulerDatabase.daoNote().deleteNote(note.getValue());
                    return null;
                }
            }.execute();
        }
    }

    private LiveData<Note> getNote(int id) {
        return schedulerDatabase.daoNote().getNote(id);
    }

    public LiveData<List<Note>> getNotes() {
        return schedulerDatabase.daoNote().fetchAllNotes();
    }
}