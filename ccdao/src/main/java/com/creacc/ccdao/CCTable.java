package com.creacc.ccdao;

import java.util.HashMap;

/**
 * Created by Creacc on 2016/5/10.
 */
public class CCTable<T> {

    private static final HashMap<Class, TableConfigure> TABLE_MAP = new HashMap<Class, TableConfigure>();

    private CCDaoContext mDaoContext;

    private TableConfigure mTableConfigure;

    public CCTable(CCDao dao, Class<T> entityClass) {
        if (entityClass.isAnnotationPresent(CCTableEntity.class)) {
            mDaoContext = dao.getContext();
            mTableConfigure = TABLE_MAP.get(entityClass);
            if (mTableConfigure == null) {
                mTableConfigure = TableConfigure.generateConfigure(entityClass);
                DaoUtils.createTable(mDaoContext, mTableConfigure);
                TABLE_MAP.put(entityClass, mTableConfigure);
            }
        } else {
            throw new CCDaoException("Please use CCTableEntity annotation");
        }
    }

    public CCDaoContext getContext() {
        return mDaoContext;
    }

    public boolean insert(T entity) {
        return DaoUtils.insert(entity, mDaoContext, mTableConfigure);
    }

    public boolean update(T entity) {
        return update(entity, 0);
    }

    public boolean update(T entity, int key) {
        return DaoUtils.update(key, entity, mDaoContext, mTableConfigure);
    }

    public boolean delete(T entity) {
        return delete(entity, 0);
    }

    public boolean delete(T entity, int key) {
        return DaoUtils.delete(key, entity, mDaoContext, mTableConfigure);
    }

    public void drop() {
        DaoUtils.dropTable(mDaoContext, mTableConfigure);
    }

}
