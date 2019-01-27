package com.example.wilfred.journalapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wilfred.journalapp.database.EntryViewModel;
import com.example.wilfred.journalapp.database.JournalEntry;

import java.util.Date;

import static com.example.wilfred.journalapp.AddEntry.DEFAULT_JOURNAL_ID;
import static com.example.wilfred.journalapp.AddEntry.JOURNAL_ID;

public class EntryDetailActivity extends AppCompatActivity {

    EntryViewModel mEntryViewModel;
    TextView mDate;
    private TextView mTitleField;
    private TextView mTextEntryField;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry);

        mTitleField = findViewById(R.id.title_field);
        mTextEntryField = findViewById(R.id.textEntry_field);
        TextView mjournalTextView = findViewById(R.id.journal_textview);
        mButton = findViewById(R.id.saveButton);


        Intent journalIntent = getIntent();
        mEntryViewModel = ViewModelProviders.of(this).get(EntryViewModel.class);

        if (journalIntent != null && journalIntent.hasExtra(JOURNAL_ID)) {

            mjournalTextView.setText(getString(R.string.update_journal_textview));

            int mEntryId = journalIntent.getIntExtra(JOURNAL_ID, DEFAULT_JOURNAL_ID);

            mEntryViewModel.getEntryById(mEntryId)
                    .observe(this, new Observer<JournalEntry>() {
                        @Override
                        public void onChanged(@Nullable JournalEntry journalEntry) {
                            assert journalEntry != null;
                            fillJournalFields(journalEntry);
                            updateEntry(journalEntry);
                        }
                    });
        }


    }

    private void updateEntry(final JournalEntry journalEntry) {
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Initialise all fields data
                String textEntry = mTextEntryField.getText().toString();
                String title = mTitleField.getText().toString();

                if (textEntry.isEmpty() && title.isEmpty()) {
                    Toast.makeText(EntryDetailActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    //Viewmodel to update any entry
                    EntryViewModel entryViewModel =
                            ViewModelProviders.of(EntryDetailActivity.this)
                            .get(EntryViewModel.class);

                    journalEntry.setEntryTitle(title);
                    journalEntry.setEntryText(textEntry);
                    entryViewModel.update(journalEntry);
                    Log.i("ADD ACTIVITY", "onClick: " + journalEntry.getEntryTitle());
                }
                finish();
            }
        });
    }

    private void fillJournalFields(JournalEntry journalEntry) {
        mTitleField.setText(journalEntry.getEntryTitle());
        mTextEntryField.setText(journalEntry.getEntryText());
    }

}
