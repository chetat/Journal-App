package com.example.wilfred.journalapp.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by wilfred on 6/29/18.
 */

public class JournalRecyclerAdapter extends RecyclerView.Adapter<JournalRecyclerAdapter.JournalVH> {

    @NonNull
    @Override
    public JournalVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull JournalVH journalVH, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
    class JournalVH extends RecyclerView.ViewHolder{

        public JournalVH(@NonNull View itemView) {
            super(itemView);
        }
    }
}
