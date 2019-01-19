package com.example.trussell.wgustudentscheduler.repo;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import com.example.trussell.wgustudentscheduler.db.SchedulerDatabase;
import com.example.trussell.wgustudentscheduler.model.Mentor;
import com.example.trussell.wgustudentscheduler.util.Constants;

import java.util.List;

public class MentorRepository implements Constants {

    private final SchedulerDatabase schedulerDatabase;
    public MentorRepository(Context context) {
        schedulerDatabase = Room.databaseBuilder(context, SchedulerDatabase.class, DB_NAME)
                .fallbackToDestructiveMigration().build();
    }

    public void insertMentor(String name, String phone, String email, int courseID) {

        Mentor mentor = new Mentor();
        mentor.setName(name);
        mentor.setPhone(phone);
        mentor.setEmail(email);
        mentor.setCourseID(courseID);

        insertMentor(mentor);
    }

    @SuppressLint("StaticFieldLeak")
    private void insertMentor(final Mentor mentor) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                schedulerDatabase.daoMentor().insertMentor(mentor);
                return null;
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void updateMentor(final Mentor mentor) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                schedulerDatabase.daoMentor().updateMentor(mentor);
                return null;
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void deleteMentor(final int id) {
        final Mentor mentor = getMentor(id);
        if (mentor != null) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    schedulerDatabase.daoMentor().deleteMentor(mentor);
                    return null;
                }
            }.execute();
        }
    }

    private Mentor getMentor(int id) {
        return schedulerDatabase.daoMentor().getMentor(id);
    }

    public LiveData<List<Mentor>> getMentors() {
        return schedulerDatabase.daoMentor().fetchAllMentors();
    }

    public LiveData<List<Mentor>> fetchMentorsByCourse(int id) {
        return schedulerDatabase.daoMentor().fetchMentorsByCourse(id);
    }
}