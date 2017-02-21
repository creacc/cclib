package com.creacc.ccdao.resolver;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by Creacc on 2016/6/11.
 */

public class ByteResolver extends CCColumnResolver<Byte> {

    private static final String COLUMN_TYPE = "integer";

    /**
     * 默认构造器
     *
     * @param name 数据库列名
     */
    public ByteResolver(String name) {
        super(name);
    }

    @Override
    public String columnType() {
        return COLUMN_TYPE;
    }

    @Override
    protected void innerSerialize(ContentValues values, String key, Byte value) {
        values.put(key, value);
    }

    @Override
    protected Byte innerDeserialize(Cursor cursor, int index) {
        return (byte) cursor.getInt(index);
    }

    @Override
    public String getWhereArgument(Byte value) {
        return Byte.toString(value);
    }
}
