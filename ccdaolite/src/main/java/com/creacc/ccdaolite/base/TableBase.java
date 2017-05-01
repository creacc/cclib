package com.creacc.ccdaolite.base;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.creacc.ccdaolite.query.QueryCase;
import com.creacc.ccdaolite.query.QueryFunction;
import com.creacc.ccdaolite.query.WhereCase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yanhaifeng on 16-11-7.
 */
public abstract class TableBase<T> {
    private static final String TAG = "TableBase";

    protected static final int DEFAULT_UPDATE_KEY = 0;

    private static final String SELECT_FROM = "SELECT * FROM ";
    private static final String SELECT_WILDCARD = "*";
    protected static final String DROP_TABLE = "drop table ";

    private DatabaseHelper mDatabaseHelper;

    private String mTableName;

    public TableBase(Context context, String tableName) {
        mTableName = tableName;
        mDatabaseHelper = new DatabaseHelper(this);
        mDatabaseHelper.open(context);
    }

    public TableBase(DatabaseHelper databaseHelper) {
        mTableName = getTableName();
        mDatabaseHelper = databaseHelper;
        mDatabaseHelper.addTable(this);
    }

    protected void open(Context context) {
        mDatabaseHelper.open(context);
    }

    protected void lockDatabase(SQLiteDatabase database) {
        mDatabaseHelper.lockDatabase(database);
    }

    protected void unlockDatabase() {
        mDatabaseHelper.unlockDatabase();
    }

    public int count() {
        return count(null);
    }

    public boolean insert(T entity) {
        SQLiteDatabase database = null;
        try {
            database = mDatabaseHelper.lockWrite();
            return database.insert(mTableName, null, insertValues(entity)) != -1;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mDatabaseHelper.unlock(database);
        }
        return false;
    }

    public boolean insert(List<T> entities) {
        SQLiteDatabase database = null;
        try {
            database = mDatabaseHelper.lockWrite();
            database.beginTransaction();
            boolean result = true;
            for (T entity : entities) {
                if (database.insert(mTableName, null, insertValues(entity)) == -1) {
                    result = false;
                    break;
                }
            }
            if (result) {
                database.setTransactionSuccessful();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                database.endTransaction();
            } finally {
                mDatabaseHelper.unlock(database);
            }
        }
        return false;
    }

    public void clear() {
        SQLiteDatabase database = null;
        try {
            database = mDatabaseHelper.lockWrite();
            database.delete(mTableName, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mDatabaseHelper.unlock(database);
        }
    }

    public List<T> query() {
        return queryWithCases();
    }

    protected int count(WhereCase whereCase) {
        return queryFunction(QueryFunction.COUNT, SELECT_WILDCARD, whereCase);
    }

    protected boolean insertOrUpdate(T entity, WhereCase whereCase) {
        return count(whereCase) > 0 ? update(entity, whereCase) : insert(entity);
    }

    protected boolean insertOrUpdate(T entity, WhereCase whereCase, int updateKey) {
        return count(whereCase) > 0 ? update(entity, whereCase, updateKey) : insert(entity);
    }

    protected boolean update(T entity, WhereCase whereCase) {
        return update(entity, whereCase, DEFAULT_UPDATE_KEY);
    }

    protected boolean update(T entity, WhereCase whereCase, int updateKey) {
        SQLiteDatabase database = null;
        try {
            database = mDatabaseHelper.lockWrite();
            return database.update(mTableName, updateWithKey(entity, updateKey), whereCase.toSelectionSQL(), null) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mDatabaseHelper.unlock(database);
        }
        return false;
    }

    protected boolean update(List<T> entities, WhereCase.WhereCaseAdapter<T> whereCaseAdapter) {
        return update(entities, whereCaseAdapter, DEFAULT_UPDATE_KEY);
    }

    protected boolean update(List<T> entities, WhereCase.WhereCaseAdapter<T> whereCaseAdapter, int updateKey) {
        SQLiteDatabase database = null;
        try {
            database = mDatabaseHelper.lockWrite();
            database.beginTransaction();
            boolean result = true;
            for (T entity : entities) {
                if (database.update(mTableName, updateWithKey(entity, updateKey),
                        whereCaseAdapter.getWhereCase(entity).toSelectionSQL(), null) <= 0) {
                    result = false;
                    break;
                }
            }
            if (result) {
                database.setTransactionSuccessful();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                database.endTransaction();
            } finally {
                mDatabaseHelper.unlock(database);
            }
        }
        return false;
    }

    protected boolean delete(List<T> entities, WhereCase.WhereCaseAdapter<T> whereCaseAdapter) {
        SQLiteDatabase database = null;
        try {
            database = mDatabaseHelper.lockWrite();
            database.beginTransaction();
            boolean result = true;
            for (T entity : entities) {
                if (database.delete(mTableName, whereCaseAdapter.getWhereCase(entity).toSelectionSQL(), null) <= 0) {
                    result = false;
                    break;
                }
            }
            if (result) {
                database.setTransactionSuccessful();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                database.endTransaction();
            } finally {
                mDatabaseHelper.unlock(database);
            }
        }
        return false;
    }

    protected boolean delete(WhereCase whereCase) {
        SQLiteDatabase database = null;
        try {
            database = mDatabaseHelper.lockWrite();
            return database.delete(mTableName, whereCase.toSelectionSQL(), null) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mDatabaseHelper.unlock(database);
        }
        return false;
    }

    protected int queryFunction(QueryFunction function, String column, WhereCase whereCase) {
        SQLiteDatabase database = null;
        Cursor cursor = null;
        try {
            database = mDatabaseHelper.lockRead();
            StringBuilder queryBuilder = new StringBuilder()
                    .append(function.toQuerySQL(mTableName, column));
            if (whereCase != null) {
                queryBuilder.append(whereCase.toQuerySQL());
            }
            cursor = database.rawQuery(queryBuilder.toString(), null);
            if (cursor.moveToFirst()) {
                return cursor.getInt(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }  finally {
            try {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
            } finally {
                mDatabaseHelper.unlock(database);
            }
        }
        return 0;
    }

    protected List<T> queryWithCases(QueryCase... queryCases) {
        SQLiteDatabase database = null;
        Cursor cursor = null;
        try {
            database = mDatabaseHelper.lockRead();
            StringBuilder queryBuilder = new StringBuilder()
                    .append(SELECT_FROM)
                    .append(mTableName);
            if (queryCases != null) {
                for (QueryCase queryCase : queryCases) {
                    queryBuilder.append(queryCase.toQuerySQL());
                }
            }
            List<T> entities = new ArrayList<>();
            cursor = database.rawQuery(queryBuilder.toString(), null);
            while (cursor.moveToNext()) {
                T entity = queryFromCursor(cursor);
                if (entity != null) {
                    entities.add(entity);
                }
            }
            return entities;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
            } finally {
                mDatabaseHelper.unlock(database);
            }
        }
        return null;
    }

    protected Cursor queryCursorWithCases(QueryCase... queryCases) {
        try {
            SQLiteDatabase database = mDatabaseHelper.lockRead();
            StringBuilder queryBuilder = new StringBuilder()
                    .append(SELECT_FROM)
                    .append(mTableName);
            if (queryCases != null) {
                for (QueryCase queryCase : queryCases) {
                    queryBuilder.append(queryCase.toQuerySQL());
                }
            }
            Cursor cursor = database.rawQuery(queryBuilder.toString(), null);
            return cursor;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // 提供出ContentProvider，不关cursor
                /*if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }*/
            } finally {
                mDatabaseHelper.unlockWithoutCloseDB();
            }
        }
        return null;
    }

    protected T querySingleWithCases(QueryCase... queryCases) {
        SQLiteDatabase database = null;
        Cursor cursor = null;
        try {
            database = mDatabaseHelper.lockRead();
            StringBuilder queryBuilder = new StringBuilder()
                    .append(SELECT_FROM)
                    .append(mTableName);
            if (queryCases != null) {
                for (QueryCase queryCase : queryCases) {
                    queryBuilder.append(queryCase.toQuerySQL());
                }
            }
            T entity = null;
            cursor = database.rawQuery(queryBuilder.toString(), null);
            if (cursor.moveToNext()) {
                entity = queryFromCursor(cursor);
            }
            return entity;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
            } finally {
                mDatabaseHelper.unlock(database);
            }
        }
        return null;
    }

    void onCreate(SQLiteDatabase db) {
        db.execSQL(createColumns());
    }

    void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        upgradeTable(db, oldVersion, newVersion);
    }

    void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        downgradeTable(db, oldVersion, newVersion);
    }

    /**
     * Must be overridden if use constructor of {@link DatabaseHelper#DatabaseHelper(TableBase)}.
     *
     * @throws UnsupportedOperationException if not be overridden
     * when use constructor of {@link DatabaseHelper#DatabaseHelper(TableBase)}
     *
     * @return database name
     */
    protected String getDatabaseName() {
        throw new UnsupportedOperationException();
    }

    /**
     * Must be overridden if use constructor of {@link DatabaseHelper#DatabaseHelper(TableBase)}.
     *
     * @throws UnsupportedOperationException if not be overridden
     * when use constructor of {@link DatabaseHelper#DatabaseHelper(TableBase)}
     *
     * @return database version
     */
    protected int getDatabaseVersion() {
        throw new UnsupportedOperationException();
    }

    /**
     * Must be overridden if use constructor of {@link DatabaseHelper#DatabaseHelper(TableBase)}.
     *
     * @throws UnsupportedOperationException if not be overridden
     * when use constructor of {@link DatabaseHelper#DatabaseHelper(TableBase)}
     *
     * @return table name
     */
    protected String getTableName() {
        throw new UnsupportedOperationException();
    }

    /**
     * Use {@link String} to create a SQL string for creating table.
     */
    protected abstract String createColumns();

    /**
     * Override if need
     */
    protected void upgradeTable(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * Override if need
     */
    protected void downgradeTable(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE + mTableName);
        db.execSQL(createColumns());
    }

    /**
     * Insert row to database.
     *
     * @param entity insert entity
     *
     * @return the values set into {@link SQLiteDatabase#insert(String, String, ContentValues)}
     */
    protected abstract ContentValues insertValues(T entity);

    /**
     * Update row to database.
     *
     * @param entity update entity
     * @param updateKey relate to the key in {@link TableBase#update(T, WhereCase, int)}
     *
     * @return the values set into {@link SQLiteDatabase#update(String, ContentValues, String, String[])}
     */
    protected abstract ContentValues updateWithKey(T entity, int updateKey);

    protected abstract T queryFromCursor(Cursor cursor);
}
