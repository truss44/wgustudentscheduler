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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.trussell.wgustudentscheduler.model.Term;
import com.example.trussell.wgustudentscheduler.parcelable.ParcelableTerm;
import com.example.trussell.wgustudentscheduler.repo.TermRepository;
import com.example.trussell.wgustudentscheduler.util.AppUtils;

import java.util.Calendar;
import java.util.Date;

public class UpdateTermActivity extends AppCompatActivity implements View.OnClickListener {

    private Term term;
    private EditText name, startDate, endDate;
    private Button saveButton, resetButton;

    private DatePickerDialog startDatePickerDialog;
    private DatePickerDialog endDatePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_term);
        findViewsById();
        setDateTimeField();
        setData();
    }

    private void findViewsById() {
        name = findViewById(R.id.nameTextBox);

        startDate = findViewById(R.id.startTextBox);
        startDate.setInputType(InputType.TYPE_NULL);

        endDate = findViewById(R.id.endTextBox);
        endDate.setInputType(InputType.TYPE_NULL);

        saveButton = findViewById(R.id.saveButton);
        resetButton = findViewById(R.id.resetButton);
    }

    private void setData() {
        ParcelableTerm parcelableTerm = this.getIntent().getParcelableExtra("termData");
        term = parcelableTerm.getTerm();

        name.setText(term.getName());
        startDate.setText(AppUtils.getFormattedDateString(term.getStartDate()));
        endDate.setText(AppUtils.getFormattedDateString(term.getEndDate()));
    }

    public void submitData(View view) {
        String validate = isValid();
        String nameText = name.getText().toString();
        Date startDateText = AppUtils.formatStringToDate(startDate.getText().toString());
        Date endDateText = AppUtils.formatStringToDate(endDate.getText().toString());

        term.setName(nameText);
        term.setStartDate(startDateText);
        term.setEndDate(endDateText);

        if (validate.length() == 0) {
            try {
                TermRepository termRepository = new TermRepository(getApplicationContext());
                termRepository.updateTerm(term);
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

    public String isValid() {
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

    private boolean saveData(View view) {
            return false;
    }

    public void resetForm(View view) {
        name.setText(term.getName());
        startDate.setInputType(InputType.TYPE_CLASS_TEXT);
        startDate.setText(AppUtils.getFormattedDateString(term.getStartDate()));
//        startDate.setInputType(InputType.TYPE_NULL);

        endDate.setInputType(InputType.TYPE_CLASS_TEXT);
        endDate.setText(AppUtils.getFormattedDateString(term.getEndDate()));
//        endDate.setInputType(InputType.TYPE_NULL);
    }

    private void setDateTimeField() {
        startDate.setOnClickListener(this);
        endDate.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        startDatePickerDialog = new DatePickerDialog(this, R.style.CustomDatePickerDialog, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                startDate.setText(AppUtils.getFormattedDateString(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        endDatePickerDialog = new DatePickerDialog(this, R.style.CustomDatePickerDialog, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                endDate.setText(AppUtils.getFormattedDateString(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
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
