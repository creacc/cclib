package com.creacc.ccdaolite.base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by yanhaifeng on 16-11-7.
 */
public class DatabaseHelper {
    private static final String TAG = "DatabaseHelper";
    private SQLiteOpenHelper mSQLiteOpenHelper;

    private LinkedList<TableBase> mSQLiteTables = new LinkedList<>();

    private String mDatabaseName;

    private int mDatabaseVersion;

    private SQLiteDatabase mLockedSqliteDatabase;

    private ReentrantLock mDatabaseLock = new ReentrantLock(true);

    public DatabaseHelper(String name, int version) {
        mDatabaseName = name;
        mDatabaseVersion = version;
    }

    /**
     * Constructor for database with single table.
     *
     * @param table table in database which must override {@link TableBase#getDatabaseName()}
     *              and {@link TableBase#getDatabaseVersion()}.
     */
    public DatabaseHelper(TableBase table) {
        mSQLiteTables.add(table);
        mDatabaseName = table.getDatabaseName();
        mDatabaseVersion = table.getDatabaseVersion();
    }

    /**
     * Add table to database.
     */
    public DatabaseHelper addTable(TableBase table) {
        mSQLiteTables.add(table);
        return this;
    }

    /**
     * Open database, do not need to close.
     *
     * @param context
     */
    public void open(Context context) {
        if (mSQLiteOpenHelper == null) {
            mSQLiteOpenHelper = new SQLiteOpenHelper(context, mDatabaseName, null, mDatabaseVersion) {

                @Override
                public void onCreate(SQLiteDatabase db) {
                    for (TableBase table : mSQLiteTables) {
                        table.onCreate(db);
                    }
                }

                @Override
                public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                    for (TableBase table : mSQLiteTables) {
                        table.onUpgrade(db, oldVersion, newVersion);
                    }
                }

                @Override
                public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                    for (TableBase table : mSQLiteTables) {
                        table.onDowngrade(db, oldVersion, newVersion);
                    }
                }
            };
        }
    }

    public void lockDatabase(SQLiteDatabase database) {
        mLockedSqliteDatabase = database;
    }

    public void unlockDatabase() {
        mLockedSqliteDatabase = null;
    }

    SQLiteDatabase lockWrite() {
        mDatabaseLock.lock();
        if (mLockedSqliteDatabase != null) {
            return mLockedSqliteDatabase;
        }
        return mSQLiteOpenHelper.getWritableDatabase();
    }

    SQLiteDatabase lockRead() {
        mDatabaseLock.lock();
        if (mLockedSqliteDatabase != null) {
            return mLockedSqliteDatabase;
        }
        return mSQLiteOpenHelper.getReadableDatabase();
    }

    void unlock(SQLiteDatabase database) {
        try {
            if (database != null && database != mLockedSqliteDatabase) {
                database.close();
            }
        } finally {
            mDatabaseLock.unlock();
        }
    }

    void unlockWithoutCloseDB() {
        mDatabaseLock.unlock();
    }

}
