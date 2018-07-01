package com.example.wilfred.journalapp.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.wilfred.journalapp.Utils.DateConverterUtils;

import java.util.Date;

/**
 * Created by wilfred on 6/29/18.
 */
@Database(entities = {JournalEntry.class}, version = 1, exportSchema = false)
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
                        .addCallback(sRoomDatabaseCallback)
                        .build();
            }
        }
        return sInstance;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){

                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    new PopulateDbAsync(sInstance).execute();
                }
            };

    // AsyncTask that deletes the contents of the database, then populates it with the one entry
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final EntryDao mDao;

        PopulateDbAsync(JournalDatabase db) {
            mDao = db.entryDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            Date date = new Date();
            JournalEntry entry = new JournalEntry("Hello", "This is just a text for demo purpose", date);
            mDao.insertEntry(entry);
            return null;
        }
    }

public abstract EntryDao entryDao();
}
