package com.example.trussell.wgustudentscheduler;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.trussell.wgustudentscheduler.model.Note;
import com.example.trussell.wgustudentscheduler.repo.NoteRepository;
import com.example.trussell.wgustudentscheduler.util.CurrentData;

import java.text.DateFormat;

public class DetailsNoteActivity extends AppCompatActivity {

    private Note note = CurrentData.noteData;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_note);
        
        TextView label1 = findViewById(R.id.label1);

        TextView name = findViewById(R.id.nameTextView);
        TextView entry = findViewById(R.id.entryTextView);

        TextView[] textViewArray = { label1 };
        for (TextView tv : textViewArray) {
            tv.append(":");
        }

        name.setText(note.getName());
        entry.setText(note.getEntry());
    }

    public void updateNote(View view) {
        Intent updateScreenIntent = new Intent(this, UpdateNoteActivity.class);
        startActivity(updateScreenIntent);
    }

    public void removeNote(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage(R.string.remove_note_alert);
        builder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        Thread thread = new Thread(new Runnable(){
                            public void run() {
                                NoteRepository noteRepository = new NoteRepository(getBaseContext());
                                noteRepository.deleteNote(note.getId());
                            }
                        });

                        thread.start();

                        try {
                            thread.join(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        finish();
                    }
                });

        builder.setNegativeButton(android.R.string.no,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        builder.create().show();
    }

    @Override
    public void onRestart() {
        super.onRestart();
        recreate();
    }
}
