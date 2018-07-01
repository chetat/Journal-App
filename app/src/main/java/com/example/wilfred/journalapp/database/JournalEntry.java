package com.example.wilfred.journalapp.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

/**
 * Created by wilfred on 6/29/18.
 */

@Entity(tableName = "journal")
public class JournalEntry {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String entryTitle;
    private String entryText;

    @ColumnInfo(name = "updated_at")
    private Date updatedAt;


    @Ignore
    public JournalEntry(String entryTitle, String entryText, Date entryDate){
        this.entryTitle = entryTitle;
        this.entryText = entryText;
        this.updatedAt = entryDate;
    }

    public JournalEntry(int id, String entryTitle, String entryText, Date updatedAt){
        this.id = id;
        this.entryTitle = entryTitle;
        this.entryText = entryText;
        this.updatedAt = updatedAt;
    }


    public String getEntryText() {
        return entryText;
    }

    public void setEntryText(String entryText) {
        this.entryText = entryText;
    }

    public String getEntryTitle() {
        return entryTitle;
    }

    public void setEntryTitle(String entryTitle) {
        this.entryTitle = entryTitle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
