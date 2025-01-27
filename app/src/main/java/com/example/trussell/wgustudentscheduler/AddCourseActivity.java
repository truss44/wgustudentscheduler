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

import com.example.trussell.wgustudentscheduler.model.Term;
import com.example.trussell.wgustudentscheduler.receiver.AlarmReceiver;
import com.example.trussell.wgustudentscheduler.repo.CourseRepository;
import com.example.trussell.wgustudentscheduler.util.AppUtils;
import com.example.trussell.wgustudentscheduler.util.CurrentData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class AddCourseActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText name, startDate, endDate;
    private CheckBox alertStart, alertEnd;
    private Spinner statusSpinner;

    private DatePickerDialog startDatePickerDialog, endDatePickerDialog;

    private Term term = CurrentData.termData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        findViewsById();

        try {
            setDateTimeField();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void findViewsById() {
        name = findViewById(R.id.nameTextBox);

        statusSpinner = findViewById(R.id.statusSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.statusArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(adapter);

        startDate = findViewById(R.id.startTextBox);
        startDate.setInputType(InputType.TYPE_NULL);

        endDate = findViewById(R.id.endTextBox);
        endDate.setInputType(InputType.TYPE_NULL);

        alertStart = findViewById(R.id.alertStartCheckBox);
        alertEnd = findViewById(R.id.alertEndCheckBox);
    }

    public void submitData(View view) {
        String validate = isValid();
        String nameText = name.getText().toString();
        String spinnerText = statusSpinner.getSelectedItem().toString();
        Date startDateText = AppUtils.formatStringToDate(startDate.getText().toString());
        Date endDateText = AppUtils.formatStringToDate(endDate.getText().toString());
        int alertStartInt = AppUtils.booleanToInteger(alertStart.isChecked());
        int alertEndInt = AppUtils.booleanToInteger(alertEnd.isChecked());
        String startIdNotificationText = null;
        String endIdNotificationText = null;

        if (validate.length() == 0) {

            if (alertStart.isChecked()) {
                String startNotificationTitle = "Course Start Reminder";
                String startNotificationText = nameText + " begins today.";

                startIdNotificationText = Integer.toString(new Random().nextInt(10000));

                Intent startNotificationIntent = new Intent(this.getApplicationContext(), AlarmReceiver.class);
                startNotificationIntent.putExtra("mNotificationTitle", startNotificationTitle);
                startNotificationIntent.putExtra("mNotificationContent", startNotificationText);
                startNotificationIntent.putExtra("mNotificationId", startIdNotificationText);

                int requestCode = Integer.parseInt(startIdNotificationText);

                PendingIntent startPendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(),
                        requestCode, startNotificationIntent, 0);

                AlarmManager startAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                    Date startDate = dateFormat.parse(AppUtils.getFormattedDateString(startDateText));
                    Calendar startCal = Calendar.getInstance();
                    startCal.setTime(startDate);
                    startAlarmManager.set(AlarmManager.RTC_WAKEUP, startCal.getTimeInMillis(), startPendingIntent);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            if (alertEnd.isChecked()) {
                String endNotificationTitle = "Course End Reminder";
                String endNotificationText = nameText + " ends today.";

                endIdNotificationText = Integer.toString(new Random().nextInt(10000));

                Intent endNotificationIntent = new Intent(this.getApplicationContext(), AlarmReceiver.class);
                endNotificationIntent.putExtra("mNotificationTitle", endNotificationTitle);
                endNotificationIntent.putExtra("mNotificationContent", endNotificationText);
                endNotificationIntent.putExtra("mNotificationId", endIdNotificationText);

                int requestCode = Integer.parseInt(endIdNotificationText);

                PendingIntent endPendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(),
                        requestCode, endNotificationIntent, 0);

                AlarmManager endAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                    Date endDate = dateFormat.parse(AppUtils.getFormattedDateString(endDateText));
                    Calendar endCal = Calendar.getInstance();
                    endCal.setTime(endDate);
                    endAlarmManager.set(AlarmManager.RTC_WAKEUP, endCal.getTimeInMillis(), endPendingIntent);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            try {
                CourseRepository courseRepository = new CourseRepository(getApplicationContext());
                courseRepository.insertCourse(nameText, spinnerText, startDateText, endDateText,
                        alertStartInt, alertEndInt, term.getId(), startIdNotificationText, endIdNotificationText);
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
        String spinnerText = statusSpinner.getSelectedItem().toString();
        String startDateText = startDate.getText().toString();
        String endDateText = endDate.getText().toString();

        if (AppUtils.isNullOrEmpty(nameText)) {
            errorMsg.append(getString(R.string.valid_name) + "\n");
        }

        if (AppUtils.isNullOrEmpty(spinnerText)) {
            errorMsg.append(getString(R.string.valid_status) + "\n");
        }

        if (AppUtils.isNullOrEmpty(startDateText) || !AppUtils.dateValidation(startDateText)) {
            errorMsg.append(getString(R.string.valid_start_date) + "\n");
        }

        if (AppUtils.isNullOrEmpty(endDateText) || !AppUtils.dateValidation(endDateText)) {
            errorMsg.append(getString(R.string.valid_end_date) + "\n");
        }

        try {
            if (AppUtils.formatStringToDate(startDateText).after(AppUtils.formatStringToDate(endDateText)) ||
                    AppUtils.formatStringToDate(startDateText).equals(AppUtils.formatStringToDate(endDateText))) {
                errorMsg.append(getString(R.string.date_after_start) + "\n");
            }
        } catch (Exception e) {
            errorMsg.append(getString(R.string.proper_start_end) + "\n");
        }

        return errorMsg.toString();
    }

    public void resetForm(View view) {
        name.setText("");
        statusSpinner.setSelection(0);
        startDate.setText("");
        endDate.setText("");
        alertStart.setChecked(true);
        alertEnd.setChecked(true);
    }

    private void setDateTimeField() throws ParseException {
        startDate.setOnClickListener(this);
        endDate.setOnClickListener(this);

        Calendar startDateCal = AppUtils.calendarFormat(startDate.getText().toString());
        Calendar endDateCal = AppUtils.calendarFormat(endDate.getText().toString());

        Calendar minDate = AppUtils.calendarFormat(term.getStartDate().toString());
        Calendar maxDate = AppUtils.calendarFormat(term.getEndDate().toString());

        startDatePickerDialog = new DatePickerDialog(this, R.style.CustomDatePickerDialog, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                startDate.setText(AppUtils.getFormattedDateString(newDate.getTime()));
            }

        },startDateCal.get(Calendar.YEAR), startDateCal.get(Calendar.MONTH), startDateCal.get(Calendar.DAY_OF_MONTH));

        startDatePickerDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());
        startDatePickerDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());

        endDatePickerDialog = new DatePickerDialog(this, R.style.CustomDatePickerDialog, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                endDate.setText(AppUtils.getFormattedDateString(newDate.getTime()));
            }

        },endDateCal.get(Calendar.YEAR), endDateCal.get(Calendar.MONTH), endDateCal.get(Calendar.DAY_OF_MONTH));

        endDatePickerDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());
        endDatePickerDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());
    }

    @Override
    public void onClick(View view) {
        CoordinatorLayout mainLayout;
        mainLayout = findViewById(R.id.mainLayout);
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);

        if (view == startDate) {
            startDatePickerDialog.show();
        } else if (view == endDate) {
            endDatePickerDialog.show();
        }
    }
}
