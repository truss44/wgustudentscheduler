package com.example.trussell.wgustudentscheduler.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.trussell.wgustudentscheduler.DetailsTermActivity;
import com.example.trussell.wgustudentscheduler.R;
import com.example.trussell.wgustudentscheduler.model.Course;

import java.text.DateFormat;

public class DetailCourseFragment extends Fragment {

    private static final Course course = DetailsTermActivity.getCourseData();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.details_tab_course, container, false);

        TextView label1 = view.findViewById(R.id.label1);
        TextView label2 = view.findViewById(R.id.label2);
        TextView label3 = view.findViewById(R.id.label3);
        TextView label4 = view.findViewById(R.id.label4);

        TextView name = view.findViewById(R.id.nameTextView);
        TextView status = view.findViewById(R.id.statusTextView);
        TextView startDate = view.findViewById(R.id.startDateTextView);
        TextView endDate = view.findViewById(R.id.endDateTextView);

        String readableStart = DateFormat.getDateInstance(DateFormat.LONG).format(course.getStartDate());
        String readableEnd = DateFormat.getDateInstance(DateFormat.LONG).format(course.getEndDate());

        TextView[] textViewArray = { label1, label2, label3, label4 };
        for (TextView tv : textViewArray) {
            tv.append(":");
        }

        name.setText(course.getName());
        status.setText(course.getStatus());
        startDate.setText(readableStart);
        endDate.setText(readableEnd);

        return view;
    }
}
