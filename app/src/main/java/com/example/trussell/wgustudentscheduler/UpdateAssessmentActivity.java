package com.example.trussell.wgustudentscheduler;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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

import com.example.trussell.wgustudentscheduler.model.Assessment;
import com.example.trussell.wgustudentscheduler.model.Course;
import com.example.trussell.wgustudentscheduler.receiver.AlarmReceiver;
import com.example.trussell.wgustudentscheduler.repo.AssessmentRepository;
import com.example.trussell.wgustudentscheduler.util.AppUtils;
import com.example.trussell.wgustudentscheduler.util.CurrentData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UpdateAssessmentActivity extends AppCompatActivity implements View.OnClickListener {

    private Course course = CurrentData.courseData;
    private Assessment assessment = CurrentData.assessmentData;
    private EditText name, dueDate, goalDate;
    private CheckBox alertGoal;
    private Spinner typeSpinner;
    private ArrayAdapter<CharSequence> adapter;

    private DatePickerDialog dueDatePickerDialog, goalDatePickerDialog;

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
    }

    private void setData() {
        name.setText(assessment.getName());
        dueDate.setText(AppUtils.getFormattedDateString(assessment.getDueDate()));
        goalDate.setText(AppUtils.getFormattedDateString(assessment.getGoalDate()));

        String typeValue = typeSpinner.getSelectedItem().toString();
        int spinnerPosition = adapter.getPosition(assessment.getType());
        typeSpinner.setSelection(spinnerPosition);

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
        String goalIdNotificationText = assessment.getAlertGoalID();

        if (validate.length() == 0) {

            if (alertGoal.isChecked()) {
                String endNotificationTitle = "Assessment Reminder";
                String endNotificationText = "Assessment '" + nameText + "' is scheduled for today.";

                Intent goalNotificationIntent = new Intent(this.getApplicationContext(), AlarmReceiver.class);
                goalNotificationIntent.putExtra("mNotificationTitle", endNotificationTitle);
                goalNotificationIntent.putExtra("mNotificationContent", endNotificationText);
                goalNotificationIntent.putExtra("mNotificationId", goalIdNotificationText);

                int requestCode = Integer.parseInt(goalIdNotificationText);

                PendingIntent goalPendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(),
                        requestCode, goalNotificationIntent, 0);

                AlarmManager goalAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                    Date goalDate = dateFormat.parse(AppUtils.getFormattedDateString(goalDateText));
                    Calendar goalCal = Calendar.getInstance();
                    goalCal.setTime(goalDate);
                    goalAlarmManager.set(AlarmManager.RTC_WAKEUP, goalCal.getTimeInMillis(), goalPendingIntent);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                Intent intent = getIntent();
                int requestCode = Integer.parseInt(goalIdNotificationText);
                PendingIntent.getBroadcast(this.getApplicationContext(), requestCode, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT).cancel();
            }

            assessment.setName(nameText);
            assessment.setType(spinnerText);
            assessment.setDueDate(dueDateText);
            assessment.setGoalDate(goalDateText);
            assessment.setAlertGoal(alertGoalInt);
            assessment.setAlertGoalID(goalIdNotificationText);

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
        name.setText(assessment.getName());

        String typeValue = typeSpinner.getSelectedItem().toString();
        int spinnerPosition = adapter.getPosition(assessment.getType());
        typeSpinner.setSelection(spinnerPosition);

        dueDate.setInputType(InputType.TYPE_CLASS_TEXT);
        dueDate.setText(AppUtils.getFormattedDateString(assessment.getDueDate()));

        goalDate.setInputType(InputType.TYPE_CLASS_TEXT);
        goalDate.setText(AppUtils.getFormattedDateString(assessment.getGoalDate()));

        boolean alertGoalBoolean = AppUtils.integerToBoolean(assessment.getAlertGoal());
        alertGoal.setChecked(alertGoalBoolean);
    }

    private void setDateTimeField() throws ParseException {
        dueDate.setOnClickListener(this);
        goalDate.setOnClickListener(this);

        Calendar dueDateCal = AppUtils.calendarFormat(assessment.getDueDate().toString());
        Calendar goalDateCal = AppUtils.calendarFormat(assessment.getGoalDate().toString());

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
