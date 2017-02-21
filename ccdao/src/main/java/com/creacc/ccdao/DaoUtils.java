package com.creacc.ccdao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.creacc.ccdao.exception.CCDaoArgumentException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Creacc on 2016/5/12.
 */
class DaoUtils {

    static void createTable(CCDaoContext dao, TableConfigure table) {
        SQLiteDatabase database = dao.lockWrite();
        try {
            database.execSQL(table.createSQL);
        } finally {
            dao.unlockWrite(database);
        }
    }

    static void dropTable(CCDaoContext dao, TableConfigure table) {
        SQLiteDatabase database = dao.lockWrite();
        try {
            database.execSQL(table.dropSQL);
        } finally {
            dao.unlockWrite(database);
        }
    }

    static boolean insert(Object entity, CCDaoContext dao, TableConfigure table) {
        SQLiteDatabase database = dao.lockWrite();
        try {
            ContentValues contentValues = new ContentValues();
            for (Map.Entry<Field, ColumnConfigure> entry : table.fieldMap.entrySet()) {
                Field field = entry.getKey();
                ColumnConfigure columnConfigure = entry.getValue();
                try {
                    columnConfigure.columnResolver.serialize(field.get(entity), contentValues);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            return database.insert(table.tableName, null, contentValues) != -1;
        } finally {
            dao.unlockWrite(database);
        }
    }

    static boolean update(int key, Object entity, CCDaoContext dao, TableConfigure table) {
        SQLiteDatabase database = dao.lockWrite();
        try {
            WhereCase whereCase = table.whereCases.get(key);
            if (whereCase == null) {
                throw new CCDaoArgumentException("The update key is not declared");
            } else {
                ContentValues contentValues = new ContentValues();
                for (Map.Entry<Field, ColumnConfigure> entry : table.fieldMap.entrySet()) {
                    Field field = entry.getKey();
                    ColumnConfigure columnConfigure = entry.getValue();
                    try {
                        columnConfigure.columnResolver.serialize(field.get(entity), contentValues);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                return database.update(table.tableName, contentValues,
                        whereCase.whereClause(), whereCase.whereArgs(entity)) > 0;
            }
        } finally {
            dao.unlockWrite(database);
        }
    }

    static boolean delete(int key, Object entity, CCDaoContext dao, TableConfigure table) {
        SQLiteDatabase database = dao.lockWrite();
        try {
            WhereCase whereCase = table.whereCases.get(key);
            if (whereCase == null) {
                throw new CCDaoArgumentException("The update key is not declared");
            } else {
                ContentValues contentValues = new ContentValues();
                for (Map.Entry<Field, ColumnConfigure> entry : table.fieldMap.entrySet()) {
                    Field field = entry.getKey();
                    ColumnConfigure columnConfigure = entry.getValue();
                    try {
                        columnConfigure.columnResolver.serialize(field.get(entity), contentValues);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                return database.delete(table.tableName, whereCase.whereClause(),
                        whereCase.whereArgs(entity)) > 0;
            }
        } finally {
            dao.unlockWrite(database);
        }
    }

    static int queryCount(CCDaoContext dao, TableConfigure table) {
        return queryCount(dao, table, null, null);
    }

    static int queryCount(CCDaoContext dao, TableConfigure table, String whereCause, String[] whereArgs) {
        SQLiteDatabase database = dao.lockRead();
        Cursor cursor = null;
        try {
            String rawSQL = table.countSQL;
            if (TextUtils.isEmpty(whereCause) == false) {
                rawSQL += " " + whereCause;
            }
            cursor = database.rawQuery(rawSQL, whereArgs);
            if (cursor.moveToNext()) {
                return (int) cursor.getLong(0);
            }
            return 0;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dao.unlockRead(database);
        }
    }

    static <T> List<T> query(CCDaoContext dao, TableConfigure table, EntityInstanceGenerator<T> generator) {
        return query(dao, table, generator, null, null, null, null);
    }

    static <T> List<T> query(CCDaoContext dao, TableConfigure table, EntityInstanceGenerator<T> generator, String whereCause, String[] whereArgs, String order, String limit) {
        SQLiteDatabase database = dao.lockRead();
        Cursor cursor = null;
        try {
            cursor = database.query(table.tableName, null, whereCause,
                    whereArgs, null, null, order, limit);
            List<T> result = new ArrayList<T>();
            while (cursor.moveToNext()) {
                T entity = generator.generateInstance();
                for (ColumnConfigure columnConfigure : table.fieldMap.values()) {
                    int columnIndex = cursor.getColumnIndex(columnConfigure.columnName);
                    if (columnIndex >= 0) {
                        try {
                            columnConfigure.columnField.set(entity, columnConfigure.columnResolver.deserialize(cursor));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
                result.add(entity);
            }
            return result;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dao.unlockRead(database);
        }
    }

}
