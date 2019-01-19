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
import com.example.trussell.wgustudentscheduler.model.Term;
import com.example.trussell.wgustudentscheduler.repo.CourseRepository;
import com.example.trussell.wgustudentscheduler.util.AppUtils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class UpdateCourseActivity extends AppCompatActivity implements View.OnClickListener {

    private static Term term = TermActivity.getTermData();
    private static final Course course = DetailsTermActivity.getCourseData();
    private EditText name, startDate, endDate;
    private CheckBox alertStart, alertEnd;
    private Spinner statusSpinner;
    private ArrayAdapter<CharSequence> adapter;

    private DatePickerDialog startDatePickerDialog, endDatePickerDialog, goalDatePickerDialog;

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

        setData();
    }

    private void findViewsById() {
        name = findViewById(R.id.nameTextBox);

        statusSpinner = findViewById(R.id.statusSpinner);
        adapter = ArrayAdapter.createFromResource(this,
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

    private void setData() {
        name.setText(course.getName());
        startDate.setText(AppUtils.getFormattedDateString(course.getStartDate()));
        endDate.setText(AppUtils.getFormattedDateString(course.getEndDate()));

        String statusValue = statusSpinner.getSelectedItem().toString();
        if (statusValue != null) {
            int spinnerPosition = adapter.getPosition(course.getStatus());
            statusSpinner.setSelection(spinnerPosition);
        } else {
            statusSpinner.setSelection(0);
        }

        boolean alertStartBoolean = AppUtils.integerToBoolean(course.getAlertStart());
        boolean alertEndBoolean = AppUtils.integerToBoolean(course.getAlertEnd());
        alertStart.setChecked(alertStartBoolean);
        alertEnd.setChecked(alertEndBoolean);
    }

    public void submitData(View view) {
        String validate = isValid();
        String nameText = name.getText().toString();
        String spinnerText = statusSpinner.getSelectedItem().toString();
        Date startDateText = AppUtils.formatStringToDate(startDate.getText().toString());
        Date endDateText = AppUtils.formatStringToDate(endDate.getText().toString());
        int alertStartInt = AppUtils.booleanToInteger(alertStart.isChecked());
        int alertEndInt = AppUtils.booleanToInteger(alertEnd.isChecked());

        course.setName(nameText);
        course.setStatus(spinnerText);
        course.setStartDate(startDateText);
        course.setEndDate(endDateText);
        course.setAlertStart(alertStartInt);
        course.setAlertEnd(alertEndInt);

        if (validate.length() == 0) {
            try {
                CourseRepository courseRepository = new CourseRepository(getApplicationContext());
                courseRepository.updateCourse(course);
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
        name.setText(course.getName());

        String statusValue = statusSpinner.getSelectedItem().toString();
        if (statusValue != null) {
            int spinnerPosition = adapter.getPosition(course.getStatus());
            statusSpinner.setSelection(spinnerPosition);
        } else {
            statusSpinner.setSelection(0);
        }

        startDate.setInputType(InputType.TYPE_CLASS_TEXT);
        startDate.setText(AppUtils.getFormattedDateString(course.getStartDate()));

        endDate.setInputType(InputType.TYPE_CLASS_TEXT);
        endDate.setText(AppUtils.getFormattedDateString(course.getEndDate()));

        boolean alertStartBoolean = AppUtils.integerToBoolean(course.getAlertStart());
        boolean alertEndBoolean = AppUtils.integerToBoolean(course.getAlertEnd());
        alertStart.setChecked(alertStartBoolean);
        alertEnd.setChecked(alertEndBoolean);
    }

    private void setDateTimeField() throws ParseException {
        startDate.setOnClickListener(this);
        endDate.setOnClickListener(this);

        Calendar startDateCal = AppUtils.calendarFormat(course.getStartDate().toString());
        Calendar endDateCal = AppUtils.calendarFormat(course.getEndDate().toString());

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
