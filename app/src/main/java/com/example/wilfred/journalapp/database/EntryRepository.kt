package com.example.wilfred.journalapp.database

import android.app.Application
import androidx.lifecycle.LiveData
import android.os.AsyncTask

/**
 * Created by wilfred on 6/30/18.
 */

class EntryRepository(application: Application) {
    private val mEntryDao: EntryDao
    //AsyncTask to retrieve all entries
    var entries: LiveData<List<JournalEntry>>

    init {
        val database = JournalDatabase.getsInstance(application)
        mEntryDao = database!!.entryDao()
        entries = mEntryDao.allEntries
    }

    fun getEntryById(id: Int): LiveData<JournalEntry> {
        return mEntryDao.getEntryById(id)
    }

    fun insert(entry: JournalEntry) {
        insertAsyncTask(mEntryDao).execute(entry)
    }


    //AsyncTask to delete an entry
    internal fun delete(entry: JournalEntry) {
        deleteAsyncTask(mEntryDao).execute(entry)
    }

    fun updateJournal(entry: JournalEntry) {
        updateAsyncTask(mEntryDao).execute(entry)

    }

    private class insertAsyncTask internal constructor(private val mAsyncTaskDao: EntryDao) : AsyncTask<JournalEntry, Void, Void>() {

        override fun doInBackground(vararg params: JournalEntry): Void? {
            mAsyncTaskDao.insertEntry(params[0])
            return null
        }
    }

    //Update Async
    private class updateAsyncTask internal constructor(private val mAsyncDao: EntryDao) : AsyncTask<JournalEntry, Void, Void>() {

        override fun doInBackground(vararg journalEntries: JournalEntry): Void? {
            mAsyncDao.updateEntry(journalEntries[0])
            return null
        }
    }


    //Delete AsyncTask
    private class deleteAsyncTask internal constructor(private val mAsyncTaskDao: EntryDao) : AsyncTask<JournalEntry, Void, Void>() {

        override fun doInBackground(vararg params: JournalEntry): Void? {
            mAsyncTaskDao.deleteEntry(params[0])
            return null
        }
    }
}
