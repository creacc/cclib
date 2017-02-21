package com.creacc.ccdao.resolver;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by Creacc on 2016/6/11.
 */

public class BooleanResolver extends CCColumnResolver<Boolean> {

    private static final String COLUMN_TYPE = "integer";

    /**
     * 默认构造器
     *
     * @param name 数据库列名
     */
    public BooleanResolver(String name) {
        super(name);
    }

    @Override
    public String columnType() {
        return COLUMN_TYPE;
    }

    @Override
    protected void innerSerialize(ContentValues values, String key, Boolean value) {
        values.put(key, value ? 1 : 0);
    }

    @Override
    protected Boolean innerDeserialize(Cursor cursor, int index) {
        return cursor.getInt(index) == 1 ? true : false;
    }

    @Override
    public String getWhereArgument(Boolean value) {
        return value ? "1" : "0";
    }
}
