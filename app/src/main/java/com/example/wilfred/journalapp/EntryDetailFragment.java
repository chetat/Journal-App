package com.example.wilfred.journalapp;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wilfred.journalapp.database.EntryViewModel;
import com.example.wilfred.journalapp.database.JournalEntry;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class EntryDetailFragment extends Fragment {

    JournalEntry mJournalEntry;
    EntryViewModel mEntryViewModel;
    int entryId;
    TextView mTitle;
    TextView mEntry;
    TextView mDate;

    public EntryDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        entryId = getActivity().getIntent().getIntExtra("itemData",0);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_entry_detail_layout, container, false);
        mDate = (TextView)view.findViewById(R.id.date);
        mTitle = (TextView)view.findViewById(R.id.title_text_view);
        mEntry = (TextView)view.findViewById(R.id.entry_text);

        mEntryViewModel = ViewModelProviders.of(this).get(EntryViewModel.class);

        mEntryViewModel.getAllEntries().observe(this, new Observer<List<JournalEntry>>() {

            @Override
            public void onChanged(List<JournalEntry> journalEntries) {

                for (JournalEntry journalEntry: journalEntries) {
                    if (journalEntry.getId() == entryId){
                        Log.i("TAG", "onChanged: "+ journalEntry.getEntryTitle());
                        mTitle.setText(journalEntry.getEntryTitle());
                    }
                }
            }
        });
        return view;
    }

}
