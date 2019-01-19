package com.example.trussell.wgustudentscheduler;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.trussell.wgustudentscheduler.model.Note;
import com.example.trussell.wgustudentscheduler.repo.NoteRepository;
import com.example.trussell.wgustudentscheduler.util.AppUtils;

public class UpdateNoteActivity extends AppCompatActivity {

    private EditText name, entry;
    private Button saveButton, resetButton;

    private static Note note = DetailsCourseActivity.getNoteData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        findViewsById();
        setData();
    }

    private void findViewsById() {
        name = findViewById(R.id.nameTextBox);
        entry = findViewById(R.id.entryTextBox);

        saveButton = findViewById(R.id.saveButton);
        resetButton = findViewById(R.id.resetButton);
    }

    private void setData() {
        name.setText(note.getName());
        entry.setText(note.getEntry());
    }

    public void submitData(View view) {
        String validate = isValid();
        String nameText = name.getText().toString();
        String entryText = entry.getText().toString();

        note.setName(nameText);
        note.setEntry(entryText);

        if (validate.length() == 0) {
            try {
                NoteRepository noteRepository = new NoteRepository(getApplicationContext());
                noteRepository.updateNote(note);
            } catch (Exception e) {
                AppUtils.showLongMessage(this, e.toString());
            }

            AppUtils.showShortMessage(this, getString(R.string.data_saved));
            finish();

        } else {
            AppUtils.showLongMessage(this, validate);
        }
    }

    public String isValid() {
        StringBuilder errorMsg = new StringBuilder();
        String nameText = name.getText().toString();
        String entryText = entry.getText().toString();

        if (AppUtils.isNullOrEmpty(nameText)) {
            errorMsg.append(getString(R.string.valid_name) + "\n");
        }

        if (AppUtils.isNullOrEmpty(entryText)) {
            errorMsg.append(getString(R.string.valid_note) + "\n");
        }

        return errorMsg.toString();
    }

    private boolean saveData(View view) {
            return false;
    }

    public void resetForm(View view) {
        name.setText(note.getName());
        entry.setText(note.getEntry());
    }
}
