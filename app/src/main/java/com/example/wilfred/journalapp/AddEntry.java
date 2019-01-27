package com.example.wilfred.journalapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wilfred.journalapp.database.EntryViewModel;
import com.example.wilfred.journalapp.database.JournalDatabase;
import com.example.wilfred.journalapp.database.JournalEntry;

import java.util.Date;
import java.util.List;

public class AddEntry extends AppCompatActivity {

    // Constant for logging
    private static final String TAG = AddEntry.class.getSimpleName();
    public static final String JOURNAL_ID = "journalId";
    public static final int DEFAULT_JOURNAL_ID = -1;
    // Fields for views
    EditText mTitleField;
    EditText mTextEntryField;
    Button mButton;
    private EntryViewModel mEntryViewModel;
    private TextView mjournalTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry);

        mTextEntryField = findViewById(R.id.textEntry_field);
        mTitleField = findViewById(R.id.title_field);
        mButton = findViewById(R.id.saveButton);
        mjournalTextView = findViewById(R.id.journal_textview);


        mEntryViewModel = ViewModelProviders.of(this).get(EntryViewModel.class);


        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Initialise all fields data
                String textEntry = mTextEntryField.getText().toString();
                String title = mTitleField.getText().toString();
                Date date = new Date();

                if (textEntry.isEmpty() && title.isEmpty()) {
                    Toast.makeText(AddEntry.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    JournalEntry entry = new JournalEntry(title, textEntry, date);
                    mEntryViewModel.insert(entry);
                }
                finish();
            }
        });

    }


}
