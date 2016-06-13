package com.creacc.ccdao;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import java.lang.reflect.Field;
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
                throw new CCDaoException("The update key is not declared");
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
                throw new CCDaoException("The update key is not declared");
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
}
