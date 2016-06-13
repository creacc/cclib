package com.creacc.cclistview.adapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/6/18.
 */
public class CCAdapterContent<ContentType> {

    private List<ContentType> mContents = new ArrayList<ContentType>();;

    private ContentObserver mObserver;

    public CCAdapterContent() {
    }

    public CCAdapterContent(List<ContentType> contents) {
        if (contents == null) {
            throw new NullPointerException();
        }
        add(contents);
    }

    void setObserver(ContentObserver observer) {
        this.mObserver = observer;
    }

    public ContentType get(int position) {
        return mContents.get(position);
    }

    public int count() {
        return this.mContents.size();
    }

    public void update(List<ContentType> contents) {
        mContents.clear();
        add(contents);
    }

    public void add(List<ContentType> contents) {
        mContents.addAll(contents);
        notifyUpdate();
    }

    public void add(int index, ContentType content) {
        mContents.add(index, content);
        notifyUpdate();
    }

    public void add(ContentType content) {
        mContents.add(content);
        notifyUpdate();
    }

    public void remove(int position) {
        ContentType content = mContents.remove(position);;
        notifyUpdate();
    }

    public void remove(ContentType content) {
        mContents.remove(content);
        notifyUpdate();
    }

    public void clear() {
        mContents.clear();
        notifyUpdate();
    }

    public void notifyUpdate() {
        if (mObserver != null) {
            mObserver.onContentChanged();
        }
    }

    public List<ContentType> toList() {
        return new ArrayList<ContentType>(mContents);
    }

    public interface ContentObserver {

        void onContentChanged();
    }
}
