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
import com.example.trussell.wgustudentscheduler.adapter.NotesListAdapter;
import com.example.trussell.wgustudentscheduler.model.Note;
import com.example.trussell.wgustudentscheduler.model.Course;
import com.example.trussell.wgustudentscheduler.repo.NoteRepository;
import com.example.trussell.wgustudentscheduler.util.AppUtils;
import com.example.trussell.wgustudentscheduler.util.RecyclerViewClickListener;
import com.example.trussell.wgustudentscheduler.util.RecyclerViewTouchListener;

import java.util.List;

public class NoteFragment extends Fragment {

    RecyclerView recyclerView;
    NotesListAdapter notesListAdapter = null;
    NoteRepository noteRepository;
    TextView emptyView;
    private static final Course course = DetailsTermActivity.getCourseData();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.notes_tab_course, container, false);
        noteRepository = new NoteRepository(getContext());

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

        updateNotesList(course.getId());

        recyclerView.setAdapter(notesListAdapter);
        return view;
    }

    private void updateNotesList(int id) {
        noteRepository.fetchNotesByCourse(id).observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                if (notes != null && notes.size() > 0) {
                    emptyView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    if (notesListAdapter == null) {
                        notesListAdapter = new NotesListAdapter(notes);
                        recyclerView.setAdapter(notesListAdapter);
                    } else {
                        notesListAdapter.addNotes(notes);
                    }
                } else {
                    emptyView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }
        });
    }
}
