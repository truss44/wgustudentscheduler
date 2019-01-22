package com.example.trussell.wgustudentscheduler.adapter;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.trussell.wgustudentscheduler.R;
import com.example.trussell.wgustudentscheduler.model.Mentor;
import com.example.trussell.wgustudentscheduler.util.diff.MentorDiffUtil;

import java.util.List;

public class MentorsListAdapter extends RecyclerView.Adapter<MentorsListAdapter.CustomViewHolder> {

    private final List<Mentor> mentors;
    public MentorsListAdapter(List<Mentor> mentors) {
        this.mentors = mentors;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_with_subtext,
                parent, false);

        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Mentor mentor = getItem(position);
        holder.itemName.setText(mentor.getName());
        holder.itemSub.setText(mentor.getEmail());
    }

    @Override
    public int getItemCount() {
        return mentors.size();
    }

    public Mentor getItem(int position) {
        return mentors.get(position);
    }

    public void addMentors(List<Mentor> newMentors) {
        MentorDiffUtil mentorDiffUtil = new MentorDiffUtil(mentors, newMentors);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(mentorDiffUtil);
        mentors.clear();
        mentors.addAll(newMentors);
        diffResult.dispatchUpdatesTo(this);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        private final TextView itemName, itemSub;
        CustomViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.nameTextView);
            itemSub = itemView.findViewById(R.id.subTextView);
        }
    }
}