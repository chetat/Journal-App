package com.example.wilfred.journalapp.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by wilfred on 6/29/18.
 */
@Dao
public interface EntryDao {


    @Query("SELECT * FROM journal")
    LiveData<List<JournalEntry>> getAllEntries();

    @Query("SELECT * FROM journal WHERE id = :entryId")
    LiveData<JournalEntry> getEntryById(int entryId);

    @Insert
    void insertEntry(JournalEntry journalEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateEntry(JournalEntry journalEntry);

    @Delete
    void deleteEntry(JournalEntry journalEntry);
}
