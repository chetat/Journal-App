package com.example.wilfred.journalapp.database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

/**
 * ViewModel that will let app data survive connfigurations changes
 */

public class EntryViewModel extends AndroidViewModel {

    private EntryRepository mRepository;
    private LiveData<List<JournalEntry>> mAllEntries;

    public EntryViewModel(@NonNull Application application) {
        super(application);
        this.mRepository = new EntryRepository(application);
        mAllEntries = this.mRepository.getEntries();
    }

    public void insert(JournalEntry entry){
        mRepository.insert(entry);
    }
    public LiveData<List<JournalEntry>> getAllEntries() {
        if (mAllEntries == null){
           mAllEntries =  mRepository.getEntries();
        }
        return mAllEntries;
    }
    public void delete(JournalEntry entry){
        mRepository.delete(entry);
    }


}
