package com.example.trussell.wgustudentscheduler.util.diff;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.example.trussell.wgustudentscheduler.model.Assessment;

import java.util.List;

public class AssessmentDiffUtil extends DiffUtil.Callback {

    private List<Assessment> oldAssessmentList;
    private List<Assessment> newAssessmentList;

    public AssessmentDiffUtil(List<Assessment> oldAssessmentList, List<Assessment> newAssessmentList) {
        this.oldAssessmentList = oldAssessmentList;
        this.newAssessmentList = newAssessmentList;
    }

    @Override
    public int getOldListSize() {
        return oldAssessmentList.size();
    }

    @Override
    public int getNewListSize() {
        return newAssessmentList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldAssessmentList.get(oldItemPosition).getId() == newAssessmentList.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldAssessmentList.get(oldItemPosition).equals(newAssessmentList.get(newItemPosition));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}