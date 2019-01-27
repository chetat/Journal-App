package com.example.wilfred.journalapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.wilfred.journalapp.settings.SettingsActivity;
import com.firebase.ui.auth.AuthUI;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //PreferenceManager for settings
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        //Initialise MainActivity Fragment
        MainActivityFragment fragment = new MainActivityFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_activity_fragment, fragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.signOut:
                AuthUI.getInstance()
                        .signOut(this);
                return true;
            case R.id.about:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupSharedPreferences() {
        // Get all of the values from shared preferences to set it up

    }

}

