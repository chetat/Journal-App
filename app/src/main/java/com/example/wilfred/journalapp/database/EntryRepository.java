package com.example.wilfred.journalapp.database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

/**
 * Created by wilfred on 6/30/18.
 */

public class EntryRepository {
    private EntryDao mEntryDao;
    private LiveData<List<JournalEntry>> mEntries;

    public EntryRepository(Application application) {
        JournalDatabase database = JournalDatabase.getsInstance(application);
        mEntryDao = database.entryDao();
        mEntries = mEntryDao.getAllEntries();
    }

    //AsyncTask to retrieve all entries
    public LiveData<List<JournalEntry>> getEntries() {
        return mEntries;
    }

    public LiveData<JournalEntry> getEntryById(int id) {
        return mEntryDao.getEntryById(id);
    }

    public void insert(JournalEntry entry) {
        new insertAsyncTask(mEntryDao).execute(entry);
    }


    //AsyncTask to delete an entry
    void delete(JournalEntry entry) {
        new deleteAsyncTask(mEntryDao).execute(entry);
    }

    public void updateJournal(JournalEntry entry) {
        new updateAsyncTask(mEntryDao).execute(entry);

    }

    private static class insertAsyncTask extends AsyncTask<JournalEntry, Void, Void> {

        private EntryDao mAsyncTaskDao;

        insertAsyncTask(EntryDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final JournalEntry... params) {
            mAsyncTaskDao.insertEntry(params[0]);
            return null;
        }
    }

    //Update Async
    private static class updateAsyncTask extends AsyncTask<JournalEntry, Void, Void> {

        private EntryDao mAsyncDao;

        updateAsyncTask(EntryDao dao) {
            mAsyncDao = dao;
        }

        @Override
        protected Void doInBackground(final JournalEntry... journalEntries) {
            mAsyncDao.updateEntry(journalEntries[0]);
            return null;
        }
    }


    //Delete AsyncTask
    private static class deleteAsyncTask extends AsyncTask<JournalEntry, Void, Void> {

        private EntryDao mAsyncTaskDao;

        deleteAsyncTask(EntryDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final JournalEntry... params) {
            mAsyncTaskDao.deleteEntry(params[0]);
            return null;
        }
    }
}
