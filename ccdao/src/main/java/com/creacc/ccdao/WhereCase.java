package com.creacc.ccdao;

import android.text.TextUtils;

import com.creacc.ccbox.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Creacc on 2016/6/11.
 */

class WhereCase {

    private List<ColumnConfigure> mWhereColumns = new ArrayList<>();

    private String mWhereClause;

    public WhereCase() {

    }

    public WhereCase(ColumnConfigure configure) {
        push(configure);
    }

    public void push(ColumnConfigure column) {
        mWhereColumns.add(column);
    }

    public String whereClause() {
        if (TextUtils.isEmpty(mWhereClause)) {
            mWhereClause = StringUtils.join(" and ", new StringUtils.StringJoinAdapter() {
                @Override
                public int getCount() {
                    return mWhereColumns.size();
                }

                @Override
                public String getValue(int index) {
                    return mWhereColumns.get(index).columnName + "=?";
                }
            });
        }
        return mWhereClause;
    }

    public String[] whereArgs(final Object object) {
        return StringUtils.toArray(new StringUtils.StringJoinAdapter() {
            @Override
            public int getCount() {
                return mWhereColumns.size();
            }

            @Override
            public String getValue(int index) {
                ColumnConfigure configure = mWhereColumns.get(index);
                try {
                    Object value = configure.columnField.get(object);
                    return configure.columnResolver.getStringValue(value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }
}
