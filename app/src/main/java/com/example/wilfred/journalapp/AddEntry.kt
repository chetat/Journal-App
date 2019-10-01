package com.example.wilfred.journalapp

import androidx.lifecycle.ViewModelProviders
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

import com.example.wilfred.journalapp.database.EntryViewModel
import com.example.wilfred.journalapp.database.JournalEntry

import java.util.Date

class AddEntry : AppCompatActivity() {
    // Fields for views
    internal lateinit var mTitleField: EditText
    internal lateinit var mTextEntryField: EditText
    internal lateinit var mButton: Button
    private var mEntryViewModel: EntryViewModel? = null
    private var mjournalTextView: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_entry)

        mTextEntryField = findViewById(R.id.textEntry_field)
        mTitleField = findViewById(R.id.title_field)
        mButton = findViewById(R.id.saveButton)
        mjournalTextView = findViewById(R.id.journal_textview)


        mEntryViewModel = ViewModelProviders.of(this).get(EntryViewModel::class.java)


        mButton.setOnClickListener(View.OnClickListener {
            //Initialise all fields data
            val textEntry = mTextEntryField.text.toString()
            val title = mTitleField.text.toString()
            val date = Date()

            if (textEntry.isEmpty() && title.isEmpty()) {
                Toast.makeText(this@AddEntry, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            } else {
                val entry = JournalEntry(title, textEntry, date)
                mEntryViewModel!!.insert(entry)
            }
            finish()
        })

    }

    companion object {
        // Constant for logging
        private val TAG = AddEntry::class.java.simpleName
        const val JOURNAL_ID = "journalId"
        const val DEFAULT_JOURNAL_ID = -1
    }


}
