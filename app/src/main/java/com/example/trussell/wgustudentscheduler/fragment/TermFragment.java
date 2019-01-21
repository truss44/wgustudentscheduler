package com.example.trussell.wgustudentscheduler.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.trussell.wgustudentscheduler.R;
import com.example.trussell.wgustudentscheduler.TermActivity;
import com.example.trussell.wgustudentscheduler.model.Term;
import com.example.trussell.wgustudentscheduler.util.CurrentData;

import java.text.DateFormat;

public class TermFragment extends Fragment {

    private Term term = CurrentData.termData;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.details_tab_term, container, false);

        TextView label1 = view.findViewById(R.id.label1);
        TextView label2 = view.findViewById(R.id.label2);
        TextView label3 = view.findViewById(R.id.label3);

        TextView name = view.findViewById(R.id.nameTextView);
        TextView startDate = view.findViewById(R.id.startDateTextView);
        TextView endDate = view.findViewById(R.id.endDateTextView);

        String readableStart = DateFormat.getDateInstance(DateFormat.LONG).format(term.getStartDate());
        String readableEnd = DateFormat.getDateInstance(DateFormat.LONG).format(term.getEndDate());

        TextView[] textViewArray = { label1, label2, label3 };
        for (TextView tv : textViewArray) {
            tv.append(":");
        }

        name.setText(term.getName());
        startDate.setText(readableStart);
        endDate.setText(readableEnd);

        return view;
    }
}
