package com.creacc.ccdao;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by Creacc on 2016/5/10.
 */
public class CCDaoContext {

    private SQLiteOpenHelper mOpenHelper;

    private ReadWriteLock mDaoLock = new ReentrantReadWriteLock();

    CCDaoContext(SQLiteOpenHelper helper) {
        mOpenHelper = helper;
    }

    public SQLiteDatabase lockRead() {
        mDaoLock.readLock().lock();
        return mOpenHelper.getReadableDatabase();
    }

    public SQLiteDatabase tryLockRead() {
        if (mDaoLock.readLock().tryLock()) {
            return mOpenHelper.getReadableDatabase();
        }
        return null;
    }

    public void unlockRead(SQLiteDatabase database) {
        database.close();
        mDaoLock.readLock().unlock();
    }

    public SQLiteDatabase lockWrite() {
        mDaoLock.writeLock().lock();
        return mOpenHelper.getWritableDatabase();
    }

    public SQLiteDatabase tryLockWrite() {
        if (mDaoLock.writeLock().tryLock()) {
            return mOpenHelper.getWritableDatabase();
        }
        return null;
    }

    public void unlockWrite(SQLiteDatabase database) {
        database.close();
        mDaoLock.writeLock().unlock();
    }
}
