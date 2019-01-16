package com.example.trussell.wgustudentscheduler;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.example.trussell.wgustudentscheduler.model.Term;
import com.example.trussell.wgustudentscheduler.parcelable.ParcelableTerm;
import com.example.trussell.wgustudentscheduler.repo.TermRepository;

import java.text.DateFormat;

public class DetailsTermActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    private static Term term;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_term);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setSubtitle(R.string.term_details);

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
                        toolbar.setSubtitle(R.string.term_details);
                        break;
                    }

                    case 1: {
                        mViewPager.setCurrentItem(1);
                        toolbar.setSubtitle(R.string.app_courses);
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

        final Intent intent = getIntent();
        if (intent.hasExtra("tabNumber")) {
            String tab = intent.getExtras().getString("tabNumber");
            switchToTab(tab);
        }
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
        }
    }

    public void updateTerm(View view) {
        ParcelableTerm parcelableTerm = new ParcelableTerm(term);
        Intent detailsScreenIntent = new Intent(this, UpdateTermActivity.class);
        detailsScreenIntent.putExtra("termData", parcelableTerm);
        startActivity(detailsScreenIntent);
    }

    public void removeTerm(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage(R.string.remove_term);
        builder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Thread thread = new Thread(new Runnable(){
                            public void run() {
                                TermRepository termRepository = new TermRepository(DetailsTermActivity.this);
                                termRepository.deleteTerm(term.getId());
                            }
                        });

                        thread.start();

                        try {
                            thread.join(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        Intent termsScreenIntent = new Intent(getApplicationContext(), TermActivity.class);
                        startActivity(termsScreenIntent);
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
        CharSequence cs = getString(R.string.add_course);
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
                ParcelableTerm parcelableTerm = new ParcelableTerm(term);
                Intent addcourseScreenIntent = new Intent(getApplicationContext(), AddCourseActivity.class);
                addcourseScreenIntent.putExtra("termData", parcelableTerm);
                startActivity(addcourseScreenIntent);
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
            ParcelableTerm parcelableTerm = getActivity().getIntent().getParcelableExtra("termData");
            term = parcelableTerm.getTerm();

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
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_details_term, container, false);
            switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
                case 1: {
                    rootView = inflater.inflate(R.layout.details_tab_term, container, false);
                    updateDetailsTab(rootView);
                    break;
                }

                case 2: {
                    rootView = inflater.inflate(R.layout.courses_tab_term, container, false);
                    break;
                }
            }
            return rootView;
        }
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
            return 2;
        }
    }
}
