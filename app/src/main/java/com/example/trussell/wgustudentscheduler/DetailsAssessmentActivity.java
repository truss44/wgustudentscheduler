package com.example.trussell.wgustudentscheduler;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.trussell.wgustudentscheduler.model.Assessment;
import com.example.trussell.wgustudentscheduler.repo.AssessmentRepository;
import com.example.trussell.wgustudentscheduler.util.CurrentData;

import java.text.DateFormat;

public class DetailsAssessmentActivity extends AppCompatActivity {

    private Assessment assessment = CurrentData.assessmentData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_assessment);

        TextView label1 = findViewById(R.id.label1);
        TextView label2 = findViewById(R.id.label2);
        TextView label3 = findViewById(R.id.label3);
        TextView label4 = findViewById(R.id.label4);

        TextView name = findViewById(R.id.nameTextView);
        TextView type = findViewById(R.id.typeTextView);
        TextView dueDate = findViewById(R.id.dueDateTextView);
        TextView goalDate = findViewById(R.id.goalDateTextView);

        String readableDueDate = DateFormat.getDateInstance(DateFormat.LONG).format(assessment.getDueDate());
        String readableGoalDate = DateFormat.getDateInstance(DateFormat.LONG).format(assessment.getGoalDate());

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
