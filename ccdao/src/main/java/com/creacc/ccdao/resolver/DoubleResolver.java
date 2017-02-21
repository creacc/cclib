package com.creacc.ccdao.resolver;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by Creacc on 2016/6/11.
 */

public class DoubleResolver extends CCColumnResolver<Double> {

    private static final String COLUMN_TYPE = "real";

    /**
     * 默认构造器
     *
     * @param name 数据库列名
     */
    public DoubleResolver(String name) {
        super(name);
    }

    @Override
    public String columnType() {
        return COLUMN_TYPE;
    }

    @Override
    protected void innerSerialize(ContentValues values, String key, Double value) {
        values.put(key, value);
    }

    @Override
    protected Double innerDeserialize(Cursor cursor, int index) {
        return cursor.getDouble(index);
    }

    @Override
    public String getWhereArgument(Double value) {
        return Double.toString(value);
    }
}
