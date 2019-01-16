package com.example.trussell.wgustudentscheduler.adapter;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.trussell.wgustudentscheduler.R;
import com.example.trussell.wgustudentscheduler.model.Course;
import com.example.trussell.wgustudentscheduler.util.diff.CourseDiffUtil;

import java.util.List;

public class CoursesListAdapter extends RecyclerView.Adapter<CoursesListAdapter.CustomViewHolder> {

    private List<Course> courses;
    public CoursesListAdapter(List<Course> courses) {
        this.courses = courses;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Course course = getItem(position);
        holder.itemName.setText(course.getName());
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public Course getItem(int position) {
        return courses.get(position);
    }

    public void addCourses(List<Course> newCourses) {
        CourseDiffUtil courseDiffUtil = new CourseDiffUtil(courses, newCourses);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(courseDiffUtil);
        courses.clear();
        courses.addAll(newCourses);
        diffResult.dispatchUpdatesTo(this);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        private TextView itemName;
        CustomViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.nameTextView);
        }
    }
}