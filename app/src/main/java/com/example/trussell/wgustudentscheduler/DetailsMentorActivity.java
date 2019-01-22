package com.example.trussell.wgustudentscheduler;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.trussell.wgustudentscheduler.model.Mentor;
import com.example.trussell.wgustudentscheduler.repo.MentorRepository;
import com.example.trussell.wgustudentscheduler.util.AppUtils;
import com.example.trussell.wgustudentscheduler.util.CurrentData;

public class DetailsMentorActivity extends AppCompatActivity {

    private Mentor mentor = CurrentData.mentorData;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_mentor);

        TextView label1 = findViewById(R.id.label1);
        TextView label2 = findViewById(R.id.label2);
        TextView label3 = findViewById(R.id.label3);

        TextView name = findViewById(R.id.nameTextView);
        final TextView phone = findViewById(R.id.phoneTextView);
        ImageView phoneImage = findViewById(R.id.phoneImageView);
        TextView email = findViewById(R.id.emailTextView);
        ImageView emailImage = findViewById(R.id.emailImageView);

        TextView[] textViewArray = { label1, label2, label3 };
        for (TextView tv : textViewArray) {
            tv.append(":");
        }

        name.setText(mentor.getName());
        phone.setText(mentor.getPhone());
        email.setText(mentor.getEmail());

        if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED) {

            final String number = phone.getText().toString().replaceAll("[^\\d.]", "");

            if (!AppUtils.isNullOrEmpty(number)) {
                phoneImage.setVisibility(View.VISIBLE);

                phoneImage.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + number));
                        startActivity(intent);
                    }
                });
            } else {
                phoneImage.setVisibility(View.GONE);
            }
        }

        final String emailAddr = email.getText().toString();

        if (!AppUtils.isNullOrEmpty(emailAddr)) {
            emailImage.setVisibility(View.VISIBLE);

            emailImage.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto",emailAddr, null));
                    startActivity(Intent.createChooser(intent, getString(R.string.choose_email_client)));
                }
            });
        } else {
            emailImage.setVisibility(View.GONE);
        }
    }

    public void updateMentor(View view) {
        Intent updateScreenIntent = new Intent(this, UpdateMentorActivity.class);
        startActivity(updateScreenIntent);
    }

    public void removeMentor(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage(R.string.remove_mentor_alert);
        builder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        Thread thread = new Thread(new Runnable(){
                            public void run() {
                                MentorRepository mentorRepository = new MentorRepository(getBaseContext());
                                mentorRepository.deleteMentor(mentor.getId());
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
