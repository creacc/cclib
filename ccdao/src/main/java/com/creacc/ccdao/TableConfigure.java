package com.creacc.ccdao;

import android.support.annotation.NonNull;
import android.util.SparseArray;

import com.creacc.ccdao.annotation.CCColumn;
import com.creacc.ccdao.annotation.CCOrder;
import com.creacc.ccdao.annotation.CCTableEntity;
import com.creacc.ccdao.annotation.CCTableExcludeColumn;
import com.creacc.ccdao.annotation.CCWhere;
import com.creacc.ccdao.exception.CCDaoArgumentException;

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

    public String countSQL;

    public String tableName;

    public Map<Field, ColumnConfigure> fieldMap;

    public SparseArray<WhereCase> whereCases;

    public SparseArray<OrderCase> orderCases;

    public Class<? extends CCUpgrader>[] tableUpgraders;

    private TableConfigure(String name, Map<Field, ColumnConfigure> map, SparseArray<WhereCase> wheres, SparseArray<OrderCase> orders, Class<? extends CCUpgrader>[] upgraders) {
        createSQL = String.format("create table if not exists %s(%s)", name, getColumnSQL(map.values()));
        dropSQL = String.format("drop table %s", name);
        countSQL = String.format("select count(*) from %s", name);
        tableName = name;
        fieldMap = map;
        whereCases = wheres;
        orderCases = orders;
        tableUpgraders = upgraders;
    }

    @NonNull
    public static TableConfigure generateConfigure(Class entityClass) {
        CCTableEntity tableAnnotation = (CCTableEntity) entityClass.getAnnotation(CCTableEntity.class);
        int tableRule = tableAnnotation.rule();
        String tableName = tableAnnotation.name();
        HashMap<Field, ColumnConfigure> columnMap = new HashMap<Field, ColumnConfigure>();
        SparseArray<WhereCase> whereCases = new SparseArray<WhereCase>();
        SparseArray<OrderCase> orderCases = new SparseArray<OrderCase>();
        while (true) {
            analyse(entityClass, tableRule, columnMap, whereCases, orderCases);
            Class superclass = entityClass.getSuperclass();
            if (superclass.isAnnotationPresent(CCTableEntity.class)) {
                entityClass = superclass;
                tableAnnotation = (CCTableEntity) entityClass.getAnnotation(CCTableEntity.class);
                tableRule = tableAnnotation.rule();
            } else {
                break;
            }
        }
        return new TableConfigure(tableName, columnMap, whereCases, orderCases, tableAnnotation.upgraders());
    }

    private static void analyse(Class entityClass, int tableRule, HashMap<Field, ColumnConfigure> columnMap, SparseArray<WhereCase> whereCases, SparseArray<OrderCase> orderCases) {
        for (Field field : entityClass.getDeclaredFields()) {
            ColumnConfigure configure = null;
            if (tableRule == CCTableEntity.EXCLUDE) {
                if (field.isAnnotationPresent(CCTableExcludeColumn.class)) {
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
                parseWhereCauses(whereCases, field, configure);
                parseOrderColumn(orderCases, field, configure);
                field.setAccessible(true);
                columnMap.put(field, configure);
            }
        }
    }

    private static void parseWhereCauses(SparseArray<WhereCase> whereCases, Field field, ColumnConfigure configure) {
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
                configure.whereOperator = whereAnno.operator();
                whereCase.push(configure);
            } else {
                throw new CCDaoArgumentException("The update key must be an integer which is bigger than " + DEFAULT_UPDATE_KEY);
            }
        }
    }

    private static void parseOrderColumn(SparseArray<OrderCase> orderCase, Field field, ColumnConfigure configure) {
        if (field.isAnnotationPresent(CCOrder.class)) {
            orderCase.put((field.getAnnotation(CCOrder.class)).key(), new OrderCase(configure));
        }
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
