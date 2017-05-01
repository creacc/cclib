package com.creacc.ccdaolite.query;

/**
 * Created by yanhaifeng on 16-11-8.
 */
public enum QueryFunction {

    COUNT("SELECT COUNT(%s) FROM %s"),
    MAX("SELECT MAX(%s) FROM %s"),
    MIN("SELECT MIN(%s) FROM %s"),
    AVG("SELECT AVG(%s) FROM %s"),
    SUM("SELECT SUM(%s) FROM %s"),
    LENGTH("SELECT LENGTH(%s) FROM %s");

    private static final String FUNCTION_VALUE_FORMAT = "(%s)";

    private String mFunctionFormat;

    QueryFunction(String functionFormat) {
        this.mFunctionFormat = functionFormat;
    }

    public String toQuerySQL(String table, String column) {
        return String.format(mFunctionFormat, column, table);
    }

    public String toValueSQL(String table, String column) {
        return String.format(FUNCTION_VALUE_FORMAT, toQuerySQL(table, column));
    }
}
