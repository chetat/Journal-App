package com.example.wilfred.journalapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

import java.util.Date

/**
 * Created by wilfred on 6/29/18.
 */

@Entity(tableName = "journal")
class JournalEntry {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var entryTitle: String? = null
    var entryText: String? = null

    @ColumnInfo(name = "updated_at")
    var updatedAt: Date? = null


    @Ignore
    constructor(entryTitle: String, entryText: String, entryDate: Date) {
        this.entryTitle = entryTitle
        this.entryText = entryText
        this.updatedAt = entryDate
    }

    constructor(id: Int, entryTitle: String, entryText: String, updatedAt: Date) {
        this.id = id
        this.entryTitle = entryTitle
        this.entryText = entryText
        this.updatedAt = updatedAt
    }
}
