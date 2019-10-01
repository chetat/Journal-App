package com.example.wilfred.journalapp

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.wilfred.journalapp.AddEntry.Companion.DEFAULT_JOURNAL_ID
import com.example.wilfred.journalapp.AddEntry.Companion.JOURNAL_ID
import com.example.wilfred.journalapp.database.EntryViewModel
import com.example.wilfred.journalapp.database.JournalEntry

class EntryDetailActivity : AppCompatActivity() {

    private lateinit var mEntryViewModel: EntryViewModel
    private var mTitleField: TextView? = null
    private var mTextEntryField: TextView? = null
    private var mButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_entry)

        mTitleField = findViewById(R.id.title_field)
        mTextEntryField = findViewById(R.id.textEntry_field)
        val mjournalTextView = findViewById<TextView>(R.id.journal_textview)
        mButton = findViewById(R.id.saveButton)


        val journalIntent = intent
        mEntryViewModel = ViewModelProviders.of(this).get(EntryViewModel::class.java)

        if (journalIntent != null && journalIntent.hasExtra(JOURNAL_ID)) {

            mjournalTextView.text = getString(R.string.update_journal_textview)

            val mEntryId = journalIntent.getIntExtra(JOURNAL_ID, DEFAULT_JOURNAL_ID)

            mEntryViewModel.getEntryById(mEntryId)
                    .observe(this, Observer { journalEntry ->
                        assert(journalEntry != null)
                        fillJournalFields(journalEntry!!)
                        updateEntry(journalEntry)
                    })
        }


    }

    private fun updateEntry(journalEntry: JournalEntry?) {
        mButton!!.setOnClickListener(View.OnClickListener {
            //Initialise all fields data
            val textEntry = mTextEntryField!!.text.toString()
            val title = mTitleField!!.text.toString()

            if (textEntry.isEmpty() && title.isEmpty()) {
                Toast.makeText(this@EntryDetailActivity, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            } else {
                //Viewmodel to update any entry
                val entryViewModel = ViewModelProviders.of(this@EntryDetailActivity)
                        .get(EntryViewModel::class.java)

                journalEntry!!.entryTitle = title
                journalEntry.entryText = textEntry
                entryViewModel.update(journalEntry)
                Log.i("ADD ACTIVITY", "onClick: " + journalEntry.entryTitle!!)
            }
            finish()
        })
    }

    private fun fillJournalFields(journalEntry: JournalEntry) {
        mTitleField!!.text = journalEntry.entryTitle
        mTextEntryField!!.text = journalEntry.entryText
    }

}
