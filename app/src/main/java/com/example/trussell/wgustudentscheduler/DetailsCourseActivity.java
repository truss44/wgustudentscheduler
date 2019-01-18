package com.example.trussell.wgustudentscheduler;

import android.arch.lifecycle.Observer;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.trussell.wgustudentscheduler.adapter.MentorsListAdapter;
import com.example.trussell.wgustudentscheduler.model.Course;
import com.example.trussell.wgustudentscheduler.model.Mentor;
import com.example.trussell.wgustudentscheduler.model.Term;
import com.example.trussell.wgustudentscheduler.repo.CourseRepository;
import com.example.trussell.wgustudentscheduler.repo.MentorRepository;
import com.example.trussell.wgustudentscheduler.util.AppUtils;
import com.example.trussell.wgustudentscheduler.util.RecyclerViewClickListener;
import com.example.trussell.wgustudentscheduler.util.RecyclerViewTouchListener;

import java.text.DateFormat;
import java.util.List;

public class DetailsCourseActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private static Term term = TermActivity.getTermData();
    private static Course course = DetailsTermActivity.getCourseData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_course);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setSubtitle(R.string.course_details);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

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

    public void switchToTab(String tab){
        int tabID = Integer.parseInt(tab);
        switch (tabID) {
            case 0: {
                mViewPager.setCurrentItem(0);
            }

            case 1: {
                mViewPager.setCurrentItem(1);
            }

            case 2: {
                mViewPager.setCurrentItem(2);
            }

            case 3: {
                mViewPager.setCurrentItem(3);
            }
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
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.action_add);
        CharSequence cs = getString(R.string.add_mentor);
        menuItem.setTitle(cs);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_add:
                Intent addmentorScreenIntent = new Intent(getApplicationContext(), AddMentorActivity.class);
                startActivity(addmentorScreenIntent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        private void updateDetailsTab(View view) {

            TextView label1 = view.findViewById(R.id.label1);
            TextView label2 = view.findViewById(R.id.label2);
            TextView label3 = view.findViewById(R.id.label3);
            TextView label4 = view.findViewById(R.id.label4);
            TextView label5 = view.findViewById(R.id.label5);

            TextView name = view.findViewById(R.id.nameTextView);
            TextView status = view.findViewById(R.id.statusTextView);
            TextView startDate = view.findViewById(R.id.startDateTextView);
            TextView endDate = view.findViewById(R.id.endDateTextView);
            TextView goalDate = view.findViewById(R.id.goalDateTextView);

            String readableStart = DateFormat.getDateInstance(DateFormat.LONG).format(course.getStartDate());
            String readableEnd = DateFormat.getDateInstance(DateFormat.LONG).format(course.getEndDate());
            String readableGoal = DateFormat.getDateInstance(DateFormat.LONG).format(course.getGoalDate());

            TextView[] textViewArray = { label1, label2, label3, label4, label5 };
            for (TextView tv : textViewArray) {
                tv.append(":");
            }

            name.setText(course.getName());
            status.setText(course.getStatus());
            startDate.setText(readableStart);
            endDate.setText(readableEnd);
            goalDate.setText(readableGoal);
        }

        TextView emptyView;
        RecyclerView recyclerView;
        MentorsListAdapter mentorsListAdapter = null;
        MentorRepository mentorRepository;

        private void updateAssessmentsTab(View view) {

        }

        private void updateMentorsTab(View view) {

            mentorRepository = new MentorRepository(getContext());

            recyclerView = view.findViewById(R.id.itemsList);
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1 , StaggeredGridLayoutManager.VERTICAL));
            recyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(getContext(), recyclerView, new RecyclerViewClickListener() {
                @Override
                public void onClick(View view, int position) {
                    AppUtils.showShortMessage(getContext(), "testing");
                }

                @Override
                public void onLongClick(final View view, final int position) {
                }
            }));

            emptyView = view.findViewById(R.id.emptyView);

            updateMentorsList(course.getId());
        }

        private void updateNotesTab(View view) {
            
        }

        private void updateMentorsList(int id) {
            mentorRepository.fetchMentorsByCourse(id).observe(this, new Observer<List<Mentor>>() {
                @Override
                public void onChanged(@Nullable List<Mentor> mentors) {
                    if (mentors != null && mentors.size() > 0) {
                        emptyView.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        if (mentorsListAdapter == null) {
                            mentorsListAdapter = new MentorsListAdapter(mentors);
                            recyclerView.setAdapter(mentorsListAdapter);

                        } else mentorsListAdapter.addMentors(mentors);
                    } else updateEmptyView();
                }
            });
        }

        private void updateEmptyView() {
            emptyView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {

            switch (item.getItemId()) {
                case R.id.action_add:
                    Intent courseScreenIntent = new Intent(getContext(), AddCourseActivity.class);
                    startActivity(courseScreenIntent);
                    return true;
            }

            return super.onOptionsItemSelected(item);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_details_course, container, false);
            switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
                case 1: {
                    rootView = inflater.inflate(R.layout.details_tab_course, container, false);
                    updateDetailsTab(rootView);
                    break;
                }

                case 2: {
                    rootView = inflater.inflate(R.layout.assessments_tab_course, container, false);
                    updateAssessmentsTab(rootView);
                    break;
                }

                case 3: {
                    rootView = inflater.inflate(R.layout.mentors_tab_course, container, false);
                    updateMentorsTab(rootView);
                    break;
                }

                case 4: {
                    rootView = inflater.inflate(R.layout.notes_tab_course, container, false);
                    updateNotesTab(rootView);
                    break;
                }
            }
            return rootView;
        }
    }

    @Override
    public void onRestart() {
        super.onRestart();
        recreate();
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 4;
        }
    }
}
