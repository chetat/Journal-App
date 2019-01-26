package com.example.wilfred.journalapp.database;

import android.arch.lifecycle.ComputableLiveData;
import android.arch.lifecycle.LiveData;
import android.arch.persistence.db.SupportSQLiteStatement;
import android.arch.persistence.room.EntityDeletionOrUpdateAdapter;
import android.arch.persistence.room.EntityInsertionAdapter;
import android.arch.persistence.room.InvalidationTracker.Observer;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.RoomSQLiteQuery;
import android.database.Cursor;
import android.support.annotation.NonNull;
import com.example.wilfred.journalapp.Utils.DateConverterUtils;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@SuppressWarnings("unchecked")
public class EntryDao_Impl implements EntryDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfJournalEntry;

  private final EntityDeletionOrUpdateAdapter __deletionAdapterOfJournalEntry;

  private final EntityDeletionOrUpdateAdapter __updateAdapterOfJournalEntry;

  public EntryDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfJournalEntry = new EntityInsertionAdapter<JournalEntry>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `journal`(`id`,`entryTitle`,`entryText`,`updated_at`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, JournalEntry value) {
        stmt.bindLong(1, value.getId());
        if (value.getEntryTitle() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getEntryTitle());
        }
        if (value.getEntryText() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getEntryText());
        }
        final Long _tmp;
        _tmp = DateConverterUtils.toTimestamp(value.getUpdatedAt());
        if (_tmp == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindLong(4, _tmp);
        }
      }
    };
    this.__deletionAdapterOfJournalEntry = new EntityDeletionOrUpdateAdapter<JournalEntry>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `journal` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, JournalEntry value) {
        stmt.bindLong(1, value.getId());
      }
    };
    this.__updateAdapterOfJournalEntry = new EntityDeletionOrUpdateAdapter<JournalEntry>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR REPLACE `journal` SET `id` = ?,`entryTitle` = ?,`entryText` = ?,`updated_at` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, JournalEntry value) {
        stmt.bindLong(1, value.getId());
        if (value.getEntryTitle() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getEntryTitle());
        }
        if (value.getEntryText() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getEntryText());
        }
        final Long _tmp;
        _tmp = DateConverterUtils.toTimestamp(value.getUpdatedAt());
        if (_tmp == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindLong(4, _tmp);
        }
        stmt.bindLong(5, value.getId());
      }
    };
  }

  @Override
  public void insertEntry(JournalEntry journalEntry) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfJournalEntry.insert(journalEntry);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteEntry(JournalEntry journalEntry) {
    __db.beginTransaction();
    try {
      __deletionAdapterOfJournalEntry.handle(journalEntry);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateEntry(JournalEntry journalEntry) {
    __db.beginTransaction();
    try {
      __updateAdapterOfJournalEntry.handle(journalEntry);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public LiveData<List<JournalEntry>> getAllEntries() {
    final String _sql = "SELECT * FROM Journal";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return new ComputableLiveData<List<JournalEntry>>() {
      private Observer _observer;

      @Override
      protected List<JournalEntry> compute() {
        if (_observer == null) {
          _observer = new Observer("Journal") {
            @Override
            public void onInvalidated(@NonNull Set<String> tables) {
              invalidate();
            }
          };
          __db.getInvalidationTracker().addWeakObserver(_observer);
        }
        final Cursor _cursor = __db.query(_statement);
        try {
          final int _cursorIndexOfId = _cursor.getColumnIndexOrThrow("id");
          final int _cursorIndexOfEntryTitle = _cursor.getColumnIndexOrThrow("entryTitle");
          final int _cursorIndexOfEntryText = _cursor.getColumnIndexOrThrow("entryText");
          final int _cursorIndexOfUpdatedAt = _cursor.getColumnIndexOrThrow("updated_at");
          final List<JournalEntry> _result = new ArrayList<JournalEntry>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final JournalEntry _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpEntryTitle;
            _tmpEntryTitle = _cursor.getString(_cursorIndexOfEntryTitle);
            final String _tmpEntryText;
            _tmpEntryText = _cursor.getString(_cursorIndexOfEntryText);
            final Date _tmpUpdatedAt;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfUpdatedAt)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfUpdatedAt);
            }
            _tmpUpdatedAt = DateConverterUtils.toDate(_tmp);
            _item = new JournalEntry(_tmpId,_tmpEntryTitle,_tmpEntryText,_tmpUpdatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    }.getLiveData();
  }
}
