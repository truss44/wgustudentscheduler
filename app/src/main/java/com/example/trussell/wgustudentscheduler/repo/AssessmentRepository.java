package com.example.trussell.wgustudentscheduler.repo;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import com.example.trussell.wgustudentscheduler.db.SchedulerDatabase;
import com.example.trussell.wgustudentscheduler.model.Assessment;
import com.example.trussell.wgustudentscheduler.util.Constants;

import java.util.Date;
import java.util.List;

public class AssessmentRepository implements Constants {

    private final SchedulerDatabase schedulerDatabase;
    public AssessmentRepository(Context context) {
        schedulerDatabase = Room.databaseBuilder(context, SchedulerDatabase.class, DB_NAME)
                .fallbackToDestructiveMigration().build();
    }

    public void insertAssessment(String name, String type, Date dueDate, Date goalDate, int alertGoal, int courseID) {

        Assessment assessment = new Assessment();
        assessment.setName(name);
        assessment.setType(type);
        assessment.setDueDate(dueDate);
        assessment.setGoalDate(goalDate);
        assessment.setAlertGoal(alertGoal);
        assessment.setCourseID(courseID);

        insertAssessment(assessment);
    }

    @SuppressLint("StaticFieldLeak")
    private void insertAssessment(final Assessment assessment) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                schedulerDatabase.daoAssessment().insertAssessment(assessment);
                return null;
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void updateAssessment(final Assessment assessment) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                schedulerDatabase.daoAssessment().updateAssessment(assessment);
                return null;
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void deleteAssessment(final int id) {
        final Assessment assessment = getAssessment(id);
        if (assessment != null) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    schedulerDatabase.daoAssessment().deleteAssessment(assessment);
                    return null;
                }
            }.execute();
        }
    }

    private Assessment getAssessment(int id) {
        return schedulerDatabase.daoAssessment().getAssessment(id);
    }

    public LiveData<List<Assessment>> getAssessments() {
        return schedulerDatabase.daoAssessment().fetchAllAssessments();
    }

    public LiveData<List<Assessment>> fetchAssessmentsByCourse(int id) {
        return schedulerDatabase.daoAssessment().fetchAssessmentsByCourse(id);
    }
}