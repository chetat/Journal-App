package com.example.wilfred.journalapp.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.db.SupportSQLiteOpenHelper.Callback;
import android.arch.persistence.db.SupportSQLiteOpenHelper.Configuration;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.RoomOpenHelper;
import android.arch.persistence.room.RoomOpenHelper.Delegate;
import android.arch.persistence.room.util.TableInfo;
import android.arch.persistence.room.util.TableInfo.Column;
import android.arch.persistence.room.util.TableInfo.ForeignKey;
import android.arch.persistence.room.util.TableInfo.Index;
import java.lang.IllegalStateException;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.HashSet;

@SuppressWarnings("unchecked")
public class JournalDatabase_Impl extends JournalDatabase {
  private volatile EntryDao _entryDao;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `journal` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `entryTitle` TEXT, `entryText` TEXT, `updated_at` INTEGER)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, \"1630789c28c8526a42ce937d3bbe5d97\")");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `journal`");
      }

      @Override
      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      protected void validateMigration(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsJournal = new HashMap<String, TableInfo.Column>(4);
        _columnsJournal.put("id", new TableInfo.Column("id", "INTEGER", true, 1));
        _columnsJournal.put("entryTitle", new TableInfo.Column("entryTitle", "TEXT", false, 0));
        _columnsJournal.put("entryText", new TableInfo.Column("entryText", "TEXT", false, 0));
        _columnsJournal.put("updated_at", new TableInfo.Column("updated_at", "INTEGER", false, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysJournal = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesJournal = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoJournal = new TableInfo("journal", _columnsJournal, _foreignKeysJournal, _indicesJournal);
        final TableInfo _existingJournal = TableInfo.read(_db, "journal");
        if (! _infoJournal.equals(_existingJournal)) {
          throw new IllegalStateException("Migration didn't properly handle journal(com.example.wilfred.journalapp.database.JournalEntry).\n"
                  + " Expected:\n" + _infoJournal + "\n"
                  + " Found:\n" + _existingJournal);
        }
      }
    }, "1630789c28c8526a42ce937d3bbe5d97", "2d973fe2dc75da26f72301cef0430ec0");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    return new InvalidationTracker(this, "journal");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `journal`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  public EntryDao entryDao() {
    if (_entryDao != null) {
      return _entryDao;
    } else {
      synchronized(this) {
        if(_entryDao == null) {
          _entryDao = new EntryDao_Impl(this);
        }
        return _entryDao;
      }
    }
  }
}
