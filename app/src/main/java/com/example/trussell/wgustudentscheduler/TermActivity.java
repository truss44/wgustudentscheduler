package com.example.trussell.wgustudentscheduler;

import android.arch.lifecycle.Observer;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.trussell.wgustudentscheduler.adapter.TermsListAdapter;
import com.example.trussell.wgustudentscheduler.model.Term;
import com.example.trussell.wgustudentscheduler.parcelable.ParcelableTerm;
import com.example.trussell.wgustudentscheduler.repo.TermRepository;
import com.example.trussell.wgustudentscheduler.util.AppUtils;
import com.example.trussell.wgustudentscheduler.util.Constants;
import com.example.trussell.wgustudentscheduler.util.RecyclerViewClickListener;
import com.example.trussell.wgustudentscheduler.util.RecyclerViewTouchListener;

import java.util.List;

public class TermActivity extends AppCompatActivity implements Constants {

    private TextView emptyView;
    private RecyclerView recyclerView;
    private TermsListAdapter termsListAdapter;
    private TermRepository termRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term);

        termRepository = new TermRepository(getApplicationContext());

        recyclerView = findViewById(R.id.itemsList);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1 , StaggeredGridLayoutManager.VERTICAL));
        recyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(getApplicationContext(), recyclerView, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                final Term term = termsListAdapter.getItem(position);
                ParcelableTerm parcelableTerm = new ParcelableTerm(term);
                Intent detailsScreenIntent = new Intent(getApplicationContext(), DetailsTermActivity.class);
                detailsScreenIntent.putExtra("termData", parcelableTerm);
                startActivity(detailsScreenIntent);
            }

            @Override
            public void onLongClick(final View view, final int position) {
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        showPopupMenu(view, position);
                    }
                });
            }
        }));

        emptyView = findViewById(R.id.emptyView);

        updateTermsList();
    }

    private void updateTermsList() {
        termRepository.getTerms().observe(this, new Observer<List<Term>>() {
            @Override
            public void onChanged(@Nullable List<Term> terms) {
                if (terms != null && terms.size() > 0) {
                    emptyView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    if (termsListAdapter == null) {
                        termsListAdapter = new TermsListAdapter(terms);
                        recyclerView.setAdapter(termsListAdapter);

                    } else termsListAdapter.addTerms(terms);
                } else updateEmptyView();
            }
        });
    }

    private void updateEmptyView() {
        emptyView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    private void showPopupMenu(View view, int position) {
        final Term term = termsListAdapter.getItem(position);
        final String item = (String) view.getTag();

        PopupMenu popup = new PopupMenu(this, view);
        popup.getMenuInflater().inflate(R.menu.popup, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menuRemove: {
                        AppUtils.showShortMessage(TermActivity.this, term.getName() + " remove");
                        return true;
                    }

                    case R.id.menuUpdate: {
                        ParcelableTerm parcelableTerm = new ParcelableTerm(term);
                        Intent detailsScreenIntent = new Intent(getApplicationContext(), UpdateTermActivity.class);
                        detailsScreenIntent.putExtra("termData", parcelableTerm);
                        startActivity(detailsScreenIntent);
                        return true;
                    }
                }

                return false;
            }
        });

        popup.show();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.action_add);
        CharSequence cs = getString(R.string.app_add_term);
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

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            super.onBackPressed();
        } else if (fm.getBackStackEntryCount() == 0) {
            onShowQuitDialog();
        }
    }

    public void onShowQuitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);

        builder.setMessage(R.string.quit);
        builder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
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
}
