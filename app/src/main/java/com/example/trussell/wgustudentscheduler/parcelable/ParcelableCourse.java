package com.example.trussell.wgustudentscheduler.parcelable;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.trussell.wgustudentscheduler.model.Course;

import java.util.Date;

public class ParcelableCourse implements Parcelable {
    private Course course;

    public Course getCourse() {
        return course;
    }

    public ParcelableCourse(Course course) {
        super();
        this.course = course;
    }

    private ParcelableCourse(Parcel in) {
        course = new Course();
        course.setId(in.readInt());
        course.setName(in.readString());
        course.setStartDate((Date) in.readSerializable());
        course.setEndDate((Date) in.readSerializable());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(course.getId());
        parcel.writeString(course.getName());
        parcel.writeSerializable(course.getStartDate());
        parcel.writeSerializable(course.getEndDate());
    }

    public static final Creator<ParcelableCourse> CREATOR =
            new Creator<ParcelableCourse>() {
                public ParcelableCourse createFromParcel(Parcel in) {
                    return new ParcelableCourse(in);
                }

                public ParcelableCourse[] newArray(int size) {
                    return new ParcelableCourse[size];
                }
            };
}