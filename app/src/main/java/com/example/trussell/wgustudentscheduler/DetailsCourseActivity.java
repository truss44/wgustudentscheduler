package com.example.trussell.wgustudentscheduler;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.trussell.wgustudentscheduler.fragment.AssessmentFragment;
import com.example.trussell.wgustudentscheduler.fragment.DetailCourseFragment;
import com.example.trussell.wgustudentscheduler.fragment.MentorFragment;
import com.example.trussell.wgustudentscheduler.fragment.NoteFragment;
import com.example.trussell.wgustudentscheduler.model.Course;
import com.example.trussell.wgustudentscheduler.repo.AssessmentRepository;
import com.example.trussell.wgustudentscheduler.repo.CourseRepository;
import com.example.trussell.wgustudentscheduler.util.CurrentData;

import java.util.ArrayList;
import java.util.List;

public class DetailsCourseActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private Course course = CurrentData.courseData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_course);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setSubtitle(R.string.course_details);

        mViewPager = findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = findViewById(R.id.tabs);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getPosition()) {
                    case 0: {
                        mViewPager.setCurrentItem(0);
                        toolbar.setSubtitle(R.string.course_details);
                        break;
                    }

                    case 1: {
                        mViewPager.setCurrentItem(1);
                        toolbar.setSubtitle(R.string.assessments);
                        break;
                    }

                    case 2: {
                        mViewPager.setCurrentItem(2);
                        toolbar.setSubtitle(R.string.mentors);
                        break;
                    }

                    case 3: {
                        mViewPager.setCurrentItem(3);
                        toolbar.setSubtitle(R.string.notes);
                        break;
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new DetailCourseFragment(), getString(R.string.details));
        adapter.addFragment(new AssessmentFragment(), getString(R.string.assessments));
        adapter.addFragment(new MentorFragment(), getString(R.string.mentors));
        adapter.addFragment(new NoteFragment(), getString(R.string.notes));
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public void updateCourse(View view) {
        Intent detailsScreenIntent = new Intent(this, UpdateCourseActivity.class);
        startActivity(detailsScreenIntent);
    }

    public void removeCourse(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage(R.string.remove_course_alert);
        builder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        Intent intent = getIntent();
                        int requestCodeStart = Integer.parseInt(course.getAlertStartID());
                        PendingIntent.getBroadcast(getApplicationContext(), requestCodeStart, intent,
                                PendingIntent.FLAG_UPDATE_CURRENT).cancel();

                        int requestCodeEnd = Integer.parseInt(course.getAlertEndID());
                        PendingIntent.getBroadcast(getApplicationContext(), requestCodeEnd, intent,
                                PendingIntent.FLAG_UPDATE_CURRENT).cancel();


                        Thread threadAssessments = new Thread(new Runnable(){
                            public void run() {
                                AssessmentRepository assessmentRepository = new AssessmentRepository(getBaseContext());

                                ArrayList <String> assessmentRequestIDs =
                                        (ArrayList <String>) assessmentRepository.getAssessmentsInCourse(course.getId());

                                for (String assessmentRequestID : assessmentRequestIDs) {
                                    Intent intent = getIntent();
                                    int requestCodeGoals = Integer.parseInt(assessmentRequestID);
                                    PendingIntent.getBroadcast(getApplicationContext(), requestCodeGoals, intent,
                                            PendingIntent.FLAG_UPDATE_CURRENT).cancel();
                                }
                            }
                        });

                        threadAssessments.start();

                        try {
                            threadAssessments.join(4000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }


                        Thread thread = new Thread(new Runnable(){
                            public void run() {
                                CourseRepository courseRepository = new CourseRepository(getBaseContext());
                                courseRepository.deleteCourse(course.getId());
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.secondary, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_add_assessment: {
                Intent addAssessmentScreenIntent = new Intent(getApplicationContext(), AddAssessmentActivity.class);
                startActivity(addAssessmentScreenIntent);
                return true;
            }

            case R.id.action_add_mentor: {
                Intent addMentorScreenIntent = new Intent(getApplicationContext(), AddMentorActivity.class);
                startActivity(addMentorScreenIntent);
                return true;
            }

            case R.id.action_add_note: {
                Intent addNoteScreenIntent = new Intent(getApplicationContext(), AddNoteActivity.class);
                startActivity(addNoteScreenIntent);
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRestart() {
        super.onRestart();
        recreate();
    }
}
