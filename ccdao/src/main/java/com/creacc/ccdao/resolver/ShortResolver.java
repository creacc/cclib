package com.creacc.ccdao.resolver;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by Creacc on 2016/6/11.
 */

public class ShortResolver extends CCColumnResolver<Short> {

    private static final String COLUMN_TYPE = "integer";

    /**
     * 默认构造器
     *
     * @param name 数据库列名
     */
    public ShortResolver(String name) {
        super(name);
    }

    @Override
    public String columnType() {
        return COLUMN_TYPE;
    }

    @Override
    protected void innerSerialize(ContentValues values, String key, Short value) {
        values.put(key, value);
    }

    @Override
    protected Short innerDeserialize(Cursor cursor, int index) {
        return cursor.getShort(index);
    }

    @Override
    public String getWhereArgument(Short value) {
        return Short.toString(value);
    }
}
