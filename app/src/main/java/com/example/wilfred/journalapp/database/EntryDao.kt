package com.example.wilfred.journalapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

/**
 * Created by wilfred on 6/29/18.
 */
@Dao
interface EntryDao {


    @get:Query("SELECT * FROM journal")
    val allEntries: LiveData<List<JournalEntry>>

    @Query("SELECT * FROM journal WHERE id = :entryId")
    fun getEntryById(entryId: Int): LiveData<JournalEntry>

    @Insert
    fun insertEntry(journalEntry: JournalEntry)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateEntry(journalEntry: JournalEntry)

    @Delete
    fun deleteEntry(journalEntry: JournalEntry)
}
