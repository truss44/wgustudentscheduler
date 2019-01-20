package com.example.trussell.wgustudentscheduler.fragment;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.trussell.wgustudentscheduler.DetailsTermActivity;
import com.example.trussell.wgustudentscheduler.R;
import com.example.trussell.wgustudentscheduler.adapter.MentorsListAdapter;
import com.example.trussell.wgustudentscheduler.model.Mentor;
import com.example.trussell.wgustudentscheduler.model.Course;
import com.example.trussell.wgustudentscheduler.repo.MentorRepository;
import com.example.trussell.wgustudentscheduler.util.AppUtils;
import com.example.trussell.wgustudentscheduler.util.RecyclerViewClickListener;
import com.example.trussell.wgustudentscheduler.util.RecyclerViewTouchListener;

import java.util.List;

public class MentorFragment extends Fragment {

    RecyclerView recyclerView;
    MentorsListAdapter mentorsListAdapter = null;
    MentorRepository mentorRepository;
    TextView emptyView;
    private static final Course course = DetailsTermActivity.getCourseData();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.mentors_tab_course, container, false);
        mentorRepository = new MentorRepository(getContext());

        recyclerView = view.findViewById(R.id.itemsList);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1 , StaggeredGridLayoutManager.VERTICAL));
        recyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(getContext(), recyclerView, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                AppUtils.showShortMessage(getContext(), "testing");
            }

            @Override
            public void onLongClick(final View view, final int position) {
            }
        }));

        emptyView = view.findViewById(R.id.emptyView);

        updateMentorsList(course.getId());

        recyclerView.setAdapter(mentorsListAdapter);
        return view;
    }

    private void updateMentorsList(int id) {
        mentorRepository.fetchMentorsByCourse(id).observe(this, new Observer<List<Mentor>>() {
            @Override
            public void onChanged(@Nullable List<Mentor> mentors) {
                if (mentors != null && mentors.size() > 0) {
                    emptyView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    if (mentorsListAdapter == null) {
                        mentorsListAdapter = new MentorsListAdapter(mentors);
                        recyclerView.setAdapter(mentorsListAdapter);
                    } else {
                        mentorsListAdapter.addMentors(mentors);
                    }
                } else {
                    emptyView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }
        });
    }
}
