package com.example.trussell.wgustudentscheduler;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.trussell.wgustudentscheduler.model.Course;
import com.example.trussell.wgustudentscheduler.model.Term;
import com.example.trussell.wgustudentscheduler.repo.NoteRepository;
import com.example.trussell.wgustudentscheduler.util.AppUtils;

public class AddNoteActivity extends AppCompatActivity {

    private EditText name, entry;
    private Button saveButton, resetButton;

    private static Term term = TermActivity.getTermData();
    private static Course course = DetailsTermActivity.getCourseData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        findViewsById();
    }

    private void findViewsById() {
        name = findViewById(R.id.nameTextBox);
        entry = findViewById(R.id.entryTextBox);

        saveButton = findViewById(R.id.saveButton);
        resetButton = findViewById(R.id.resetButton);
    }

    public void submitData(View view) {
        String validate = isValid();
        String nameText = name.getText().toString();
        String entryText = entry.getText().toString();

        if (validate.length() == 0) {
            try {
                int courseID = course.getId();
                NoteRepository noteRepository = new NoteRepository(getApplicationContext());
                noteRepository.insertNote(nameText, entryText, courseID);
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
        name.setText("");
        entry.setText("");
    }
}
