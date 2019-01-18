package com.example.trussell.wgustudentscheduler.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.trussell.wgustudentscheduler.dao.DaoAssessment;
import com.example.trussell.wgustudentscheduler.dao.DaoCourse;
import com.example.trussell.wgustudentscheduler.dao.DaoMentor;
import com.example.trussell.wgustudentscheduler.dao.DaoNote;
import com.example.trussell.wgustudentscheduler.dao.DaoTerm;
import com.example.trussell.wgustudentscheduler.model.Assessment;
import com.example.trussell.wgustudentscheduler.model.Course;
import com.example.trussell.wgustudentscheduler.model.Mentor;
import com.example.trussell.wgustudentscheduler.model.Note;
import com.example.trussell.wgustudentscheduler.model.Term;

import static com.example.trussell.wgustudentscheduler.util.Constants.DB_VERSION;

@Database(entities = {
        Term.class,
        Course.class,
        Assessment.class,
        Mentor.class,
        Note.class
}, version = DB_VERSION, exportSchema = false)

public abstract class SchedulerDatabase extends RoomDatabase {

    public abstract DaoTerm daoTerm();
    public abstract DaoCourse daoCourse();
    public abstract DaoAssessment daoAssessment();
    public abstract DaoMentor daoMentor();
    public abstract DaoNote daoNote();
}