package com.example.trussell.wgustudentscheduler.util.diff;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import java.util.List;
import com.example.trussell.wgustudentscheduler.model.Term;

public class TermDiffUtil extends DiffUtil.Callback {

    private List<Term> oldTermList;
    private List<Term> newTermList;

    public TermDiffUtil(List<Term> oldTermList, List<Term> newTermList) {
        this.oldTermList = oldTermList;
        this.newTermList = newTermList;
    }

    @Override
    public int getOldListSize() {
        return oldTermList.size();
    }

    @Override
    public int getNewListSize() {
        return newTermList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldTermList.get(oldItemPosition).getId() == newTermList.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldTermList.get(oldItemPosition).equals(newTermList.get(newItemPosition));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}