package com.example.trussell.wgustudentscheduler;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.trussell.wgustudentscheduler.repo.TermRepository;
import com.example.trussell.wgustudentscheduler.util.AppUtils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class AddTermActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText name, startDate, endDate;

    private DatePickerDialog startDatePickerDialog;
    private DatePickerDialog endDatePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_term);
        findViewsById();

        try {
            setDateTimeField();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void findViewsById() {
        name = findViewById(R.id.nameTextBox);

        startDate = findViewById(R.id.startTextBox);
        startDate.setInputType(InputType.TYPE_NULL);

        endDate = findViewById(R.id.endTextBox);
        endDate.setInputType(InputType.TYPE_NULL);
    }

    public void submitData(View view) {
        String validate = isValid();
        String nameText = name.getText().toString();
        Date startDateText = AppUtils.formatStringToDate(startDate.getText().toString());
        Date endDateText = AppUtils.formatStringToDate(endDate.getText().toString());

        if (validate.length() == 0) {
            try {
                TermRepository termRepository = new TermRepository(getApplicationContext());
                termRepository.insertTerm(nameText, startDateText, endDateText);
            } catch (Exception e) {
                AppUtils.showLongMessage(this, e.toString());
            }

            AppUtils.showShortMessage(this, getString(R.string.data_saved));
            Intent termScreenIntent = new Intent(this, TermActivity.class);
            startActivity(termScreenIntent);

        } else {
            AppUtils.showLongMessage(this, validate);
        }
    }

    private String isValid() {
        StringBuilder errorMsg = new StringBuilder();
        String nameText = name.getText().toString();
        String startDateText = startDate.getText().toString();
        String endDateText = endDate.getText().toString();

        if (AppUtils.isNullOrEmpty(nameText)) {
            errorMsg.append(getString(R.string.valid_name) + "\n");
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
        startDate.setText("");
        endDate.setText("");
    }

    private void setDateTimeField() throws ParseException {
        startDate.setOnClickListener(this);
        endDate.setOnClickListener(this);

        Calendar startDateCal = AppUtils.calendarFormat(startDate.getText().toString());
        Calendar endDateCal = AppUtils.calendarFormat(endDate.getText().toString());

        startDatePickerDialog = new DatePickerDialog(this, R.style.CustomDatePickerDialog, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                startDate.setText(AppUtils.getFormattedDateString(newDate.getTime()));
            }

        },startDateCal.get(Calendar.YEAR), startDateCal.get(Calendar.MONTH), startDateCal.get(Calendar.DAY_OF_MONTH));

        endDatePickerDialog = new DatePickerDialog(this, R.style.CustomDatePickerDialog, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                endDate.setText(AppUtils.getFormattedDateString(newDate.getTime()));
            }

        },endDateCal.get(Calendar.YEAR), endDateCal.get(Calendar.MONTH), endDateCal.get(Calendar.DAY_OF_MONTH));
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
