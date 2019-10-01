package com.example.wilfred.journalapp.database

import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context

import com.example.wilfred.journalapp.Utils.DateConverterUtils

/**
 * Created by wilfred on 6/29/18.
 */
@Database(entities = [JournalEntry::class], version = 1, exportSchema = false)
@TypeConverters(DateConverterUtils::class)
abstract class JournalDatabase : RoomDatabase() {

    /*AsyncTask that deletes the contents of the database, then populates it with the one entry
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final EntryDao mDao;

        PopulateDbAsync(JournalDatabase db) {
            mDao = db.entryDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            return null;
        }
    }
*/
    abstract fun entryDao(): EntryDao

    companion object {
        private val LOCK = Any()
        private val DATABASE_NAME = "journalApp"
        private var sInstance: JournalDatabase? = null


        fun getsInstance(context: Context): JournalDatabase? {
            if (sInstance == null) {
                synchronized(LOCK) {
                    sInstance = Room.databaseBuilder(context.applicationContext,
                            JournalDatabase::class.java, DATABASE_NAME)
                            .addCallback(sRoomDatabaseCallback)
                            .build()
                }
            }
            return sInstance
        }

        private val sRoomDatabaseCallback = object : RoomDatabase.Callback() {

        }
    }
}
