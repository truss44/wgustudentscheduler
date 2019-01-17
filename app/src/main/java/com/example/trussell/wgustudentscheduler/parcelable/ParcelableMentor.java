package com.example.trussell.wgustudentscheduler.parcelable;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.trussell.wgustudentscheduler.model.Mentor;

import java.util.Date;

public class ParcelableMentor implements Parcelable {
    private Mentor mentor;

    public Mentor getMentor() {
        return mentor;
    }

    public ParcelableMentor(Mentor mentor) {
        super();
        this.mentor = mentor;
    }

    private ParcelableMentor(Parcel in) {
        mentor = new Mentor();
        mentor.setId(in.readInt());
        mentor.setName(in.readString());
        mentor.setPhone(in.readString());
        mentor.setEmail(in.readString());
        mentor.setCourseID(in.readInt());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(mentor.getId());
        parcel.writeString(mentor.getName());
        parcel.writeString(mentor.getPhone());
        parcel.writeString(mentor.getEmail());
        parcel.writeInt(mentor.getCourseID());
    }

    public static final Creator<ParcelableMentor> CREATOR =
            new Creator<ParcelableMentor>() {
                public ParcelableMentor createFromParcel(Parcel in) {
                    return new ParcelableMentor(in);
                }

                public ParcelableMentor[] newArray(int size) {
                    return new ParcelableMentor[size];
                }
            };
}