package com.example.trussell.wgustudentscheduler.util;

import android.app.Application;

import com.example.trussell.wgustudentscheduler.model.Assessment;
import com.example.trussell.wgustudentscheduler.model.Course;
import com.example.trussell.wgustudentscheduler.model.Mentor;
import com.example.trussell.wgustudentscheduler.model.Note;
import com.example.trussell.wgustudentscheduler.model.Term;

public class CurrentData extends Application {

    public static Term termData;
    public static Course courseData;
    public static Assessment assessmentData;
    public static Mentor mentorData;
    public static Note noteData;

}
