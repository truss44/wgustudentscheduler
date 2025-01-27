package com.example.trussell.wgustudentscheduler.adapter;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.trussell.wgustudentscheduler.R;
import com.example.trussell.wgustudentscheduler.model.Term;
import com.example.trussell.wgustudentscheduler.util.diff.TermDiffUtil;

import java.text.DateFormat;
import java.util.List;

public class TermsListAdapter extends RecyclerView.Adapter<TermsListAdapter.CustomViewHolder> {

    private final List<Term> terms;
    public TermsListAdapter(List<Term> terms) {
        this.terms = terms;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_with_subtext, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Term term = getItem(position);

        String startDate = DateFormat.getDateInstance(DateFormat.LONG).format(term.getStartDate());
        String endDate = DateFormat.getDateInstance(DateFormat.LONG).format(term.getEndDate());
        String subData = startDate + " - " + endDate;

        holder.itemName.setText(term.getName());
        holder.itemSub.setText(subData);
    }

    @Override
    public int getItemCount() {
        return terms.size();
    }

    public Term getItem(int position) {
        return terms.get(position);
    }

    public void addTerms(List<Term> newTerms) {
        TermDiffUtil termDiffUtil = new TermDiffUtil(terms, newTerms);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(termDiffUtil);
        terms.clear();
        terms.addAll(newTerms);
        diffResult.dispatchUpdatesTo(this);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        private final TextView itemName, itemSub;
        CustomViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.nameTextView);
            itemSub = itemView.findViewById(R.id.subTextView);
        }
    }
}