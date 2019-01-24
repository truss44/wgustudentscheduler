package com.example.trussell.wgustudentscheduler.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.trussell.wgustudentscheduler.R;
import com.example.trussell.wgustudentscheduler.model.Term;
import com.example.trussell.wgustudentscheduler.util.AppUtils;
import com.example.trussell.wgustudentscheduler.util.CurrentData;

import java.text.DateFormat;
import java.util.Date;

public class TermFragment extends Fragment {

    private Term term = CurrentData.termData;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.details_tab_term, container, false);

        TextView label1 = view.findViewById(R.id.label1);
        TextView label2 = view.findViewById(R.id.label2);
        TextView label3 = view.findViewById(R.id.label3);
        TextView label4 = view.findViewById(R.id.label4);

        TextView name = view.findViewById(R.id.nameTextView);
        TextView startDate = view.findViewById(R.id.startDateTextView);
        TextView endDate = view.findViewById(R.id.endDateTextView);
        ProgressBar progressBar = view.findViewById(R.id.progressBar);

        String readableStart = DateFormat.getDateInstance(DateFormat.LONG).format(term.getStartDate());
        String readableEnd = DateFormat.getDateInstance(DateFormat.LONG).format(term.getEndDate());

        Date currentDate = AppUtils.formatDate(new Date());

        int daysBetweenStartEnd = AppUtils.calculateBetweenDates(term.getStartDate(), term.getEndDate());
        int daysBetweenStartNow = AppUtils.calculateBetweenDates(term.getStartDate(), currentDate);

        float progressDays = (float) (((double) daysBetweenStartNow / (double) daysBetweenStartEnd) * 100);

        if (Math.round(progressDays) > 100) {
            progressDays = 100;
        } else if (Math.round(progressDays) < 0) {
            progressDays = 0;
        }

        progressBar.setProgress(Math.round(progressDays));

        TextView[] textViewArray = { label1, label2, label3, label4 };
        for (TextView tv : textViewArray) {
            tv.append(":");
        }

        name.setText(term.getName());
        startDate.setText(readableStart);
        endDate.setText(readableEnd);

        return view;
    }
}
