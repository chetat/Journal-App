package com.example.wilfred.journalapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem

import com.example.wilfred.journalapp.settings.SettingsActivity
import com.firebase.ui.auth.AuthUI

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //PreferenceManager for settings
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false)

        //Initialise MainActivity Fragment
        val fragment = MainActivityFragment()
        supportFragmentManager.beginTransaction()
                .add(R.id.main_activity_fragment, fragment)
                .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.signOut -> {
                AuthUI.getInstance()
                        .signOut(this)
                return true
            }
            R.id.about -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun setupSharedPreferences() {
        // Get all of the values from shared preferences to set it up

    }

}

