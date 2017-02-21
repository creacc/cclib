package com.creacc.ccdao;

/**
 * Created by Creacc on 2016/10/30.
 */

public class OrderCase {

    private String mOrderBy;

    public OrderCase(ColumnConfigure configure) {
        mOrderBy = "ORDER BY " + configure.columnName;
    }

    public String getOrderBy() {
        return mOrderBy;
    }
}
