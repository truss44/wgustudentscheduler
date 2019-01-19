package com.example.trussell.wgustudentscheduler;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.trussell.wgustudentscheduler.model.Course;
import com.example.trussell.wgustudentscheduler.repo.AssessmentRepository;
import com.example.trussell.wgustudentscheduler.util.AppUtils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class AddAssessmentActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText name, dueDate, goalDate;
    private CheckBox alertGoal;
    private Spinner typeSpinner;

    private DatePickerDialog dueDatePickerDialog, goalDatePickerDialog;

    private static final Course course = DetailsTermActivity.getCourseData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assessment);
        findViewsById();

        try {
            setDateTimeField();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void findViewsById() {
        name = findViewById(R.id.nameTextBox);

        typeSpinner = findViewById(R.id.typeSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.typeArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);

        dueDate = findViewById(R.id.dueTextBox);
        dueDate.setInputType(InputType.TYPE_NULL);

        goalDate = findViewById(R.id.goalTextBox);
        goalDate.setInputType(InputType.TYPE_NULL);

        alertGoal = findViewById(R.id.alertGoalCheckBox);
    }

    public void submitData(View view) {
        String validate = isValid();
        String nameText = name.getText().toString();
        String spinnerText = typeSpinner.getSelectedItem().toString();
        Date dueDateText = AppUtils.formatStringToDate(dueDate.getText().toString());
        Date goalDateText = AppUtils.formatStringToDate(goalDate.getText().toString());
        int alertGoalInt = AppUtils.booleanToInteger(alertGoal.isChecked());

        if (validate.length() == 0) {
            try {
                AssessmentRepository assessmentRepository = new AssessmentRepository(getApplicationContext());
                assessmentRepository.insertAssessment(nameText, spinnerText, dueDateText,
                        goalDateText, alertGoalInt, course.getId());
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
        String spinnerText = typeSpinner.getSelectedItem().toString();
        String dueDateText = dueDate.getText().toString();
        String goalDateText = goalDate.getText().toString();

        if (AppUtils.isNullOrEmpty(nameText)) {
            errorMsg.append(getString(R.string.valid_name) + "\n");
        }

        if (AppUtils.isNullOrEmpty(spinnerText)) {
            errorMsg.append(getString(R.string.valid_assessment_type) + "\n");
        }

        if (AppUtils.isNullOrEmpty(dueDateText) || !AppUtils.dateValidation(dueDateText)) {
            errorMsg.append(getString(R.string.valid_due_date) + "\n");
        }

        if (AppUtils.isNullOrEmpty(goalDateText) || !AppUtils.dateValidation(goalDateText)) {
            errorMsg.append(getString(R.string.valid_goal_date) + "\n");
        }

        try {
            if (AppUtils.formatStringToDate(goalDateText).after(AppUtils.formatStringToDate(dueDateText))) {
                errorMsg.append(getString(R.string.goal_date_before_due_date) + "\n");
            }
        } catch (Exception e) {
            errorMsg.append(getString(R.string.proper_goal_date) + "\n");
        }

        return errorMsg.toString();
    }

    public void resetForm(View view) {
        name.setText("");
        typeSpinner.setSelection(0);
        dueDate.setText("");
        goalDate.setText("");
        alertGoal.setChecked(true);
    }

    private void setDateTimeField() throws ParseException {
        dueDate.setOnClickListener(this);
        goalDate.setOnClickListener(this);

        Calendar dueDateCal = AppUtils.calendarFormat(dueDate.getText().toString());
        Calendar goalDateCal = AppUtils.calendarFormat(goalDate.getText().toString());

        Calendar minDate = AppUtils.calendarFormat(course.getStartDate().toString());
        Calendar maxDate = AppUtils.calendarFormat(course.getEndDate().toString());

        dueDatePickerDialog = new DatePickerDialog(this, R.style.CustomDatePickerDialog, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dueDate.setText(AppUtils.getFormattedDateString(newDate.getTime()));
            }

        },dueDateCal.get(Calendar.YEAR), dueDateCal.get(Calendar.MONTH), dueDateCal.get(Calendar.DAY_OF_MONTH));

        dueDatePickerDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());
        dueDatePickerDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());

        goalDatePickerDialog = new DatePickerDialog(this, R.style.CustomDatePickerDialog, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                goalDate.setText(AppUtils.getFormattedDateString(newDate.getTime()));
            }

        },goalDateCal.get(Calendar.YEAR), goalDateCal.get(Calendar.MONTH), goalDateCal.get(Calendar.DAY_OF_MONTH));

        goalDatePickerDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());
        goalDatePickerDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());
    }

    @Override
    public void onClick(View view) {
        CoordinatorLayout mainLayout;
        mainLayout = findViewById(R.id.mainLayout);
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);

        if (view == dueDate) {
            dueDatePickerDialog.show();
        } else if (view == goalDate) {
            goalDatePickerDialog.show();
        }
    }
}
