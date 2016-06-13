package com.creacc.ccdao.resolver;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by Creacc on 2016/5/12.
 */
public class IntegerResolver extends CCColumnResolver<Integer> {

    private static final String COLUMN_TYPE = "integer";

    public IntegerResolver(String name) {
        super(name);
    }

    @Override
    public String columnType() {
        return COLUMN_TYPE;
    }

    @Override
    protected void innerSerialize(ContentValues values, String key, Integer value) {
        values.put(key, value);
    }

    @Override
    protected Integer innerDeserialize(Cursor cursor, int index) {
        return cursor.getInt(index);
    }

    @Override
    public String getStringValue(Integer value) {
        return Integer.toString(value);
    }


}
