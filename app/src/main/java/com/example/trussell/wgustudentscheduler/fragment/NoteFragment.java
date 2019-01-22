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

import com.example.trussell.wgustudentscheduler.DetailsNoteActivity;
import com.example.trussell.wgustudentscheduler.R;
import com.example.trussell.wgustudentscheduler.adapter.NotesListAdapter;
import com.example.trussell.wgustudentscheduler.model.Course;
import com.example.trussell.wgustudentscheduler.model.Note;
import com.example.trussell.wgustudentscheduler.repo.NoteRepository;
import com.example.trussell.wgustudentscheduler.util.CurrentData;
import com.example.trussell.wgustudentscheduler.util.RecyclerViewClickListener;
import com.example.trussell.wgustudentscheduler.util.RecyclerViewTouchListener;

import java.util.List;
import java.util.Objects;

public class NoteFragment extends Fragment {

    RecyclerView recyclerView;
    NotesListAdapter notesListAdapter = null;
    NoteRepository noteRepository;
    TextView emptyView;
    private Course course = CurrentData.courseData;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.notes_tab_course, container, false);
        noteRepository = new NoteRepository(getContext());

        recyclerView = view.findViewById(R.id.itemsList);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1 , StaggeredGridLayoutManager.VERTICAL));
        recyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(getContext(), recyclerView, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                Note note = notesListAdapter.getItem(position);
                Intent detailsScreenIntent = new Intent(getContext(), DetailsNoteActivity.class);
                CurrentData.noteData = note;
                startActivity(detailsScreenIntent);
            }

            @Override
            public void onLongClick(final View view, final int position) {
            }
        }));

        recyclerView.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getContext()), LinearLayoutManager.VERTICAL));

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
