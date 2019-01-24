package com.example.trussell.wgustudentscheduler;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.trussell.wgustudentscheduler.model.Assessment;
import com.example.trussell.wgustudentscheduler.model.Course;
import com.example.trussell.wgustudentscheduler.repo.AssessmentRepository;
import com.example.trussell.wgustudentscheduler.util.AppUtils;
import com.example.trussell.wgustudentscheduler.util.CurrentData;

import java.text.DateFormat;
import java.util.Date;

public class DetailsAssessmentActivity extends AppCompatActivity {

    private Assessment assessment = CurrentData.assessmentData;
    private Course course = CurrentData.courseData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_assessment);

        TextView label1 = findViewById(R.id.label1);
        TextView label2 = findViewById(R.id.label2);
        TextView label3 = findViewById(R.id.label3);
        TextView label4 = findViewById(R.id.label4);
        TextView label5 = findViewById(R.id.label5);

        TextView name = findViewById(R.id.nameTextView);
        TextView type = findViewById(R.id.typeTextView);
        TextView dueDate = findViewById(R.id.dueDateTextView);
        TextView goalDate = findViewById(R.id.goalDateTextView);
        ProgressBar progressBar = findViewById(R.id.progressBar);

        String readableDueDate = DateFormat.getDateInstance(DateFormat.LONG).format(assessment.getDueDate());
        String readableGoalDate = DateFormat.getDateInstance(DateFormat.LONG).format(assessment.getGoalDate());

        Date currentDate = AppUtils.formatDate(new Date());

        int daysBetweenStartEnd = AppUtils.calculateBetweenDates(course.getStartDate(), assessment.getDueDate());
        int daysBetweenStartNow = AppUtils.calculateBetweenDates(course.getStartDate(), currentDate);

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

        name.setText(assessment.getName());
        type.setText(assessment.getType());
        dueDate.setText(readableDueDate);
        goalDate.setText(readableGoalDate);
    }

    public void updateAssessment(View view) {
        Intent updateScreenIntent = new Intent(this, UpdateAssessmentActivity.class);
        startActivity(updateScreenIntent);
    }

    public void removeAssessment(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage(R.string.remove_assessment_alert);
        builder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        Intent intent = getIntent();
                        int requestCode = Integer.parseInt(assessment.getAlertGoalID());
                        PendingIntent.getBroadcast(getApplicationContext(), requestCode, intent,
                                PendingIntent.FLAG_UPDATE_CURRENT).cancel();

                        Thread thread = new Thread(new Runnable(){
                            public void run() {
                                AssessmentRepository assessmentRepository = new AssessmentRepository(getBaseContext());
                                assessmentRepository.deleteAssessment(assessment.getId());
                            }
                        });

                        thread.start();

                        try {
                            thread.join(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        finish();
                    }
                });

        builder.setNegativeButton(android.R.string.no,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        builder.create().show();
    }

    @Override
    public void onRestart() {
        super.onRestart();
        recreate();
    }
}
