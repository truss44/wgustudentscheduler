package com.example.trussell.wgustudentscheduler.adapter;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.trussell.wgustudentscheduler.R;
import com.example.trussell.wgustudentscheduler.model.Assessment;
import com.example.trussell.wgustudentscheduler.util.diff.AssessmentDiffUtil;

import java.text.DateFormat;
import java.util.List;

public class AssessmentsListAdapter extends RecyclerView.Adapter<AssessmentsListAdapter.CustomViewHolder> {

    private final List<Assessment> assessments;
    public AssessmentsListAdapter(List<Assessment> assessments) {
        this.assessments = assessments;
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
        Assessment assessment = getItem(position);

        String dueDate = DateFormat.getDateInstance(DateFormat.LONG).format(assessment.getDueDate());
        String subData = "Due Date: " + dueDate;

        holder.itemName.setText(assessment.getName());
        holder.itemSub.setText(subData);
    }

    @Override
    public int getItemCount() {
        return assessments.size();
    }

    public Assessment getItem(int position) {
        return assessments.get(position);
    }

    public void addAssessments(List<Assessment> newAssessments) {
        AssessmentDiffUtil assessmentDiffUtil = new AssessmentDiffUtil(assessments, newAssessments);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(assessmentDiffUtil);
        assessments.clear();
        assessments.addAll(newAssessments);
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