package com.example.wilfred.journalapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class EntryDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_detail);

        Fragment detailFragment = new EntryDetailFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.entry_detail_activity_fragment, detailFragment)
                .commit();
    }
}
