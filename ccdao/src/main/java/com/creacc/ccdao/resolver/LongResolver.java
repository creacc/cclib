package com.creacc.ccdao.resolver;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by Creacc on 2016/6/11.
 */

public class LongResolver extends CCColumnResolver<Long> {

    private static final String COLUMN_TYPE = "text";

    /**
     * 默认构造器
     *
     * @param name 数据库列名
     */
    public LongResolver(String name) {
        super(name);
    }

    @Override
    public String columnType() {
        return COLUMN_TYPE;
    }

    @Override
    protected void innerSerialize(ContentValues values, String key, Long value) {
        values.put(key, Long.toString(value));
    }

    @Override
    protected Long innerDeserialize(Cursor cursor, int index) {
        return Long.parseLong(cursor.getString(index));
    }

    @Override
    public String getWhereArgument(Long value) {
        return Long.toString(value);
    }
}
