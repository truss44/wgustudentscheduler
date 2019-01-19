package com.example.trussell.wgustudentscheduler.util.diff;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.example.trussell.wgustudentscheduler.model.Mentor;

import java.util.List;

public class MentorDiffUtil extends DiffUtil.Callback {

    private final List<Mentor> oldMentorList;
    private final List<Mentor> newMentorList;

    public MentorDiffUtil(List<Mentor> oldMentorList, List<Mentor> newMentorList) {
        this.oldMentorList = oldMentorList;
        this.newMentorList = newMentorList;
    }

    @Override
    public int getOldListSize() {
        return oldMentorList.size();
    }

    @Override
    public int getNewListSize() {
        return newMentorList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldMentorList.get(oldItemPosition).getId() == newMentorList.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldMentorList.get(oldItemPosition).equals(newMentorList.get(newItemPosition));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}