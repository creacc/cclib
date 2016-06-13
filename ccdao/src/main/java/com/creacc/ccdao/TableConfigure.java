package com.creacc.ccdao;

import android.support.annotation.NonNull;
import android.util.SparseArray;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Creacc on 2016/5/12.
 */
class TableConfigure {

    public static final int DEFAULT_UPDATE_KEY = 0;

    public String createSQL;

    public String dropSQL;

    public String tableName;

    public int tableRule;

    public Map<Field, ColumnConfigure> fieldMap;

    public SparseArray<WhereCase> whereCases;

    private TableConfigure(String name, int rule, Map<Field, ColumnConfigure> map) {
        createSQL = String.format("create table if not exists %s(%s);", tableName, getColumnSQL(map.values()));
        dropSQL = String.format("drop table %s;", tableName);
        tableName = name;
        tableRule = rule;
        fieldMap = map;
    }

    @NonNull
    public static TableConfigure generateConfigure(Class entityClass) {
        CCTableEntity tableAnnotation = (CCTableEntity) entityClass.getAnnotation(CCTableEntity.class);
        String tableName = tableAnnotation.name();
        int tableRule = tableAnnotation.rule();
        HashMap<Field, ColumnConfigure> columnMap = new HashMap<Field, ColumnConfigure>();
        SparseArray<WhereCase> whereCases = new SparseArray<>();
        ColumnConfigure configure = null;
        for (Field field : entityClass.getDeclaredFields()) {
            if (tableRule == CCTableEntity.EXCLUDE) {
                if (field.isAnnotationPresent(CCTableExcludeColumn.class) == false) {
                    continue;
                }
                if (field.isAnnotationPresent(CCColumn.class)) {
                    configure = ColumnConfigure.generateConfigure(field, field.getAnnotation(CCColumn.class));
                } else {
                    configure = ColumnConfigure.generateConfigure(field, null);
                }
            } else {
                if (field.isAnnotationPresent(CCColumn.class)) {
                    configure = ColumnConfigure.generateConfigure(field, field.getAnnotation(CCColumn.class));
                }
            }
            if (configure != null) {
                if (configure.isKey()) {
                    whereCases.put(DEFAULT_UPDATE_KEY, new WhereCase(configure));
                }
                if (field.isAnnotationPresent(CCWhere.class)) {
                    CCWhere whereAnno = field.getAnnotation(CCWhere.class);
                    int key = whereAnno.key();
                    if (key > DEFAULT_UPDATE_KEY) {
                        WhereCase whereCase = whereCases.get(key);
                        if (whereCase == null) {
                            whereCase = new WhereCase();
                            whereCases.put(key, whereCase);
                        }
                        whereCase.push(configure);
                    } else {
                        throw new CCDaoException("The update key must be an integer which is not equal to " + DEFAULT_UPDATE_KEY);
                    }
                }
                field.setAccessible(true);
                columnMap.put(field, configure);
            }
        }
        return new TableConfigure(tableName, tableRule, columnMap);
    }

    @NonNull
    private static String getColumnSQL(Collection<ColumnConfigure> configures) {
        StringBuilder columnSQLBuilder = new StringBuilder();
        for (ColumnConfigure configure : configures) {
            if (columnSQLBuilder.length() > 0) {
                columnSQLBuilder.append(",");
            }
            columnSQLBuilder.append(configure.columnName);
            columnSQLBuilder.append(" ");
            columnSQLBuilder.append(configure.columnResolver.columnType());
            if (configure.isKey()) {
                columnSQLBuilder.append(" primary key");
                if (configure.isIncrementKey()) {
                    columnSQLBuilder.append(" autoincrement ");
                }
            }
        }
        return columnSQLBuilder.toString();
    }

}
