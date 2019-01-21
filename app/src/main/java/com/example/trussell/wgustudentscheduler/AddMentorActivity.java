package com.example.trussell.wgustudentscheduler;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.example.trussell.wgustudentscheduler.model.Course;
import com.example.trussell.wgustudentscheduler.repo.MentorRepository;
import com.example.trussell.wgustudentscheduler.util.AppUtils;
import com.example.trussell.wgustudentscheduler.util.CurrentData;

public class AddMentorActivity extends AppCompatActivity {

    private EditText name, phone, email;

    private Course course = CurrentData.courseData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mentor);

        findViewsById();
    }

    private void findViewsById() {
        name = findViewById(R.id.nameTextBox);
        phone = findViewById(R.id.phoneTextBox);
        email = findViewById(R.id.emailTextBox);
    }

    public void submitData(View view) {
        String validate = isValid();
        String nameText = name.getText().toString();
        String phoneText = AppUtils.formatPhone(phone.getText().toString());
        String emailText = email.getText().toString();

        if (validate.length() == 0) {
            try {
                int courseID = course.getId();
                MentorRepository mentorRepository = new MentorRepository(getApplicationContext());
                mentorRepository.insertMentor(nameText, phoneText, emailText, courseID);
            } catch (Exception e) {
                AppUtils.showLongMessage(this, e.toString());
            }

            AppUtils.showShortMessage(this, getString(R.string.data_saved));
            finish();
        } else {
            AppUtils.showLongMessage(this, validate);
        }
    }

    private String isValid() {
        StringBuilder errorMsg = new StringBuilder();
        String nameText = name.getText().toString();
        String emailText = email.getText().toString();

        if (AppUtils.isNullOrEmpty(nameText)) {
            errorMsg.append(getString(R.string.valid_name) + "\n");
        }

        if (AppUtils.isNullOrEmpty(emailText) || !AppUtils.isValidEmail(emailText)) {
            errorMsg.append(getString(R.string.valid_email) + "\n");
        }

        return errorMsg.toString();
    }

    public void resetForm(View view) {
        name.setText("");
        phone.setText("");
        email.setText("");
    }
}
