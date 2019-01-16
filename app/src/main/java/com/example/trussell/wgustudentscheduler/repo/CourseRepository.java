package com.example.trussell.wgustudentscheduler.repo;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import com.example.trussell.wgustudentscheduler.db.SchedulerDatabase;
import com.example.trussell.wgustudentscheduler.model.Course;
import com.example.trussell.wgustudentscheduler.util.Constants;

import java.util.Date;
import java.util.List;

public class CourseRepository implements Constants {

    private SchedulerDatabase schedulerDatabase;
    public CourseRepository(Context context) {
        schedulerDatabase = Room.databaseBuilder(context, SchedulerDatabase.class, DB_NAME).build();
    }

    public void insertCourse(String name, Date startDate, Date endDate, int termID) {

        Course course = new Course();
        course.setName(name);
        course.setStartDate(startDate);
        course.setEndDate(endDate);
        course.setTermID(termID);

        insertCourse(course);
    }

    @SuppressLint("StaticFieldLeak")
    private void insertCourse(final Course course) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                schedulerDatabase.daoCourse().insertCourse(course);
                return null;
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void updateCourse(final Course course) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                schedulerDatabase.daoCourse().updateCourse(course);
                return null;
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void deleteCourse(final int id) {
        final LiveData<Course> course = getCourse(id);
        if (course != null) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    schedulerDatabase.daoCourse().deleteCourse(course.getValue());
                    return null;
                }
            }.execute();
        }
    }

    private LiveData<Course> getCourse(int id) {
        return schedulerDatabase.daoCourse().getCourse(id);
    }

    public LiveData<List<Course>> getCourses() {
        return schedulerDatabase.daoCourse().fetchAllCourses();
    }
}