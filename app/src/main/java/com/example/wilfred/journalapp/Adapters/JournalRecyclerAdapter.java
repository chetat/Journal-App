package com.example.wilfred.journalapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wilfred.journalapp.R;
import com.example.wilfred.journalapp.database.JournalEntry;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by wilfred on 6/29/18.
 */

public class JournalRecyclerAdapter extends RecyclerView.Adapter<JournalRecyclerAdapter.JournalVH> {

    // Member variable to handle item clicks
    final private ItemClickListener mItemClickListener;
    //Date format Constant
    private static final String DATE_FORMAT = "dd/MM/yyyy";

    //final private ItemClickListener itemClickListener;

    private List<JournalEntry> mJournalEntries;
    private Context mContext;

    //Date formater
    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());


    public JournalRecyclerAdapter(Context context, ItemClickListener itemClickListener) {
        mContext = context;
        mItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public JournalVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.entry_item, viewGroup, false);
        return new JournalVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JournalVH holder, int position) {
        JournalEntry entry = mJournalEntries.get(position);
        String title = entry.getEntryTitle();
        String entryText = entry.getEntryText();
        String date = dateFormat.format(entry.getUpdatedAt());

        holder.mTitleTextView.setText(title);
        holder.mEntryTextView.setText(entryText);
        holder.mDateTextView.setText(date);

    }

    /**
     * Returns the number of items to display.
     */
    @Override
    public int getItemCount() {
        if (mJournalEntries == null) {
            return 0;
        }
        return mJournalEntries.size();
    }

    public List<JournalEntry> getEntries() {
        return mJournalEntries;
    }

    /**
     * When data changes, this method updates the list of taskEntries
     * and notifies the adapter to use the new values on it
     */
    public void setEntries(List<JournalEntry> entries) {
        mJournalEntries = entries;
        notifyDataSetChanged();
    }

    public interface ItemClickListener {
        void onItemClickListener(int itemId);
    }

    class JournalVH extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView mEntryTextView;
        TextView mTitleTextView;
        TextView mDateTextView;


        JournalVH(@NonNull View itemView) {
            super(itemView);

            mEntryTextView = itemView.findViewById(R.id.entry_text_view);
            mTitleTextView = itemView.findViewById(R.id.title_text_view);
            mDateTextView = itemView.findViewById(R.id.date_text_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int elementId = mJournalEntries.get(getAdapterPosition()).getId();
            mItemClickListener.onItemClickListener(elementId);
        }

    }
}
