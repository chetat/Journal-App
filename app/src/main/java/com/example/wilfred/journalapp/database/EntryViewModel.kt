package com.example.wilfred.journalapp.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

/**
 * ViewModel that will let app data survive connfigurations changes
 */

class EntryViewModel(application: Application) : AndroidViewModel(application) {

    private val mRepository: EntryRepository = EntryRepository(application)
    private var mAllEntries: LiveData<List<JournalEntry>>? = null

    val allEntries: LiveData<List<JournalEntry>>?
        get() {
            if (mAllEntries == null) {
                mAllEntries = mRepository.entries
            }
            return mAllEntries
        }

    init {
        mAllEntries = this.mRepository.entries
    }

    fun insert(entry: JournalEntry) {
        mRepository.insert(entry)
    }

    fun delete(entry: JournalEntry) {
        mRepository.delete(entry)
    }

    fun getEntryById(id: Int): LiveData<JournalEntry> {
        return mRepository.getEntryById(id)
    }


    fun update(entry: JournalEntry) {
        mRepository.updateJournal(entry)
    }
}
