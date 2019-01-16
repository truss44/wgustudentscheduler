package com.example.trussell.wgustudentscheduler;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
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
import com.example.trussell.wgustudentscheduler.util.AppUtils;

import java.text.DateFormat;

public class DetailsTermActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    private static Term term;

    public void updateTerm(View view) {
        ParcelableTerm parcelableTerm = new ParcelableTerm(term);
        Intent detailsScreenIntent = new Intent(this, UpdateTermActivity.class);
        detailsScreenIntent.putExtra("termData", parcelableTerm);
        startActivity(detailsScreenIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_term);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
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
                Intent termScreenIntent = new Intent(this, AddTermActivity.class);
                startActivity(termScreenIntent);
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
            String readableEnd = DateFormat.getDateInstance(DateFormat.LONG).format(term.getStartDate());

            label1.append(":");
            label2.append(":");
            label3.append(":");

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
