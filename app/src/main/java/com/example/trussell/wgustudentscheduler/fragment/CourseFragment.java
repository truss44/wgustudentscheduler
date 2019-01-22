package com.example.trussell.wgustudentscheduler.fragment;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.trussell.wgustudentscheduler.DetailsCourseActivity;
import com.example.trussell.wgustudentscheduler.R;
import com.example.trussell.wgustudentscheduler.adapter.CoursesListAdapter;
import com.example.trussell.wgustudentscheduler.model.Course;
import com.example.trussell.wgustudentscheduler.model.Term;
import com.example.trussell.wgustudentscheduler.repo.CourseRepository;
import com.example.trussell.wgustudentscheduler.util.CurrentData;
import com.example.trussell.wgustudentscheduler.util.RecyclerViewClickListener;
import com.example.trussell.wgustudentscheduler.util.RecyclerViewTouchListener;

import java.util.List;
import java.util.Objects;

public class CourseFragment extends Fragment {

    RecyclerView recyclerView;
    CoursesListAdapter coursesListAdapter = null;
    CourseRepository courseRepository;
    TextView emptyView;
    private Term term = CurrentData.termData;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.courses_tab_term, container, false);
        courseRepository = new CourseRepository(getContext());

        recyclerView = view.findViewById(R.id.itemsList);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1 , StaggeredGridLayoutManager.VERTICAL));
        recyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(getContext(), recyclerView, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                Course course = coursesListAdapter.getItem(position);
                Intent detailsScreenIntent = new Intent(getContext(), DetailsCourseActivity.class);
                CurrentData.courseData = course;
                startActivity(detailsScreenIntent);
            }

            @Override
            public void onLongClick(final View view, final int position) {
            }
        }));

        recyclerView.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getContext()), LinearLayoutManager.VERTICAL));

        emptyView = view.findViewById(R.id.emptyView);

        updateCoursesList(term.getId());

        recyclerView.setAdapter(coursesListAdapter);
        return view;
    }

    private void updateCoursesList(int id) {
        courseRepository.fetchCoursesByTerm(id).observe(this, new Observer<List<Course>>() {
            @Override
            public void onChanged(@Nullable List<Course> courses) {
                if (courses != null && courses.size() > 0) {
                    emptyView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    if (coursesListAdapter == null) {
                        coursesListAdapter = new CoursesListAdapter(courses);
                        recyclerView.setAdapter(coursesListAdapter);
                    } else {
                        coursesListAdapter.addCourses(courses);
                    }
                } else {
                    emptyView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }
        });
    }
}
