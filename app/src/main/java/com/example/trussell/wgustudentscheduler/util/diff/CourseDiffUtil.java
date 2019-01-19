package com.example.trussell.wgustudentscheduler.util.diff;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.example.trussell.wgustudentscheduler.model.Course;

import java.util.List;

public class CourseDiffUtil extends DiffUtil.Callback {

    private final List<Course> oldCourseList;
    private final List<Course> newCourseList;

    public CourseDiffUtil(List<Course> oldCourseList, List<Course> newCourseList) {
        this.oldCourseList = oldCourseList;
        this.newCourseList = newCourseList;
    }

    @Override
    public int getOldListSize() {
        return oldCourseList.size();
    }

    @Override
    public int getNewListSize() {
        return newCourseList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldCourseList.get(oldItemPosition).getId() == newCourseList.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldCourseList.get(oldItemPosition).equals(newCourseList.get(newItemPosition));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}