package com.creacc.ccdao.resolver;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by Creacc on 2016/5/23.
 */

public class StringResolver extends CCColumnResolver<String> {

    private static final String COLUMN_TYPE = "text";

    /**
     * 默认构造器
     *
     * @param name 数据库列名
     */
    public StringResolver(String name) {
        super(name);
    }

    @Override
    public String columnType() {
        return COLUMN_TYPE;
    }

    @Override
    protected void innerSerialize(ContentValues values, String key, String value) {
        values.put(key, value);
    }

    @Override
    protected String innerDeserialize(Cursor cursor, int index) {
        return cursor.getString(index);
    }

    @Override
    public String getStringValue(String value) {
        return value;
    }
}
