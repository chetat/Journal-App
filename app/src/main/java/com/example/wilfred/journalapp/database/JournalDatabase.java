package com.example.wilfred.journalapp.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.example.wilfred.journalapp.Utils.DateConverterUtils;

/**
 * Created by wilfred on 6/29/18.
 */
@Database(entities = {JournalEntry.class}, version = 1, exportSchema =
false)
@TypeConverters(DateConverterUtils.class)
public abstract class JournalDatabase  extends RoomDatabase{
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "journalApp";
    private static JournalDatabase sInstance;


    public static JournalDatabase getsInstance(Context context){
        if (sInstance == null){
            synchronized (LOCK){
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        JournalDatabase.class, JournalDatabase.DATABASE_NAME)
                        .allowMainThreadQueries()
                        .build();
            }
        }
        return sInstance;
    }

public abstract EntryDao entryDao();
}
