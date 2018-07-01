package com.example.wilfred.journalapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wilfred.journalapp.database.JournalEntry;

import java.util.Date;

public class UpdateEntry extends AppCompatActivity {
    TextView mItemTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_entry);
        mItemTextView = findViewById(R.id.entry_text_view);
    }
}
