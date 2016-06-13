package com.creacc.ccdao.resolver;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.Arrays;

/**
 * Created by Creacc on 2016/6/11.
 */

public class ByteArrayResolver extends CCColumnResolver<byte[]> {

    private static final String COLUMN_TYPE = "blob";

    /**
     * 默认构造器
     *
     * @param name 数据库列名
     */
    public ByteArrayResolver(String name) {
        super(name);
    }

    @Override
    public String columnType() {
        return COLUMN_TYPE;
    }

    @Override
    protected void innerSerialize(ContentValues values, String key, byte[] value) {
        values.put(key, value);
    }

    @Override
    protected byte[] innerDeserialize(Cursor cursor, int index) {
        return cursor.getBlob(index);
    }

    @Override
    public String getStringValue(byte[] value) {
        return Arrays.toString(value);
    }
}
