package com.creacc.ccdao;

import android.database.sqlite.SQLiteDatabase;

import com.creacc.ccdao.annotation.CCTableEntity;
import com.creacc.ccdao.exception.CCDaoInitializeException;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Creacc on 2016/5/10.
 */
public class CCTable<T> {

    private static final HashMap<Class, TableConfigure> TABLE_MAP = new HashMap<Class, TableConfigure>();

    private CCDaoContext mDaoContext;

    private TableConfigure mTableConfigure;

    private EntityInstanceGenerator<T> mInstanceGenerator;

    public CCTable(CCDao dao, Class<T> entityClass) {
        this(dao, entityClass, null);
    }

    public CCTable(CCDao dao, Class<T> entityClass, EntityInstanceGenerator<T> generator) {
        if (entityClass.isAnnotationPresent(CCTableEntity.class)) {
            mInstanceGenerator = generator == null ? new DefaultInstanceGenerator<T>(entityClass) : generator;
            mTableConfigure = TABLE_MAP.get(entityClass);
            if (mTableConfigure == null) {
                mTableConfigure = TableConfigure.generateConfigure(entityClass);
                TABLE_MAP.put(entityClass, mTableConfigure);
            }
            dao.addTable(this);
        } else {
            throw new CCDaoInitializeException("Please use CCTableEntity annotation");
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

    public List<T> query() {
        return DaoUtils.query(mDaoContext, mTableConfigure, mInstanceGenerator);
    }

    public int queryCount() {
        return DaoUtils.queryCount(mDaoContext, mTableConfigure);
    }

    public QueryTransaction<T> queryTransaction() {
        return new QueryTransaction<T>(mDaoContext, mTableConfigure, mInstanceGenerator);
    }

    public void drop() {
        DaoUtils.dropTable(mDaoContext, mTableConfigure);
    }

    void initializeDaoContext(CCDaoContext daoContext) {
        mDaoContext = daoContext;
    }

    void create(SQLiteDatabase database) {
        DaoUtils.createTable(mDaoContext, mTableConfigure);
    }

    protected void upgrade(SQLiteDatabase database, int originalVersion, int finalVersion) {

    }

    protected void downgrade(SQLiteDatabase database, int originalVersion, int finalVersion) {

    }

}
