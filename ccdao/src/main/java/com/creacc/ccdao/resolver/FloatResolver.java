package com.creacc.ccdao.resolver;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by Creacc on 2016/6/11.
 */

public class FloatResolver extends CCColumnResolver<Float> {

    private static final String COLUMN_TYPE = "real";

    public FloatResolver(String name) {
        super(name);
    }

    @Override
    public String columnType() {
        return COLUMN_TYPE;
    }

    @Override
    protected void innerSerialize(ContentValues values, String key, Float value) {
        values.put(key, value);
    }

    @Override
    protected Float innerDeserialize(Cursor cursor, int index) {
        return cursor.getFloat(index);
    }

    @Override
    public String getStringValue(Float value) {
        return Float.toString(value);
    }
}
