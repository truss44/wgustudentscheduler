package com.example.trussell.wgustudentscheduler.parcelable;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.trussell.wgustudentscheduler.model.Term;

import java.util.Date;

public class ParcelableTerm implements Parcelable {
    private Term term;

    public Term getTerm() {
        return term;
    }

    public ParcelableTerm(Term term) {
        super();
        this.term = term;
    }

    private ParcelableTerm(Parcel in) {
        term = new Term();
        term.setId(in.readInt());
        term.setName(in.readString());
        term.setStartDate((Date) in.readSerializable());
        term.setEndDate((Date) in.readSerializable());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(term.getId());
        parcel.writeString(term.getName());
        parcel.writeSerializable(term.getStartDate());
        parcel.writeSerializable(term.getEndDate());
    }

    public static final Parcelable.Creator<ParcelableTerm> CREATOR =
            new Parcelable.Creator<ParcelableTerm>() {
                public ParcelableTerm createFromParcel(Parcel in) {
                    return new ParcelableTerm(in);
                }

                public ParcelableTerm[] newArray(int size) {
                    return new ParcelableTerm[size];
                }
            };
}