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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.trussell.wgustudentscheduler.model.Assessment;
import com.example.trussell.wgustudentscheduler.model.Course;
import com.example.trussell.wgustudentscheduler.repo.AssessmentRepository;
import com.example.trussell.wgustudentscheduler.util.AppUtils;

import java.util.Calendar;
import java.util.Date;

public class UpdateAssessmentActivity extends AppCompatActivity implements View.OnClickListener {

    private static Course course = DetailsTermActivity.getCourseData();
    private static Assessment assessment = DetailsCourseActivity.getAssessmentData();
    private EditText name, dueDate, goalDate;
    private CheckBox alertGoal;
    private Button saveButton, resetButton;
    private Spinner typeSpinner;
    ArrayAdapter<CharSequence> adapter;

    private DatePickerDialog dueDatePickerDialog, goalDatePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assessment);
        findViewsById();
        setDateTimeField();
        setData();
    }

    private void findViewsById() {
        name = findViewById(R.id.nameTextBox);

        typeSpinner = findViewById(R.id.typeSpinner);
        adapter = ArrayAdapter.createFromResource(this,
                R.array.typeArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);

        dueDate = findViewById(R.id.dueTextBox);
        dueDate.setInputType(InputType.TYPE_NULL);

        goalDate = findViewById(R.id.goalTextBox);
        goalDate.setInputType(InputType.TYPE_NULL);

        alertGoal = findViewById(R.id.alertGoalCheckBox);

        saveButton = findViewById(R.id.saveButton);
        resetButton = findViewById(R.id.resetButton);
    }

    private void setData() {
        name.setText(assessment.getName());
        dueDate.setText(AppUtils.getFormattedDateString(assessment.getDueDate()));
        goalDate.setText(AppUtils.getFormattedDateString(assessment.getGoalDate()));

        String typeValue = typeSpinner.getSelectedItem().toString();
        if (typeValue != null) {
            int spinnerPosition = adapter.getPosition(assessment.getType());
            typeSpinner.setSelection(spinnerPosition);
        } else {
            typeSpinner.setSelection(0);
        }

        boolean alertGoalBoolean = AppUtils.integerToBoolean(assessment.getAlertGoal());
        alertGoal.setChecked(alertGoalBoolean);
    }

    public void submitData(View view) {
        String validate = isValid();
        String nameText = name.getText().toString();
        String spinnerText = typeSpinner.getSelectedItem().toString();
        Date dueDateText = AppUtils.formatStringToDate(dueDate.getText().toString());
        Date goalDateText = AppUtils.formatStringToDate(goalDate.getText().toString());
        int alertGoalInt = AppUtils.booleanToInteger(alertGoal.isChecked());

        assessment.setName(nameText);
        assessment.setType(spinnerText);
        assessment.setDueDate(dueDateText);
        assessment.setGoalDate(goalDateText);
        assessment.setAlertGoal(alertGoalInt);

        if (validate.length() == 0) {
            try {
                AssessmentRepository assessmentRepository = new AssessmentRepository(getApplicationContext());
                assessmentRepository.updateAssessment(assessment);
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

    private boolean saveData(View view) {
            return false;
    }

    public void resetForm(View view) {
        name.setText(assessment.getName());

        String typeValue = typeSpinner.getSelectedItem().toString();
        if (typeValue != null) {
            int spinnerPosition = adapter.getPosition(assessment.getType());
            typeSpinner.setSelection(spinnerPosition);
        } else {
            typeSpinner.setSelection(0);
        }

        dueDate.setInputType(InputType.TYPE_CLASS_TEXT);
        dueDate.setText(AppUtils.getFormattedDateString(assessment.getDueDate()));

        goalDate.setInputType(InputType.TYPE_CLASS_TEXT);
        goalDate.setText(AppUtils.getFormattedDateString(assessment.getGoalDate()));

        boolean alertGoalBoolean = AppUtils.integerToBoolean(assessment.getAlertGoal());
        alertGoal.setChecked(alertGoalBoolean);
    }

    private void setDateTimeField() {
        dueDate.setOnClickListener(this);
        goalDate.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        dueDatePickerDialog = new DatePickerDialog(this, R.style.CustomDatePickerDialog, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dueDate.setText(AppUtils.getFormattedDateString(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        goalDatePickerDialog = new DatePickerDialog(this, R.style.CustomDatePickerDialog, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                goalDate.setText(AppUtils.getFormattedDateString(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
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