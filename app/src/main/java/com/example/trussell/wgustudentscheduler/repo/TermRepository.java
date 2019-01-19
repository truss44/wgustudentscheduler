package com.example.trussell.wgustudentscheduler.repo;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import com.example.trussell.wgustudentscheduler.db.SchedulerDatabase;
import com.example.trussell.wgustudentscheduler.model.Term;
import com.example.trussell.wgustudentscheduler.util.Constants;

import java.util.Date;
import java.util.List;

public class TermRepository implements Constants {

    private final SchedulerDatabase schedulerDatabase;
    public TermRepository(Context context) {
        schedulerDatabase = Room.databaseBuilder(context, SchedulerDatabase.class, DB_NAME)
                .fallbackToDestructiveMigration().build();
    }

    public void insertTerm(String name, Date startDate, Date endDate) {

        Term term = new Term();
        term.setName(name);
        term.setStartDate(startDate);
        term.setEndDate(endDate);

        insertTerm(term);
    }

    @SuppressLint("StaticFieldLeak")
    private void insertTerm(final Term term) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                schedulerDatabase.daoTerm().insertTerm(term);
                return null;
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void updateTerm(final Term term) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                schedulerDatabase.daoTerm().updateTerm(term);
                return null;
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void deleteTerm(final int id) {
        final Term term = getTerm(id);
        if (term != null) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    schedulerDatabase.daoTerm().deleteTerm(term);
                    return null;
                }
            }.execute();
        }
    }

    public Term getTerm(int id) {
        return schedulerDatabase.daoTerm().getTerm(id);
    }

    public LiveData<List<Term>> getTerms() {
        return schedulerDatabase.daoTerm().fetchAllTerms();
    }
}