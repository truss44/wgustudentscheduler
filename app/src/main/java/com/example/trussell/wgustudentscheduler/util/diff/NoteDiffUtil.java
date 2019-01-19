package com.example.trussell.wgustudentscheduler.util.diff;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.example.trussell.wgustudentscheduler.model.Note;

import java.util.List;

public class NoteDiffUtil extends DiffUtil.Callback {

    private final List<Note> oldNoteList;
    private final List<Note> newNoteList;

    public NoteDiffUtil(List<Note> oldNoteList, List<Note> newNoteList) {
        this.oldNoteList = oldNoteList;
        this.newNoteList = newNoteList;
    }

    @Override
    public int getOldListSize() {
        return oldNoteList.size();
    }

    @Override
    public int getNewListSize() {
        return newNoteList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldNoteList.get(oldItemPosition).getId() == newNoteList.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldNoteList.get(oldItemPosition).equals(newNoteList.get(newItemPosition));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}