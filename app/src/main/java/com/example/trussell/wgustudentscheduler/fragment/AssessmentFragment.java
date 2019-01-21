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

import com.example.trussell.wgustudentscheduler.DetailsAssessmentActivity;
import com.example.trussell.wgustudentscheduler.DetailsCourseActivity;
import com.example.trussell.wgustudentscheduler.DetailsTermActivity;
import com.example.trussell.wgustudentscheduler.R;
import com.example.trussell.wgustudentscheduler.adapter.AssessmentsListAdapter;
import com.example.trussell.wgustudentscheduler.model.Assessment;
import com.example.trussell.wgustudentscheduler.model.Course;
import com.example.trussell.wgustudentscheduler.repo.AssessmentRepository;
import com.example.trussell.wgustudentscheduler.util.AppUtils;
import com.example.trussell.wgustudentscheduler.util.CurrentData;
import com.example.trussell.wgustudentscheduler.util.RecyclerViewClickListener;
import com.example.trussell.wgustudentscheduler.util.RecyclerViewTouchListener;

import java.util.List;

public class AssessmentFragment extends Fragment {

    RecyclerView recyclerView;
    AssessmentsListAdapter assessmentsListAdapter = null;
    AssessmentRepository assessmentRepository;
    TextView emptyView;
    private Course course = CurrentData.courseData;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.assessments_tab_course, container, false);
        assessmentRepository = new AssessmentRepository(getContext());

        recyclerView = view.findViewById(R.id.itemsList);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1 , StaggeredGridLayoutManager.VERTICAL));
        recyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(getContext(), recyclerView, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                Assessment assessment = assessmentsListAdapter.getItem(position);
                Intent detailsScreenIntent = new Intent(getContext(), DetailsAssessmentActivity.class);
                CurrentData.assessmentData = assessment;
                startActivity(detailsScreenIntent);
            }

            @Override
            public void onLongClick(final View view, final int position) {
            }
        }));

        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        emptyView = view.findViewById(R.id.emptyView);

        updateAssessmentsList(course.getId());

        recyclerView.setAdapter(assessmentsListAdapter);
        return view;
    }

    private void updateAssessmentsList(int id) {
        assessmentRepository.fetchAssessmentsByCourse(id).observe(this, new Observer<List<Assessment>>() {
            @Override
            public void onChanged(@Nullable List<Assessment> assessments) {
                if (assessments != null && assessments.size() > 0) {
                    emptyView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    if (assessmentsListAdapter == null) {
                        assessmentsListAdapter = new AssessmentsListAdapter(assessments);
                        recyclerView.setAdapter(assessmentsListAdapter);
                    } else {
                        assessmentsListAdapter.addAssessments(assessments);
                    }
                } else {
                    emptyView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }
        });
    }
}
