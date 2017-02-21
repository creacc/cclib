package com.creacc.ccdao;

import com.creacc.ccdao.exception.CCDaoArgumentException;

import java.util.List;

/**
 * Created by Creacc on 2016/10/30.
 */

public class QueryTransaction<T> {

    private CCDaoContext mDaoContext;

    private TableConfigure mTableConfigure;

    private EntityInstanceGenerator<T> mInstanceGenerator;

    private String mWhereCause;

    private String[] mWhereArgs;

    private String mOrderBy;

    private String mLimitRange;

    public QueryTransaction(CCDaoContext daoContext, TableConfigure tableConfigure, EntityInstanceGenerator<T> instanceGenerator) {
        mDaoContext = daoContext;
        mTableConfigure = tableConfigure;
        mInstanceGenerator = instanceGenerator;
    }

    public QueryTransaction where(int key, T entity) {
        WhereCase whereCase = mTableConfigure.whereCases.get(key);
        if (whereCase != null) {
            mWhereCause = whereCase.whereClause();
            mWhereArgs = whereCase.whereArgs(entity);
        } else {
            throw new CCDaoArgumentException("The key of where case is not declared");
        }
        return this;
    }

    public QueryTransaction order(int key) {
        OrderCase orderCase = mTableConfigure.orderCases.get(key);
        if (orderCase != null) {
            mOrderBy = orderCase.getOrderBy();
        } else {
            throw new CCDaoArgumentException("The key of order case is not declared");
        }
        return this;
    }

    public QueryTransaction limit(int count) {
        limit(0, count);
        return this;
    }

    public QueryTransaction limit(int start, int count) {
        mLimitRange = "LIMIT " + start + ", " + count;
        return this;
    }

    public List<T> query() {
        return DaoUtils.query(mDaoContext, mTableConfigure, mInstanceGenerator, mWhereCause, mWhereArgs, mOrderBy, mLimitRange);
    }

    public int queryCount() {
        return DaoUtils.queryCount(mDaoContext, mTableConfigure, mWhereCause, mWhereArgs);
    }
}
