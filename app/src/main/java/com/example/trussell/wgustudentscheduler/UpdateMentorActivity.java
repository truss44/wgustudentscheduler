package com.example.trussell.wgustudentscheduler;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.example.trussell.wgustudentscheduler.model.Mentor;
import com.example.trussell.wgustudentscheduler.repo.MentorRepository;
import com.example.trussell.wgustudentscheduler.util.AppUtils;
import com.example.trussell.wgustudentscheduler.util.CurrentData;

public class UpdateMentorActivity extends AppCompatActivity {

    private EditText name, phone, email;

    private Mentor mentor = CurrentData.mentorData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mentor);
        findViewsById();
        setData();
    }

    private void findViewsById() {
        name = findViewById(R.id.nameTextBox);
        phone = findViewById(R.id.phoneTextBox);
        email = findViewById(R.id.emailTextBox);
    }

    private void setData() {
        name.setText(mentor.getName());
        phone.setText(AppUtils.formatPhone(mentor.getPhone()));
        email.setText(mentor.getEmail());
    }

    public void submitData(View view) {
        String validate = isValid();
        String nameText = name.getText().toString();
        String phoneText = AppUtils.formatPhone(phone.getText().toString());
        String emailText = email.getText().toString();

        if (validate.length() == 0) {

            mentor.setName(nameText);
            mentor.setPhone(phoneText);
            mentor.setEmail(emailText);

            try {
                MentorRepository mentorRepository = new MentorRepository(getApplicationContext());
                mentorRepository.updateMentor(mentor);
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
        String phoneText = phone.getText().toString();
        String emailText = email.getText().toString();

        if (AppUtils.isNullOrEmpty(nameText)) {
            errorMsg.append(getString(R.string.valid_name) + "\n");
        }

        if (AppUtils.isNullOrEmpty(phoneText)) {
            errorMsg.append(getString(R.string.valid_phone_number) + "\n");
        }

        if (AppUtils.isNullOrEmpty(emailText) || !AppUtils.isValidEmail(emailText)) {
            errorMsg.append(getString(R.string.valid_email) + "\n");
        }

        return errorMsg.toString();
    }

    public void resetForm(View view) {
        name.setText(mentor.getName());
        phone.setText(mentor.getPhone());
        email.setText(mentor.getEmail());
    }
}
