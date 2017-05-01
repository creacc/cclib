package com.creacc.cclistview.adapter;

import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A handler to control content. <br/>
 * Every modification of content will call {@link BaseAdapter#notifyDataSetChanged()} automatically.
 *
 * Created by yanhaifeng on 16-7-27.
 */

public class CCAdapterHandler<T> extends ArrayList<T> {

    private ContentObserver mContentObserver;

    private AtomicBoolean mIsDataChanged = new AtomicBoolean(false);

    private AtomicBoolean mIsNotifyPaused = new AtomicBoolean(false);

    public CCAdapterHandler() {
        super();
    }

    public CCAdapterHandler(Collection<T> collection) {
        super(collection);
    }

    void setContentObserver(ContentObserver contentObserver) {
        mContentObserver = contentObserver;
    }

    /**
     * Pause auto notify, when you have a serial modifications to content.<br/>
     * Avoid calling {@link BaseAdapter#notifyDataSetChanged()} frequently.
     */
    public void pauseNotify() {
        mIsNotifyPaused.set(true);
    }

    /**
     * Resume auto notify, after a serial modifications.<br/>
     * If has modification between pause and resume operation, this method will call {@link BaseAdapter#notifyDataSetChanged()} to update display,
     * otherwise do nothing.
     */
    public void resumeNotify() {
        if (mIsNotifyPaused.compareAndSet(true, false) && mIsDataChanged.compareAndSet(true, false)) {
            notifyObserver(true);
        }
    }

    @Override
    public boolean add(T object) {
        try {
            return super.add(object);
        } finally {
            notifyObserver();
        }
    }

    @Override
    public void add(int index, T object) {
        try {
            super.add(index, object);
        } finally {
            notifyObserver();
        }
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {
        try {
            return super.addAll(collection);
        } finally {
            notifyObserver();
        }
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> collection) {
        try {
            return super.addAll(index, collection);
        } finally {
            notifyObserver();
        }
    }

    @Override
    public T set(int index, T object) {
        try {
            return super.set(index, object);
        } finally {
            notifyObserver();
        }
    }

    public void set(Collection<T> collection) {
        try {
            super.clear();
            super.addAll(collection);
        } finally {
            notifyObserver();
        }
    }

    @Override
    public boolean remove(Object object) {
        try {
            return super.remove(object);
        } finally {
            notifyObserver();
        }
    }

    @Override
    public T remove(int index) {
        try {
            return super.remove(index);
        } finally {
            notifyObserver();
        }
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        try {
            super.removeRange(fromIndex, toIndex);
        } finally {
            notifyObserver();
        }
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        try {
            return super.removeAll(collection);
        } finally {
            notifyObserver();
        }
    }

    @Override
    public void clear() {
        try {
            super.clear();
        } finally {
            notifyObserver();
        }
    }

    private void notifyObserver() {
        notifyObserver(false);
    }

    private void notifyObserver(boolean force) {
        boolean needToNotify = force;
        if (force == false) {
            if (mIsNotifyPaused.get()) {
                mIsDataChanged.set(true);
            } else {
                needToNotify = true;
            }
        }
        if (needToNotify && mContentObserver != null) {
            try {
                mContentObserver.onContentChanged();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    interface ContentObserver {

        void onContentChanged();

    }
}
